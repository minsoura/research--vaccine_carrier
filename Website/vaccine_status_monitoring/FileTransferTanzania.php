<?php
  
	$inf = pathinfo($_FILES['uploaded_file']['name']);
	$desiredName = basename($_FILES['uploaded_file']['name']);
	$randomNumber = rand(1, 200);
	$stringNumber = strval($randomNumber);
          $file_path = "tanzania/".$stringNumber."_".$desiredName;    
   
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path) ){
        echo "success";
    } else{
        echo "fail";
    }
 ?>