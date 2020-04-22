<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログイン画面</title>
<!-- CSS -->
<link rel="stylesheet" type="text/css"  href="css/login.css">
<!-- JavaScript -->
<script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="js/login.js"></script>
<script type="text/javascript" src="js/scheduleCommon.js"></script>

</head>
<body>
	<h1>ログイン</h1>

	<c:if test="${not empty errorMsg }">
		<div class="msg" id="msg">${errorMsg }</div>
	</c:if>


	<form action="/WorkSchedule/Login" method="POST">
		<input type="text" id="id" name="id" placeholder="社員ID" /><br>
		<input type="password" id="pass" name="pass" placeholder="パスワード"/><br>
		<input type="submit"  id="login" value="ログイン"/><br>
	</form>


	<table class="center">
		<tr>
			<td width="200"><hr></td>
			<td>または</td>
			<td width="200"><hr></td>
		</tr>
	</table>

	<input type="button" id="register" value="新規登録"/>



</body>
</html>