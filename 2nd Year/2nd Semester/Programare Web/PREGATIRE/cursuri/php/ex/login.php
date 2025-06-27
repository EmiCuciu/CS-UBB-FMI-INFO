<?php
session_start();
?>

<!DOCTYPE html>
<html lang="ro">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<?php
if (isset($_SESSION['flash'])): ?>
    <p style="color: green">
        <?php
            echo htmlentities($_SESSION['flash'], ENT_QUOTES, 'UTF-8');
            unset($_SESSION['flash']);
        ?>
    </p>
<?php endif; ?>

<form method="post">
    <!-- câmpuri de login -->
    <input type="text" name="user" placeholder="Utilizator"><br>
    <input type="password" name="pass" placeholder="Parolă"><br>
    <button type="submit">Loghează‑te</button>
</form>

</body>