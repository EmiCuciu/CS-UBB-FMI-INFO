<?php
require_once "connect.php";

$id = -1;
if (isset($_GET["id"]) && is_numeric($_GET["id"]))
  $id = addslashes($_GET["id"]);
 
// hardcoded pentru a persista comentariul care exploateaza vulnerabilitatea XSS
if ($id == 8) $id=-1; 

$stmt = $pdo->prepare("delete from comentarii where stare=0 and id=:id");
// se pot sterge doar comentariile neaprobate (aflate in starea 0)
$stmt->bindParam(":id", $id);
$stmt->execute();
header("Location: moderateComments.php");
?>
