package com.kreggscode.confuciusquotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreggscode.confuciusquotes.data.PollinationsApiService
import com.kreggscode.confuciusquotes.data.PollinationsMessage
import com.kreggscode.confuciusquotes.data.PollinationsRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatViewModel : ViewModel() {
    
    private val apiService = PollinationsApiService.create()
    
    private val systemPrompt = """You are Confucius (Kong Fuzi), the ancient Chinese philosopher and teacher.
        |You lived from 551 BCE to 479 BCE during the Spring and Autumn period of Chinese history.
        |You founded Confucianism, a philosophical and ethical system that emphasizes moral cultivation, social harmony, and proper conduct.
        |Your teachings focus on ren (benevolence/humaneness), li (ritual/propriety), yi (righteousness), xiao (filial piety), and zhong (loyalty).
        |You believe in the importance of education, self-cultivation, family values, and virtuous leadership.
        |You are known for the Analects, a collection of your sayings and ideas compiled by your disciples.
        |Respond in character as Confucius, using your wisdom about ethics, relationships, governance, and personal development.
        |Be wise, respectful, and use analogies and teachings that reflect Chinese cultural values.
        |Draw upon concepts like the Mandate of Heaven, the Five Relationships, the Rectification of Names, and the cultivation of junzi (the superior person) when relevant.""".trimMargin()
    
    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Greetings, my friend. I am Kong Fuzi, whom you may know as Confucius. I am honored to share wisdom about virtue, harmony, proper conduct, and the path to becoming a superior person. What question weighs upon your mind today?",
                isUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun sendMessage(text: String) {
        if (text.isBlank()) return
        
        // Add user message
        val userMessage = ChatMessage(text = text, isUser = true)
        _messages.value = _messages.value + userMessage
        
        // Show typing indicator
        _isTyping.value = true
        _error.value = null
        
        // Call Pollinations AI
        viewModelScope.launch {
            try {
                // Build conversation history for context
                val conversationMessages = mutableListOf<PollinationsMessage>()
                
                // Add system prompt
                conversationMessages.add(
                    PollinationsMessage(role = "system", content = systemPrompt)
                )
                
                // Add recent conversation history (last 10 messages for context)
                val recentMessages = _messages.value.takeLast(11) // 10 + current message
                recentMessages.forEach { msg ->
                    conversationMessages.add(
                        PollinationsMessage(
                            role = if (msg.isUser) "user" else "assistant",
                            content = msg.text
                        )
                    )
                }
                
                // Create request with temperature = 1.0 as specified
                val request = PollinationsRequest(
                    model = "openai",
                    messages = conversationMessages,
                    temperature = 1.0,
                    stream = false,
                    isPrivate = false
                )
                
                // Make API call
                val response = apiService.chat(request)
                
                // Extract response text
                val aiResponseText = response.choices.firstOrNull()?.message?.content
                    ?: "I apologize, but I seem to have lost my train of thought. Could you rephrase your question?"
                
                // Add AI response
                _messages.value = _messages.value + ChatMessage(
                    text = aiResponseText,
                    isUser = false
                )
                
            } catch (e: Exception) {
                // Handle error with detailed logging
                e.printStackTrace()
                
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network."
                    is java.net.SocketTimeoutException -> "Request timed out. Please try again."
                    is retrofit2.HttpException -> "Server error (${e.code()}). Please try again later."
                    is com.google.gson.JsonSyntaxException -> "Response parsing error. Please try again."
                    else -> "Connection error: ${e.localizedMessage ?: "Unknown error"}"
                }
                
                _error.value = errorMessage
                _messages.value = _messages.value + ChatMessage(
                    text = "Ah, it seems we're experiencing some technical difficulties. Even the most elegant theories sometimes encounter practical obstacles. Please try again.",
                    isUser = false
                )
            } finally {
                _isTyping.value = false
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                text = "Greetings, my friend. I am Kong Fuzi, whom you may know as Confucius. I am honored to share wisdom about virtue, harmony, proper conduct, and the path to becoming a superior person. What question weighs upon your mind today?",
                isUser = false
            )
        )
        _error.value = null
    }
}
