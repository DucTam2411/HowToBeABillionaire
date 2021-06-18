<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if(isset($_POST['id_outcome']) && isset($_POST['datestart']) && isset($_POST['dateend'])){
    $id_outcome = $_POST['id_outcome'];
    $datestart = $_POST['datestart'];
    $dateend = $_POST['dateend'];

    $result = array();
    $result['data'] = array();
    $select = "SELECT outcomdetail.ID_CATEGORY, NAME, SUM(MONEY) TOTAL
    FROM outcomdetail INNER join outcomecategory on outcomdetail.ID_CATEGORY = outcomecategory.ID_CATEGORY
    WHERE ID_OUTCOME = '$id_outcome' and DATE BETWEEN '$datestart' AND '$dateend'
    GROUP by outcomdetail.ID_CATEGORY";

    $reponse = mysqli_query($connect, $select);
    if($reponse){
        while($row = mysqli_fetch_row($reponse)){
            $index['NAME'] = $row['1'];
            $index['TOTAL'] = $row['2'];

            array_push($result['data'], $index);
        }}
    else{
        echo "Error: " . mysqli_error($connect). "\n Get income false" ;
    }

    $result["success"] = "1";
    echo json_encode($result);
    mysqli_close($connect);
}
?>