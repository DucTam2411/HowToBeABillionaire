<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if (isset($_POST['username']) && isset($_POST['fullname']) && isset($_POST['userimage'])) {

    $target_dir = "ImagesUser/";
    $image = $_POST['userimage'];
    $imageStore = rand() . "_" . time() . ".jpeg";
    $target_dir = $target_dir . "/" . $imageStore;
    file_put_contents($target_dir, base64_decode($image));

    $fullname = $_POST['fullname'];
    $username = $_POST['username'];
    $select = "UPDATE user SET FULLNAME = '$fullname', USERIMAGE = '$imageStore' WHERE USERNAME = '$username' ";
    $response = mysqli_query($connect, $select);
    if($response){
        echo "Update user success";
        mysqli_close($connect);
    }
    else{
        echo "Error: " . mysqli_error($connect). "\n Update user failed" ;
    }
} else echo "All fields are required";
?>