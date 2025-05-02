<?php
    error_reporting(0);
?>
<html>
<head><title>Va rugam sa introduceti numele de utilizator si parola</title></head>
<body>

<form method="POST" action="loginBad2.php">
    <table>
    <tr><td>E-mail:</td><td><input type="text" name="email"/></td></tr>
    <tr><td>Parola:</td><td><input type="password" name="parola"/></td></tr>
    <tr><td colspan="2"><input type="submit" value="Login"/></td></tr>
    </table>
</form>
</body>
</html>