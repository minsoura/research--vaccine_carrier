<?php
  
	$inf = pathinfo($_FILES['uploaded_file']['name']);
	$ext = $inf['extension'];
	date_default_timezone_set("Asia/Seoul");
	$RegisteredTime = date("Ymd");
	$DotString = ".";
	$desiredName = $RegisteredTime.$DotString.$ext;
    $file_path = "upload/".$desiredName;
     
   
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path) ){
        echo "success";
    } else{
        echo "fail";
    }
 ?>