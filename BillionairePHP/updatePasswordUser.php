<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if (isset($_POST['username']) && isset($_POST['password']) && isset($_POST['new_password'])) {

    $username = $_POST['username'];
    $password = $_POST['password'];

    $select = "SELECT * FROM user WHERE USERNAME = '$username'";
    $response = mysqli_query($connect, $select);

    if ($response) {
        $row = mysqli_fetch_row($response);
        if (mysqli_num_rows($response) != 0) {
            $dbuser = row['USERNAME'];
            $dbpassword = row['PASSWORD'];
            if ($dbuser == $username && password_verify($password, $dbpassword)) {
                $new_password = $_POST['new_password'];
                //Check old password and new password
                if (strcmp($password, $new_password) == 0) {
                    echo "The old and the new password the same";
                } else {
                    $new_password = password_hash($new_password, PASSWORD_DEFAULT);

                    $select = "UPDATE user SET PASSWORD = '$new_password' WHERE USERNAME = '$username' ";
                    $response = mysqli_query($connect, $select);
                    if ($response) {
                        echo "Update password success";
                    } else {
                        echo "Error: " . mysqli_error($connect) . "\n Update password fail";
                    }
                }
            } else {
                echo "Username or Password wrong";
                mysqli_close($connect);
            }
        } else {
            echo "Username or Password wrong";
            mysqli_close($connect);
        }
    } else {
        echo "Error: " . mysqli_error($connect) . "\n Login failed";
    }
} else echo "All fields are required";

?>
