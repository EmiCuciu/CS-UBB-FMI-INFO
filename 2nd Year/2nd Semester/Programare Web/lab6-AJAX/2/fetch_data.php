<?php
header('Content-Type: application/json');

$host = 'localhost';
$db = 'ceir3511';
$user = 'ceir3511';
$pass = 'YzUxZWQzMDYz';

$limit = 3;
$offset = isset($_GET['offset']) ? (int)$_GET['offset'] : 0;    // initial pag 0

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db;charset=utf8mb4", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);  // setam ca erorile sa fie aruncate ca exceptii

    $stmt_total = $pdo->query("SELECT COUNT(*) FROM persoane");
    $total_inregistrari = (int)$stmt_total->fetchColumn();

    $stmt = $pdo->prepare("SELECT nume, prenume, telefon, email 
                            FROM persoane 
                            ORDER BY id 
                            LIMIT :limit 
                            OFFSET :offset
                            ");
    $stmt->bindValue(':limit', $limit, PDO::PARAM_INT);
    $stmt->bindValue(':offset', $offset, PDO::PARAM_INT);
    $stmt->execute();

    $results = $stmt->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        'data' => $results,
        'total' => $total_inregistrari,
        'are_urmatoare' => $offset + $limit < $total_inregistrari
    ]);

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => $e->getMessage()]);
}
?>
