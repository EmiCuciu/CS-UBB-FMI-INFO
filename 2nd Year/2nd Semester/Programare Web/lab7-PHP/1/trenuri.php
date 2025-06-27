<?php
$host = 'localhost';
$db = 'PHP_ProgramareWeb';
$user = 'postgres';
$pass = '0000';

try {
    $pdo = new PDO("pgsql:host=$host;dbname=$db", $user, $pass);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Conexiunea la baza de date a eșuat: " . $e->getMessage());
}

function escape($string): string
{
    return htmlspecialchars($string, ENT_QUOTES, 'UTF-8');
}

?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Lab7-1</title>
    <link rel="stylesheet" href="1.css">
</head>
<body>
<h1>Caută trenuri</h1>
<form action="trenuri.php" method="GET">
    <label>
        Plecare:
        <input type="text" name="plecare">
    </label>
    <br>
    <label>
        Sosire:
        <input type="text" name="sosire">
    </label>
    <br>
    <label>
        <input type="checkbox" name="doar_direct" value="1">
        Doar curse directe
    </label><br>
    <button type="submit">Caută</button>
</form>
<?php
if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['plecare'], $_GET['sosire'])) {

    $plecare = trim($_GET['plecare']);
    $sosire = trim($_GET['sosire']);
    $doar_direct = isset($_GET['doar_direct']) && $_GET['doar_direct'] === '1';

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
            echo '<p>Nu există curse directe între ' . escape($plecare) . ' și ' . escape($sosire) . '.</p>';
        } else {
            echo '<table>';
            echo '<tr><th>Nr tren</th><th>Tip</th><th>Plecare</th><th>Sosire</th><th>Ora plecare</th><th>Ora sosire</th></tr>';
            foreach ($rez as $r) {
                echo '<tr>';
                echo '<td>' . escape($r['nr_tren']) . '</td>';
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
        $routes = findRoutes($pdo, $plecare, $sosire);

        if (empty($routes)) {
            echo '<p>Nu există nicio legătură între aceste stații.</p>';
        } else {
            echo '<h3>Legături disponibile între ' . escape($plecare) . ' și ' . escape($sosire) . ':</h3>';
            echo '<table>';
            echo '<tr><th>Nr tren</th><th>Tip</th><th>Plecare</th><th>Sosire</th><th>Ora plecare</th><th>Ora sosire</th></tr>';

            $routeCount = 1;
            foreach ($routes as $route) {
                foreach ($route as $train) {
                    echo '<tr class="legatura-' . $routeCount . '">';
                    echo '<td>' . escape($train['nr_tren']) . '</td>';
                    echo '<td>' . escape($train['tip_tren']) . '</td>';
                    echo '<td>' . escape($train['localitate_plecare']) . '</td>';
                    echo '<td>' . escape($train['localitate_sosire']) . '</td>';
                    echo '<td>' . escape($train['ora_plecare']) . '</td>';
                    echo '<td>' . escape($train['ora_sosire']) . '</td>';
                    echo '</tr>';
                }

                echo '<tr class="separator"><td colspan="6"></td></tr>';
                $routeCount++;
            }
            echo '</table>';
        }
    }
}

function findRoutes($pdo, $source, $destination): array {
    $routes = [];
    $visited = [$source => true]; // marcam vizitat sa nu avem cicluri
    $currentRoute = [];

    findRoutesRecursive($pdo, $source, $destination, $visited, $currentRoute, $routes);

    return $routes;
}

function findRoutesRecursive($pdo, $current, $destination, $visited, $currentRoute, &$routes) {

    if ($current === $destination && !empty($currentRoute)) {
        $routes[] = $currentRoute;
        return;
    }

    // luam urmatoarele sosiri
    $stmt = $pdo->prepare("
        SELECT * FROM trenuri 
        WHERE localitate_plecare = :current
        ORDER BY ora_plecare
    ");
    $stmt->execute([':current' => $current]);
    $nextTrains = $stmt->fetchAll(PDO::FETCH_ASSOC);

    foreach ($nextTrains as $train) {
        $nextStop = $train['localitate_sosire'];

        // daca am mai fost aici dam skip sa nu avem cicluri
        if (isset($visited[$nextStop])) {
            continue;
        }

        // verificam daca urmatorul tren sa fie mai tarziu
        if (!empty($currentRoute)) {
            $lastTrain = end($currentRoute);
            $lastArrival = strtotime($lastTrain['ora_sosire']);
            $nextDeparture = strtotime($train['ora_plecare']);

            if ($nextDeparture < $lastArrival) {
                continue;
            }
        }

        // adaugam trenul
        $newRoute = $currentRoute;
        $newRoute[] = $train;

        // marcam urmatorul ca vizitat
        $newVisited = $visited;
        $newVisited[$nextStop] = true;

        findRoutesRecursive($pdo, $nextStop, $destination, $newVisited, $newRoute, $routes);
    }
}
?>
</body>
</html>
