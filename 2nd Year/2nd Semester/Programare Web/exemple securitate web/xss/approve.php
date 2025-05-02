<?php
require_once "checkSession.php";
require_once "connect.php";

$id = -1;
if (isset($_GET["id"]) && is_numeric($_GET["id"]))
  $id = addslashes($_GET["id"]);

// hardcoded pentru a persista comentariul care exploateaza vulnerabilitatea XSS
if ($id == 8) $id=-1;

$stmt = $pdo->prepare("update comentarii set stare=1 where id=:id");
$stmt->bindParam(":id", $id);
$stmt->execute();
header("Location: moderateComments.php");
?>
