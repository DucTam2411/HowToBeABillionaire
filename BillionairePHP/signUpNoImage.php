<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if (isset($_POST['username']) && isset($_POST['password']) && isset($_POST['email']) &&
    isset($_POST['fullname']) && isset($_POST['datestart']) && isset($_POST['income'])) {

    $username = $_POST['username'];
    $email = $_POST['email'];


    $select ="SELECT * FROM user WHERE USERNAME = $username and EMAIL = $email ";
    $response = mysqli_query($connect, $select);

    if($response){
        $row = mysqli_fetch_row($response);
        if($row == 0){
            $fullname = $_POST['fullname'];
            $datestart = $_POST['datestart'];
            $income = $_POST['income'];
            $password = $_POST['password'];
            $password = password_hash($password, PASSWORD_DEFAULT);

            $select = "INSERT INTO user(USERNAME, PASSWORD, EMAIL, FULLNAME, DATESTART, INCOME) VALUES (
            '$username', '$password', '$email', '$fullname', '$datestart', ''$income')";
            $response = mysqli_query($connect, $select);

            if ($response) {
                echo "Sign Up success";
                mysqli_close($connect);
            } else {
                echo "Error: " . mysqli_error($connect) . "\n Sign up Failed";
            }
        } else{
            echo "This username or email has already exist.";
        }
    } else{
        echo "Error: " . mysqli_error($connect). "\n Sign up Failed" ;
    }
} else echo "All fields are required";
?>
