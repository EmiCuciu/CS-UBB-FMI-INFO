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

    $stmt = $pdo->query("SELECT id FROM persoane ORDER BY id");
    $ids = $stmt->fetchAll(PDO::FETCH_COLUMN);

    echo json_encode(['ids' => $ids]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Eroare de conexiune: ' . $e->getMessage()]);
}
?>