<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>簡単打刻</title>
<!-- CSS -->
<link rel="stylesheet" type="text/css" href="css/schedule.css">
<!-- JavaScript -->
<script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="js/dateSet.js"></script>
<script type="text/javascript" src="js/scheduleCommon.js"></script>
</head>
<body>
<%

%>
		<div class="center">
			<input type="text" id="year" name="year" size="4" readonly/>年
			<input type="text" id="month" name="month" size="1" readonly/>月度
		</div>
		<div class="center">


			<select id="dayOfMonth" name="dayOfMonth">
<%
	for (int i = 1; i <= 31; i++) {


%>
				<option value="<%= i%>" id="dayOfMonth<%=i %>"><%=i %></option>
<%
	}
%>
			</select>日
		</div>
		<div class="center">
			<input type="text" id="time" name="time" size="5" readonly/>
		</div>
		<br/>

		<div class="button">
			<input type="button" id="go" name="embossCondition" value="出勤"/>
			<input type="button" id="leave" name="embossCondition" value="退社"/>
		</div>
</body>
</html>