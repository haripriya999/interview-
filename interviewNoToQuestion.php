<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "interview_database";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
function getAns($stuid){
    global $conn;
$sql = "SELECT question_bank from interview_questiontable where interview_no=".$stuid;
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
    
        echo $row["question_bank"]."<br>";
    }
} else {
    echo "no interview scheduled for this roll number";
}
}
getAns($_GET["interview"]);
$conn->close();
?>