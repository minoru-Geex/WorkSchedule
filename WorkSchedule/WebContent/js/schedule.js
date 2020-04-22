$(function() {
	var url = "CreateWorkSchedule";
	// 開いている勤務表の年・月を取得する。
	var year = $("#year").val();
	var month = $("#month").val();


	// 初期処理：出勤日数及び休出日数を算出する。
	countWork(year, month)

	// 初期処理：欠勤区分のそれぞれの値を算出する。
	getAbsence();

	// 「簡単打刻ボタン」押下時に子ウィンドウを画面中央に生成する。
	$("#emboss").on("click", function() {
		let subWinPath = "emboss.jsp";
		let subWinName = "簡単打刻";
		openSubWin(100, 150, subWinPath, subWinName);
	})

	// 「就業時間設定ボタン」押下時に子ウィンドウを画面中央に生成する。
	$("#workConfig").on("click",function() {
		let subWinPath = "workConfig.jsp";
		let subWinName = "就業時間設定";
		openSubWin(500, 650, subWinPath, subWinName);
	})

	// 年のコンボボックスの値が変更した時に、getリクエストを送信する。
	$("select[name=year]").on("change", sendGet);

	// 月のコンボボックスの値が変更した時に、getリクエストを送信する。
	$("select[name=month]").on("change", sendGet);

	/*
	 ***************************************************************
	 * ajax処理
	 */

	// 「出勤」の値を変更した時に、新規登録or更新処理を実行する。
	$("input[name=go]").on("change", function() {
		let action = "attendance";
		let id = $(this).attr("id");
		let dayOfMonth = id.substring(2);
		let attendance = $(this).val();

		// 入力チェックを行い、不正の場合は空欄にする。
		if (validate(attendance)) {
			$(this).val("");
		// 入力チェックが不正でない場合、DB登録を行う。
		} else {
			// 勤怠及び勤怠合計データを画面に設定する。
			setData(url, action, year, month, dayOfMonth, attendance);
			// コロンを除く入力値が4桁の場合、先頭の0を取り除いてセット。
			$(this).val(modifyDisplay(attendance));
		}
	})

	// 「退社」の値を変更した時に、新規登録or更新処理を実行する。
	$("input[name=leave]").on("change", function(){
		let action = "leave";
		let id = $(this).attr("id");
		let dayOfMonth = id.substring(5);
		let leave = $(this).val();

		// 入力チェックを行い、不正の場合は空欄にする。
		if (validate(leave)) {
			$(this).val("");
		// 入力チェックが不正でない場合、DB登録を行う。
		} else {
			// 勤怠及び勤怠合計データを画面に設定する。
			setData(url, action, year, month, dayOfMonth, leave);
			// コロンを除く入力値が4桁の場合、先頭の0を取り除いてセット。
			$(this).val(modifyDisplay(leave));
		}
	})

	// 「休日区分」の値を変更した時に、新規登録or更新処理を実行する。
	$("select[name=holiday]").on("change", function() {
		let action = "holiday";
		let id = $(this).attr("id");
		let dayOfMonth = id.substring(7);
		let holiday = $(this).val(); //0～1

		// 勤怠及び勤怠合計データを画面に設定する。
		setData(url, action, year, month, dayOfMonth, holiday);
	})

	// 「欠勤区分」の値を変更した時に、新規登録or更新処理をし、それぞれの区分の値を再計算する。
	$("select[name=absence]").on("change", function(){
		let action = "absence";
		let id = $(this).attr("id");
		let dayOfMonth = id.substring(7);
		let absence = $(this).val(); // 0～4

		$.ajax({
			url:url,
			type:"post",
			data:{action: action, year: year, month: month, dayOfMonth: dayOfMonth, data: absence}
		}).done(function(){
			getAbsence();
		}).fail(function() {
			alert("読み込み失敗");
		});

	})


	// 備考の値が変更されたときに登録処理を実行する。
	$("input[name=remark]").on("change", function() {
		let action = "remark";
		let id = $(this).attr("id");
		let dayOfMonth = id.substring(6);
		let remark = $(this).val();

		// 入力チェックが不正の場合、入力欄を空文字にしてDB処理は行わない。
		if (validateRemark(remark)) {
			$(this).val("");
		} else {
			$.ajax({
				url:url,
				type:"post",
				data:{action: action, year: year, month: month, dayOfMonth: dayOfMonth, data: remark}
			}).fail(function() {
				alert("読み込み失敗");
			});
		}
	})

	/*
	 ***************************************************************
	 */


});