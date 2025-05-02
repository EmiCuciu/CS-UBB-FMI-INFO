<?php

$formEmail = $_POST['email'];
$formPassword = $_POST['parola'];

mysql_connect("localhost", "root", "") or die(mysql_error());
mysql_select_db("pw") or die(mysql_error());

$query = "SELECT * FROM useri WHERE email='" . $formEmail . "'";
// print $query . "<br>";
$result = mysql_query($query);

if (mysql_num_rows($result) != 1) {
  header("Location: invalidLogin.php");
  return;
}

$row = mysql_fetch_array($result);
$dbPassword = $row['parola'];

if ($dbPassword != $formPassword) {
  header("Location: invalidLogin.php");
  return;
}

session_start();
print "Parola valida. Sunteti autentificat ca " . $formEmail . "<br>";
$_SESSION['email'] = $formEmail;
?>

Click <a href="bankAccount.php">aici</a> pentru a vedea cati bani aveti in cont.
