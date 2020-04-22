
	var formName = "scheduleForm";

	/*
	 * 欠勤区分のそれぞれの値の算出する。
	 */
	function getAbsence() {
		let closed = 0;
		let special = 0;
		let compensatory = 0;
		let absence = 0;

		for (var i = 0; i <= 31; i++) {
			absenceSelect = $("#absence" + i).val();

			switch (absenceSelect) {
				case "1":
					closed++;
					break;
				case "2":
					special++;
					break;
				case "3":
					compensatory++;
					break;
				case "4":
					absence++;
					break;
			}
		}
		$("#closed").text(closed);
		$("#special").text(special);
		$("#compensatory").text(compensatory);
		$("#absence").text(absence);
	}

	/*
	 * 勤怠データを親画面のそれぞれの項目に設定する。
	 * 第1引数：Attendanceオブジェクト、 第2引数：打刻日
	 */
	function setAttendanceDataOpener(data ,dayOfMonth) {
		var workTime = data.workTime;
		var earlyTime = data.earlyTime;
		var normalOverTime = data.normalOverTime;
		var lateOverTime = data.lateOverTime;
		var breakTime = data.breakTime;
		var lateTime = data.lateTime;
		var leavingEarlyTime = data.leavingEarlyTime;

		// 親画面のそれぞれの項目に勤怠データを設定する。
		window.opener.$("#workTime"+ dayOfMonth).text(workTime);
		window.opener.$("#earlyTime"+ dayOfMonth).text(earlyTime);
		window.opener.$("#normalOverTime"+ dayOfMonth).text(normalOverTime);
		window.opener.$("#lateOverTime"+ dayOfMonth).text(lateOverTime);
		window.opener.$("#breakTime"+ dayOfMonth).text(breakTime);
		window.opener.$("#lateTime"+ dayOfMonth).text(lateTime);
		window.opener.$("#leavingEarlyTime"+ dayOfMonth).text(leavingEarlyTime);
	}

	/*
	 * 勤怠データをそれぞれの項目に設定する。
	 * 第1引数：Attendanceオブジェクト、 第2引数：打刻日
	 */
	function setAttendanceData(data ,dayOfMonth) {
		var workTime = data.workTime;
		var earlyTime = data.earlyTime;
		var normalOverTime = data.normalOverTime;
		var lateOverTime = data.lateOverTime;
		var breakTime = data.breakTime;
		var lateTime = data.lateTime;
		var leavingEarlyTime = data.leavingEarlyTime;

		// 親画面のそれぞれの項目に勤怠データを設定する。
		$("#workTime"+ dayOfMonth).text(workTime);
		$("#earlyTime"+ dayOfMonth).text(earlyTime);
		$("#normalOverTime"+ dayOfMonth).text(normalOverTime);
		$("#lateOverTime"+ dayOfMonth).text(lateOverTime);
		$("#breakTime"+ dayOfMonth).text(breakTime);
		$("#lateTime"+ dayOfMonth).text(lateTime);
		$("#leavingEarlyTime"+ dayOfMonth).text(leavingEarlyTime);
	}

	/*
	 * 勤怠合計データを親画面のそれぞれの項目に設定する。
	 * 第1引数：勤怠合計Map
	 */
	function setAttendanceSumDataOpener(data) {
		window.opener.$("#sumWorkTime").text(data.sumWorkTime);
		window.opener.$("#sumEarlyTime").text(data.sumEarlyTime);
		window.opener.$("#sumNormalOverTime").text(data.sumNormalOverTime);
		window.opener.$("#sumLateOverTime").text(data.sumLateOverTime);
		window.opener.$("#sumBreakTime").text(data.sumBreakTime);
		window.opener.$("#sumLateTime").text(data.sumLateTime);
		window.opener.$("#sumLeavingEarlyTime").text(data.sumLeavingEarlyTime);
	}

	/*
	 * 勤怠合計データをそれぞれの項目に設定する。
	 * 第1引数：勤怠合計Map
	 */
	function setAttendanceSumData(data) {
		$("#sumWorkTime").text(data.sumWorkTime);
		$("#sumEarlyTime").text(data.sumEarlyTime);
		$("#sumNormalOverTime").text(data.sumNormalOverTime);
		$("#sumLateOverTime").text(data.sumLateOverTime);
		$("#sumBreakTime").text(data.sumBreakTime);
		$("#sumLateTime").text(data.sumLateTime);
		$("#sumLeavingEarlyTime").text(data.sumLeavingEarlyTime);
	}

	/*
	 * 就業データを親画面のそれぞれの項目に設定する。
	 * 第1引数：Employeeオブジェクト
	 */
	function setWorkConfigOpener(data) {
		var workTimeFrom = data.workTimeFrom;
		var workTimeTo = data.workTimeTo;
		var normalOverTimeFrom = data.normalOverTimeFrom;
		var normalOverTimeTo = data.normalOverTimeTo;
		var lateOverTimeFrom = data.lateOverTimeFrom;
		var lateOverTimeTo = data.lateOverTimeTo;
		var lunchBreakFrom = data.lunchBreakFrom;
		var lunchBreakTo = data.lunchBreakTo;
		var eveningBreakFrom = data.eveningBreakFrom;
		var eveningBreakTo = data.eveningBreakTo;
		var nightBreakFrom = data.nightBreakFrom;
		var nightBreakTo = data.nightBreakTo;
		var earlyAppearance = data.earlyAppearance;


		// 親画面のそれぞれの項目に勤怠データを設定する。
		window.opener.$("#workFrom").text(workTimeFrom);
		window.opener.$("#workTo").text(workTimeTo);
		window.opener.$("#normalOverFrom").text(normalOverTimeFrom);
		window.opener.$("#normalOverTo").text(normalOverTimeTo);
		window.opener.$("#lateOverFrom").text(lateOverTimeFrom);
		window.opener.$("#lateOverTo").text(lateOverTimeTo);
		window.opener.$("#lunchFrom").text(lunchBreakFrom);
		window.opener.$("#lunchTo").text(lunchBreakTo);
		window.opener.$("#eveningFrom").text(eveningBreakFrom);
		window.opener.$("#eveningTo").text(eveningBreakTo);
		window.opener.$("#nightFrom").text(nightBreakFrom);
		window.opener.$("#nightTo").text(nightBreakTo);
		window.opener.$("#early").text(earlyAppearance);
	}

	/*
	 * 就業時間または休出のそれぞれの個数を算出する。
	 * 第1引数：年、第2引数：月
	 */
	function countWork(year, month) {
		// 該当付きの最終日を取得する
		var lastDay = new Date(year, month, 0).getDate(); // Dateクラスの「月」は0始まり。日に0を入れることで前月の末尾となる。

		var workCount = 0;
		var breakCount = 0;

		for (var i = 1; i <= lastDay; i++) {
			var attendance = $("#go" + i).val();
			var leave = $("#leave" + i).val();
			var holiday = $("#holiday" + i).val(); // 休日区分取得 (0:平日,1:休日)
			var workTime = $("#workTime" + i).text();
			var earlyTime = $("#earlyTime" + i).text();
			var normalOverTime = $("#normalOverTime" + i).text();
			var lateOverTime = $("#lateOverTime" + i).text();
			var breakTime = $("#breakTime" + i).text();
			var lateTime = $("#lateTime" + i).text();
			var leavingEarlyTime = $("#leavingEarlyTime" + i).val();

			// 出勤日数：休日区分：0 かつ 就業、早出、普通、深夜、遅刻、早退のいずれかに値がある。
			if (holiday == 0 && (workTime != "" || earlyTime != "" || normalOverTime != "" || lateOverTime != "" || lateTime != "" || leavingEarlyTime != "" || (attendance != "" && leave != ""))) {
				workCount++;
			}

			// 休出日数：休日区分：1 かつ 深夜、休出のいずれかに値がある。
			if (holiday == 1 && (lateOverTime != "" || breakTime != "" || (attendance != "" && leave != ""))) {
				breakCount++;
			}
		}

		$("#attendanceCount").text(workCount);
		$("#breakCount").text(breakCount);
	}

	/*
	 * 勤怠及び勤怠合計データを画面に設定する。
	 * 第1引数：アクション、第2引数：年、 第3引数：月、 第4引数：日、 第5引数：変更値
	 */
	function setData(url, action, year, month, dayOfMonth, data) {
		$.ajax({
			url:url,
			type:"post",
			data:{action: action, year: year, month: month, dayOfMonth: dayOfMonth, data: data},
			dataType: "json"
		}).done(function(data) {
			var attendance = data.attendance;
			var attendanceMap = data.attendanceMap;
			setAttendanceData(attendance, dayOfMonth);
			setAttendanceSumData(attendanceMap);
			countWork(year, month);
		}).fail(function() {
			alert("読み込み失敗");
		});
	}

	/*
	 * 勤怠及び勤怠合計データを画面に設定する。
	 * 第1引数：アクション、第2引数：年、 第3引数：月、 第4引数：日、 第5引数：変更値
	 */
	function setDataOpener(url, year, month, dayOfMonth, time, button) {
		$.ajax({
			url:url,
			type:"post",
			data:{year: year, month: month, dayOfMonth: dayOfMonth, time: time, embossCondition: button},
			dataType: "json"
		}).done(function(data) {
			var attendance = data.attendance;
			var attendanceMap = data.attendanceMap;
			setAttendanceDataOpener(attendance, dayOfMonth);
			setAttendanceSumDataOpener(attendanceMap);
			window.close();
		}).fail(function() {
			alert("読み込み失敗");
			window.close();
		});
	}

	/*
	 * 就業条件を親画面に設定する。
	 * 第1引数：送信url, 第2引数：就業開始期間、 第3引数：就業終了期間、 第4引数：就業開始時刻、 第5引数：就業終了引数、 第6引数：普通残業開始、 第7引数：普通残業終了、第8引数：休憩1開始、 第9引数：休憩1終了、 第10引数：休憩2開始、 第11引数：休憩2終了、 第12引数：早出分前
	 */
	function setWorkConfig(url, workPeriodFrom, workPeriodTo, workTimeFrom, workTimeTo, normalOverTimeFrom, normalOverTimeTo, lunchBreakFrom, lunchBreakTo, eveningBreakFrom, eveningBreakTo, earlyAppearance, msg) {
		$.ajax({
			url: url,
			type: "post",
			data: {workPeriodFrom: workPeriodFrom, workPeriodTo: workPeriodTo, workTimeFrom: workTimeFrom, workTimeTo: workTimeTo, normalOverTimeFrom: normalOverTimeFrom, normalOverTimeTo: normalOverTimeTo, lunchBreakFrom: lunchBreakFrom, lunchBreakTo: lunchBreakTo, eveningBreakFrom: eveningBreakFrom, eveningBreakTo: eveningBreakTo, earlyAppearance: earlyAppearance},
			dataType: "json"
		}).done(function(data) {
			setWorkConfigOpener(data);
			window.close();
		}).fail(function() {
			$("#msg").text(msg);
		});
	}

	/*
	 *「出勤」及び「退社」の入力チェック
	 * 第1引数：入力値
	 */
	function validate(input) {

		if (input == "") {
			return false;
		}

		if (input.match(/^\d{1,2}:\d{2}$/) == null) {
			//alert("入力値は全て半角で【〇〇:〇〇】という表記にしてください。");
			return true;
		}

		// 時刻表記の「時間」が24以上もしくは「分」が60以上の場合エラー
		var inputArray = input.split(":");
		if(inputArray[0] >= 24 || inputArray[1] >= 60) {
			//alert("時間は0～23、分は00～59までの時刻表示で記入してください");
			return true;
		}

		return false;
	}

	/*
	 *「備考」の入力チェック
	 * 第1引数：入力値
	 */
	function validateRemark(input) {

		if (input == "") {
			return false;
		}

		if (input.length > 30) {
			return true;
		}

		return false;
	}

	/*
	 * 「就業時間設定」の就業期間入力チェック
	 *  第1引数：入力値
	 */
	function validateWorkPeriod(input) {
		// 未入力はOK
		if (input == "") {
			return false;
		}

		// 日付表示(年/月/日)でなければNG
		if (input.match(/^\d{4}\/\d{1,2}\/\d{1,2}$/) == null) {
			return true;
		}

		// 日付チェック
		let dateArray = input.split("/");
		let year = dateArray[0];
		let month = dateArray[1];
		let day = dateArray[2];

		if (!(validateDate(year, month, day))) {
			return true;
		}
		return false;
	}

	/*
	 * 日付チェック
	 * 第1引数：年、 第2引数：月、 第3引数：日
	 */
	function validateDate(year, month, day) {
		let date = new Date(year, month-1, day);
		return (date.getFullYear() == year && date.getMonth() == month-1 && date.getDate() == day);
	}

	/*
	 * 「就業時間設定」の時刻入力チェック
	 *  第1引数：入力値
	 */
	function validateConfig(input) {

		if (input.match(/^\d{1,2}:\d{2}$/) == null) {
			//alert("入力値は全て半角で【〇〇:〇〇】という表記にしてください。");
			return true;
		}

		// 時刻表記の「時間」が24以上もしくは「分」が60以上の場合エラー
		var inputArray = input.split(":");
		if(inputArray[0] >= 24 || inputArray[1] >= 60) {
			//alert("時間は0～23、分は00～59までの時刻表示で記入してください");
			return true;
		}

		return false;
	}

	/*
	 * 「早出分前」の入力チェック
	 *  第1引数：入力値
	 */
	function validateEarly(input) {
		if (input.match(/^\d{1,3}$/) == null) {
			return true;
		}
		return false;
	}

	/*
	 * 「就業時間設定」で入力チェックがNGの場合、入力値を空文字にして入力欄を赤くするクラスを付与。
	 *  第1引数：入力値
	 */
	function ngExecuteConfig(input) {
		input.val("");
		input.attr("class", "error");
	}



