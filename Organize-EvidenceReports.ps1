# Script para organizar evidencias de Serenity por ejecución, tipo y escenario
# Implementa política A: conservar PNG/TXT en evidences, archivar JSON/XML, copiar sitio completo
# Uso: .\Organize-EvidenceReports.ps1

param(
    [string]$SerenityPath = "target/site/serenity",
    [string]$SurefirePath = "target/surefire-reports",
    [string]$ReportRoot = "target/serenity-reports",
    [string]$ExecutionTimestamp = $(Get-Date -Format "yyyy-MM-dd_HH-mm-ss")
)

# Destinos
$EvidencePath = Join-Path $ReportRoot "evidences"
$SiteDestPath = Join-Path $ReportRoot "site"
$SurefireDestPath = Join-Path $ReportRoot "surefire-reports"
$ArchivePath = Join-Path $ReportRoot "archive"

Write-Host "====================================="
Write-Host "Organizador de Reportes y Evidencias (Política A)"
Write-Host "====================================="
Write-Host "Timestamp de ejecución: $ExecutionTimestamp"
Write-Host "Origen de Serenity: $SerenityPath"
Write-Host "Origen de Surefire: $SurefirePath"
Write-Host "Destino base: $ReportRoot"

# Crear directorios base
New-Item -ItemType Directory -Path $EvidencePath -Force | Out-Null
New-Item -ItemType Directory -Path $SiteDestPath -Force | Out-Null
New-Item -ItemType Directory -Path $SurefireDestPath -Force | Out-Null
New-Item -ItemType Directory -Path $ArchivePath -Force | Out-Null

# Mapeo de tipos de prueba basado en patrones en nombre del archivo JSON
$TypeMapping = @{
    "_api_"           = "API"
    "_login"          = "UI"
    "_register"       = "UI"
    "_dashboard"      = "UI"
    "_contacts"       = "UI"
    "_authentication" = "API"
    "_e2e"            = "E2E"
}

function Get-TestType {
    param([string]$FileName)
    foreach ($pattern in $TypeMapping.Keys) {
        if ($FileName -match $pattern) {
            return $TypeMapping[$pattern]
        }
    }
    return "GENERAL"
}

function Get-ScenarioName {
    param([string]$FileName)
    $withoutExt = $FileName -replace '\.json$', ''
    if ($withoutExt -match '_sl_(.+)$') {
        $scenarioName = $matches[1]
        $scenarioName = $scenarioName -replace '_', ' ' -replace 'windows', '' -replace '^\s+|\s+$', ''
        return $scenarioName -replace ' ', '_'
    }
    return $withoutExt -replace '[^a-zA-Z0-9 ]', ' ' -replace '\s+', ' ' -replace ' ', '_'
}

# 1) Procesar JSON: copiar PNGs referenciadas y registrar escenarios
$jsonFiles = Get-ChildItem -Path $SerenityPath -Filter "*.json" -ErrorAction SilentlyContinue
if (-not $jsonFiles) {
    Write-Host "⚠  No se encontraron archivos JSON de reportes en: $SerenityPath"
} else {
    Write-Host "Encontrados $($jsonFiles.Count) reportes de Serenity"
    foreach ($json in $jsonFiles) {
        $testType = Get-TestType $json.Name
        $scenarioName = Get-ScenarioName $json.Name
        $evidenceDirPath = Join-Path $EvidencePath "$ExecutionTimestamp/$testType/$scenarioName"
        New-Item -ItemType Directory -Path $evidenceDirPath -Force | Out-Null

        $jsonContent = Get-Content $json.FullName -Raw
        $imageHashes = [regex]::Matches($jsonContent, '[a-f0-9]{64}') | ForEach-Object { $_.Value } | Select-Object -Unique

        if ($imageHashes.Count -gt 0) {
            Write-Host "📋 $scenarioName ($testType)"
            foreach ($hash in $imageHashes) {
                $sourceImage = Join-Path $SerenityPath "$hash.png"
                if (Test-Path $sourceImage) {
                    $destImage = Join-Path $evidenceDirPath "$hash.png"
                    Copy-Item $sourceImage $destImage -Force | Out-Null
                    Write-Host "   └─ 📸 $hash.png"
                }
            }
        }

        # Copiar archivos .txt relacionados si existen en target/serenity-reports/evidences
        $projectRoot = Resolve-Path "." | Select-Object -ExpandProperty Path
        $apiTxtBase = Join-Path $projectRoot "target\serenity-reports\evidences"
        if (Test-Path $apiTxtBase) {
            $matchingTxts = Get-ChildItem -Path $apiTxtBase -Recurse -Filter "*.txt" -ErrorAction SilentlyContinue |
                            Where-Object { $_.FullName -match $scenarioName -or $_.Name -match $scenarioName }
            foreach ($txt in $matchingTxts) {
                Copy-Item $txt.FullName (Join-Path $evidenceDirPath $txt.Name) -Force -ErrorAction SilentlyContinue
                Write-Host "   └─ 📝 $($txt.Name)"
            }
        }

        # Mover archivo JSON al archive (para reducir ruido en evidences)
        $archiveDir = Join-Path $ArchivePath "$ExecutionTimestamp/json"
        New-Item -ItemType Directory -Path $archiveDir -Force | Out-Null
        Move-Item -Path $json.FullName -Destination $archiveDir -Force -ErrorAction SilentlyContinue
    }
}

