      <html lang="zxx">
        <head>
          <meta content="text/html; charset=utf-8" http-equiv="content-type">
          <title>Deft a Corporate Category Bootstrap Responsive Website Template
            | Home :: w3layouts</title>
          <!-- custom-theme -->
          <meta name="viewport" content="width=device-width, initial-scale=1">
          <meta name="keywords" content="Deft Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template, 
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design">
          <script type="application/x-javascript">
		addEventListener("load", function () {
			setTimeout(hideURLbar, 0);
		}, false);

		function hideURLbar() {
			window.scrollTo(0, 1);
		}
	</script>
          <!-- //custom-theme -->
          <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all">
          <!-- Owl-carousel-CSS -->
          <link href="css/owl.carousel.css" rel="stylesheet">
          <link rel="stylesheet" href="css/team.css" type="text/css" media="all">
          <link href="css/style.css" rel="stylesheet" type="text/css" media="all">
          <!-- font-awesome-icons -->
          <link href="css/font-awesome.css" rel="stylesheet">
          <!-- //font-awesome-icons -->
          <link href="//fonts.googleapis.com/css?family=Raleway:100,100i,200,200i,300,300i,400,400i,500,500i,600,600i,700,700i,800"

            rel="stylesheet">
          <link href="//fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i,800"

            rel="stylesheet">
        </head>
        <body> <!-- banner -->
          <div class="main_section_agile" id="home">
            <div class="agileits_w3layouts_banner_nav">
              <nav class="navbar navbar-default">
                <div class="navbar-header navbar-left"> <button type="button" class="navbar-toggle collapsed"

                    data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
                    <span class="icon-bar"></span> <span class="icon-bar"></span>
                  </button>
                  <h1><a class="navbar-brand" href="index.html"> <span>B</span>iometric
                      Attendance</a></h1>
                </div>
                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse navbar-right" id="bs-example-navbar-collapse-1">
                  <nav class="menu-hover-effect menu-hover-effect-4">
                    <ul class="nav navbar-nav">
                      <li><a href="index.html" class="hvr-ripple-in"> Home</a></li>
                      <li class="active"><a href="report.php" class="hvr-ripple-in">Attendance
                          Report</a></li>
                      <li><br>
                      </li>
                      <li><br>
                      </li>
                      <li class="dropdown"> <br>
                        <ul class="dropdown-menu agile_short_dropdown">
                          <li><a href="icons.html">Web Icons</a></li>
                          <li><a href="typography.html">Typography</a></li>
                        </ul>
                      </li>
                      <li><br>
                      </li>
                    </ul>
                  </nav>
                </div>
              </nav>
              <div class="clearfix"> </div>
            </div>
          </div>
          <div class="about" id="welcome">
            <div class="container"><br>
            </div>
          </div>
          <div class="modal fade" id="myModal4" tabindex="-1" role="dialog">
            <div class="modal-dialog">
              <!-- Modal content-->
              <div class="modal-content">
                <div class="modal-header"> <button type="button" class="close"

                    data-dismiss="modal">�</button>
                  <div class="modal-info">
                    <h4>Deft</h4>
                    <img src="images/banner2.jpg" alt=" " class="img-responsive">
                    <h5>Sub Heading here</h5>
                    <p class="para-agileits-w3layouts">Duis sit amet nisi quis
                      leo fermentum vestibulum vitae eget augue. Nulla quam
                      nunc, vulputate id urna at, tempor tincidunt metus. Sed
                      feugiat quam nec mauris mattis malesuada.</p>
                  </div>
                </div>
              </div>
            </div>
            <!-- Modal4 --></div>
          <br>
          <br>
          <br>
          
          <?php
$un = "root";
$pwd = "root";
$hostname = "localhost";
$db = "onlineattendence";

//global $dbc;
$dbc = @mysqli_connect($hostname, $un, $pwd, $db)
	or die('Could not connect to MySQL ' .mysqli_connect_error());

?>

<html>
<head></head>

