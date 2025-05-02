<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Adaugare comentariu</title>
<body>
<?php
error_reporting(0);

$nume = addslashes(htmlentities($_POST["nume"], ENT_COMPAT, "UTF-8"));
$comentariu = addslashes(htmlentities($_POST["comentariu"], ENT_COMPAT, "UTF-8"));

//Pentru testarea vulnerabilitatii XSS comentati liniile de mai sus,
// si decomentati liniile de mai jos
//$nume = $_POST["nume"];
//$comentariu = $_POST["comentariu"];

require_once "connect.php";

$sql = "INSERT INTO comentarii (nume, comentariu) VALUES (:nume, :comentariu)";
$stmt = $pdo->prepare($sql);
$stmt->bindParam(':nume', $nume);
$stmt->bindParam(':comentariu', $comentariu);
if (! $stmt->execute())
    print "Eroare la salvarea comentariului dumneavoastra in baza de date.<br>";
else
    print "Comentariul dumneavoastra a fost transmis spre moderare administratorului.<br>";
?>
Click <a href="index.php">aici</a> pentru a va intoarce la pagina principala.
</body>
</html>