<?php
header('Content-Type: application/json');

$host = 'localhost';
$db = 'ceir3511';
$user = 'ceir3511';
$pass = 'YzUxZWQzMDYz';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $data = json_decode(file_get_contents('php://input'), true);
    $id = isset($data['id']) ? (int)$data['id'] : 0;
    $nume = $data['nume'] ?? '';
    $prenume = $data['prenume'] ?? '';
    $telefon = $data['telefon'] ?? '';
    $email = $data['email'] ?? '';

    if ($id <= 0 || empty($nume) || empty($prenume) || empty($telefon) || empty($email)) {
        throw new Exception('Date invalide');
    }

    $stmt = $pdo->prepare("UPDATE persoane SET nume = :nume, prenume = :prenume, telefon = :telefon, email = :email WHERE id = :id");
    $stmt->bindValue(':id', $id, PDO::PARAM_INT);
    $stmt->bindValue(':nume', $nume, PDO::PARAM_STR);
    $stmt->bindValue(':prenume', $prenume, PDO::PARAM_STR);
    $stmt->bindValue(':telefon', $telefon, PDO::PARAM_STR);
    $stmt->bindValue(':email', $email, PDO::PARAM_STR);
    $stmt->execute();

    if ($stmt->rowCount() === 0) {
        throw new Exception('Nicio modificare nu a fost salvată');
    }

    echo json_encode(['success' => true]);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Eroare de conexiune: ' . $e->getMessage()]);
} catch (Exception $e) {
    http_response_code(400);
    echo json_encode(['error' => $e->getMessage()]);
}
?>