<body>
	<form method="post" action="RuntimeReport.php">
		
		<?php
		
		
			$query = "call getEmployeeList(null)";
			$response = @mysqli_query($dbc, $query);
			
			echo "Select Employee to view whole attendance. ";
			echo "<select name=\"empIDs\" >";

			if($response){
				while($row = mysqli_fetch_array($response)){
					echo "<option value=\"$row[0]\">$row[1]: $row[2] $row[3]</option>";
				}
			}
			echo "</select> <br><br>";
		
		
			echo "Select Starting date: ";
			echo "<select name=\"startDateYear\" >";
				for($i = 1970; $i <= 2100; $i++){
					echo "<option value=\"$i\">$i</option>";
				}
			echo "</select>";
			echo "<select name=\"startDateMonth\" >";
				for($i = 1; $i <= 12; $i++){
					echo "<option value=\"$i\">$i</option>";
				}
			echo "</select>";
			echo "<select name=\"startDateDay\" >";
				for($i = 1; $i <= 31; $i++){
					echo "<option value=\"$i\">$i</option>";
				}
			echo "</select> <br><br>";
		
		
			echo "Select Ending Date date: ";
			echo "<select name=\"endDateYear\" >";
				for($i = 1970; $i <= 2100; $i++){
					echo "<option value=\"$i\">$i</option>";
				}
			echo "</select>";
			echo "<select name=\"endDateMonth\" >";
				for($i = 1; $i <= 12; $i++){
					echo "<option value=\"$i\">$i</option>";
				}
			echo "</select>";
			echo "<select name=\"endDateDay\" >";
				for($i = 1; $i <= 31; $i++){
					echo "<option value=\"$i\">$i</option>";
				}
			echo "</select> <br><br>";
		
			@mysqli_close();
		?>
		<input type="submit" name="getReportButton" value="Get Report">
	</form>
</body>
</html>

    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <!-- footer -->
    <div class="footer">
      <div class="f-bg-w3l">
        <div class="container">
          <div class="col-md-8 w3layouts_footer_grid">
            <p>� 2017 Larsen &amp; Toubro. All Rights Reserved | Design by <a href="https://w3layouts.com/"

target="_blank">w3layouts</a></p>
          </div>
          <div class="clearfix"> </div>
        </div>
      </div>
    </div>
    <!-- //footer -->
    <!-- js -->
    <script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
    <!-- //js -->
    <!-- stats -->
    <script src="js/jquery.waypoints.min.js"></script>
    <script src="js/jquery.countup.js"></script>
    <script>
		$('.counter').countUp();
	</script>
    <!-- //stats -->
    <!-- Slider script -->
    <script src="js/responsiveslides.min.js"></script>
    <script>
			// You can also use "$(window).load(function() {"
			$(function () {
				$("#slider").responsiveSlides({
					auto: true,
					nav: true,
					manualControls: '#slider3-pager',
				});
			});
		</script>
    <!-- /nav -->
    <script src="js/simplePlayer.js"></script>
    <script>
		$("document").ready(function () {
			$("#video").simplePlayer();
		});
	</script>
    <script src="js/modernizr-2.6.2.min.js"></script>
    <!-- //nav -->
    <!-- js for portfolio lightbox -->
    <script src="js/jquery.chocolat.js%20"></script>
    <link rel="stylesheet " href="css/chocolat.css%20" type="text/css" media="all">
    <!--light-box-files -->
    <script type="text/javascript ">
		$(function () {
			$('.portfolio-grids a').Chocolat();
		});
	</script>
    <!-- /js for portfolio lightbox -->
    <!-- requried-jsfiles-for owl -->
    <script src="js/owl.carousel.js"></script>
    <script>
		$(document).ready(function () {
			$("#owl-demo2").owlCarousel({
				items: 1,
				lazyLoad: false,
				autoPlay: true,
				navigation: false,
				navigationText: false,
				pagination: true,
			});
		});
	</script>
    <!-- //requried-jsfiles-for owl -->
    <script type="text/javascript" src="js/bootstrap-3.1.1.min.js"></script>
</body></html>