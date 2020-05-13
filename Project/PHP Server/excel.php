<?php  
 //excel.php  
 $file = 'excelFile-'.date("Y-M-D")."-".time().'.xls';
 ob_start();
 $content = $_POST["tb"];
 echo $content;
 $content = ob_get_contents();
 
 header("Expires: 0");
 header("Last-Modified: " . gmdate("D, d M Y H:i:s") . " GMT");
 header("Cache-Control: no-store, no-cache, must-revalidate");
 header("Cache-Control: post-check=0, pre-check=0", false);
 
 header("Pragma: no-cache");  header("Content-type: application/vnd.ms-excel;charset:UTF-8");
 header('Content-length: '.strlen($content));
 header('Content-disposition: attachment; filename='.basename($file));
 exit;
 ?>  