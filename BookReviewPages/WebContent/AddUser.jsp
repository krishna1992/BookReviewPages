<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="mystyle.css" />
</head>
<body>
	<%@ include file="header.jsp"%>
	<%@ include file="Footer.jsp"%>

	<br />

	<h1 align='center'>Welcome Admin</h1>
	<h3 align='center'>Please fill the details to User</h3>


	<main>
	<form action='AddUser' method='post'>
		<table align='center' bgcolor='white'>

			<tr>
				<td>Select User Role :</td>
				<td><select name='role'>
						<option>Admin</option>
						<option>User</option>
				</select></td>
				<td><font color='red'>*</font></td>
			</tr>

			<tr>
				<td>Enter User Id :</td>
				<td><input type='text' name='id' required></td>
				<td><font color='red'>*</font></td>
			</tr>
			<tr>
				<td><input type='submit' value='Add User' align="middle"
					style="height: 30px; width: 80px"></td>
			</tr>

		</table>
	</form>

	</main>
</body>
</html>
