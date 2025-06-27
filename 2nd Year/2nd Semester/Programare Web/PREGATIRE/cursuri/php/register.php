Am primit de la browser:<br>
<?php

print "Username: " . $_REQUEST["username"] . "<br>\n";
print "Email: " . $_REQUEST["email"] . "<br>\n";
print "Parola: " . $_REQUEST["pass"] . "<br>\n";
print "Parola2: " . $_REQUEST["pass2"] . "<br>\n";

print "Request method este: " . $_SERVER["REQUEST_METHOD"];

?>