# 2) Copiar ficheros .txt directamente presentes en $SerenityPath (si existe)
$txtInSite = Get-ChildItem -Path $SerenityPath -Recurse -Filter "*.txt" -ErrorAction SilentlyContinue
foreach ($t in $txtInSite) {
    $dest = Join-Path $SiteDestPath "$ExecutionTimestamp/extra_txt"
    New-Item -ItemType Directory -Path $dest -Force | Out-Null
    Copy-Item $t.FullName (Join-Path $dest $t.Name) -Force -ErrorAction SilentlyContinue
}

# 3) Copiar sito completo a site/{timestamp}
$siteExecutionPath = Join-Path $SiteDestPath $ExecutionTimestamp
if (Test-Path $SerenityPath) {
    Write-Host ""
    Write-Host "📁 Copiando sitio de Serenity a: $siteExecutionPath"
    New-Item -ItemType Directory -Path $siteExecutionPath -Force | Out-Null
    Copy-Item -Path (Join-Path $SerenityPath '*') -Destination $siteExecutionPath -Recurse -Force -ErrorAction SilentlyContinue
} else {
    Write-Host "⚠  No se encontró el directorio de Serenity en: $SerenityPath"
}

# 4) Mover JSON y XML residuales (directorio raíz de serenity) a archive
$xmlFiles = Get-ChildItem -Path $SerenityPath -Filter "SERENITY-*.xml" -ErrorAction SilentlyContinue
if ($xmlFiles) {
    $xmlArchive = Join-Path $ArchivePath "$ExecutionTimestamp/xml"
    New-Item -ItemType Directory -Path $xmlArchive -Force | Out-Null
    foreach ($x in $xmlFiles) {
        Move-Item -Path $x.FullName -Destination $xmlArchive -Force -ErrorAction SilentlyContinue
    }
}

# 5) Copiar surefire-reports (sin cambios)
$surefireExecutionPath = Join-Path $SurefireDestPath $ExecutionTimestamp
if (Test-Path $SurefirePath) {
    Write-Host ""
    Write-Host "📁 Copiando Surefire reports a: $surefireExecutionPath"
    New-Item -ItemType Directory -Path $surefireExecutionPath -Force | Out-Null
    Copy-Item -Path (Join-Path $SurefirePath '*') -Destination $surefireExecutionPath -Recurse -Force -ErrorAction SilentlyContinue
} else {
    Write-Host "⚠  No se encontró el directorio de Surefire en: $SurefirePath"
}

Write-Host ""
Write-Host "✔ Reorganización completada (Política A)"
Write-Host "📁 Evidencias (PNG/TXT) guardadas en: $EvidencePath/$ExecutionTimestamp"
Write-Host "📁 Site completo guardado en: $siteExecutionPath"
Write-Host "📁 Archivos JSON/XML movidos a: $ArchivePath/$ExecutionTimestamp"