/*
	 * 「ログイン」の入力チェック
	 *  第1引数：社員ID入力値、 第2引数：パスワード入力値

	function validateLogin(id, pass) {

		// id入力は半角数字のみ、1～3文字でなければNG
		if (id.match(/^\d{1,3}$/) == null) {
			return true;
		}

		// パスワードは8文字以上15文字以下で小文字半角英字、大文字半角英字、半角数字がそれぞれ1個以上使われている。
		if (pass.match(/^(?=.*?[a-z])(?=.*?[A-Z])(?=.*?\d)[a-zA-Z\d]{8,15}$/) == null) {
			return true;
		}

		return false;
	}*/


	/*
	 * 年or月のコンボボックスの値を変更した時に、getリクエストを送信する。
	 */
	function sendGet() {
		var form = document.forms[formName];

		form.method = "GET";
		form.action = url;
		form.submit();

		return true;
	}

	/*
	 * コロンを除く入力値が4桁で先頭の値が0の場合、先頭の0を取り除く。
	 * 第1引数：入力値
	 */
	function modifyDisplay(input) {
		var inputArray = input.split(":");

		let inputTimeNumber = "";

		inputArray.forEach(function(value) {
			inputTimeNumber += value;
		});


		// 入力値が4桁 かつ 先頭の値が0
		if (inputTimeNumber.length == 4 && inputTimeNumber.charAt(0) == 0) {
			inputTimeNumber = inputTimeNumber.substring(1,2) + ":" + inputTimeNumber.substring(2,4);
			return inputTimeNumber
		}

		return input;
	}

	/*
	 * 値が変更されたとき、先頭0付きの時刻表示の場合は、先頭0を無くした値をセットする。
	 * 第1引数：入力値
	 */
	function setModifyValue(input) {
		let inputValue = input.val();
		let modifiedValue = modifyDisplay(inputValue);

		input.val(modifiedValue);
	}


	/*
	 * 「簡単打刻」もしくは「就業時間設定」押下時に、子ウィンドウを画面中央に生成する。
	 *  第1引数：
	 */
	function openSubWin(width, height, subWinPath, subWinName) {
		let left = window.innerWidth / 2;
		let top = window.innerHeight / 2;

		let WIDTH = width;
		let HEIGHT = height;
		let x = left - (WIDTH / 2);
		let y = top - (HEIGHT / 2);
		let WinFeature = "toolbar=no, location=no, menubar=no, scrollbars=no, status=no, resizable=no, top=" + y + ", left=" + x + ", width=" + WIDTH + ", height=" + HEIGHT;
		// 第2引数に名前を付けて、サブウィンドウを1つだけ生成する。
		window.open(subWinPath, subWinName, WinFeature);
	}

	/*
	 * 社員
	 *
	 */

	/*
	 * 数値表示→ 分表示に変換
	 * 第1引数：数値表示
	 */
	function changeNumToMin(timeNumber) {
		// 数値 → 分
		var minute = Math.floor(timeNumber / 100) * 60 +Math.floor(timeNumber % 100);
		// 小数点を切り捨て
		return minute;
	}

	/*
	 * 時刻表示を分表示の数値に変換する。
	 * 第1引数：時刻表示の文字列
	 */
	function changeTimeToMin(timeString) {
		// 時刻表示 → 分表示
		var timeNumber = changeTimeToNum(timeString);
		var minute = changeNumToMin(timeNumber);

		return minute;
	}

	/*
	 * 時刻表示を数値表示に変換する。
	 * 第1引数：時刻表示
	 */
	function changeTimeToNum(timeString) {
		var timeArray = timeString.split(":");

		var result = "";

		for (var time of timeArray) {
			result += time;
		}

		var timeNumber = parseInt(result, 10);

		return timeNumber;
	}

	/*
	 * 数値表示 →時刻表示に変換する。
	 * 第1引数：数値表示
	 */
	function changeNumToTime(number) {
		var stringDisplay = String(number);
		var length = stringDisplay.length;

		var timeString = "";

		switch(length) {
			case 1:
				timeString = "0:0" + number;
				break;
			case 2:
				timeString = "0:" + number;
				break;
			case 3:
			case 4:
			case 5:
				timeString = Math.floor(number / 100) + ":" + stringDisplay.slice(-2);
		}
		return timeString;
	}

	/*
	 * 分表示の数値を時刻表示に変換する。
	 * 第1引数：分表示の数値
	 */
	function changeMinToTime(minute) {
		// 分表示 → 数値表示
		var timeNumber = Math.floor(minute / 60) * 100 + Math.floor(minute % 60);
		// 数値 → 時刻
		var timeString = changeNumToTime(timeNumber);

		return timeString;
	}
