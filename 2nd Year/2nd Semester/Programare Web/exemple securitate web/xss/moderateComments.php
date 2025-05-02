<?php
require_once "checkSession.php";
require_once "connect.php";
$stm = $pdo->query("SELECT * FROM comentarii where stare=0");
$rows = $stm->fetchAll(PDO::FETCH_ASSOC);
?>
<table>
<tr><th>Id</th><th>Nume</th><th>Comentariu</th><th>Data</th><th>Aproba</th><th>Sterge</th></tr>
<?php
foreach($rows as $row) {
   $id = $row['id'];
   $nume = $row['nume'];
   $comentariu = $row['comentariu'];
   $data = $row['data'];
   print "<tr><td>$id</td><td>$nume</td><td>$comentariu</td><td>$data</td>";
   print "<td><a href='approve.php?id=$id'>aproba</a></td>";
   print "<td><a href='delete.php?id=$id'>sterge</td></tr>";
}
?>
</table>
<a href="logout.php">Logout</a>
<br><br>
Observatie: Comentariul cu id-ul 8 nu poate fi nici sters, nici aprobat.
Este pastrat in lista comentariilor neaprobate pentru a demonstra injectia
JavaScript care se poate face (exploatarea vulnerabilitatii XSS) pentru a fura
cookie-ul de sesiune al administratorului si a-l trimite spre logare unui
site controlat de atacator.
