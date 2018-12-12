<?php
$servername = "localhost";
$username = "root";
$password = "sunny123";
$dbname = "arp";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

//select question_id
$command = "SELECT question_id FROM question_to_bank WHERE bank_id = '" . $_GET["bank_id"] . "'";
$result = $conn->query($command);

//if such entries exist
if($result->num_rows > 0)
{
    //print out question_id
    while($row = $result->fetch_assoc()) {
        echo $row["question_id"] . ",";
    }
}
?>