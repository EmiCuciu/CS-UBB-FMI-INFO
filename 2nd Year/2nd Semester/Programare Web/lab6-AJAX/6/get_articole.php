<?php
header('Content-Type: application/json');

$host = 'localhost';
$db = 'ceir3511';
$user = 'ceir3511';
$pass = 'YzUxZWQzMDYz';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db;charset=utf8mb4", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $query = "SELECT id, nume, producator, procesor, memorie, pret FROM articole WHERE 1=1";
    $params = [];

    if (!empty($_GET['producator'])) {
        $query .= " AND producator = :producator";
        $params[':producator'] = $_GET['producator'];
    }
    if (!empty($_GET['procesor'])) {
        $query .= " AND procesor = :procesor";
        $params[':procesor'] = $_GET['procesor'];
    }
    if (!empty($_GET['memorie'])) {
        $query .= " AND memorie = :memorie";
        $params[':memorie'] = $_GET['memorie'];
    }

    $stmt = $pdo->prepare($query);
    $stmt->execute($params);
    $articole = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode($articole);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Eroare de conexiune: ' . $e->getMessage()]);
}
?>