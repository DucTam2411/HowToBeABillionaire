<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

if(isset($_POST['id_outcomedetail'])){
    $id_outcomedetail = $_POST['id_outcomedetail'];
    $result = array();
    $result['data'] = array();
    $select = "SELECT MONEY, DESCRIPTION, DATE, NAME, outcomecategory.IMAGE, outcomdetail.IMAGE, AUDIO 
FROM outcomdetail inner join outcomecategory on outcomdetail.ID_CATEGORY = outcomecategory.ID_CATEGORY 
WHERE ID_OUTCOMEDETAIL = '$id_outcomedetail'";
    $reponse = mysqli_query($connect, $select);

    while($row = mysqli_fetch_row($reponse)){
        $index['MONEY'] = $row['0'];
        $index['DESCRIPTION'] = $row['1'];
        $index['DATE'] = $row['2'];
        $index['NAME'] = $row['3'];
        if($row['4'] == null){$index['IMAGECATEGORY'] = "null";}
        else{$index['IMAGECATEGORY'] = $row['4'];}
        if($row['5'] == null){$index['IMAGE'] = "null";}
        else{$index['IMAGE'] = $row['5'];}
        if($row['6'] == null){$index['AUDIO'] = "null";}
        else{$index['AUDIO'] = $row['6'];}

        array_push($result['data'], $index);
    }

    $result["success"] = "1";
    echo json_encode($result);
    mysqli_close($connect);
}
?>
