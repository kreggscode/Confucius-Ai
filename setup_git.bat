@echo off
git config user.email "kreg9da@gmail.com"
git config user.name "KreggsCode"
git commit -m "Initial commit - Confucius Wisdom App"
git branch -M main
git remote add origin https://github.com/kreggscode/Confucius-Ai.git
echo.
echo Git repository initialized successfully!
echo.
echo To push to GitHub, run:
echo git push -u origin main
pause
