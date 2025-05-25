<?php
$plecare = $_GET['plecare'];
$baza = [
    'Oras1' => ['Oras2', 'Oras3'],
    'Oras2' => ['Oras4', 'Oras5'],
    'Oras6' => ['Oras7'],
    'gol' => ['golgolut']
];
echo json_encode($baza[$plecare] ?? []);    // daca nu exista, returneaza un array gol
?>