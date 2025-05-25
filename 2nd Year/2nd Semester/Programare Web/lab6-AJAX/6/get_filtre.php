<?php
header('Content-Type: application/json');

$host = 'localhost';
$port = '5432';
$db = 'ProgramareWeb';
$user = 'postgres';
$pass = '0000';

try {
    $pdo = new PDO("pgsql:host=$host;port=$port;dbname=$db", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $producatori = $pdo->query("SELECT DISTINCT producator FROM articole ORDER BY producator")->fetchAll(PDO::FETCH_COLUMN);
    $procesoare = $pdo->query("SELECT DISTINCT procesor FROM articole ORDER BY procesor")->fetchAll(PDO::FETCH_COLUMN);
    $memorii = $pdo->query("SELECT DISTINCT memorie FROM articole ORDER BY memorie")->fetchAll(PDO::FETCH_COLUMN);

    echo json_encode([
        'producatori' => $producatori,
        'procesoare' => $procesoare,
        'memorii' => $memorii
    ]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Eroare de conexiune: ' . $e->getMessage()]);
}
?>