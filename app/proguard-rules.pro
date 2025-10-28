# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Preserve line numbers for debugging stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep crash reporting information
-keepattributes *Annotation*

# Keep data classes for kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep ALL data classes - CRITICAL for preventing ClassCastException
# NO obfuscation, NO shrinking, NO optimization for data classes
-keep class com.kreggscode.aristotlequotes.data.** { *; }
-keep class com.kreggscode.aristotlequotes.model.** { *; }
-keepclassmembers class com.kreggscode.aristotlequotes.data.** { *; }
-keepclassmembers class com.kreggscode.aristotlequotes.model.** { *; }
-keepnames class com.kreggscode.aristotlequotes.data.**
-keepnames class com.kreggscode.aristotlequotes.model.**

# Keep UI screen classes with NO obfuscation or shrinking
-keep class com.kreggscode.aristotlequotes.ui.screens.** { *; }
-keepclassmembers class com.kreggscode.aristotlequotes.ui.screens.** { *; }
-keepnames class com.kreggscode.aristotlequotes.ui.screens.**

# Keep ViewModels
-keep class com.kreggscode.aristotlequotes.viewmodel.** { *; }

# Keep Compose classes - CRITICAL for preventing ClassCastException
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-dontwarn androidx.compose.**

# Keep Retrofit and OkHttp - CRITICAL for API calls
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

# Keep Retrofit interfaces and methods - CRITICAL
-keep interface com.kreggscode.aristotlequotes.data.PollinationsApiService { *; }
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Keep Gson - CRITICAL for JSON serialization
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepclassmembers class com.google.gson.** { *; }

# Keep all fields with SerializedName annotation - NO obfuscation
-keepclassmembers class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Gson uses generic type information stored in a class file when working with fields
# This is required for proper Gson deserialization
-keepattributes Signature, InnerClasses, EnclosingMethod
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Gson specific classes
-dontwarn sun.misc.**

# Keep API request/response models - CRITICAL for Pollinations AI Chat
# These MUST NOT be obfuscated or the API will fail
-keep class com.kreggscode.aristotlequotes.data.PollinationsMessage { *; }
-keep class com.kreggscode.aristotlequotes.data.PollinationsRequest { *; }
-keep class com.kreggscode.aristotlequotes.data.PollinationsChoice { *; }
-keep class com.kreggscode.aristotlequotes.data.PollinationsResponse { *; }
-keepclassmembers class com.kreggscode.aristotlequotes.data.Pollinations** { *; }
-keepnames class com.kreggscode.aristotlequotes.data.PollinationsMessage
-keepnames class com.kreggscode.aristotlequotes.data.PollinationsRequest
-keepnames class com.kreggscode.aristotlequotes.data.PollinationsChoice
-keepnames class com.kreggscode.aristotlequotes.data.PollinationsResponse

# Keep all @SerializedName annotations for API classes
-keepclassmembers class com.kreggscode.aristotlequotes.data.Pollinations** {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep all data classes used for JSON - prevent field name obfuscation
# These are explicitly listed to ensure R8 doesn't touch them
-keep class com.kreggscode.aristotlequotes.data.MajorWork { *; }
-keep class com.kreggscode.aristotlequotes.data.Section { *; }
-keep class com.kreggscode.aristotlequotes.data.EquationDetail { *; }
-keep class com.kreggscode.aristotlequotes.data.Essay { *; }
-keep class com.kreggscode.aristotlequotes.data.KeyPoint { *; }
-keep class com.kreggscode.aristotlequotes.data.Letter { *; }
-keep class com.kreggscode.aristotlequotes.data.Paper { *; }
-keep class com.kreggscode.aristotlequotes.data.PaperWorkItem { *; }
-keep class com.kreggscode.aristotlequotes.data.Prediction { *; }
-keep class com.kreggscode.aristotlequotes.data.SubPaper { *; }
-keepnames class com.kreggscode.aristotlequotes.data.MajorWork
-keepnames class com.kreggscode.aristotlequotes.data.Section
-keepnames class com.kreggscode.aristotlequotes.data.EquationDetail

# Keep Kotlin coroutines - CRITICAL for preventing ClassCastException in suspend functions
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keep class kotlinx.coroutines.** { *; }
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Keep all suspend functions and coroutine continuations
-keepclassmembers class * {
    *** invokeSuspend(...);
}
-keep class kotlin.coroutines.Continuation
-keep class kotlin.coroutines.** { *; }

# Keep OkHttp - CRITICAL for network calls
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Keep OkHttp logging interceptor
-keep class okhttp3.logging.** { *; }

# Keep Parcelable implementations
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Navigation arguments and state
-keepnames class androidx.navigation.fragment.NavHostFragment
-keep class * extends androidx.navigation.Navigator
-keepclassmembers class * {
    @androidx.navigation.** *;
}

# Keep Compose state and saveable classes - CRITICAL for state restoration
-keep class androidx.compose.runtime.saveable.** { *; }
-keepclassmembers class * implements androidx.compose.runtime.saveable.Saver {
    *;
}
-keep class * implements android.os.Parcelable {
    *;
}
-keep class * implements java.io.Serializable {
    *;
}

# R8 full mode optimization - but be careful with state classes
# Temporarily disable these for stability
# -allowaccessmodification
# -repackageclasses

# Disable aggressive optimizations that can cause ClassCastException
-optimizations !code/simplification/cast,!code/removal/advanced

# Keep source file names and line numbers for better crash reports
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep Kotlin metadata for reflection
-keep class kotlin.Metadata { *; }

# Keep Kotlin coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Prevent stripping of generic signatures
-keepattributes Signature,InnerClasses,EnclosingMethod

# Keep Room database classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Keep WorkManager classes
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.CoroutineWorker
-keepclassmembers class * extends androidx.work.Worker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

# Additional Retrofit rules for production
-keepattributes *Annotation*,Signature,Exception
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
