<?php
include 'DatabaseCredentials.php';

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db) or die('Could not connect to MySQL ' .mysqli_connect_error());

//$_POST["name"]
		
$deptID = $_GET["deptIDs"];
$df = 'Y-m-d';
$dfTemp = $_GET["dateFormats"];
$startDate = $_GET["startDateYear"]."-".$_GET["startDateMonth"]."-".$_GET["startDateDay"];
$dateTime = date_create_from_format($df, $startDate);
$startDate = date_format($dateTime, $df);
$startDate = replaceFutureDate($startDate,$df);

$startDateTemp = date_format($dateTime, $dfTemp);

$endDate = $_GET["endDateYear"]."-".$_GET["endDateMonth"]."-".$_GET["endDateDay"];
$dateTime = date_create_from_format($df, $endDate);

$endDate = date_format($dateTime, $df);
$endDate = replaceFutureDate($endDate,$df);

$endDateTemp = date_format($dateTime, $dfTemp);

$empIDArray = array();
$empNameArray = array();

$query = "call getEmployeeListByDepartmentReport($deptID)";
$response = @mysqli_query($dbc, $query);

if($response){
	while($row = mysqli_fetch_array($response)){
	global $empIDArray, $empNameArray;
		$empIDArray[] = $row[0];
		$empNameArray[] = "$row[2] $row[3] ($row[1])";
	}
}		

$DaysCount = numberOfDaysBetween2Dates($startDate, $endDate) + 1;
	
?>

<html>
<head>
<title>Institute Report</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>
<center>
<div class="table-responsive">
	<?php ob_start(); ?><table border=1  class="table table-condensed table-hover"><tr><th nowrap><center>Name</center></th><?php
			
		for($i=0; $i<$DaysCount; $i++){	
			if($dfTemp == '-n'){
				echo "<th ><center>" .($i + 1) ."</center></th>";
			}
			else{
				echo "<th ><center>" .dateIncrementer($startDate, $i, $df) ."</center></th>";
			}
		}
		
		echo "</tr>";
		
		for($employeeTraverser=0; $employeeTraverser < count($empIDArray); $employeeTraverser++){
			echo "<tr>";
			echo "<td nowrap>$empNameArray[$employeeTraverser]</td>";
			
			unset($AttendanceRow);
			$AttendanceRow = array();
			
			$dbc = @mysqli_connect($hostname, $un, $pwd, $db) or die('Could not connect to MySQL ' .mysqli_connect_error());
			
	
			$query = "call displayEmployeeAttendence($empIDArray[$employeeTraverser],'$startDate','$endDate')";
			$response = @mysqli_query($dbc, $query);
			//echo "Query Fired-> $query ";

			if($response){
				//echo "Query Executed <br>";
				while($row = mysqli_fetch_array($response)){
					$AttendanceRow[] = $row[6];
					//echo "$row[6] row <br>";
				}
				//echo "$row row";
			}
			
			while(count($AttendanceRow) != $DaysCount){
				$AttendanceRow[] = "-";
			}
			
			for($attendancePerEmp=$DaysCount - 1; $attendancePerEmp>=0; $attendancePerEmp--){
				$op = $AttendanceRow[$attendancePerEmp];
				
				if($op == "P"){
					echo "<td nowrap><font color=\"#00ff00\">$op</font></td>";
				}else if($op == "A"){
					echo "<td nowrap><font color=\"#ff0000\">$op</font></td>";
				}else if($op == "H"){
					echo "<td nowrap><font color=\"#0000ff\">$op</font></td>";
				}else {
					echo "<td nowrap>$op</td>";				
				}
				
			}
			
			echo "</tr>";
		}

		?></table><?php $content = ob_get_contents(); ?>
	</div>
	<form method="post" action="excel.php">
	<?php 
		ob_end_flush(); 
		
		echo "<input type=\"hidden\" name=\"tb\" value=\"" .htmlentities($content) ."\">";
		//echo $content;
	?>
	
		<input type="submit" name="getReportButton" value="Download Report" class="btn btn-success">
	</form>
</center>
	
	
</body>

<?php	
@mysqli_close();

function dateIncrementer($DateString, $NumberOfDays, $DateFormat){
	global $dfTemp;
	$dateTime = date_create_from_format($DateFormat, $DateString);
	$date = date_format($dateTime, $DateFormat);
	$date = date($DateFormat,strtotime("+$NumberOfDays day", strtotime($date)));
	$dateTime2 = date_create_from_format($DateFormat, $date);
	return date_format($dateTime2, $dfTemp);
}

function replaceFutureDate($DateString, $DateFormat){
	$currentDate = date($DateFormat);
	$dateTime = date_create_from_format($DateFormat, $DateString);
	$dateTime = date_format($dateTime, $DateFormat);
	
	if($dateTime > $currentDate){
		$date = date_create_from_format($DateFormat, $currentDate);
		return date_format($date, $DateFormat);
	}
	else{
		return $DateString;
	}
	
	return date_format($dateTime2, $DateFormat);
}

function numberOfDaysBetween2Dates($DateFrom, $DateTo){
	$date1=date_create($DateFrom);
	$date2=date_create($DateTo);
	$diff=date_diff($date1,$date2);
	$days=abs($diff->format('%R%a'));
	return $days;
}

/*

?>
<?php

*/


?>

























