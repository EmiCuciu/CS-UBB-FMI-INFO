<?php
    error_reporting(0);
    session_start();

    if (!isset($_SESSION['email'])|| $_SESSION['email'] === "") {
        // userul nu este logat
        header("Location: index.php");
        die;
    }
?>
<form action="doTransfer.php" method="GET">
<table>
   <tr><td>E-mail destinatar:</td><td><input type="text" name="contdestinatie"></td></tr>
   <tr><td>Suma:</td><td><input type="text" name="suma"></td></tr>
   <tr><td colspan="2"><input type="submit" value="Transfera"></td></tr>
</table>
</form>
