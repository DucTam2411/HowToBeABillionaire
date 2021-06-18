<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if(isset($_POST['id_income'])){
    $id_income = $_POST['id_income'];
    $result = array();
    $result['data'] = array();
    $select = "SELECT DATE_FORMAT(DATE, '%d-%m-%Y') DATE, COUNT(*) FROM incomedetail  WHERE ID_INCOME = '$id_income' GROUP BY DATE_FORMAT(DATE, '%d-%m-%Y')";
    $reponse = mysqli_query($connect, $select);

    while($row = mysqli_fetch_row($reponse)){
        $index['DATE'] = $row['0'];
        $index['COUNT'] = $row['1'];

        array_push($result['data'], $index);
    }

    $result["success"] = "1";
    echo json_encode($result);
    mysqli_close($connect);
}
?>