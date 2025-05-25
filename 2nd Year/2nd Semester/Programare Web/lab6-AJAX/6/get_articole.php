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