<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
	var longitud = ${crime.longitud};
	var latitud = ${crime.latitud};
	
	var LatLng = {
		lat : latitud,
		lng : longitud
	};
	
	
	function initialize() {
		geocoder = new google.maps.Geocoder();
		var mapOptions = {
			zoom : 8,
			center : LatLng
		}
		map = new google.maps.Map(document.getElementById("map"), mapOptions);
		initMarkerTest();
	}

	function codeAddress() {
		var address = document.getElementById("address").value;

		geocoder.geocode({
			'address' : address
		}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				map.setCenter(results[0].geometry.location);
				var marker = new google.maps.Marker({
					map : map,
					position : results[0].geometry.location
				});
			} else {
				alert("Geocode was not successful for the following reason: "
						+ status);
			}
		});
	}

	var infowindow = new google.maps.InfoWindow({
		content : '${crime.description}'
	});

	function initMarkerTest() {


		map.setCenter(LatLng);
		var marker = new google.maps.Marker({
			map : map,
			position : LatLng,
			title : '${crime.title}'
		});

		marker.addListener('click', function() {
			infowindow.open(map, marker);
		});

	}
	
	
</script>

</head>
<body onload="initialize()">
	<div id="map"></div>
	<div>
		<input id="address" type="textbox" value="Stockholm"> <input
			type="button" value="testa" onclick="codeAddress()">
	</div>

	${crime.title }
	<br> ${crime.description }
	<br>

	<a href="<%=request.getContextPath()%>/testfil.html">länk lönk </a>

</body>
</html>