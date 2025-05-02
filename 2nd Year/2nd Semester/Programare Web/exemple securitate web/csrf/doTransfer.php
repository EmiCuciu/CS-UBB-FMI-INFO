<?php
    error_reporting(0);
    session_start();
        
    if (!isset($_SESSION['email'])|| $_SESSION['email'] === "") {
       // userul nu este logat
       header("Location: index.php");
       die;
    }

    mysql_connect("localhost", "root", "") or die(mysql_error());
    mysql_select_db("pw") or die(mysql_error());

    $contsursa = $_SESSION['email'];
    $contdestinatie = $_GET['contdestinatie'];
    $suma = $_GET['suma'];

    // TODO - best practice: cele doua query-uri de mai jos ar trebui facute tranzactional    
    $query = "UPDATE useri SET suma = suma + $suma WHERE email='$contdestinatie'";
    $result = mysql_query($query);
    if (mysql_affected_rows() == 1) { // am transferat banii cu succes, scade-i de la expeditor
       $query = "UPDATE useri SET suma = suma - $suma WHERE email='$contsursa'";
       $result = mysql_query($query);
       echo "Am transferat banii cu succes. Este posibil sa fiti pe minus.<br>";
    } else {
       echo "Nu am putut transfera banii. Destinatar invalid.<br>";
    }
?>
<a href="bankAccount.php">Verifica soldul</a></br>
<a href="logout.php">Log out</a>
