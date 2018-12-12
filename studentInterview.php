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
$sql = "SELECT interview_no from student_interview_mapping where student_id=".$stuid;
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        
        echo $row["interview_no"]."<br>";
    }
} else {
    echo "no interview scheduled for this roll number";
}
}
getAns($_GET["student_id"]);
$conn->close();
?>