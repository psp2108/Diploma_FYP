<?php

include 'DatabaseCredentials.php';

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db)
	or die('Could not connect to MySQL ' .mysqli_connect_error());

$selectedDate = $_POST["DateYear"]."-".$_POST["DateMonth"]."-".$_POST["DateDay"];
$purpose = $_POST["HolidayFor"];

$query = "call addHoliday('$selectedDate','$purpose')";
$response = @mysqli_query($dbc, $query);

if($response){
	$row = mysqli_fetch_array($response);
	echo $row[0];
}
		
@mysqli_close();
header("Location:CallHolidayEntryPage.php");
exit;
?>
