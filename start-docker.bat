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
echo Starting Docker Compose
echo ================================

docker-compose up --build

echo ================================
echo Shutting down Docker Compose
echo ================================

docker-compose down

pause