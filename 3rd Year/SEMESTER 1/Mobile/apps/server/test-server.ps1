# Test Script for Server

Write-Host "Starting Inventar Server Test..." -ForegroundColor Green
Write-Host ""

# Start server in background
$serverJob = Start-Job -ScriptBlock {
    Set-Location "D:\GithubRepositories\CS-UBB-FMI-INFO\3rd Year\Mobile\apps\server"
    node server.js
}

Write-Host "Server started in background (Job ID: $($serverJob.Id))" -ForegroundColor Yellow
Write-Host "Waiting 3 seconds for server to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

# Test GET /product?page=0
Write-Host ""
Write-Host "Testing GET /product?page=0..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:3000/product?page=0" -Method Get
    Write-Host "SUCCESS: Retrieved $($response.products.Count) products" -ForegroundColor Green
    Write-Host "Total products: $($response.total)" -ForegroundColor Green
    Write-Host "Sample product: $($response.products[0].name)" -ForegroundColor Green
} catch {
    Write-Host "FAILED: $_" -ForegroundColor Red
}

# Test POST /item
Write-Host ""
Write-Host "Testing POST /item..." -ForegroundColor Cyan
try {
    $body = @{
        code = 1
        quantity = 10
    } | ConvertTo-Json

    $response = Invoke-RestMethod -Uri "http://localhost:3000/item" -Method Post -Body $body -ContentType "application/json"
    Write-Host "SUCCESS: Item added - Code: $($response.code), Quantity: $($response.quantity)" -ForegroundColor Green
} catch {
    Write-Host "FAILED: $_" -ForegroundColor Red
}

# Test GET /item
Write-Host ""
Write-Host "Testing GET /item..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:3000/item" -Method Get
    Write-Host "SUCCESS: Retrieved $($response.Count) items" -ForegroundColor Green
} catch {
    Write-Host "FAILED: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "Tests completed!" -ForegroundColor Green
Write-Host "Server is running in background. Press any key to stop it..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

# Stop server
Stop-Job -Job $serverJob
Remove-Job -Job $serverJob
Write-Host "Server stopped." -ForegroundColor Green

