<?php
$user = $_POST["user"];
$parola = $_POST["parola"];

if ($user != "admin" || $parola != "654321") {
   header("Location: admin.php");
   die;
}

session_start();
$_SESSION["loggedin"] = TRUE;
header("Location: moderateComments.php");
?>
