<?php	
			
include 'DatabaseCredentials.php';
$dbc = @mysqli_connect($hostname, $un, $pwd, $db) or die('Could not connect to MySQL ' .mysqli_connect_error());
	
$Username = $_POST["username"];		
$Password = $_POST["password"];		
	
$query = "call getHashedPassword('" .$Username ."')";
$response = @mysqli_query($dbc, $query);
	
if($response){
	if($row = mysqli_fetch_array($response)){
		$passwordMatched = shell_exec("java MessageDigest \"$Password\" \"$row[0]\"");
		
		if($passwordMatched == 'false'){
			header("Location:index.php");
			exit;
		}
	}
	else{
		header("Location:index.php");
		exit;
	}
}
else{
	header("Location:index.php");
	exit;
}

header("Location:CallHomePage.php");
exit;
	

?>