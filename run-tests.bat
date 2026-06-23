@echo off
REM Script para ejecutar tests de Serenity y reorganizar evidencias
REM Uso: run-tests.bat [@ui | @api | @e2e]

setlocal enabledelayedexpansion

set TAG=%1
if "%TAG%"=="" (
    set TAG=@ui
)

echo.
echo =====================================
echo   EJECUTOR DE TESTS SERENITY
echo =====================================
echo Filtro: %TAG%
echo.

REM Ejecutar tests
echo [1/3] Compilando y ejecutando tests...
call mvn clean test verify "-Dcucumber.filter.tags=%TAG%"

if %ERRORLEVEL% neq 0 (
    echo ⚠  Los tests se ejecutaron con errores (ERRORLEVEL: %ERRORLEVEL%)
)

REM Organizar evidencias y reportes
echo.
echo [2/3] Reorganizando evidencias y reportes...
powershell -ExecutionPolicy Bypass -File ".\Organize-EvidenceReports.ps1"

REM Abrir reporte
echo.
echo [3/3] Abriendo reporte HTML de Serenity...
echo Abra el reporte en: target\site\serenity\index.html
start target\site\serenity\index.html

echo.
echo  Proceso completado
echo.
echo  Estructura de evidencias:
echo  target/serenity-reports/evidences/{fecha-hora}/{tipo}/{escenario}/
echo  Reporte Serenity copiado a:
echo  target/serenity-reports/site/{fecha-hora}/
echo  Surefire copiado a:
echo  target/serenity-reports/surefire-reports/{fecha-hora}/
echo.

endlocal
pause
