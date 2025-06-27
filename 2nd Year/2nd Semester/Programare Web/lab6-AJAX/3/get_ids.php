<?php
header('Content-Type: application/json');

$host = 'localhost';
$db = 'ceir3511';
$user = 'ceir3511';
$pass = 'YzUxZWQzMDYz';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $stmt = $pdo->query("SELECT id FROM persoane ORDER BY id");
    $ids = $stmt->fetchAll(PDO::FETCH_COLUMN);

    echo json_encode(['ids' => $ids]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Eroare de conexiune: ' . $e->getMessage()]);
}
?>