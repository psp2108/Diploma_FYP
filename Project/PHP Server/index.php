<!DOCTYPE html>
<html>
	<head>
		<meta content="text/html; charset=UTF-8" http-equiv="content-type">
		<title>Login Form</title>
		<link rel="stylesheet" href="css/loginstyle.css">
	</head>

	<body>
		<div class="login-page">
			<div class="form"> 
			<img style="height: 100px; width: 100px;margin-bottom:20px;" alt="LNT_ICON" src="images/lntlogo.png">
				<form action="VerifiedCallHomePage.php" method="post"> 
					<input name="username" type="text" placeholder="Username">
					<input name="password" type="password" placeholder="Password"> 
					<button >LOGIN</button>
				</form>
			</div>
		</div>
		<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
		<script src="js/index.js"></script>
	</body>
</html>