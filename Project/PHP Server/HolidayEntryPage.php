<?php

include 'DatabaseCredentials.php';

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db) or die('Could not connect to MySQL ' .mysqli_connect_error());

?>

<html>
<head>
<title>Holiday Master Entry</title>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>

<body>
	<form method="post" action="AddHolidayPage.php">
	<table width="100%" height="150">
		<?php
		
			echo "<tr><td>Select date: </td>";
			echo "<td><div class=\"form-inline\" >";
			echo "<select name=\"DateYear\"  class=\"form-control\" >";
				for($i = 1970; $i <= date('Y'); $i++){
					if($i != date('Y')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"DateMonth\" class=\"form-control\"  >";
				for($i = 1; $i <= 12; $i++){
					if($i != date('m')){
						echo "<option value=\"$i\">$i</option>";
					}
					else{
						echo "<option value=\"$i\" selected>$i</option>";
					}
				}
			echo "</select>";
			echo "<select name=\"DateDay\" class=\"form-control\"  >";
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
		
			echo "<tr><td>Holiday For: </td>";
			echo "<td><input type=\"text\" name=\"HolidayFor\" class=\"form-control\"> ";
			echo "</td></tr>";
			
		
			@mysqli_close();
		?>
		<tr><td colspan="2" ><center>
		<input class="btn btn-primary btn-block" type="submit" name="getReportButton" value="Add">
		</center></td></tr>
		</table>
	</form>
</body>
</html>