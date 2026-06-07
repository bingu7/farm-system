# Workaround for Maven spring-boot:run failing on Windows paths with non-ASCII characters.
# Maven may pass a garbled classpath (e.g. 农产品 -> mojibake), causing ClassNotFoundException.

Set-Location $PSScriptRoot

Write-Host "Compiling backend..."
mvn -q compile dependency:build-classpath "-Dmdep.outputFile=target/classpath.txt" "-Dmdep.includeScope=runtime"
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

$classes = (Resolve-Path "target\classes").Path
$deps = Get-Content "target\classpath.txt" -Raw
$cp = "$classes;$deps"

Write-Host "Starting Spring Boot on http://localhost:9090 ..."
java -cp $cp com.example.SpringbootApplication
