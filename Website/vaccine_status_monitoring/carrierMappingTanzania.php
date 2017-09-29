
<html>
    <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="format-detection" content="telephone=no">
    <link rel="stylesheet" type="text/css" href="http://blossome.be/css/bootstrap.min.css">
   <link rel="stylesheet" type="text/css" href="http://blossome.be/Style2.css">
   <link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
   <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.4.0/css/font-awesome.min.css">


    <script type="text/javascript"
  src=
"http://maps.googleapis.com/maps/api/js?key=AIzaSyB1tbIAqN0XqcgTR1-          FxYoVTVq6Is6lD98&sensor=false">
    </script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.1.1/jquery.js"></script>
    <script src="http://blossome.be/js/bootstrap.min.js"></script>

    <style type="text/css">
        html { height: 100% }
       body { height: 100%; margin: 0; padding: 0 }
       #map_canvas { height: 100% }

       #loader {  position: absolute;
        left:42%;
        top: 45%;
      
        border: 4px solid #f3f3f3; /* Light grey */
        border-top: 4px solid white; /* Blue */
        border-radius: 50%;
        width: 60px;
        height: 60px;
        animation: spin 2s linear infinite;
    } 

    @-webkit-keyframes spin {
      0% { -webkit-transform: rotate(0deg); }
      100% { -webkit-transform: rotate(360deg); }
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    /* Add animation to "page content" */
    .animate-bottom {
      position: relative;
      -webkit-animation-name: animatebottom;
      -webkit-animation-duration: 1s;
      animation-name: animatebottom;
      animation-duration: 1s
    }

    @-webkit-keyframes animatebottom {
      from { bottom:-100px; opacity:0 } 
      to { bottom:0px; opacity:1 }
    }

    @keyframes animatebottom { 
      from{ bottom:-100px; opacity:0 } 
      to{ bottom:0; opacity:1 }
    }

    #container {
      visibility: hidden;
      text-align: center;
    }



      </style>

 </head>

 <body id='target' style="color:#373737; " onload="progress();">

<header>
<a href="http://blossome.be/carrierMappingTanzania.php'" style="color:#FF4081;">Choco Cookie</a>;
</header>

<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
  $fileData = $_POST['fileName'];
  echo '<form id="none" action="/." method="POST">
  <input type="hidden" name="fileName" value="'.$fileData.'" id="fileName"></form>';  
}
?>

<div id="page-content" style="padding:0px;">
 <div class="row" style="width:40%; margin-left: auto; margin-top: 50px; margin-bottom:30px; margin-right: auto;" >
  <text style="margin-bottom:5px;font-weight:bold; color:white">Select File:</text>
  <select class="form-control" name="fileNameSpinner" id="fileNameSpinner" onchange="fileNameSpinnerListener(); ">    
  <?php
  $path ='tanzania/';
  $files = scandir($path);
    if($_SERVER['REQUEST_METHOD']=='POST'){
     
      echo '<option>'.$_POST['fileName'].'</option>';
    }else{
      echo '<option>SELECT FILE</option>';
    }
  for($i=0; $i<sizeof($files); $i++){
    if(strpos($files[$i],'txt')){
      echo '<option>'.$files[$i].'</option>';
    }
    
    }
  ?>
  </select>
 </div>
<div id="textString"></div>
<center><div id="default" style="height:70%; width:40%; border-radius: 10px;"></div><center>
</div>

<form action="/carrierMappingTanzania.php" method="POST" id="formSend">
<input type="hidden" name="fileName" value="" id="fileNameSend">
</form>

<script type="text/javascript">
var parenthesisOpen ="[";
var parenthesisClose ="]";
var string;
var latitudeArray=[];
var longitudeArray=[];
var tempArray=[];
var timeArray=[];
var locations =[];
var longitudeArrayReal=[];
var latitudeArrayReal=[];
var tempArrayReal =[];
var timeArrayReal =[];
var map;

var pageUpdater = setInterval(onLoad, 60000);

