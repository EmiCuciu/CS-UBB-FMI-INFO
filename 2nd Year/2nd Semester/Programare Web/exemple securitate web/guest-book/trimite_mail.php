<?php
     $email = $_POST['email'];
     $from = $_POST['from'];

     mail("bufny@cs.ubbcluj.ro", "Salutari din Guest Book", $email, "From: $from");
     // parametrii in ordine sunt: destinatar, subiect, continut e-mail, antete e-mail
     print "Done. Mail sent.";
?>
