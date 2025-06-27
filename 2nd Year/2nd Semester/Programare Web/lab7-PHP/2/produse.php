<?php
$host = 'localhost';
$db = 'ceir3511';
$user = 'ceir3511';
$pass = 'YzUxZWQzMDYz';

try {
    $pdo = new PDO("mysql:host=$host;dbname=$db;charset=utf8mb4", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Conexiunea la baza de date a eșuat: " . $e->getMessage());
}

?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Lab7-2</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .pagination { margin-top: 20px; }
        .pagination a { margin: 0 5px; text-decoration: none; }
        .pagination a:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <h1>Lista Produse</h1>
    <form method="GET" action="produse.php">
        <label for="n">Produse pe pagină:</label>
        <select name="n" id="n">
            <option value="1" <?php if(isset($_GET['n']) && $_GET['n'] == 1) echo 'selected'; ?>>1</option>
            <option value="5" <?php if(isset($_GET['n']) && $_GET['n'] == 5) echo 'selected'; ?>>5</option>
            <option value="10" <?php if(isset($_GET['n']) && $_GET['n'] == 10) echo 'selected'; ?>>10</option>
            <option value="20" <?php if(isset($_GET['n']) && $_GET['n'] == 20) echo 'selected'; ?>>20</option>
        </select>
        <input type="submit" value="Aplică">
    </form>

    <?php
    $n = isset($_GET['n']) && in_array($_GET['n'], ['1','5', '10', '20']) ? (int)$_GET['n'] : 5;
    $pagina = isset($_GET['pagina']) && is_numeric($_GET['pagina']) && $_GET['pagina'] > 0 ? (int)$_GET['pagina'] : 1;
    $offset = ($pagina - 1) * $n;

    $stmt = $pdo->prepare("SELECT * FROM produse LIMIT ? OFFSET ?");
    $stmt->bindValue(1, $n, PDO::PARAM_INT);
    $stmt->bindValue(2, $offset, PDO::PARAM_INT);
    $stmt->execute();
    $produse = $stmt->fetchAll(PDO::FETCH_ASSOC);

    $total = $pdo->query("SELECT COUNT(*) FROM produse")->fetchColumn();
    $nr_pagini = ceil($total / $n);

    if (count($produse) > 0) {
        echo "<table>";
        echo "<tr><th>ID</th><th>Nume</th><th>Descriere</th><th>Preț</th></tr>";
        foreach ($produse as $produs) {
            echo "<tr>";
            echo "<td>" . htmlspecialchars($produs['id_produs']) . "</td>";
            echo "<td>" . htmlspecialchars($produs['nume']) . "</td>";
            echo "<td>" . htmlspecialchars($produs['descriere']) . "</td>";
            echo "<td>" . htmlspecialchars($produs['pret']) . " RON</td>";
            echo "</tr>";
        }
        echo "</table>";
    } else {
        echo "<p>Nu există produse de afișat.</p>";
    }

    echo "<div class='pagination'>";
    if ($pagina > 1) {
        echo "<a href='?n=$n&pagina=" . ($pagina - 1) . "'>Anterior</a>";
    }
    for ($i = 1; $i <= $nr_pagini; $i++) {
        echo "<a href='?n=$n&pagina=$i'>" . ($i == $pagina ? "<b>$i</b>" : $i) . "</a>";
    }
    if ($pagina < $nr_pagini) {
        echo "<a href='?n=$n&pagina=" . ($pagina + 1) . "'>Următor</a>";
    }
    echo "</div>";
    ?>
</body>
</html>
