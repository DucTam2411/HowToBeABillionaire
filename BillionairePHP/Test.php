<?php
$connect = mysqli_connect("localhost", "id17019440_tester",
    "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");

$result = array();
$result['data'] = array();
$select = "SELECT MONEY, DESCRIPTION, DATE, NAME, incomedetail.IMAGE, AUDIO 
FROM incomedetail inner join incomecategory on incomedetail.ID_CATEGORY = incomecategory.ID_CATEGORY 
WHERE (DATE_FORMAT(DATE, '%d-%m-%Y') = '14-06-2021') AND (ID_INCOME = '1') 
ORDER BY DATE";
$reponse = mysqli_query($connect, $select);

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
}

$result["success"] = "1";
echo json_encode($result);
mysqli_close($connect);
?>