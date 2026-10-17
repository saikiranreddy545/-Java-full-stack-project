@echo off
title Push Project to GitHub
color 0A
echo ============================================================
echo        PUSH ONLINE E-COMMERCE SYSTEM TO GITHUB
echo ============================================================
echo.
echo Both your Frontend and Backend local Git repositories are
echo already initialized, committed, and ready to push!
echo.
echo ------------------------------------------------------------
echo STEP 1: FRONTEND REPOSITORY
echo ------------------------------------------------------------
set /p FRONT_URL="Enter your Frontend GitHub Repo URL (e.g. https://github.com/username/ecommerce-frontend.git): "
if not "%FRONT_URL%"=="" (
    echo Pushing Frontend...
    cd /d "c:\Users\saibu\OneDrive\Desktop\tt-a11\frontend"
    git remote remove origin 2>nul
    git remote add origin %FRONT_URL%
    git branch -M main
    git push -u origin main
    echo [DONE] Frontend pushed successfully!
)

echo.
echo ------------------------------------------------------------
echo STEP 2: BACKEND REPOSITORY
echo ------------------------------------------------------------
set /p BACK_URL="Enter your Backend GitHub Repo URL (e.g. https://github.com/username/ecommerce-backend.git): "
if not "%BACK_URL%"=="" (
    echo Pushing Backend...
    cd /d "c:\Users\saibu\OneDrive\Desktop\tt-a11\backend"
    git remote remove origin 2>nul
    git remote add origin %BACK_URL%
    git branch -M main
    git push -u origin main
    echo [DONE] Backend pushed successfully!
)

echo.
echo ============================================================
echo All Done! You can now copy your repository links to your form!
echo ============================================================
pause
