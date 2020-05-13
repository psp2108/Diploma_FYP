<html>
<head>
<title>Student Report</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<center>
<?php

include 'DatabaseCredentials.php';

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db)
	or die('Could not connect to MySQL ' .mysqli_connect_error());

//$_POST["name"]
		
$empID = $_GET["empIDs"];
$startDate = $_GET["startDateYear"]."-".$_GET["startDateMonth"]."-".$_GET["startDateDay"];
$endDate = $_GET["endDateYear"]."-".$_GET["endDateMonth"]."-".$_GET["endDateDay"];

$query = "call displayEmployeeAttendence($empID,'$startDate','$endDate')";
$response = @mysqli_query($dbc, $query);

if($response){
	$row = mysqli_fetch_array($response);
	
	echo "<h3>Details of $row[1] $row[2] ($row[0]), from $startDate to $endDate</h3>";		                                                                                 
	echo "<div class=\"table-responsive\">";   
	
	ob_start();
	echo "<table border=1  class=\"table table-condensed table-hover\">";
		echo "<tr>";
		/*echo "<th>PS Number</th>";
		echo "<th>First Name</th>";
		echo "<th>Last Name</th>";*/
		echo "<th><center>Date</center></th>";
		echo "<th><center>In Time</center></th>";
		echo "<th><center>Out Time</center></th>";
		echo "<th><center>Status</center></th>";
		echo "</tr>";
		
		do{
			echo "<tr>";
			/*echo "<td>$row[0]</td>";
			echo "<td>$row[1]</td>";
			echo "<td>$row[2]</td>";*/
			echo "<td>$row[3]</td>";
			echo "<td>$row[4]</td>";
			echo "<td>$row[5]</td>";
			$op = $row[6];
			
			if($op == "P"){
				echo "<td nowrap><font color=\"#00ff00\">$op</font></td>";
			}else if($op == "A"){
				echo "<td nowrap><font color=\"#ff0000\">$op</font></td>";
			}else if($op == "H"){
				echo "<td nowrap><font color=\"#0000ff\">$op</font></td>";
			}else {
				echo "<td nowrap>$op</td>";				
			}
			echo "</tr>";
		}while($row = mysqli_fetch_array($response));
		
	echo "</table>";
	$content = ob_get_contents();
	ob_end_flush(); 
	echo "</div>";
	
	?>
	
	<form method="post" action="excel.php">
		
	<?php
	echo "<input type=\"hidden\" name=\"tb\" value=\"" .htmlentities($content) ."\">";
	?>
	
	<input type="submit" name="getReportButton" value="Download Report" class="btn btn-success" >
	</form>
</center>
</body>
</html>

<?php

}
		
@mysqli_close();
?>
