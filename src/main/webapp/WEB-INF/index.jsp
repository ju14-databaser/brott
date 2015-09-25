<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script src="https://maps.googleapis.com/maps/api/js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>POLISHÄNDELSER i Stockholm</title>

<style type="text/css">
body {
	font-family: Verdana, Geneva, sans-serif;
	background-color: #539ADF;
	padding: 0px;
	height: 100%;
}

#header {
	height: 80px;
	background-color: #FFFFFF;
	color: #2879b3;
	padding-left: 30%;
	padding-top: 1%;
	padding-bottom: 1%;
}

.logo {
	width: 65px;
	height: 65px;
	float: left;
	padding-right: 1%;
}

#context {
	margin: 0px auto;
	margin-top: 2%;
	margin-bottom: 10px;
	margin-left: 15%;
}

.consolas {
	margin: 0px auto;
	font-size: 2.5em;
	font-family: "Consolas", "Monaco", "Monospace";
	display: inline;
}

.stencil {
	margin: 0px auto;
	font-size: 4em;
	font-family: Impact, Charcoal, sans-serif;
	display: inline;
}

#map {
	width: 80%;
	height: 400px;
	margin-top: 2%;
}

p {
	color: white;
	padding-left: 15%;
}

#buttons {
	display: inline;
}

ul {
	background-color: #fff;
}

li:hover {
	background-color: #A0B7FF;
}

li {
	border-style: solid;
	border-width: 1px;
	border-color: #D2D7D9;
}

.scrollable-menu {
	height: auto;
	max-height: 200px;
	overflow-x: hidden;
}
</style>

<script>

	
	var map;
	var markerArray = [];
	function initialize() {
	//	geocoder = new google.maps.Geocoder();
		var mapOptions = {
			zoom : 8	
		}
		map = new google.maps.Map(document.getElementById("map"), mapOptions);
		
		initMarkerTest();
	}

	function initMarkerTest() {
	
		// här ska loop kod in

		<c:forEach items="${Crimes}" var="crimes"> // Startar for each
			
	
			<c:if test="${not empty crimes.geoLocation.lat && not empty crimes.geoLocation.lng}"> // kollar så att det finns coordinater
	
				var geolat=${crimes.geoLocation.lat};
				var geolng=${crimes.geoLocation.lng};
				
				var geoLocation = {
					lat : geolat,
					lng : geolng
				};
		
				map.setCenter(geoLocation); // Sätter marker från koordinater
				var marker = new google.maps.Marker({
					map : map,
					position : geoLocation,
					title : '${crimes.title}',
					category:'${crimes.crimecategory.category}',
					relevant:'${crimes.crimecategory.isrelevant}'
				});

				marker.addListener('click', function() {
					
					infowindow.setContent('${crimes.description}');
					infowindow.open(map, this);
				});
		
				
				var infowindow = new google.maps.InfoWindow();
				
				markerArray.push(marker);
	
			</c:if>

		</c:forEach> // ###### Slutar for each

	}
	
	function filterMap(category){
		console.log(category);
		
	}
    $(document).ready(function () {
        $("ul[id*=myid] li").click(function () {
        	
            console.log($(this).text());
            for(var i=0; i<markerArray.length;i++){
            	if(markerArray[i].category!=$(this).text()){
            		markerArray[i].setVisible(false);
            	}else{
            		markerArray[i].setVisible(true);
            	}
            }
            
        });
    });
    
    function showAllMarkers(){
    	for(var i=0;i<markerArray.length;i++){
    		markerArray[i].setVisible(true);
    		
    	}
    	
    }
    function showRelevantCrimes(){

    	for(var i=0;i<markerArray.length;i++){
    		if(markerArray[i].relevant=="Y"){
    			markerArray[i].setVisible(true);
    		}else{
    			markerArray[i].setVisible(false);			
    		}
    	}
    	
    }

</script>

</head>
<body onload="initialize()">

	<div id="header">
		<h1 class=stencil>POLISHÄNDELSER</h1>
		<h1 class=consolas>i Stockholm</h1>
		<img class=logo src="http://nomakhaza.co.za/criminal.png"
			alt="logo burgler" />
	</div>


	<div id="context">
		<div id="buttons" class="dropdown" style="width: 30%;">
			<button class="btn btn-primary" type="button"
				onClick="showAllMarkers()">Visa alla brott</button>
			<button class="btn btn-primary dropdown-toggle" type="button"
				data-toggle="dropdown">
				Filtrera <span class="caret"></span>
			</button>
			<ul id="myid" class="dropdown-menu scrollable-menu" role="menu">
				<c:forEach items="${Crimecat}" var="cat">
					<li id="index">${cat.category}</li>
				</c:forEach>
			</ul>
			<button class="btn btn-primary" type="button"
				onClick="showRelevantCrimes()">Relevanta händelser vid
				bostadsköp</button>
			<div id="map"></div>
		</div>
	</div>

	<p>Här ser du polishändelser i Stockholm. Hoovra över röda
		markeringarna, klicka på dem eller zooma in för en närmare titt!</p>

</body>
</html>