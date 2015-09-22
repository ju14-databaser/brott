<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

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
	padding-top: 1%;
}

.consolas {
	margin: 0px auto;
	font-size: 2.5em;
	font-family: "Consolas";
	display: inline;
}

.stencil {
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
	size: 1, 5em;
}

.textShown {
	color: white;
	padding-left: 40%;
	padding-top: 10%;
	size: 2em;
}
</style>

<body>

	<div id="header">
		<h1 class=stencil>BROTT</h1>
		<h1 class=consolas>i Stockholm</h1>
	</div>
	<div class=textShown>
	<p>Antal brott som blivit inlästa: ${crimeNo }</p>
	</div>


</body>
</html>