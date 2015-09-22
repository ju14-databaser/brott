<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link href="/css/resetcss.css"
	rel="stylesheet" type="text/css">
<link href="/css/style.css"
	rel="stylesheet" type="text/css">

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BROTT i Stockholm</title>

<style type="text/css">

body {
	font-family: Verdana, Geneva, sans-serif;
	background-color: #539ADF;
	color: red;	
	padding: 0px;
	height: 100%;
}

#header {
	height: 70px;
	margin: 0px auto;
	background-color: #FFFFFF;
	color: #2879b3;
	padding-left: 35%;
	padding-top:1%;
}

 .consolas{
	margin: 0px auto;
	font-size: 2.5em;
	font-family: "Consolas";
	display: inline;
}

 .stencil{
	margin: 0px auto;
	font-size: 3.5em;
	font-family: "Stencil";
	display: inline;
}

#map {
	width: 80%;
	height: 400px;
	margin: 0px auto;
	margin-top: 10px;
	margin-bottom: 10px;
}

#link {
	margin: 0px auto;
	padding-left: 45%;
	font-size: 1,5em;
}

p {
	color:white;
	padding-left: 15%;
}
</style>

<script src="https://maps.googleapis.com/maps/api/js"></script>


<script>

	var geocoder;
	var map;

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
					title : '${crimes.title}'
				});
		
				marker.addListener('click', function() {
					infowindow.open(map, marker);
				});
		
				var infowindow = new google.maps.InfoWindow({
					content : '${crimes.description}'
				});
	
			</c:if>

		</c:forEach> // ###### Slutar for each

	}
</script>

</head>
<body onload="initialize()">

		<div id="header">
			<h1 class=stencil>BROTT  </h1>
			<h1 class=consolas> i Stockholm</h1>
		</div>
		
		<p>Här kan du se brott som begåtts i Stockholm. Hoovra över röda markeringarna eller zooma in för en närmare titt! </p>
		
		<div id="map"></div>

		<div id=link> <a href="Brott/src/main/webapp/WEB-INF/allCrimes.jsp">..jag är en länk!</a> </div>
	

</body>
</html>