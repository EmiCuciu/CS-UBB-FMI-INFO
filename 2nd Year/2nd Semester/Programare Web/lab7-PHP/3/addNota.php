<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lab7-3</title>
</head>
<body>
<div class="container">
    <h1>Adaugare Nota</h1>
    <hr>

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
    ?>

    Student:
    <select name="idStudent" form="notare">
        <?php
        try {
            $stmt = $pdo->query("SELECT * FROM students");
            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                echo "<option value='" . htmlspecialchars($row["id"]) . "'>" . htmlspecialchars($row["nume"]) . "</option>";
            }
        } catch (PDOException $e) {
            echo "Eroare: " . $e->getMessage();
        }
        ?>
    </select>
    <br><br>

    Disciplina:
    <select name='idDisciplina' form="notare">
        <?php
        try {
            $stmt = $pdo->query("SELECT * FROM subjects");
            while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                echo "<option value='" . htmlspecialchars($row["id"]) . "'>" . htmlspecialchars($row["nume"]) . "</option>";
            }
        } catch (PDOException $e) {
            echo "Eroare: " . $e->getMessage();
        }
        ?>
    </select>
    <br><br>

    Nota:
    <select name="idNota" form="notare">
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
        <option value="4">4</option>
        <option value="5">5</option>
        <option value="6">6</option>
        <option value="7">7</option>
        <option value="8">8</option>
        <option value="9">9</option>
        <option value="10">10</option>
    </select>
    <br>
    <br>
    <form id="notare" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="POST">
        <input type="submit" value="Noteaza">
    </form>
    <hr>

    <div>
        <?php
        function test_input($data) {
            $data = trim($data);
            $data = stripslashes($data);
            return htmlspecialchars($data);
        }

        $idS = "";
        $idD = "";
        $nota = "";
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            $idS = test_input($_POST["idStudent"]);
            $idD = test_input($_POST["idDisciplina"]);
            $nota = test_input($_POST["idNota"]);

            try {
                $stmt = $pdo->prepare("INSERT INTO notare(ids, idd, nota) VALUES (:ids, :idd, :nota)");
                $stmt->bindParam(':ids', $idS);
                $stmt->bindParam(':idd', $idD);
                $stmt->bindParam(':nota', $nota);

                if ($stmt->execute()) {
                    header("Location: showNote.php");
                    exit();
                }
            } catch (PDOException $e) {
                echo "Eroare: " . $e->getMessage();
            }
        }
        ?>
    </div>
</div>
</body>
</html>