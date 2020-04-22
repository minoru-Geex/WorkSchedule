<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>社員登録</title>
<!-- CSS -->
<link rel="stylesheet" type="text/css"  href="css/loginForm.css">
<!-- JavaScript -->
<script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="js/registerForm.js"></script>
</head>
<body>
<%
String id = null;
String lastName = null;
String firstName = null;
String department = null;
String pass = null;


if (request.getAttribute("id") != null) {
	id = request.getAttribute("id").toString();
}

if (request.getAttribute("lastName") != null) {
	lastName = 	request.getAttribute("lastName").toString();
}

if (request.getAttribute("firstName") != null) {
	firstName = request.getAttribute("firstName").toString();
}

if (request.getAttribute("department") != null) {
	department = request.getAttribute("department").toString();
}

if (request.getAttribute("pass") != null) {
	pass = request.getAttribute("pass").toString();
}
%>
<h1>社員登録</h1>
<c:if test="${not empty msg }">
	<p class="error">${msg }</p>
</c:if>

<form action="Register" method="POST">
	<p>
		社員ID<br>
		<input type="text" id="id" name="id" placeholder="社員ID" <%if (id != null) { %>class="error"<%} %> <%if (id == null) { %>value= "${registerEmp.id }" <%} %>><br>
		<span class="msg">総務に確認してください。</span>
	</p>
	<p>
		名前<br>
		<input type="text" <%if (lastName != null) { %>class="error"<%} %> id="lastName" name ="lastName" placeholder="姓" <%if (id == null) { %>value="${registerEmp.lastName }"<%} %>/>
		<input type="text" <%if (firstName != null) { %>class="error"<%} %> id="firstName" name="firstName" placeholder="名" <%if (id == null) { %>value="${registerEmp.firstName }"<%} %>/><br>
	</p>
	<p>
		部署名<br>
		<input type="radio" id="1" name="departments" value="システム開発部" checked/>システム開発部<br>
		<input type="radio" id="2" name="departments" value="総務部" <%if (department != null && "総務部".equals(department)) { %>checked<%} %>/>総務部<br>
	</p>
	<p>
		パスワード<br>
		<input type="password" <%if (pass != null) { %>class="error"<%} %> id="pass" name="pass" placeholder="パスワード"><br>
		<span class="msg">小文字半角英字、大文字半角英字、数字を組み合わせて 8 文字以上15文字以下で入力してください。</span>
	</p>
	<input type="button" id="back" value="戻る">
	<input type="submit" id="register" value="確認">
</form>
</body>
</html>