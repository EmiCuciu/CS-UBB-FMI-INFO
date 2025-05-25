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

    $id = isset($_GET['id']) ? (int)$_GET['id'] : 0;
    if ($id <= 0) {
        throw new Exception('ID invalid');
    }

    $stmt = $pdo->prepare("SELECT nume, prenume, telefon, email FROM persoane WHERE id = :id");
    $stmt->bindValue(':id', $id, PDO::PARAM_INT);
    $stmt->execute();
    $person = $stmt->fetch(PDO::FETCH_ASSOC);

    if (!$person) {
        throw new Exception('Persoana nu a fost găsită');
    }

    echo json_encode($person);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Eroare de conexiune: ' . $e->getMessage()]);
} catch (Exception $e) {
    http_response_code(400);
    echo json_encode(['error' => $e->getMessage()]);
}
?>