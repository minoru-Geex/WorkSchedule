$(function() {
	// ★★モーダルウィンドウにする

	// 【初期設定】年・月・日を設定する。
	var today = new Date();
	//var thisYear = today.getFullYear();
	//var thisMonth = today.getMonth() + 1;
	var thisYear = window.opener.$("#year").val();
	var thisMonth = window.opener.$("#month").val();
	var dayOfMonth = today.getDate();
	var hour = today.getHours();
	var minute = today.getMinutes();
	// 取得した「分」を表示用に加工するため文字列型にする。
	var displayMinute = String(minute);

	// minuteが0～9の時は0を付けて01などと表示する。
	if (displayMinute.match(/^\d{1}$/)) {
		displayMinute = "0" + displayMinute;
	}

	var displayTime = hour + ":" + displayMinute;

	$("#year").val(thisYear);
	$("#month").val(thisMonth);
	$("select[name=dayOfMonth]").val(dayOfMonth);
	$("#time").val(displayTime);

	// 「出勤ボタン」押下字にajax通信を行う。
	// 通信が成功した場合、勤怠データを受け取り、親データに反映させてから、サブウィンドウを閉じる。
	// 通信が失敗した場合、読み込み失敗をアラートしてサブウィンドウを閉じる。
	$("#go").on("click", function() {
		// 親画面の出勤に時刻を反映する。
		window.opener.$("#go"+ dayOfMonth).val(displayTime);

		// この時、親画面の退社が空文字でなければ、ajaxにて勤怠情報を算出し、親画面に反映させる。
		let leave = window.opener.$("#leave" + dayOfMonth).val();
		let button = $("#go").val();

		if (leave != "") {
			let url = "CreateWorkSchedule";
			setDataOpener(url, thisYear, thisMonth, dayOfMonth, displayTime, button);
		}
	})

	// 「退社ボタン」押下字にajax通信を行う。
	// 通信が成功した場合、勤怠データを受け取り、親データに反映させてから、サブウィンドウを閉じる。
	// 通信が失敗した場合、読み込み失敗をアラートしてサブウィンドウを閉じる。
	$("#leave").on("click", function() {
		// 親画面の退社に時刻を反映する。
		window.opener.$("#leave"+ dayOfMonth).val(displayTime);

		// この時、親画面の出勤が空文字でなければ、ajaxにて勤怠情報を算出し、親画面に反映させる。
		let attendance = window.opener.$("#go" + dayOfMonth).val();
		let button = $("#leave").val();


		if (attendance != "") {
			let url = "CreateWorkSchedule";
			setDataOpener(url, thisYear, thisMonth, dayOfMonth, displayTime, button);
		}
	})
});