package model;

public class AttendanceLogic {
	WorkScheduleCommon common = new WorkScheduleCommon();
	/*
	 * 就業時間を算出する。
	 * 第1引数：出勤時間、 第2引数：退社時間、 第3引数：就業開始、 第4引数：就業終了、 第5引数：休憩1開始、 第6引数：休憩1終了、 第7引数：休憩2開始、 第8引数：休憩2終了、 第9引数：深夜終了
	 */
	public String calcWorkTime(int attendance, int leave, int workTimeFrom, int workTimeTo, int lunchBreakFrom, int lunchBreakTo, int eveningBreakFrom, int eveningBreakTo, int lateOverTimeTo) {
		int work = 0;

		// 出勤 < 就業開始のとき 出勤時間を就業開始にする。
		if (attendance < workTimeFrom) {
			attendance = workTimeFrom;
		}

		// 出勤 >= 就業開始 かつ 出勤 <= 休憩1開始の場合
		if (attendance >=  workTimeFrom && attendance <= lunchBreakFrom) {

			// 退社 > 就業開始 かつ 退社 <= 休憩1開始ならば
			if (leave > workTimeFrom && leave <= lunchBreakFrom) {
				work = leave - attendance;
			}

			// 退社 > 休憩1開始 かつ 退社 <= 休憩1終了
			if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
				work = lunchBreakFrom - attendance;
			}

			// 退社 > 休憩1終了 かつ 退社 <= 就業終了ならば
			if (leave > lunchBreakTo && leave <= workTimeTo) {
				work = leave - attendance - (lunchBreakTo - lunchBreakFrom);
			}

			// 退社 > 就業終了ならば
			if (leave > workTimeTo) {
				work= workTimeTo - attendance - (lunchBreakTo - lunchBreakFrom);
			}
		}

		// 出勤が休憩1中の場合
		if (attendance > lunchBreakFrom && attendance <= lunchBreakTo) {
			// 退社 > 休憩1終了 かつ 退社 <= 就業終了ならば
			if (leave > lunchBreakTo && leave <= workTimeTo) {
				work = leave - lunchBreakTo;
			}

			// 退社 > 就業終了ならば
			if (leave > workTimeTo) {
				work= workTimeTo - lunchBreakTo;
			}
		}

		// 出勤 > 休憩1終了 かつ 出勤 < 就業終了の場合
		if (attendance > lunchBreakTo && attendance < workTimeTo) {
			// 退社 > 休憩1終了 かつ 退社 <= 就業終了ならば
			if (leave > lunchBreakTo && leave <= workTimeTo) {
				work = leave - attendance;
			}

			// 退社 > 就業終了ならば
			if (leave > workTimeTo) {
				work= workTimeTo - attendance;
			}
		}

		String workTime = "";

		// 分表示 → 文字列表示に変換する。
		if (work != 0) {
			workTime = common.changeMinToTime(work);
		}


