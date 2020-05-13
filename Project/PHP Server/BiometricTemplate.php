<!--
author: W3layoutsauthor URL: http://w3layouts.comLicense: Creative Commons Attribution 3.0 UnportedLicense URL: http://creativecommons.org/licenses/by/3.0/-->
<!DOCTYPE html><html lang="zxx">
  <head>
    <meta content="text/html; charset=utf-8" http-equiv="content-type">
    <title>Biometric Attendance</title>
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
    <style> #imagesMain {
                          padding: 0;
                          margin-left: auto;
                          margin-right: auto;
                          margin-top: 20px;
                          text-align: center;
                        }
                        #imagesMain img {
                          vertical-align: middle;
                        }
    </style>
    <?php 
		global $page_content;						?>
  </head>
  <body>
    <div id="imagesMain"> <img style="height: 100px; width: 100px;" alt="LNT_ICON"

        src="images/lntlogo.png"> <img style="height: 100px; width: 518px; margin-left: 10px;"

        alt="LTPCT_ICON" src="images/ltpctlogo.png"> </div>
    <!-- banner -->
    <div class="main_section_agile" id="home">
      <div class="agileits_w3layouts_banner_nav">
        <nav class="navbar navbar-default">
          <div class="navbar-header navbar-left"> <button type="button" class="navbar-toggle collapsed"

              data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
              <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
              <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
          </div>
          <div class="navbar-header navbar-left">
            <h2><a style="height: 75px; margin-top: 40; width: 1047px; margin-left: 20px"

                class="navbar-brand" href="CallHomePage.php"> <span>B</span>iometric
                Attendance</a></h2>
          </div>
          <div class="collapse navbar-collapse navbar-right" id="bs-example-navbar-collapse-1">
            <nav class="menu-hover-effect menu-hover-effect-4">
              <ul class="nav navbar-nav" id="navBar">
                <li><a href="CallHomePage.php" class="hvr-ripple-in">Home</a></li>
                <li><a href="CallFilterEmployeeAttendence.php" class="hvr-ripple-in">Student
                    Attendance</a></li>
                <li><a href="CallDepartmentAttendence.php" class="hvr-ripple-in">Institute
                    Attendance</a></li>
                <li><a href="CallHolidayEntryPage.php" class="hvr-ripple-in">Holiday Table</a></li>
                <li><a href="index.php" class="hvr-ripple-in">Logout</a></li>
                <li><br>
                </li>
                <li><br>
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
    <hr style="margin-top: 4px;">
    <div class="modal fade" id="myModal4" tabindex="-1" role="dialog">
      <div class="modal-dialog">
        <!-- Modal content-->
        <div class="modal-content">
          <div class="modal-header"> <button type="button" class="close" data-dismiss="modal">×</button>
            <div class="modal-info">
              <h4>Deft</h4>
              <img src="images/banner2.jpg" alt=" " class="img-responsive">
              <h5>Sub Heading here</h5>
              <p class="para-agileits-w3layouts">Duis sit amet nisi quis leo
                fermentum vestibulum vitae eget augue. Nulla quam nunc,
                vulputate id urna at, tempor tincidunt metus. Sed feugiat quam
                nec mauris mattis malesuada.</p>
            </div>
          </div>
        </div>
      </div>
      <!-- Modal4 --></div>
    <!-- banner-bottom -->
    <div class="banner-bottom">
      <div class="container">
        <div style="margin-top: -63px;"><?php include($page_content);	?> </div>
      </div>
      <!-- //banner-bottom --> <br>
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
              <p>© 2017 Larsen &amp; Toubro. All Rights Reserved </p>
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
      <script src="js/activeTab.js"></script>
      <!-- //requried-jsfiles-for owl -->
      <script type="text/javascript" src="js/bootstrap-3.1.1.min.js"></script> </div>
  </body>
</html>
