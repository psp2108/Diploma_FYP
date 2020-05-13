$(document).ready(function($){
  var path = window.location.pathname.split("/").pop();
  if( path == 'index.php' || path == '' ){
	path = 'CallHomePage.php';
  }
  //alert("Path: "+path);
  console.log("Path: "+path);
  var target = $('a[href="'+path+'"]');
  target.parent().addClass("active");
});