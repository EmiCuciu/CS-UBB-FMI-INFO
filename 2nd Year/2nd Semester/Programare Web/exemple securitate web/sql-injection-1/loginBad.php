<?php
    $formEmail = $_POST['email'];
    $formPassword = $_POST['parola'];

    mysql_connect("localhost", "root", "") or die(mysql_error());
    mysql_select_db("pw") or die(mysql_error());

    $query = "SELECT * FROM useri WHERE email='$formEmail' AND parola='$formPassword'";
    $result = mysql_query($query);
    if (mysql_num_rows($result) == 0) {
        // Userul si parola sunt incorecte
        header("Location: invalidLogin.php");
    }
    else { // Userul si parola sunt corecte, autentificare reusita
        session_start();
        $_SESSION["email"] = $formEmail;
        header("Location: bankAccount.php");
    }
?>