		return workTime;
	}

	/*
	 * 早出残業時間を算出する。
	 * 第1引数：出勤時間、 第2引数：就業開始、 第3引数：早出前(時間)
	 */
	public String calcEarlyTime(int attendance, int workTimeFrom, int earlyAppearance) {
		int early = 0;

		// 出勤時間 <= (就業開始 - 早出分前)
		if (attendance <= (workTimeFrom - earlyAppearance)) {
			early = workTimeFrom - attendance;
		}

		String earlyTime = "";

		// 分表示 → 文字列表示に変換する。
		if (early != 0) {
			earlyTime = common.changeMinToTime(early);
		}

		return earlyTime;
	}

	/*
	 * 普通残業時間を算出する。
	 * 第1引数：退社時間、 第2引数：普通残業開始、 第3引数：普通残業終了、 第4引数：深夜残業終了
	 */
	public String calcNormalOverTime(int leave, int normalOverTimeFrom, int normalOverTimeTo, int lateOverTimeTo) {
		int normalOver = 0;

		// 退社が18:01(普通残業開始) ～ 5:00(深夜残業終了)のとき、
		if (leave > normalOverTimeFrom && leave <= lateOverTimeTo) {
			// 退社時間が普通残業終了を超えたとき、退社時間を普通残業終了時間にする。
			if (leave > normalOverTimeTo) {
				leave = normalOverTimeTo;
			}

			normalOver = leave - normalOverTimeFrom;
		}

		String normalOverTime = "";

		// 分表示 → 文字列表示に変換する。
		if (normalOver != 0) {
			normalOverTime = common.changeMinToTime(normalOver);
		}

		return normalOverTime;
	}

	/*
	 * 休日区分0(平日):深夜残業時間を算出する。
	 * 第1引数：退社時間、 第2引数：深夜残業開始、 第3引数：深夜残業終了、 第4引数：休憩3開始、 第5引数：休憩3終了
	 */
	public String calcLateOverTime1(int leave, int lateOverTimeFrom, int lateOverTimeTo, int nightBreakFrom, int nightBreakTo) {
		int lateOver = 0;

		// 退社が22:00(深夜開始)～2:00(休憩3開始)の場合
		if (leave > lateOverTimeFrom && leave <= nightBreakFrom) {
			lateOver = leave - lateOverTimeFrom;
		}

		// 退社 > 2:00(休憩3開始) かつ 退社 <= 500 (深夜終了)の場合
		if (leave > nightBreakFrom && leave <= lateOverTimeTo) {
			// 退社 > 休憩3開始 もしくは <= 休憩3終了ならば 退社 = 休憩3開始
			if (leave > nightBreakFrom && leave <= nightBreakTo) {
				leave = nightBreakTo;
			}
			lateOver = leave - lateOverTimeFrom - (nightBreakTo - nightBreakFrom);
		}

		String lateOverTime = "";

		// 分表示 → 文字列表示に変換する。
		if (lateOver != 0) {
			lateOverTime = common.changeMinToTime(lateOver);
		}

		return lateOverTime;
	}

	/*
	 * 休日区分1(休日): 出勤 < 退社のときの深夜残業時間を算出する。
	 * 第1引数：退社時間、 第2引数：深夜残業開始、 第3引数：深夜残業終了、 第4引数：休憩3開始、 第5引数：休憩3終了、 第6引数：24時間(分)
	 */
	public String calcLateOverTime2(int attendance, int leave, int lateOverTimeFrom, int lateOverTimeTo, int nightBreakFrom, int nightBreakTo, int hour24) {
		int lateOver = 0;

		// 退社 > 深夜開始 かつ 退社 < 2400の場合、
		if (leave > lateOverTimeFrom && leave < hour24) {

			// 出勤 >= 0 かつ 出勤 <= 深夜残業開始ならば
			if (attendance >= 0 && attendance <= lateOverTimeFrom) {
				// 退社 - 深夜残業開始
				lateOver = leave - lateOverTimeFrom;
			}

			// 出勤 > 深夜残業開始 かつ 出勤 < 2400ならば
			if (attendance > lateOverTimeFrom && attendance < hour24) {
				lateOver = leave - attendance;
			}
		}

		// 退社 > 0 かつ 退社 <= 休憩3開始の場合、
		if (leave > 0 && leave <= nightBreakTo) {

			// 出勤 > 深夜残業終了 かつ 出勤 <= 深夜残業開始ならば
			if (attendance > lateOverTimeTo  && attendance <= lateOverTimeFrom) {
				// 退社 - 深夜残業開始
				lateOver = (leave + hour24)  - lateOverTimeFrom;
			}

			// 出勤 > 深夜残業開始 かつ 出勤 < 2400ならば
			if (attendance > lateOverTimeFrom && attendance < hour24) {
				lateOver = (leave + hour24) - attendance;
			}

			// 出勤 > 0 かつ 出勤 <= 深夜残業終了
			if (attendance > 0 && attendance <= (lateOverTimeTo - hour24)) {
				// 出勤 > 休憩3開始 かつ 出勤 <= 休憩3終了ならば 出勤 = 休憩3終了
				if (attendance > (nightBreakFrom - hour24) && attendance <= (nightBreakTo - hour24)) {
					attendance = (nightBreakTo - hour24);
				}
				lateOver = leave - attendance;
			}
		}

		// 退社 > 休憩3開始 かつ 退社 <= 休憩3終了の場合、
		if (leave > (nightBreakFrom - hour24) && leave <= (nightBreakTo - hour24)) {
			//退社 = 休憩3開始
			leave = (nightBreakFrom - hour24);

			// 出勤 > 深夜残業終了 かつ 出勤 <= 深夜残業開始ならば
			if (attendance > (lateOverTimeTo - hour24) && attendance <= lateOverTimeFrom) {
				// 退社 - 深夜残業開始
				lateOver = (leave + hour24)  - lateOverTimeFrom;
			}

			// 出勤 > 深夜残業開始 かつ 出勤 < 2400ならば
			if (attendance > lateOverTimeFrom && attendance < hour24) {
				lateOver = (leave + hour24) - attendance;
			}

			// 出社 > 0 かつ 出社 <= 深夜残業終了
			if (attendance > 0 && attendance <= (lateOverTimeTo - hour24)) {
				// 出勤 > 休憩3開始 かつ 出勤 <= 休憩3終了ならば 出勤 = 休憩3終了
				if (attendance > (nightBreakFrom - hour24) && attendance <= (nightBreakTo - hour24)) {
					attendance = (nightBreakTo - hour24);
				}
				lateOver = leave - attendance;
			}
		}

		// 退社 > 休憩3終了 かつ 退社 <= 深夜残業終了の場合、
		if (leave > (nightBreakTo - hour24) && leave <= (lateOverTimeTo - hour24)) {

			// 出社 > 深夜残業終了 かつ 出社 <= 深夜残業開始ならば
			if (attendance > (lateOverTimeTo - hour24) && attendance <= lateOverTimeFrom) {
				// 退社 - 深夜残業開始
				lateOver = (leave + hour24)  - lateOverTimeFrom;
			}

			// 出社 > 深夜残業開始 かつ 出社 < 2400ならば
			if (attendance > lateOverTimeFrom && attendance < hour24) {
				lateOver = (leave + hour24) - attendance;
			}

			// 出社 > 0 かつ 出社 <= 深夜残業終了
			if (attendance > 0 && attendance <= (lateOverTimeTo - hour24)) {
				// 出勤 > 休憩3開始 かつ 出勤 <= 休憩3終了ならば 出勤 = 休憩3終了
				if (attendance > (nightBreakFrom - hour24) && attendance <= (nightBreakTo - hour24)) {
					attendance = (nightBreakTo - hour24);
				}
				lateOver = leave - attendance;
			}
		}

		String lateOverTime = "";

		// 分表示 → 文字列表示に変換する。
		if (lateOver != 0) {
			lateOverTime = common.changeMinToTime(lateOver);
		}

		return lateOverTime;
	}

	/*
	 * 休日区分1(休日): 出勤 >= 退社のときの深夜残業時間を算出する。
	 * 第1引数：出勤時間、第2引数：退社時間、 第3引数：休憩1開始、 第4引数：休憩1終了、第5引数：休憩2開始、 第6引数：休憩2終了、 第7引数：深夜残業開始、 第8引数：深夜残業終了、 第9引数：休憩3開始、 第10引数：休憩3終了、 第11引数：24時間(分)
	 */
	public String calcLateOverTime3(int attendance, int leave, int lunchBreakFrom, int lunchBreakTo, int eveningBreakFrom, int eveningBreakTo, int lateOverTimeFrom, int lateOverTimeTo, int nightBreakFrom, int nightBreakTo, int hour24) {

		int lateOver = 0;

		// 出勤 > 0 かつ 出勤 <= 深夜残業終了の場合
		if (attendance >= 0 && attendance <= (lateOverTimeTo - hour24)) {
			// 出勤 > 休憩3開始 かつ 出勤 <= 休憩3終了ならば 出勤 = 休憩3開始
			if (attendance > (nightBreakFrom - hour24) && attendance <= (nightBreakTo - hour24)) {
				attendance = (nightBreakTo - hour24);
			}

			// 退社 > 0 かつ 退社 <= 深夜残業終了
			if (leave >= 0 && leave <= (lateOverTimeTo - hour24)) {
				// // 退社 > 休憩3開始 かつ 退社 <= 休憩3終了ならば 退社 = 休憩3開始
				if (leave > (nightBreakFrom - hour24) && leave <= (nightBreakTo - hour24)) {
					leave = (nightBreakFrom - hour24);
				}
				lateOver = (lateOverTimeTo - hour24) - attendance + ((leave + hour24) - lateOverTimeFrom);
			}
		}

		// 退社 >= 0 かつ 退社 <= 5:00のとき24h(1440)を足す
		if (leave >= 0 && leave <= 300) {
			leave += hour24;
		}


		// 出勤 > 深夜残業終了 かつ 出勤 <= 深夜残業開始の場合
		if (attendance > (lateOverTimeTo - hour24) && attendance <= lateOverTimeFrom) {
			// 退社 > 0 かつ 退社 <= 深夜残業開始ならば
			if (leave > 0 && leave <=  lateOverTimeFrom) {
				lateOver = lateOverTimeTo - lateOverTimeFrom - (nightBreakTo - nightBreakFrom);
			}

			// 退社 > 深夜残業開始 かつ 退社 <= 休憩3終了ならば
			if (leave > lateOverTimeFrom && leave <= nightBreakTo) {
				// 退社 > 休憩3開始 かつ 退社 <= 休憩3終了のとき 退社 = 休憩3開始
				if (leave > nightBreakFrom && leave <= nightBreakTo) {
					leave = nightBreakFrom;
				}
				lateOver = leave - lateOverTimeFrom;
			}

			// 退社 > 休憩3終了のならば
			if (leave > nightBreakTo) {
				lateOver = leave - lateOverTimeFrom - (nightBreakTo - nightBreakFrom);
			}
		}

		// 出勤 > 深夜残業開始 かつ 出勤 < 2400の場合
		if (attendance > lateOverTimeFrom && attendance < hour24) {

			// 退社 > 0 かつ 退社 <= 深夜残業開始ならば
			if (leave > 0 && leave <=  lateOverTimeFrom) {
				lateOver = lateOverTimeTo - attendance - (nightBreakTo - nightBreakFrom);
			}

			// 退社 > 深夜残業開始 かつ 退社 < 2400ならば
			if (leave > lateOverTimeFrom  && leave < hour24) {
				lateOver = leave - attendance;
			}

			// 退社 > 2400 かつ 退社 <= 休憩3終了ならば
			if (leave > hour24 && leave <= (nightBreakTo)) {
				// 退社 > 休憩3開始 かつ 退社 <= 休憩3終了のとき 退社 = 休憩3開始
				if (leave > nightBreakFrom && leave <= nightBreakTo) {
					leave = nightBreakFrom;
				}
				// 退社 - 出勤
				lateOver = leave - attendance;
			}

			// 退社 > 休憩3終了 かつ 退社 <= 深夜残業終了ならば
			if (leave > nightBreakTo && leave <= (lateOverTimeTo)) {
				// 退社 - 出勤 - (休憩3終了 - 休憩3開始)
				lateOver = leave - attendance - (nightBreakTo - nightBreakFrom);
			}
		}

		String lateOverTime = "";

		// 分表示 → 文字列表示に変換する。
		if (lateOver != 0) {
			lateOverTime = common.changeMinToTime(lateOver);
		}

		return lateOverTime;
	}

	/*
	 * 遅刻時間を算出する。
	 * 第1引数：出勤、 第2引数：就業開始、 第3引数：就業終了、 第4引数：休憩1開始、 第4引数：休憩1終了
	 */
	public String calcLateTime(int attendance, int workTimeFrom, int workTimeTo, int lunchBreakFrom, int lunchBreakTo) {
		int late = 0;

		// 出勤 > 就業開始 かつ 出勤 <= 休憩1終了のとき
		if (attendance > workTimeFrom && attendance <= lunchBreakTo) {
			// 出勤が休憩1の間のならば、出勤 = 休憩1開始とする。
			if (attendance > lunchBreakFrom && attendance <= lunchBreakTo) {
				attendance = lunchBreakFrom;
			}
			late = attendance - workTimeFrom;
		}

		// 出勤 > 休憩1終了 かつ 出勤 < 就業終了のとき
		if (attendance > lunchBreakTo && attendance < workTimeTo) {
			late = attendance - workTimeFrom - (lunchBreakTo - lunchBreakFrom);
		}

		String lateTime = "";

		// 分表示 → 文字列表示に変換する。
		if (late != 0) {
			lateTime = common.changeMinToTime(late);
		}

		return lateTime;
	}

	/*
	 * 早退時間を算出する。
	 * 第1引数：退社、 第2引数：就業開始、 第3引数：就業終了、 第4引数：休憩1開始、 第5引数：休憩1終了
	 */
	public String calcLeavingEarlyTime(int leave, int workTimeFrom, int workTimeTo, int lunchBreakFrom, int lunchBreakTo) {
		int leavingEarly = 0;

		// 退社 > 就業開始 かつ 退社 <= 休憩1終了の場合、
		if (leave > workTimeFrom && leave <= lunchBreakTo) {
			// 退社が休憩1の間ならば、退社 = 休憩1終了とする。
			if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
				leave = lunchBreakFrom;
			}
			leavingEarly = workTimeTo - leave - (lunchBreakTo - lunchBreakFrom);
		}

		// 退社 > 休憩1終了 かつ 退社 < 就業終了の場合
		if (leave > lunchBreakTo && leave < workTimeTo) {
			leavingEarly = workTimeTo - leave;
		}

		String leavingEarlyTime = "";

		// 分表示 → 文字列表示に変換する。
		if (leavingEarly != 0) {
			leavingEarlyTime = common.changeMinToTime(leavingEarly);
		}

		return leavingEarlyTime;
	}

	/*
	 * 出勤 < 退社のときの「休出時間」を算出する。
	 * 第1引数： 出勤時間、 第2引数：退社時間、 第3引数：休憩1開始、 第4引数：休憩1終了、 第5引数：休憩2開始、 第6引数：休憩2終了、 第7引数：深夜残業開始時間、 第8引数：24h
	 */
	public String calcBreakTime1(int attendance, int leave, int lunchBreakFrom, int lunchBreakTo, int eveningBreakFrom, int eveningBreakTo, int lateOverTimeFrom, int hour24 ) {

		int calcBreak = 0;

		// 出勤 > 0 かつ 出勤 <= 休憩1開始の場合
		if (attendance > 0 && attendance <= lunchBreakFrom) {

			// 退社 > 0:00 かつ 退社 <= 休憩1終了ならば
			if (leave > 0 && leave <= lunchBreakTo) {
				// 退社 > 休憩1開始 かつ 退社 <= 休憩1終了ならば 退社 = 休憩1開始に設定する
				if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
					leave = lunchBreakFrom;
				}
				calcBreak = leave - attendance;
			}

			// 退社 > 休憩1終了 かつ 退社 <= 1800(休憩2終了)ならば
			if (leave > lunchBreakTo && leave <= eveningBreakTo) {
				// 退社 > 休憩2開始 かつ 退社 <= 休憩2終了ならば 退社 = 休憩2開始
				if (leave > eveningBreakFrom && leave <= eveningBreakTo) {
					leave = eveningBreakFrom;
				}
				calcBreak = leave - attendance - (lunchBreakTo - lunchBreakFrom);
			}

			// 退社 > 休憩2終了 かつ 退社 < 2400ならば
			if (leave > eveningBreakTo && leave < hour24) {
				// 退社 > 深夜残業開始ならば 退社 = 深夜残業開始
				if (leave >lateOverTimeFrom) {
					leave = lateOverTimeFrom;
				}
				calcBreak = leave - attendance - (lunchBreakTo - lunchBreakFrom) - (eveningBreakTo - eveningBreakFrom);
			}
		}

		// 出勤 > 休憩1開始 かつ 出勤 <= 休憩1終了の場合
		if (attendance > lunchBreakFrom && attendance <= lunchBreakTo) {

			// 退社 > 休憩1終了 かつ 退社 <= 1800(休憩2終了)ならば
			if (leave > lunchBreakTo && leave <= eveningBreakTo) {
				// 退社 > 休憩2開始 かつ 退社 <= 休憩2終了ならば 退社 = 休憩2開始
				if (leave > eveningBreakFrom && leave <= eveningBreakTo) {
					leave = eveningBreakFrom;
				}
				calcBreak = leave - lunchBreakTo;
			}

			// 退社 > 休憩2終了 かつ 退社 < 2400ならば
			if (leave > eveningBreakTo && leave < hour24) {
				// 退社 > 深夜残業開始ならば 退社 = 深夜残業開始
				if (leave >lateOverTimeFrom) {
					leave = lateOverTimeFrom;
				}
				calcBreak = leave - lunchBreakTo - (eveningBreakTo - eveningBreakFrom);
			}
		}

		// 出勤 > 休憩1終了 かつ 出勤 < 2200(深夜残業開始)の場合
		if (attendance > lunchBreakTo && attendance < lateOverTimeFrom) {

			// 退社 > 休憩1終了 かつ 退社 < 2200(深夜残業開始)ならば
			if (leave > lunchBreakTo && leave <= lateOverTimeFrom) {
				// 退社 > 休憩2開始 かつ 退社 <= 休憩2終了ならば 退社 = 休憩2開始
				if (leave > eveningBreakFrom && leave <= eveningBreakTo) {
					leave = eveningBreakFrom;
				}
				calcBreak = leave - attendance;
			}

		}

		String breakTime = "";

		// 分表示 → 文字列表示に変換する。
		if (calcBreak != 0) {
			breakTime = common.changeMinToTime(calcBreak);
		}

		return breakTime;
	}

	/*
	 * 出勤 >= 退社のときの「休出時間」を算出する。
	 * 第1引数：出勤時間、 第2引数：退社時間、 第3引数：就業開始時間、 第4引数：休憩1開始、 第5引数：休憩1終了、 第6引数：休憩2開始、 第7引数：休憩2終了、 第8引数：深夜残業時間開始、 第9引数：深夜残業終了
	 */
	public String calcBreakTime2(int attendance, int leave, int workTimeFrom, int lunchBreakFrom, int lunchBreakTo, int eveningBreakFrom, int eveningBreakTo, int lateOverTimeFrom, int lateOverTimeTo, int hour24) {

		int calcBreak = 0;

		// 退社 >= 0 かつ 退社 <= 500のとき 24h(1440)を足す。
		if (leave >= 0 && leave <= 300) {
			leave += hour24;
		}

		// 出勤 >= 0 かつ 出勤 <= 休憩1開始の場合
		if (attendance >= 0 && attendance <= lunchBreakFrom) {

			// 退社 > 0 かつ 退社 <= 深夜残業終了ならば
			if (leave >= hour24 && leave <= lateOverTimeTo) {
				// (深夜残業開始 - 出勤 - (休憩1終了 - 休憩1開始) - (休憩2終了 - 休憩2開始))
				calcBreak = lateOverTimeFrom - attendance - (lunchBreakTo - lunchBreakFrom) - (eveningBreakTo - eveningBreakFrom);
			}

			// 退社 > 深夜残業終了 かつ 退社 <= 休憩1終了ならば
			if (leave > (lateOverTimeTo - 1440) && leave <= lunchBreakTo) {
				// (深夜残業開始 - 出勤 - (休憩1終了 - 休憩1開始) - (休憩2終了 - 休憩2開始)) + (退社 -深夜残業終了)
				calcBreak = (lateOverTimeFrom - attendance - (lunchBreakTo - lunchBreakFrom) - (eveningBreakTo - eveningBreakFrom)) + (leave - (lateOverTimeTo - hour24));
			}

		}

		// 出勤 > 休憩1開始 かつ 出勤 <= 休憩1終了の場合
		if (attendance > lunchBreakFrom && attendance <= lunchBreakTo) {

			//  出勤 > 休憩1開始 かつ 出勤 <= 休憩1終了の場合、 出勤 = 休憩1終了
			if (attendance > lunchBreakFrom && attendance <= lunchBreakTo) {
				attendance = lunchBreakTo;
			}

			// 退社 > 0 かつ 退社 <= 深夜残業終了ならば
			if (leave >= hour24 && leave <= lateOverTimeTo) {
				// (深夜残業開始 - 出勤 - (休憩2終了 - 休憩2開始))
				calcBreak = lateOverTimeFrom - attendance - (eveningBreakTo - eveningBreakFrom);
			}

			// 退社 > 深夜残業終了 かつ 退社 <= 休憩1終了ならば
			if (leave > (lateOverTimeTo - 1440) && leave <= lunchBreakTo) {
				// 退社 > 休憩1開始 かつ 退社 <= 休憩1終了ならば、退社 = 休憩1開始
				if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
					leave = lunchBreakFrom;
				}
				// (深夜残業開始 - 出勤 - (休憩2終了 - 休憩2開始)) + (退社 -深夜残業終了)
				calcBreak = (lateOverTimeFrom - attendance - (eveningBreakTo - eveningBreakFrom)) + (leave - (lateOverTimeTo - hour24));
			}
		}

		// 出勤 > 休憩1終了 かつ 出勤 <= 休憩2開始の場合、
		if (attendance > lunchBreakTo && attendance <= eveningBreakTo) {

			// 退社 > 0 かつ 退社 <= 深夜残業終了ならば
			if (leave >= hour24 && leave <= lateOverTimeTo) {
				// 深夜残業開始 - 出勤 - (休憩2終了 - 休憩2開始)
				calcBreak = lateOverTimeFrom - attendance - (eveningBreakTo - eveningBreakFrom);
			}

			// 退社 > 深夜残業終了 かつ 退社 <= 休憩1終了ならば
			if (leave > lateOverTimeTo - 1440 && leave <= lunchBreakTo) {
				// 退社 > 休憩1開始 かつ 退社 <= 休憩1終了ならば、退社 = 休憩1開始
				if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
					leave = lunchBreakFrom;
				}
				// (深夜残業開始 - 出勤 - (休憩2終了 - 休憩2開始)) + (退社 -深夜残業終了)
				calcBreak = (lateOverTimeFrom - attendance - (eveningBreakTo - eveningBreakFrom)) + (leave - (lateOverTimeTo - hour24));
			}

			// 退社 > 休憩1終了 かつ 退社 <= 休憩2開始ならば
			if (leave > lunchBreakTo && leave <= eveningBreakTo) {

				// (深夜残業開始 - 出勤 - (休憩2終了 - 休憩2開始)) + (退社 - 深夜残業終了 - (休憩1終了 - 休憩1開始))
				calcBreak = (lateOverTimeFrom - attendance - (eveningBreakTo - eveningBreakFrom)) + (leave - (lateOverTimeTo - hour24) - (lunchBreakTo - lunchBreakFrom));
			}
		}

		// 出勤 > 休憩2開始 かつ 出勤 <= 休憩2終了の場合、
		if (attendance > eveningBreakFrom && attendance <= eveningBreakTo) {
			// 出勤 > 休憩2開始 かつ 出勤 <= 休憩2終了ならば 出勤 = 休憩2終了
			if (attendance > eveningBreakFrom && attendance <= eveningBreakTo) {
				attendance = eveningBreakTo;
			}

			// 退社 > 0 かつ 退社 <= 深夜残業終了ならば
			if (leave >= hour24 && leave <= lateOverTimeTo) {
				// 深夜残業開始 - 出勤
				calcBreak = lateOverTimeFrom - attendance;
			}

			// 退社 > 深夜残業終了 かつ 退社 <= 休憩1終了ならば
			if (leave > lateOverTimeTo - 1440 && leave <= lunchBreakTo) {
				// 退社 > 休憩1開始 かつ 退社 <= 休憩1終了ならば、退社 = 休憩1開始
				if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
					leave = lunchBreakFrom;
				}
				// (深夜残業開始 - 出勤) + (退社 -深夜残業終了)
				calcBreak = (lateOverTimeFrom - attendance) + (leave - (lateOverTimeTo - hour24));
			}

			// 退社 > 休憩1終了 かつ 退社 <= 1800(休憩2終了)ならば
			if (leave > lunchBreakTo && leave <= eveningBreakTo) {
				// 退社 > 17:00(休憩2開始)かつ 退社 <= 休憩2終了ならば 退社 = 休憩2開始
				if (leave > eveningBreakFrom && leave <= eveningBreakTo) {
					leave = eveningBreakFrom;
				}
				// (深夜残業開始 - 出勤) + (退社 - 深夜残業終了 - (休憩1終了 - 休憩1開始))
				calcBreak = (lateOverTimeFrom - attendance) + (leave - (lateOverTimeTo - hour24) - (lunchBreakTo - lunchBreakFrom));
			}
		}


		// 出勤 > 休憩2終了 かつ 出勤 <= 2200(深夜残業開始)の場合
		if (attendance > eveningBreakTo && attendance <= lateOverTimeFrom) {

			// 退社 > 0 かつ 退社 <= 深夜残業終了ならば
			if (leave >= hour24 && leave <= lateOverTimeTo) {
				// 深夜残業開始 - 出勤
				calcBreak = lateOverTimeFrom - attendance;
			}

			// 退社 > 深夜残業終了 かつ 退社 <= 休憩1終了ならば
			if (leave > lateOverTimeTo - 1440 && leave <= lunchBreakTo) {
				// 退社 > 休憩1開始 かつ 退社 <= 休憩1終了ならば、退社 = 休憩1開始
				if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
					leave = lunchBreakFrom;
				}
				// (深夜残業開始 - 出勤) + (退社 -深夜残業終了)
				calcBreak = (lateOverTimeFrom - attendance) + (leave - (lateOverTimeTo - hour24));
			}

			// 退社 > 休憩1終了 かつ 退社 <= 1800(休憩2終了)ならば
			if (leave > lunchBreakTo && leave <= eveningBreakTo) {
				// 退社 > 17:00(休憩2開始)かつ 退社 <= 休憩2終了ならば 退社 = 休憩2開始
				if (leave > eveningBreakFrom && leave <= eveningBreakTo) {
					leave = eveningBreakFrom;
				}
				// (深夜残業開始 - 出勤) + (退社 - 深夜残業終了 - (休憩1終了 - 休憩1開始))
				calcBreak = (lateOverTimeFrom - attendance) + (leave - (lateOverTimeTo - hour24) - (lunchBreakTo - lunchBreakFrom));
			}

			// 退社 > 休憩2終了 かつ 退社 < 深夜残業開始 ならば
			if (leave > eveningBreakTo && leave < lateOverTimeFrom) {
				// (深夜残業開始 - 出勤) + (退社 - 深夜残業終了 - (休憩1終了 - 休憩1開始) - (休憩2終了 - 休憩2開始))
				calcBreak = (lateOverTimeFrom - attendance) + (leave - (lateOverTimeTo - hour24) - (lunchBreakTo - lunchBreakFrom) - (eveningBreakTo - eveningBreakFrom));
			}

		}

		// 出勤 > 深夜残業開始 かつ 出勤 < 2400の場合、
		if (attendance > lateOverTimeFrom && attendance < hour24) {

			// 退社 > 深夜残業終了 かつ 退社 <= 休憩1終了ならば、
			if (leave > (lateOverTimeTo - hour24) && leave <= lunchBreakTo) {
				// 退社 > 休憩1開始 かつ 退社 <= 休憩1終了ならば 退社 = 休憩1開始に設定する
				if (leave > lunchBreakFrom && leave <= lunchBreakTo) {
					leave = lunchBreakFrom;
				}
				// 退社 - 深夜残業終了
				calcBreak = leave - (lateOverTimeTo - hour24);
			}

			// 退社 > 休憩1終了 かつ 退社 <= 1800(休憩2終了)ならば
			if (leave > lunchBreakTo && leave <= eveningBreakTo) {
				// 退社 > 17:00(休憩2開始)かつ 退社 <= 休憩2終了ならば 退社 = 休憩2開始
				if (leave > eveningBreakFrom && leave <= eveningBreakTo) {
					leave = eveningBreakFrom;
				}
				//退社 - 深夜残業終了 - (休憩1終了 - 休憩1開始)
				calcBreak = leave - (lateOverTimeTo - hour24) - (lunchBreakTo - lunchBreakFrom);
			}

			// 退社 > 休憩2終了 かつ 退社 <= 深夜残業開始ならば
			if (leave > eveningBreakTo && leave <= lateOverTimeFrom) {
				// 退社 - 深夜残業終了 - (休憩1終了 - 休憩1開始) - (休憩2終了 - 休憩2開始)
				calcBreak = leave - (lateOverTimeTo - hour24) - (lunchBreakTo - lunchBreakFrom) - (eveningBreakTo - eveningBreakFrom);
			}
		}

		String breakTime = "";

		if (calcBreak != 0) {
			breakTime = common.changeMinToTime(calcBreak);
		}

		return breakTime;
	}
}