function onLoad(){
  var txtFile= new XMLHttpRequest();
  var fileDirectory ="/tanzania/";
  var fileName = document.getElementById('fileName').value;
  var fullPath = fileDirectory.concat(fileName);
  txtFile.open("GET", fullPath, true);
  txtFile.send();
  txtFile.onreadystatechange = function(){
    if (txtFile.readyState == 4 && txtFile.status == 200) {
      if(txtFile.responseText != ""){
            string = txtFile.responseText;
                    
            string = string.replace(/,\s*$/, "");
            var finalString = parenthesisOpen.concat(string, parenthesisClose);


            var jsonObject = JSON.parse(finalString);
            for (var i =0; i <jsonObject.length; i++){
                latitudeArray.push(jsonObject[i]['lat']);
                longitudeArray.push(jsonObject[i]['long']);
                tempArray.push(jsonObject[i]['temp']);
                timeArray.push(jsonObject[i]['time']);
            }

            for(var i=0; i<latitudeArray.length; i++){
              var latString = latitudeArray[i].trim().split(" ", 2);
              var frontLatString = latString[0];
              var backLatString = latString[1];
              backLatString = backLatString*10/6/100;

              var longString = longitudeArray[i].trim().split(" ", 2);
              var frontLongString = longString[0];
              var backLongString = longString[1];
              backLongString = backLongString*10/6/100;

              var lat = parseFloat(frontLatString) + parseFloat(backLatString);
              var long = parseFloat(frontLongString) + parseFloat(backLongString);
              if(lat !==0){

                latitudeArrayReal.push(lat.toFixed(5));
                longitudeArrayReal.push(long.toFixed(5));
                tempArrayReal.push(tempArray[i]);
                timeArrayReal.push(timeArray[i]);
              }

            }

            for(var i=0; i<latitudeArrayReal.length; i++){
              var mover = new Object();
              mover['latitude'] = latitudeArrayReal[i];
              mover['longitude'] = longitudeArrayReal[i];
              mover['temp'] = tempArrayReal[i];
              mover['time'] = timeArrayReal[i];
              locations[i] = mover;
            }
            initialize()                     
        }else{
          document.write("Unable to read the file");
        }
      }            
  }         
 
}


function initialize(){

   var myOptions = {

    center: new google.maps.LatLng(locations[0]['latitude'], locations[0]['longitude']),zoom: 16,mapTypeId: google.maps.MapTypeId.ROADMAP

   };

  map = new google.maps.Map(document.getElementById("default"),myOptions);

  setMarkers(map,locations);
}

function setMarkers(map,locations){

  var marker, i

  for (i = 0; i < locations.length; i++)
   {  

      var lat = locations[i]['latitude']

      var long = locations[i]['longitude']
      var temp=  locations[i]['temp']

      latlngset = new google.maps.LatLng(lat, long);

      var marker = new google.maps.Marker({  
          map: map, title: temp , position: latlngset  
      });

      map.setCenter(marker.getPosition())

      if(locations[i]['temp'] <=8){
         marker.setIcon('http://maps.google.com/mapfiles/ms/icons/green-dot.png')
       }
      else{
      marker.setIcon('http://maps.google.com/mapfiles/ms/icons/red-dot.png')
      }

     if(i == locations.length-1){
      marker.setAnimation(google.maps.Animation.BOUNCE);
      marker.setIcon('http://maps.google.com/mapfiles/ms/icons/blue-dot.png')
     }

     var content = '<p><b>Latitude: </b>'+locations[i]['latitude']+'</p><p><b>Longitude: </b>' + locations[i]['longitude']+ '</p><p><b>Temperature:</b> '+locations[i]['temp'] +'</p>' + '<p><b>Time:</b> '+locations[i]['time'] +'</p>'

     var infowindow = new google.maps.InfoWindow()

     google.maps.event.addListener(marker,'click', (function(marker,content,infowindow){ 
            return function() {
               infowindow.setContent(content);
               infowindow.open(map,marker);
            };
        })(marker,content,infowindow)); 

      }
  }

  google.maps.event.addDomListener(window,'load', onLoad);


function fileNameSpinnerListener(){

    var spinner= document.getElementById("fileNameSpinner");
    var spinnerItem = spinner.options[spinner.selectedIndex].text;  
    document.getElementById("fileNameSend").value = spinnerItem;
    document.getElementById("formSend").submit();
  

}

  </script>
 </body>
  </html>
