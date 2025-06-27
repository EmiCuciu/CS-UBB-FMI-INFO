<?php
session_start();

// setam mesajul flash
$_SESSION['flash'] = "Cont creat cu succes!";

// redirectionam catre pagina de login
header("Location: login.php");
exit;