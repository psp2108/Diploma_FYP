<?php

include 'DatabaseCredentials.php';

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db)
	or die('Could not connect to MySQL ' .mysqli_connect_error());

?>

<html>
<head>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 </head>

<body>
	<form method="get" action="RuntimeDepartmentReport.php">
	
	<table width="100%" height="250">
		<?php
		
		
			$query = "call getDepartmentList()";
			$response = @mysqli_query($dbc, $query);
			
			echo "<tr><td>Select Institute: </td>";
			echo "<td><select name=\"deptIDs\"  class=\"form-control\" data-live-search=\"true\">";

			if($response){
				while($row = mysqli_fetch_array($response)){
					echo "<option value=\"$row[0]\">$row[1]: $row[2] ($row[3])</option>";
				}
			}
			echo "</select></td>";
		
		
			echo "<tr><td>Starting date: </td>";
			echo "<td><div class=\"form-inline\" >";
			echo "<select name=\"startDateYear\"  class=\"form-control\" data-live-search=\"true\">";
				for($i = 1970; $i <= date('Y'); $i++){
					if($i != date('Y')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"startDateMonth\"  class=\"form-control\" data-live-search=\"true\">";
				for($i = 1; $i <= 12; $i++){
					if($i != date('m')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"startDateDay\"  class=\"form-control\" data-live-search=\"true\">";
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
			echo "<select name=\"endDateYear\"  class=\"form-control\" data-live-search=\"true\">";
				for($i = 1970; $i <= date('Y'); $i++){
					if($i != date('Y')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"endDateMonth\"  class=\"form-control\" data-live-search=\"true\">";
				for($i = 1; $i <= 12; $i++){
					if($i != date('m')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"endDateDay\"  class=\"form-control\" data-live-search=\"true\">";
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
		
		<tr>
		<td>Select Date Format: </td>
		<td>
			<select name="dateFormats"  class="form-control">
				<option value="d-m-y">dd-mm-yy</option>
				<option value="d-y-m">dd-yy-mm</option>
				<option value="y-m-d">yy-mm-dd</option>
				<option value="y-d-m">yy-dd-mm</option>
				<option value="m-y-d">mm-yy-dd</option>
				<option value="m-d-y">mm-dd-yy</option>
				<option value="d-m-Y">dd-mm-yyyy</option>
				<option value="d-Y-m">dd-yyyy-mm</option>
				<option value="Y-m-d">yyyy-mm-dd</option>
				<option value="Y-d-m">yyyy-dd-mm</option>
				<option value="m-Y-d">mm-yyyy-dd</option>
				<option value="m-d-Y">mm-dd-yyyy</option>
				<option value="m-d">mm-dd</option>
				<option value="d-m">dd-mm</option>
				<option value="d m y">dd mm yy</option>
				<option value="d y m">dd yy mm</option>
				<option value="y m d">yy mm dd</option>
				<option value="y d m">yy dd mm</option>
				<option value="m y d">mm yy dd</option>
				<option value="m d y">mm dd yy</option>
				<option value="d m Y">dd mm yyyy</option>
				<option value="d Y m">dd yyyy mm</option>
				<option value="Y m d">yyyy mm dd</option>
				<option value="Y d m">yyyy dd mm</option>
				<option value="m Y d">mm yyyy dd</option>
				<option value="m d Y">mm dd yyyy</option>
				<option value="m d">mm dd</option>
				<option value="d m">dd mm</option>
				<option value="d/m/y">dd/mm/yy</option>
				<option value="d/y/m">dd/yy/mm</option>
				<option value="y/m/d">yy/mm/dd</option>
				<option value="y/d/m">yy/dd/mm</option>
				<option value="m/y/d">mm/yy/dd</option>
				<option value="m/d/y">mm/dd/yy</option>
				<option value="d/m/Y">dd/mm/yyyy</option>
				<option value="d/Y/m">dd/yyyy/mm</option>
				<option value="Y/m/d">yyyy/mm/dd</option>
				<option value="Y/d/m">yyyy/dd/mm</option>
				<option value="m/Y/d">mm/yyyy/dd</option>
				<option value="m/d/Y">mm/dd/yyyy</option>
				<option value="m/d">mm/dd</option>
				<option value="d/m">dd/mm</option>
				<option value="d">dd</option>
				<option value="-n">Numeric 1 .. n</option>
			</select>
		</td>
		</tr>
		
		
		<tr><td colspan="2" ><center>
		<input class="btn btn-primary btn-block" type="submit" name="getReportButton" value="Get Report">
		</center></td></tr>
		</table>
	</form>
</body>
</html>