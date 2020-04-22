<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>社員登録</title>
<!-- CSS -->
<link rel="stylesheet" type="text/css"  href="css/loginForm.css">
</head>
<body>
下記の社員を登録します。

<form action="Register" method="GET">
	社員ID:${registerEmp.id }<br>
	名前:${registerEmp.lastName } ${registerEmp.firstName }<br>
	部署名:${registerEmp.department }<br>

	<input type="submit" id="back" name="action" value="戻る"/>
	<input type="submit" id="register" name="action" value="登録"/>
</form>

</body>
</html>