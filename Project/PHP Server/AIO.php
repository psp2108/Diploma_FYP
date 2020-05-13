<?php
ob_start();
require 'DatabaseCredentials.php';
ob_end_clean();

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db)
	or die('Could not connect to MySQL ' .mysqli_connect_error());

global $parameterList;
global $procedureID;
$deliminitor = ":-:";

if(isset($_GET["id"])){
	if(!empty($_GET["id"])){
		$procedureID = $_GET["id"];		
	}
}
if(isset($_GET["param"])){
	if(!empty($_GET["param"])){
		$parameterList = $_GET["param"];		
	}
}
/*
echo $procedureID ."<br>";
echo $parameterList;
*/
switch ($procedureID){
	case 1:
		enrollEmployeeDetails($parameterList);
		break;
	case 2:
		markAttendance($parameterList);
		break;
	case 3:
		getEmployeePSandName($parameterList);
		break;
	case 4:
		getAdminPasswordHash($parameterList);
		break;
	case 5:
		insertAdminCredentials($parameterList);
		break;
	case 6:
		insertAdminDetailsNewDepartment($parameterList);
		break;
	case 7:
		insertAdminDetailsOldDepartment($parameterList);
		break;
	case 8:
		getDepartments();
		break;
	case 9:
		getLocations();
		break;
	case 10:
		getEmailAndSendOTP($parameterList);
		break;
	case 11:
		updateAdminPassword($parameterList);
		break;
	case 12:
		getAdminProfileDetails($parameterList);
		break;
	case 13:
		getEmployees($parameterList);
		break;
	case 14:
		checkIfUsernameIsAvailable($parameterList);
		break;
	case 15:
		removeBiometricsOfEmployee($parameterList);
		break;
	case 16:
		checkForAdminLoginTable();
		break;
}

@mysqli_close();



function enrollEmployeeDetails($parameterList){
	global $dbc;
	$query = "call insertEmployeeDetails(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}
function markAttendance($parameterList){
	global $dbc;
	$query = "call markAttendence(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}
function getEmployeePSandName($parameterList){
	global $dbc, $deliminitor;
	$query = "call getEmpNameAndPS(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0] .$deliminitor .$row[1] .$deliminitor .$row[2] ."\n";
		}
	}
}
function getAdminPasswordHash($parameterList){
	global $dbc, $deliminitor;
	$query = "call getHashedPassword(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0] .$deliminitor .$row[1];
		}
	}
}
function insertAdminCredentials($parameterList){
	global $dbc;
	
	$query = "call insertAdminCredentials(" .$parameterList .")";
	$exploded = explode(",",$parameterList);
	$notInterrupted = shell_exec('java MessageDigest "' .str_replace("'","",$exploded[1]) .'" "' .str_replace("'","",$exploded[2]) .'"');
	
	if($notInterrupted == 'true'){
		$response = @mysqli_query($dbc, $query);
		if($response){
			while($row = mysqli_fetch_array($response)){
				echo $row[0] ."\n";
			}
		}
	}
	else{
		echo "Password Interrupted";
	}
	
}
function insertAdminDetailsNewDepartment($parameterList){
	global $dbc;
	$query = "call updateAdminProfileNewDepartment(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);	

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}
function insertAdminDetailsOldDepartment($parameterList){
	global $dbc;
	$query = "call updateAdminProfileOldDepartment(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}
function getDepartments(){
	global $dbc, $deliminitor;
	$query = "call getDepartmentList()";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0] .$deliminitor .$row[1] .$deliminitor .$row[2] .$deliminitor .$row[3] ."\n";
		}
	}
}
function getLocations(){
	global $dbc, $deliminitor;
	$query = "call getLocationList()";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0] .$deliminitor .$row[1] ."\n";
		}
	}
}	
function getEmailAndSendOTP($parameterList){
	global $dbc;
	$query = "call getAdminEmail(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);	

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}

function updateAdminPassword($parameterList){
	global $dbc;
	$query = "call updateAdminPassword(" .$parameterList .")";
	
	$exploded = explode(",",$parameterList);
	$notInterrupted = shell_exec('java MessageDigest "' .str_replace("'","",$exploded[1]) .'" "' .str_replace("'","",$exploded[2]) .'"');
	
	if($notInterrupted == 'true'){
		$response = @mysqli_query($dbc, $query);
		if($response){
			while($row = mysqli_fetch_array($response)){
				echo $row[0] ."\n";
			}
		}
	}
	else{
		echo "Password Interrupted";
	}
}

function getAdminProfileDetails($parameterList){
	global $dbc, $deliminitor;
	$query = "call getAdminProfile(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0] .$deliminitor 
			.$row[1] .$deliminitor 
			.$row[2] .$deliminitor 
			.$row[3] .$deliminitor 
			.$row[4] .$deliminitor 
			.$row[5] .$deliminitor 
			.$row[6] .$deliminitor 
			.$row[7] ."\n";
		}
	}
}

function getEmployees($parameterList){
	global $dbc, $deliminitor;
	$query = "call getEmployeeList(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0] .$deliminitor 
			.$row[1] .$deliminitor 
			.$row[2] .$deliminitor 
			.$row[3] .$deliminitor 
			.$row[4] ."\n";
		}
	}
}

function checkIfUsernameIsAvailable($parameterList){
	global $dbc;
	$query = "call checkAdminUserAvailability(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);	

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}

function removeBiometricsOfEmployee($parameterList){
	global $dbc;
	$query = "call removeEmployeeBiometrics(" .$parameterList .")";
	$response = @mysqli_query($dbc, $query);	

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}

function checkForAdminLoginTable(){
	global $dbc;
	$query = "call checkIfAnyAdminIsAvailable()";
	$response = @mysqli_query($dbc, $query);	

	if($response){
		while($row = mysqli_fetch_array($response)){
			echo $row[0];
		}
	}
}
?>