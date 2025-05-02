<?php
    error_reporting(0);
    session_start();

    if (!isset($_SESSION['email'])|| $_SESSION['email'] === "") {
        // userul nu este logat
        header("Location: index.php");
        die;
    }
    $email = $_SESSION['email'];

    mysql_connect("localhost", "root", "") or die(mysql_error());
    mysql_select_db("pw") or die(mysql_error());

    $query = "SELECT suma FROM useri WHERE email='$email'";
    $result = mysql_query($query);
    $row = mysql_fetch_array($result);
    $suma = $row['suma'];

    print "Sunteti autentificat ca $email<br/>";
    print "In depozitul dumneavoastra se gasesc $suma euro.<br/>";
?>
<br/>  
<a href="logout.php">Log out</a>
