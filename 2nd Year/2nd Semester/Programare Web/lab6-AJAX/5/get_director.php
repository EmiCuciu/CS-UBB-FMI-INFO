<?php
header('Content-Type: application/json');


$root_dir = realpath(__DIR__ . '/files');

$cale = isset($_GET['cale']) ? $_GET['cale'] : '/';

$full_path = $cale === '/' ? $root_dir : realpath($root_dir . '/' . ltrim($cale, '/'));

if (!$full_path || strpos($full_path, $root_dir) !== 0 || !is_dir($full_path)) {
    $error = "Calea $full_path este invalidă sau inaccesibilă";
    http_response_code(400);
    echo json_encode(['error' => $error]);
    exit;
}

try {
    $continut = [];
    $files = scandir($full_path);

    foreach ($files as $item) {
        if ($item === '.' || $item === '..') {
            continue;
        }
        $item_path = $full_path . '/' . $item;
        $continut[] = [
            'nume' => $item,
            'tip' => is_dir($item_path) ? 'director' : 'fisier'
        ];
    }

    echo json_encode($continut);
} catch (Exception $e) {
    $error = 'Eroare server: ' . $e->getMessage();
    http_response_code(500);
    echo json_encode(['error' => $error]);
}
?>