$(function() {

	// 「設定」押下時にajax通信を行い、DB登録したデータを親画面にセットする。
	$("#config").on("click", function() {
		let url = "WorkConfigRegister";
		let workPeriodFrom = $("#workPeriodFrom").val();
		let workPeriodTo = $("#workPeriodTo").val();
		let workTimeFrom = $("#workTimeFrom").val();
		let workTimeTo = $("#workTimeTo").val();
		let normalOverTimeFrom = $("#normalOverTimeFrom").val();
		let normalOverTimeTo = $("#normalOverTimeTo").val();
		let lunchBreakFrom = $("#lunchBreakFrom").val();
		let lunchBreakTo = $("#lunchBreakTo").val();
		let eveningBreakFrom = $("#eveningBreakFrom").val();
		let eveningBreakTo = $("#eveningBreakTo").val();
		let earlyAppearance = $("#earlyAppearance").val();

		// 入力チェックを行い、OKの場合、ajax通信を行う。NGの場合、「設定」下にエラーメッセージを表示させ、不正の入力欄を赤くする。
		let ngCount = 0;
		// 「就業期間」入力チェック
		if (validateWorkPeriod(workPeriodFrom)) {
			ngExecuteConfig($("#workPeriodFrom"));
			ngCount++;
		}

		if (validateWorkPeriod(workPeriodTo)) {
			ngExecuteConfig($("#workPeriodTo"));
			ngCount++;
		}

		// 「就業時間」、「普通残業時刻」、「休憩時刻1」、「休憩時刻2」の入力チェック
		if (validateConfig(workTimeFrom)) {
			ngExecuteConfig($("#workTimeFrom"));
			ngCount++;
		}

		if (validateConfig(workTimeTo)) {
			ngExecuteConfig($("#workTimeTo"));
			ngCount++;
		}

		if (validateConfig(normalOverTimeFrom)) {
			ngExecuteConfig($("#normalOverTimeFrom"));
			ngCount++;
		}

		if (validateConfig(normalOverTimeTo)) {
			ngExecuteConfig($("#normalOverTimeTo"));
			ngCount++;
		}

		if (validateConfig(lunchBreakFrom)) {
			ngExecuteConfig($("#lunchBreakFrom"));
			ngCount++;
		}

		if (validateConfig(lunchBreakTo)) {
			ngExecuteConfig($("#lunchBreakTo"));
			ngCount++;
		}

		if (validateConfig(eveningBreakFrom)) {
			ngExecuteConfig($("#eveningBreakFrom"));
			ngCount++;
		}

		if (validateConfig(eveningBreakTo)) {
			ngExecuteConfig($("#eveningBreakTo"));
			ngCount++;
		}

		// 「早出分前」の入力チェック
		if (validateEarly(earlyAppearance)) {
			ngExecuteConfig($("#earlyAppearance"));
			ngCount++;
		}

		// ngCountが0でなければ、エラーメッセージを表示。
		if (ngCount != 0) {
			$("#msg").text("入力に不正があります。修正してください。")
		// 登録済み社員IDである場合、登録処理を行わず、エラーメッセージを表示。
		// その時、全ての入力欄を赤→黒に戻しておく。
		} else {
			$("#workPeriodFrom").removeAttr("class");
			$("#workPeriodTo").removeAttr("class");
			$("#workTimeFrom").removeAttr("class");
			$("#workTimeTo").removeAttr("class");
			$("#normalOverTimeFrom").removeAttr("class");
			$("#normalOverTimeTo").removeAttr("class");
			$("#lunchBreakFrom").removeAttr("class");
			$("#lunchBreakTo").removeAttr("class");
			$("#eveningBreakFrom").removeAttr("class");
			$("#eveningBreakTo").removeAttr("class");
			$("#earlyAppearance").removeAttr("class");
			let msg = "すでに社員IDに就業情報が設定してあります。";
			setWorkConfig(url, workPeriodFrom, workPeriodTo, workTimeFrom, workTimeTo, normalOverTimeFrom, normalOverTimeTo, lunchBreakFrom, lunchBreakTo, eveningBreakFrom, eveningBreakTo, earlyAppearance, msg );
		}


	})

	// 「就業時間終了」の値が変更されたとき、「普通残業開始時刻」、「休憩開始時刻2」、および「休憩終了時刻2」の値を変更する。
	$("#workTimeTo").on("change", function() {
		let input = $(this);
		let modifiedValue = modifyDisplay(input.val());
		input.val(modifiedValue);

		// 入力チェックを行ってOKなら、入力欄を黒く戻し、3つの値を変更し、NGなら値を空文字にして入力欄を赤くする。
		if (validateConfig(modifiedValue)) {
			ngExecuteConfig(input);
		} else {
			input.removeAttr("class");
			// 就業時間終了時刻を分表示に変換。
			var minute = changeTimeToMin(modifiedValue);
			// 就業時刻終了時刻に30分足して「普通残業開始時刻」及び「休憩終了時刻2」を算出する。
			minute += 30;

			// 普通残業開始時刻」及び「休憩終了時刻2」を分表示 → 時刻表示に変換
			var time = changeMinToTime(minute);

			// 「就業時間終了時刻」を「休憩開始時刻2」に設定する。
			$("#eveningBreakFrom").val(modifiedValue);

			// 「普通残業開始時刻」及び「休憩終了時刻2」を設定する。
			$("#normalOverTimeFrom").val(time);
			$("#eveningBreakTo").val(time);
		}
	})

	// 「就業開始時間」の値が変更された時、先頭0付きの時刻表示の場合は、先頭0を無くす。
	$("#workTimeFrom").on("change", function() {
		setModifyValue($(this));
	})

	// 「休憩開始時刻1」の値が変更された時、先頭0付きの時刻表示の場合は、先頭0を無くす。
	$("#lunchBreakFrom").on("change", function() {
		setModifyValue($(this));
	})

	// 「休憩終了時刻2」の値が変更された時、先頭0付きの時刻表示の場合は、先頭0を無くす。
	$("#lunchBreakTo").on("change", function() {
		setModifyValue($(this));
	})
});