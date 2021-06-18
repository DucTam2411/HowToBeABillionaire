<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if (isset($_POST['email'])) {
    $email = $_POST['email'];
    $result = array();

    $select = "SELECT * FROM user WHERE EMAIL = '$email' ";
    $reponse = mysqli_query($connect, $select);

    while($row = mysqli_fetch_row($reponse)){
        $index['PASSWORD'] = $row['2'];
        array_push($result['data'], $index);
    }

    $result["success"] = "1";
    echo json_encode($result);
    mysqli_close($connect);
} else echo "All fields are required";
?>
