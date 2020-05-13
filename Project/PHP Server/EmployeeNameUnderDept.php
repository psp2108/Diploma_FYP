<?php
$param = $_POST['dropdownValue'];

include 'DatabaseCredentials.php';

$dbc = @mysqli_connect($hostname, $un, $pwd, $db) or die('Could not connect to MySQL ' .mysqli_connect_error());

$query = "call getEmployeeListByDepartment(\"$param\")";

$response = @mysqli_query($dbc, $query);

if($response){
	while($row = mysqli_fetch_array($response)){
		echo "<option value=\"$row[0]\">$row[1]: $row[2] $row[3]</option>";
	}
}

	
@mysqli_close();



?>