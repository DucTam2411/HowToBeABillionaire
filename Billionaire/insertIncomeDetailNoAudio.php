<?php
$connect = mysqli_connect("localhost", "id17019440_tester", "V=9Gx|_ASsl?&TD#", "id17019440_billionaire");
if (isset($_POST['idmoney']) && isset($_POST['money']) && isset($_POST['idcategory']) &&
    isset($_POST['description']) && isset($_POST['date']) && isset($_POST['image'])){

    $target_dir = "Images/";
    $image = $_POST['image'];
    $imageStore = rand(). "_" .time(). ".jpeg";
    $target_dir = $target_dir."/".$imageStore;
    file_put_contents($target_dir, base64_decode($image));

    $id_money = $_POST['idmoney'];
    $money = $_POST['money'];
    $id_category = $_POST['idcategory'];
    $description = $_POST['description'];
    $date = $_POST['date'];

    $select = "INSERT INTO incomedetail (ID_INCOME, MONEY, ID_CATEGORY, DESCRIPTION, DATE, IMAGE) VALUES (
            '$id_money', '$money', '$id_category', '$description', '$date', '$imageStore')";

    $response = mysqli_query($connect, $select);

    if($response){
        echo "Add new income detailed success";
        mysqli_close($connect);
    }
    else{
        echo "Error: " . mysqli_error($connect). "\n Add new income detailed failed" ;
    }
}
else {
    echo "All field required";
}
?>
