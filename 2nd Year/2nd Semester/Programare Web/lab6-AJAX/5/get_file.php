<?php
header('Content-Type: application/json');

$root_dir = __DIR__ . '/files';
$cale = isset($_GET['cale']) ? $_GET['cale'] : '';

$full_path = realpath($root_dir . '/' . ltrim($cale, '/'));

if (!$full_path || strpos($full_path, realpath($root_dir)) !== 0 || !is_file($full_path)) {
    http_response_code(400);
    echo json_encode(['error' => 'Fișierul este invalid sau inaccesibil']);
    exit;
}

try {
    $content = file_get_contents($full_path);
    echo json_encode(['content' => $content]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['error' => 'Eroare server: ' . $e->getMessage()]);
}
?>