<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>就業時間設定</title>
<!-- CSS -->
<link rel="stylesheet" type="text/css" href="css/workConfig.css">
<!-- JavaScript -->
<script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="js/workConfig.js"></script>
<script type="text/javascript" src="js/scheduleCommon.js"></script>
</head>
<body>
	<h1>就業時間設定</h1>
	<div>
		<span id="msg" class="errorMsg"></span>
	</div>

	<form action="WorkConfigRegister" method="post">
		<p>
			就業期間<br>
			<input type="text" id="workPeriodFrom" name="workPeriodFrom" placeholder="入力例:2019/11/01"/> ～ <input type="text" id="workPeriodTo" name="workPeriodTo" placeholder="入力例:2020/02/28"/><br>
			<span class="msg">分かっている場合は記載してください。</span>
		</p>
		<p>
			就業時間<br>
			<input type="text" id="workTimeFrom" name="workTimeFrom" value="9:00"/> ～ <input type="text" id="workTimeTo" name="workTimeTo" value="18:00"/>
		</p>
		<p>
			普通残業時刻<br>
			<input type="text" id="normalOverTimeFrom" name="normalOverTimeFrom" value="18:30"/> ～ <input type="text" id="normalOverTimeTo" name="normalOverTimeTo" value="22:00"/><br>
			<span class="msg">自動設定されます。確認してください。</span>
		</p>
		<p>
			休憩時刻1(昼休憩)<br>
			<input type="text" id="lunchBreakFrom" name="lunchBreakFrom" value="12:00"/> ～ <input type="text" id="lunchBreakTo" name="lunchBreakTo" value="13:00"/>
		</p>
		<p>
			休憩時刻2(業後休憩)<br>
			<input type="text" id="eveningBreakFrom" name="eveningBreakFrom" value="18:00"/> ～ <input type="text" id="eveningBreakTo" name="eveningBreakTo" value="18:30"/><br>
			<span class="msg">自動設定されます。確認してください。</span>
		</p>
		<p>
			早出分前<br>
			<input type="text" id="earlyAppearance" name="earlyAppearance" value="60" maxlength=3 size=3/>分
		</p>
		<input type="button" id="config" value="設定">
	</form>
</body>
</html>