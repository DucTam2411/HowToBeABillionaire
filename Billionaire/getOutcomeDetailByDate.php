<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if(isset($_POST['id_outcome']) && isset($_POST['date'])){
    $id_outcome = $_POST['id_outcome'];
    $date = $_POST['date'];
    $result = array();
    $result['data'] = array();
    $select = "SELECT MONEY, DESCRIPTION, DATE, NAME, outcomdetail.IMAGE, AUDIO 
FROM outcomdetail inner join outcomecategory on outcomdetail.ID_CATEGORY = outcomecategory.ID_CATEGORY 
WHERE (DATE_FORMAT(DATE, '%d-%m-%Y') = '$date') AND (ID_OUTCOME = '$id_outcome') 
ORDER BY DATE";
    $reponse = mysqli_query($connect, $select);
    if($reponse){
    while($row = mysqli_fetch_row($reponse)){
        $index['MONEY'] = $row['0'];
        $index['DESCRIPTION'] = $row['1'];
        $index['DATE'] = $row['2'];
        $index['NAME'] = $row['3'];
        if($row['4'] == null){$index['IMAGE'] = "null";}
        else{$index['IMAGE'] = $row['4'];}
        if($row['5'] == null){$index['AUDIO'] = "null";}
        else{$index['AUDIO'] = $row['5'];}

        array_push($result['data'], $index);
    }}
    else{
        echo "Error: " . mysqli_error($connect). "\n Insert outcome false" ;
    }

    $result["success"] = "1";
    echo json_encode($result);
    mysqli_close($connect);
}
?>
