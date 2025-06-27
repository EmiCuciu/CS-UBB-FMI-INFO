<!-- Înainte de orice HTML, includem config.php -->
<?php
// Database configuration
$host = 'localhost';
$db = 'PHP_ProgramareWeb';
$user = 'postgres';
$pass = '0000';

// Create PDO connection
try {
    $pdo = new PDO("pgsql:host=$host;dbname=$db", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Conexiunea la baza de date a eșuat: " . $e->getMessage());
}

// Utility function to escape output
function escape($string) {
    return htmlspecialchars($string, ENT_QUOTES, 'UTF-8');
}

// Set default timezone
date_default_timezone_set('Europe/Bucharest');

// Error reporting (comment these for production)
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Căutare trenuri</title>
    <link rel="stylesheet" href="stil.css">
</head>
<body>
<h1>Caută trenuri</h1>
<form action="trenuri.php" method="GET">
    <label>
        Plecare:
        <input type="text" name="plecare" required maxlength="100"
               pattern="[A-Za-zăîâșțĂÎÂȘȚ\- ]+">
    </label><br>
    <label>
        Sosire:
        <input type="text" name="sosire" required maxlength="100"
               pattern="[A-Za-zăîâșțĂÎÂȘȚ\- ]+">
    </label><br>
    <label>
        <input type="checkbox" name="doar_direct" value="1">
        Doar curse directe
    </label><br>
    <!-- Token CSRF la GET nu e esențial, dar dacă faci GET cu efect (nu ar trebui) -->
    <button type="submit">Caută</button>
</form>
<?php
// Dacă există parametrii plecare/sosire, afișează rezultatele
if ($_SERVER['REQUEST_METHOD'] === 'GET'
    && isset($_GET['plecare'], $_GET['sosire'])) {

    // Validări server-side:
    $plecare = trim($_GET['plecare']);
    $sosire = trim($_GET['sosire']);
    $doar_direct = isset($_GET['doar_direct']) && $_GET['doar_direct']==='1';

    // Validare machetă litere/spații:
    if (!preg_match('/^[A-Za-zăîâșțĂÎÂȘȚ\- ]+$/u', $plecare)
        || !preg_match('/^[A-Za-zăîâșțĂÎÂȘȚ\- ]+$/u', $sosire)) {
        echo '<p class="eroare">Date de intrare invalide.</p>';
    } else {
        $sql = '';
        $params = [];
        if ($doar_direct) {
            $sql = "SELECT * FROM trenuri
                        WHERE localitate_plecare = :plecare
                          AND localitate_sosire   = :sosire
                        ORDER BY ora_plecare";
            $params = [':plecare' => $plecare, ':sosire' => $sosire];

            $stmt = $pdo->prepare($sql);
            $stmt->execute($params);
            $rez = $stmt->fetchAll(PDO::FETCH_ASSOC);

            if (count($rez) === 0) {
                echo '<p>Nu există curse directe între ' .
                    escape($plecare) . ' și ' . escape($sosire) . '.</p>';
            } else {
                echo '<table>';
                echo '<tr><th>Nr tren</th><th>Tip</th><th>Plecare</th><th>Sosire</th><th>Ora plecare</th><th>Ora sosire</th></tr>';
                foreach ($rez as $r) {
                    echo '<tr>';
                    echo '<td>' . escape($r['numar_tren']) . '</td>';
                    echo '<td>' . escape($r['tip_tren']) . '</td>';
                    echo '<td>' . escape($r['localitate_plecare']) . '</td>';
                    echo '<td>' . escape($r['localitate_sosire']) . '</td>';
                    echo '<td>' . escape($r['ora_plecare']) . '</td>';
                    echo '<td>' . escape($r['ora_sosire']) . '</td>';
                    echo '</tr>';
                }
                echo '</table>';
            }
        } else {
            // Căutăm curse directe + cu legătură (un schimb).
            // Algoritm simplu:
            // 1) Trenuri de la plecare la o localitate X.
            // 2) Trenuri de la X la sosire.
            // 3) Ora de sosire a primului tren < ora de plecare a celui de-al doilea (macar cu 5 min întârziere).

            // Notă: Pentru simplitate, în acest exemplu nu impunem minim 5 minute, ci doar <.

            $sql = "
                  SELECT t1.nr_tren AS tren1, t1.tip_tren AS tip1,
                         t1.localitate_sosire AS nod_intermediar,
                         t1.ora_sosire AS ora_sosire1,
                         t2.nr_tren AS tren2, t2.tip_tren AS tip2,
                         t2.localitate_plecare AS nod_intermediar2,
                         t2.ora_plecare AS ora_plecare2
                  FROM trenuri t1
                  JOIN trenuri t2 
                    ON t1.localitate_sosire = t2.localitate_plecare
                  WHERE t1.localitate_plecare = :plecare
                    AND t2.localitate_sosire = :sosire
                    AND t1.ora_sosire < t2.ora_plecare
                  ORDER BY t1.ora_plecare, t2.ora_plecare
                ";
            $stmt = $pdo->prepare($sql);
            $stmt->execute([':plecare'=>$plecare, ':sosire'=>$sosire]);
            $rez = $stmt->fetchAll(PDO::FETCH_ASSOC);

            if (count($rez) === 0) {
                echo '<p>Nu există nicio legătură între aceste stații.</p>';
            } else {
                echo '<table>';
                echo '<tr><th>Tren 1</th><th>Tip 1</th><th>Plecare</th><th>Sosire</th><th>Ora sosire</th>';
                echo '<th>Tren 2</th><th>Tip 2</th><th>Plecare</th><th>Sosire</th><th>Ora plecare</th></tr>';
                foreach ($rez as $r) {
                    echo '<tr>';
                    echo '<td>' . escape($r['tren1']) . '</td>';
                    echo '<td>' . escape($r['tip1']) . '</td>';
                    echo '<td>' . escape($plecare) . '</td>';
                    echo '<td>' . escape($r['nod_intermediar']) . '</td>';
                    echo '<td>' . escape($r['ora_sosire1']) . '</td>';
                    echo '<td>' . escape($r['tren2']) . '</td>';
                    echo '<td>' . escape($r['tip2']) . '</td>';
                    echo '<td>' . escape($r['nod_intermediar2']) . '</td>';
                    echo '<td>' . escape($sosire) . '</td>';
                    echo '<td>' . escape($r['ora_plecare2']) . '</td>';
                    echo '</tr>';
                }
                echo '</table>';
            }
        }
    }
}
?>
</body>
</html>
