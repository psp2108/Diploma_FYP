<?php

include 'DatabaseCredentials.php';

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db) or die('Could not connect to MySQL ' .mysqli_connect_error());

?>

<html>
<head>

  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

  
  
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
	<script>
		$(document).ready(function(){
		$('#_deptIDs').change(function(){
			//Selected value
			var inputValue = $(this).val();

			//Ajax for calling php function
			$.post('EmployeeNameUnderDept.php', { dropdownValue: inputValue }, function(data){
			
				$("#_empIDs option").remove();
				$('#_empIDs').append(data);
				//alert('ajax completed. Response:  '+data); 
				//do after submission operation in DOM
			});
		});
	});
	</script>

</head>

<body>
	<form method="get" action="RuntimeEmployeeReport.php">
	<table width="100%" height="250">
		<?php
		
			//////////////////////////////////////////////////////////////////////
			$query = "call getDepartmentList()";
			$response = @mysqli_query($dbc, $query);
			
			$firstEntry = true;
			
			
			echo "<tr><td>Select Institute: </td>";
			echo "<td><select name=\"deptIDs\" id=\"_deptIDs\" class=\"form-control\" data-live-search=\"true\">";

			if($response){
				while($row = mysqli_fetch_array($response)){
					echo "<option value=\"$row[0]\">$row[1]: $row[2] ($row[3])</option>";
					if($firstEntry){
						$firstEntry = false;
						$dept_ID = $row[0];
					}
				}
			}
			echo "</select></td></tr>";
			echo "</div>";
			
			////////////////////////////////////////////////////////////////////////
			/*
			
			
			echo "Select Student. ";
			echo "<select name=\"empIDs\" >";

			echo "</select> <br><br>";
			
			*/
			////////////////////////////////////////////////////////////////////////
			$dbc = @mysqli_connect($hostname, $un, $pwd, $db) or die('Could not connect to MySQL ' .mysqli_connect_error());
			$query = "call getEmployeeListByDepartmentReport($dept_ID)";
			$response = @mysqli_query($dbc, $query);
			
			echo "<tr><td>Select Student: </td>";
			echo "<td><select name=\"empIDs\"  class=\"form-control\" id=\"_empIDs\">";
			
			if($response){
				while($row = mysqli_fetch_array($response)){
					echo "<option value=\"$row[0]\">$row[1]: $row[2] $row[3]</option>";
				}
			}
			
			echo "</select></td></tr>";
			///////////////////////////////////////////////////////////////////////
		
			echo "<tr><td>Starting date: </td>";
			echo "<td><div class=\"form-inline\" >";
			echo "<select name=\"startDateYear\"  class=\"form-control\" >";
				for($i = 1970; $i <= date('Y'); $i++){
					if($i != date('Y')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"startDateMonth\" class=\"form-control\"  >";
				for($i = 1; $i <= 12; $i++){
					if($i != date('m')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"startDateDay\" class=\"form-control\"  >";
				for($i = 1; $i <= 31; $i++){
					if($i != date('d')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "</div></td></tr>";
		
			echo "<tr><td>Ending date: </td>";
			echo "<td><div class=\"form-inline\" >";
			echo "<select name=\"endDateYear\" class=\"form-control\"  >";
				for($i = 1970; $i <= date('Y'); $i++){
					if($i != date('Y')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"endDateMonth\" class=\"form-control\"  >";
				for($i = 1; $i <= 12; $i++){
					if($i != date('m')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"endDateDay\" class=\"form-control\"  >";
				for($i = 1; $i <= 31; $i++){
					if($i != date('d')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "</div></td></tr>";
			
		
			@mysqli_close();
		?>
		<tr><td colspan="2" ><center>
		<input class="btn btn-primary btn-block" type="submit" name="getReportButton" value="Get Report">
		</center></td></tr>
		</table>
	</form>
</body>
</html>