<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<script src="https://maps.googleapis.com/maps/api/js"></script>

<style>
#map {
	width: 100%;
	height: 550px;
}
</style>
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
					infowindow.setContent('${crimes.description}');
					infowindow.open(map, this);
				});
		
				var infowindow = new google.maps.InfoWindow();
	
			</c:if>

		</c:forEach> // ###### Slutar for each

	}
</script>

</head>
<body onload="initialize()">
	<div id="map"></div>

	<a href="<%=request.getContextPath()%>/testfil.html">länk lönk </a>

</body>
</html>