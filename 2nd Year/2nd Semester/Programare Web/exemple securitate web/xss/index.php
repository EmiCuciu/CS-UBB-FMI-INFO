<html>
<head><title>Thailanda</title></head>
<body>
<h1>Thailanda</h1>

<img src="Thailanda.jpg" width="450"><br><br>

Va place fotografia mea?

<h2>Comentarii</h2>
<?php
require_once "connect.php";

// selectam doar comentariile aprobate (stare = 1)
$stm = $pdo->query("SELECT * FROM comentarii where stare=1");
$rows = $stm->fetchAll(PDO::FETCH_ASSOC);

foreach($rows as $row) {
   $id = $row['id'];
   $nume = $row['nume'];
   $comentariu = $row['comentariu'];
   $data = $row['data'];
   print "<p>Id $id, $nume, $data: $comentariu</p>\n";
}
?>
<h2>Comenteaza si tu</h2>

<form method="POST" action="saveComment.php">
    <table>
    <tr><td>Nume:</td><td><input type="text" name="nume" style="width: 200px"></td></tr>
    <tr><td>Comentariu:</td><td><textarea name="comentariu" style="width: 200px"></textarea></td></tr>
    <tr><td colspan="2" style="text-align: right"><input type="submit" value="Commenteaza"></td></tr>
    </table>
</form>
<a href="admin.php">Autentificare admin moderare comentarii</a>
</body>
</html>