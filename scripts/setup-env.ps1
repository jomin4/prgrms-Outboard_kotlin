# 코틀린 명언게시판 프로젝트 - 환경 재현 스크립트
# 목적: JDK25가 기본 설치된 환경에서도, 강의와 동일한 Gradle 8.10 + JDK 21 조합을
#       매번 프롬프트로 재현하지 않고 이 스크립트 한 번으로 재현한다.
# 사용법: 프로젝트 루트에서 실행 -> .\scripts\setup-env.ps1

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$jdksDir = "$env:USERPROFILE\.jdks"
$requiredJdkGlob = "jdk-21*"

Write-Output "=== 1. JDK 21 확인 ==="
$existingJdk = Get-ChildItem $jdksDir -Directory -ErrorAction SilentlyContinue |
    Where-Object { $_.Name -like $requiredJdkGlob } |
    Select-Object -First 1

if ($existingJdk) {
    Write-Output "JDK 21 이미 존재: $($existingJdk.FullName)"
    $jdkPath = $existingJdk.FullName
} else {
    Write-Output "JDK 21 없음 -> Temurin 21 다운로드"
    New-Item -ItemType Directory -Force -Path $jdksDir | Out-Null
    $zip = "$env:TEMP\temurin21.zip"
    $url = "https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse"
    Invoke-WebRequest -Uri $url -OutFile $zip
    Expand-Archive -Path $zip -DestinationPath $jdksDir -Force
    Remove-Item $zip
    $jdkPath = (Get-ChildItem $jdksDir -Directory | Where-Object { $_.Name -like $requiredJdkGlob } | Select-Object -First 1).FullName
    Write-Output "설치 완료: $jdkPath"
}

Write-Output ""
Write-Output "=== 2. gradle.properties에 JDK 경로 고정 ==="
$gradlePropsPath = "$projectRoot\gradle.properties"
$jdkPathForward = $jdkPath -replace '\\', '/'
$javaHomeLine = "org.gradle.java.home=$jdkPathForward"

if (Test-Path $gradlePropsPath) {
    $content = Get-Content $gradlePropsPath -Raw
    if ($content -match "org\.gradle\.java\.home=") {
        $content = $content -replace "org\.gradle\.java\.home=.*", $javaHomeLine
        Set-Content -Path $gradlePropsPath -Value $content -NoNewline
    } else {
        Add-Content -Path $gradlePropsPath -Value $javaHomeLine
    }
} else {
    Set-Content -Path $gradlePropsPath -Value "kotlin.code.style=official`n$javaHomeLine`n"
}
Write-Output "gradle.properties 설정 완료: $javaHomeLine"

Write-Output ""
Write-Output "=== 3. IntelliJ Gradle JVM 설정 (.idea/gradle.xml) ==="
$ideaDir = "$projectRoot\.idea"
$gradleXmlPath = "$ideaDir\gradle.xml"
if (Test-Path $gradleXmlPath) {
    $xml = Get-Content $gradleXmlPath -Raw
    if ($xml -notmatch "gradleJvm") {
        $xml = $xml -replace '(<option name="externalProjectPath" value="\$PROJECT_DIR\$" />)', "`$1`n        <option name=`"gradleJvm`" value=`"temurin-21`" />"
        Set-Content -Path $gradleXmlPath -Value $xml -NoNewline
        Write-Output "gradle.xml에 gradleJvm=temurin-21 추가"
    } else {
        Write-Output "gradle.xml에 gradleJvm 이미 설정됨"
    }
} else {
    Write-Output "gradle.xml 없음 (IntelliJ에서 프로젝트를 아직 안 열었을 수 있음) - 건너뜀"
}

Write-Output ""
Write-Output "=== 4. 빌드 검증 ==="
Set-Location $projectRoot
& "$projectRoot\gradlew.bat" build --console=plain
if ($LASTEXITCODE -eq 0) {
    Write-Output ""
    Write-Output "=== 환경 세팅 완료: BUILD SUCCESSFUL ==="
} else {
    Write-Output ""
    Write-Output "=== 빌드 실패 - 로그를 확인하세요 ==="
    exit 1
}
