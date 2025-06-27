<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lab7-3</title>
</head>
<body>
<div class="container">
    <h1>Login</h1>
    <hr>
    <form action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="POST">
        <label>Username</label><br>
        <label>
            <input type="text" name='username' placeholder="Enter Username" required>
        </label><br>
        <label>Password</label><br>
        <label>
            <input type="password" name='password' placeholder="Enter Password" required>
        </label>
        <br><br>
        <button type="submit">Login</button>
    </form>
    <hr>
    <div>

        <?php
        $host = 'localhost';
        $db = 'PHP_ProgramareWeb';
        $user = 'postgres';
        $pass = '0000';

        function test_input($data): string
        {
            $data = trim($data);
            $data = stripslashes($data);    //elimin \
            return htmlspecialchars($data);
        }

        $username = "";
        $password = "";
        if ($_SERVER["REQUEST_METHOD"] == "POST") {
            $username = test_input($_POST["username"]);
            $password = test_input($_POST["password"]);

            if (preg_match("/^[a-zA-Z][a-zA-Z0-9-_.]/", $username)) {
                try {
                    $pdo = new PDO("pgsql:host=$host;dbname=$db", $user, $pass);
                    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                    $stmt = $pdo->prepare("SELECT COUNT(*) FROM users WHERE username = :username AND password = :password");
                    $stmt->bindParam(':username', $username);
                    $stmt->bindParam(':password', $password);
                    $stmt->execute();

                    $count = $stmt->fetchColumn();

                    if ($count == 1) {
                        header("Location: addNota.php");
                        exit();
                    }
                } catch (PDOException $e) {
                    die("Conexiunea la baza de date a eșuat: " . $e->getMessage());
                }
            }
        }
        ?>
    </div>
</div>
</body>
</html>