<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lab7-3</title>
</head>
<body>
<div class="container">
    <h1>Afisare Note</h1>
    <hr>

    <table border="1">
        <tr>
            <th>Nume</th>
            <th>Denumire</th>
            <th>Nota</th>
        </tr>
        <?php
        $host = 'localhost';
        $db = 'PHP_ProgramareWeb';
        $user = 'postgres';
        $pass = '0000';

        try {
            $pdo = new PDO("pgsql:host=$host;dbname=$db", $user, $pass);
            $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

            $sql = "SELECT s.nume as nume, sb.nume as denumire, n.nota as nota 
                    FROM students s, subjects sb, notare n 
                    WHERE s.id = n.ids AND sb.id = n.idd";
            $stmt = $pdo->query($sql);

            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                echo "<tr>";
                echo "<td>" . htmlspecialchars($row["nume"]) . "</td>";
                echo "<td>" . htmlspecialchars($row["denumire"]) . "</td>";
                echo "<td>" . htmlspecialchars($row["nota"]) . "</td>";
                echo "</tr>";
            }
        } catch (PDOException $e) {
            die("Conexiunea la baza de date a eșuat: " . $e->getMessage());
        }
        ?>
    </table>
</div>
</body>
</html>