<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if(isset($_POST['username']) && isset($_POST['password'])){
    $username = $_POST['username'];
    $password = $_POST['password'];

    $select = "SELECT * FROM user WHERE USERNAME = '$username'";
    $response = mysqli_query($connect, $select);

    if($response){
        $row = mysqli_fetch_row($response);
        if (mysqli_num_rows($response) != 0) {
            $dbuser = row['USERNAME'];
            $dbpassword = row['PASSWORD'];
            if($dbuser == $username && password_verify($password, $dbpassword)){
                $result = array();
                $result['data'] = array();

                $select = "SELECT * FROM user WHERE USERNAME = '$username' ";
                $reponse = mysqli_query($connect, $select);

                while($row = mysqli_fetch_row($reponse)){
                    $index['ID_USER'] = $row['0'];
                    array_push($result['data'], $index);
                }

                $result["success"] = "Login Success";
                echo json_encode($result);
                mysqli_close($connect);
            }
            else{
                echo "Username or Password wrong";
                mysqli_close($connect);
            }
        }
        else{
            echo "Username or Password wrong";
            mysqli_close($connect);
        }
    }else {
        echo "Error: " . mysqli_error($connect) . "\n Login failed";
    }

} else echo "All fields are required";

?>