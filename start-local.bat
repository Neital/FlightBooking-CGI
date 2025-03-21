@echo off
echo ================================
echo Building the backend application
echo ================================
call .\gradlew build

if %errorlevel% neq 0 (
    echo Build failed. Exiting...
    pause
    exit /b %errorlevel%
)

echo ================================
echo Starting Spring Boot application
echo ================================
start cmd /k ".\gradlew bootRun --args='--spring.docker.compose.enabled=false'"

echo ================================
echo Starting Vue.js frontend
echo ================================
cd .\frontend\
npm run serve

pause