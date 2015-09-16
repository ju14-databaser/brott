<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Databasens inneh�ll</title>
</head>
<body>
	<h1>Statistik och funktioner f�r databasen</h1>

	<h2>Statistik</h2>
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
	<h2>Funktioner</h2>

	<table>
		<tr>
			<td>H�mta hela Polisens RSS feed f�r att fylla databasen:</td>
			<td>
				<form action="/Brott/load">
					<input type="submit" value="h�mta allt">
				</form>
			</td>
		</tr>

		<tr>
			<td>Uppdatera geolocation f�r 10 tomma i taget</td>
			<td>
				<form action="/Brott/update">
					<input type="submit" value="uppdatera">
				</form>
			</td>
		</tr>
	</table>
	Antal uppdaterade rader: ${updated }

</body>
</html>