<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin</title>
</head>

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
	padding-left: 20%;
	padding-top: 1%;
	padding-bottom: 1%;
}

.logo {
	 width: 65px;
    height: 65px;
    float:left;
    padding-right: 1%;
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

#dbinfo {
	padding-left: 25%;
	color: white;
	padding-top: 2%;
}

</style>

<body>

		<div id="header">
			<h1 class=stencil>POLISHÄNDELSER  </h1>
			<h1 class=consolas> i Stockholm</h1>
			<img class=logo src="http://nomakhaza.co.za/criminal.png" alt="logo burgler" />
		</div>

	<div id=dbinfo>
	<h2>Adminfunktioner för polishändelsedatabasen</h2>

	<h3>Info</h3>
	<table>

		<tr>
			<td>Antal rader i databasen:</td>
			<td>${noRows }</td>
		</tr>

		<tr>
			<td>Antal rader utan Geoloaction:</td>
			<td>${noEmpty }</td>
		</tr>

	</table>
	<h3>Funktioner</h3>
	
	<I>Polisens RSS-feed tillåter enbart ETT anrop från server, för att hämta händelser<br>mer än en gång måste programmet startas om. Gäller ej geolocation.<br><br></I>

	<table>
		<tr>
			<td>Fyll tabeller vid nystart av databas:</td>
			<td>
				<form action="/Brott/load">
					<input type="submit" value="Fyll">
				</form>
			</td>
		</tr>
		
		<tr>
			<td>Hämta nya händelser från polisen:</td>
			<td>
				<form action="/Brott/New">
					<input type="submit" value="Hämta">
				</form>
			</td>
		</tr>

		<tr>
			<td>Uppdatera geolocation manuellt för 10 händelser:</td>
			<td>
				<form action="/Brott/update">
					<input type="submit" value="Uppdatera">
				</form>
			</td>
		</tr>
	</table>
	Antal uppdaterade rader: ${updated }
	<p>Följ länken till <a href="/Brott/brottkarta">händelsekartan</a>.</p>
	</div>
	

</body>
</html>