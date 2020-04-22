package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class WorkScheduleLogic {

	/*
	 * 社員情報を取得する。(idとパスワード)
	 */
	public Employee getEmp(int id, String pass) {
		EmpDAO empDAO = new EmpDAO();

		Employee emp = empDAO.getEmployee(id, pass);
		return emp;
	}

	/*
	 * 社員情報をrequestスコープにセットする。
	 */
	public void setEmpInfo(Employee emp) {
		EmpDAO empDAO = new EmpDAO();
		empDAO.setInfo(emp);
	}

	/*
	 * 初期表示：現在日付の年・月を設定する。
	 * 第1引数：request, 第2引数：カレンダーインスタンス
	 */
	public void setDate(HttpServletRequest request, Calendar cal) {

		// 年が設定されていればその値を取得。そうでなければ、今年の年号をセット。
		if (request.getParameter("year") == null) {
			request.setAttribute("year", cal.get(Calendar.YEAR));
		} else {
			request.setAttribute("year", request.getParameter("year"));
		}

		// 月が設定されていればその値を取得。そうでなければ、今年の月をセット。
		if (request.getParameter("month") == null) {
			request.setAttribute("month", cal.get(Calendar.MONTH) + 1);
		} else {
			request.setAttribute("month", request.getParameter("month"));
		}
	}

	//
	public void setCalendarDate(HttpServletRequest request, Calendar cal, String button_Condition) {

		// 「今月ボタン」押下時、今年の年号を設定する。そうでなければ、設定されている値を再設定。
		if ("今月".equals(button_Condition)) {
			request.setAttribute("year", cal.get(Calendar.YEAR));
		} else {
			request.setAttribute("year", request.getParameter("year"));
		}

		// 「今月ボタン」押下時、今年の月を設定する。
		// 「前月ボタン」押下時、月の値を-1する。その時、月が1の時は同時に年も-1する。
		// 「次月ボタン」押下時、月の値を+1する。その時、月が12の時は同時に年も+1する。
		//  上記のいずれでもなければ、設定されている値を再設定。
		if ("今月".equals(button_Condition)) {
			request.setAttribute("month", cal.get(Calendar.MONTH) + 1);
		} else if ("前月".equals(button_Condition)) {
			int year = Integer.parseInt(request.getParameter("year"));
			int month = Integer.parseInt(request.getParameter("month"));

			if (month == 1) {
				month = 12;
				year--;
			} else {
				month--;
			}

			request.setAttribute("year", Integer.toString(year));
			request.setAttribute("month", Integer.toString(month));
		} else if ("次月".equals(button_Condition)) {
			int year = Integer.parseInt(request.getParameter("year"));
			int month = Integer.parseInt(request.getParameter("month"));

			if (month == 12) {
				month = 1;
				year++;
			} else {
				month++;
			}

			request.setAttribute("year", Integer.toString(year));
			request.setAttribute("month", Integer.toString(month));
		} else {
			request.setAttribute("month", request.getParameter("month"));
		}
	}

	/*
	 * 該当月の最終日を設定する。
	 * 第1引数：request, 第2引数：カレンダーインスタンス
	 */
	public int setLastDay(Calendar cal, int year, int month) {

		// 当月の初日をセット
		cal.set(year, month - 1, 1);

		// 月末の日付を取得(翌月の1日から1日引く)
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		int lastDay = cal.get(Calendar.DATE);

		return lastDay;
	}

	/*
	 * 祝祭日マスターより祝祭日の日付と名前を取得し、requestスコープにセット。
	 * 第1引数：リクエスト
	 */
	public List<Holidays> getHoliday() {
		HolidayDAO holidayDAO = new HolidayDAO();

		List<Holidays> holidayList = holidayDAO.getHolidays();


		return holidayList;
	}

	/*
	 * 登録日にレコードがあるか判定する。
	 * 第1引数：登録日
	 */
	public boolean isRecord(String date, int employeeId) {
		AttendanceDAO attendanceDAO = new AttendanceDAO();

		// DBから登録日のレコードを取得する。
		String record = attendanceDAO.getRecord(date, employeeId);

		// 3項演算子：打刻日にレコードがあればtrue、なければfalse代入。
		return record != null ? true : false;
	}

	/*
	 * 打刻日にレコードがあれば更新処理、なければ新規登録を行う。
	 * 第1引数：リクエスト、 第2引数：打刻ボタン名、 第3引数：打刻日、 第4引数：従業員ID、 第5引数：休日区分
	 */
	public void embossExecute(HttpServletRequest request, String embossCondition, String date, Employee emp, int holidayCategory) {
		WorkScheduleCommon common = new WorkScheduleCommon();
		AttendanceDAO attendanceDAO = new AttendanceDAO();

		// 出退勤時間の取得。
		String time = request.getParameter("time");

		// 数値表示の時刻を取得する。
		int attendanceTime = common.changeTimeToNum(time);

		// 3項演算子：打刻ボタンが「出勤」であればattendance、そうでなければleavingを代入。
		String action = "出勤".equals(embossCondition) ? "attendance" : "leave";

		// 打刻日にレコードがあれば更新処理、なければ新規登録を行う。
		if (isRecord(date, emp.getId())) {
			attendanceDAO.updateAttendanceData(date, attendanceTime, action, emp.getId());
		} else {
			attendanceDAO.registerAttendanceData(date, emp.getId(), holidayCategory, attendanceTime, action);
		}
	}

	/*
	 * 変更時にレコードがあれば更新処理、なければ新規登録を行う。(ajax)
	 *
	 */
	public String changeExecute(HttpServletRequest request, String date, Employee emp, int holidayCategory) {
		WorkScheduleCommon common = new WorkScheduleCommon();
		AttendanceDAO attendanceDAO = new AttendanceDAO();

		// ajaxで送られたデータを取得する。
		String data = request.getParameter("data");
		// 値が変更された項目を取得する。「holiday」,「attendance」, 「leave」,「absence」, 「remark」
		String action = request.getParameter("action");

		// 打刻日にレコードがあれば更新処理、なければ新規登録を行う。
		if (isRecord(date, emp.getId())) {
			// 「出勤」or「退社」の値が送信された時、数値表示の時刻に変換してから更新。
			if (("attendance".equals(action) && !("".equals(data))) || ("leave".equals(action) && !("".equals(data)))) {
				int attendanceTime = common.changeTimeToNum(data);
				attendanceDAO.updateAttendanceData(date, attendanceTime, action, emp.getId());
			} else {
				attendanceDAO.updateData(date, data, action, emp.getId());
			}
		} else {
			// 「出勤」or「退社」の値が送信された時、数値表示の時刻に変換してから新規登録。
			if (("attendance".equals(action) && !("".equals(data))) || ("leave".equals(action) && !("".equals(data)))) {
				int attendanceTime = common.changeTimeToNum(data);
				attendanceDAO.registerAttendanceData(date, emp.getId(), holidayCategory, attendanceTime, action);
			} else {
				attendanceDAO.registerData(date, emp.getId(), holidayCategory, data, action);
			}
		}
		return action;
	}

	/*
	 * 打刻日の休日区分(0 or 1)を取得する。
	 * 打刻日の曜日を取得し、土日であれば休日区分：1とし、その他であれば平日区分:0とする。
	 * また、祝祭日リストと一致する日付があれば休日区分：1とし、なければ平日区分：0とする。
	 * 第1引数: 打刻年, 第2引数：打刻月, 第3引数：打刻日, 第4引数：祝祭日リスト
	 */
	public int getHolidayCategory(int embossYear, int embossMonth, int embossDayOfMonth, List<Holidays> holidayList) {
		// 休日区分を0(平日区分)で初期化。
		int holidayCategory = 0;

		// 打刻日の曜日を取得する。
		Calendar cal = Calendar.getInstance();
		cal.set(embossYear, embossMonth - 1, embossDayOfMonth);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

		// 曜日が日曜日もしくは土曜日の時(1 or 7)、休日区分を1にする。
		if (dayOfWeek == 1 || dayOfWeek == 7) {
			holidayCategory = 1;
		}

		// 月と日は1～9の時、二桁にして01などと表示するようにする。
		try {
			String monthDate = String.format("%tm", cal.getTime());
			String dayOfMonthDate = String.format("%td", cal.getTime());

			// 打刻日を取得する。
			String today = embossYear + "-" + monthDate + "-" + dayOfMonthDate;

			// 祝祭日リストの日付と一致した場合、休日区分を1にする。
			for (int i = 0; i < holidayList.size(); i++) {
				String holidayDate = holidayList.get(i).getHolidayDate();

				if (today.equals(holidayDate)) {
					holidayCategory = 1;
					break;
				}
			}
		} catch (IllegalFormatException e) {
			e.printStackTrace();
		}

		return holidayCategory;
	}

	/*
	 * 該当年・月のレコードの中の「就業」、「早出残」、「普通残」、「深夜残業」、「休出」、「遅刻」、「早退」の合計を取得する。
	 * 第1引数： 勤怠レコードリスト
	 */
	public Map<String, String> getAttendanceSumMap(List<Attendance> attendanceList) {
		WorkScheduleCommon common = new WorkScheduleCommon();

		int sumWorkTime = 0; // 就業時間の合計値(分)
		int sumEarlyTime = 0; // 早出残業の合計値(分)
		int sumNormalOverTime = 0; // 普通残業の合計値(分)
		int sumLateOverTime = 0; // 深夜残業の合計値(分)
		int sumBreakTime = 0; // 休出の合計値(分)
		int sumLateTime = 0; // 遅刻の合計値(分)
		int sumLeavingEarlyTime = 0; // 早退の合計値(分)

		for (int i = 0; i < attendanceList.size(); i++) {
			// 文字列表示の時刻を取得する。
			String workTime = attendanceList.get(i).getWorkTime(); // 就業時間
			String earlyTime = attendanceList.get(i).getEarlyTime(); // 早出残業
			String normalOverTime = attendanceList.get(i).getNormalOverTime(); // 普通残業
			String lateOverTime = attendanceList.get(i).getLateOverTime(); // 深夜残業
			String breakTime = attendanceList.get(i).getBreakTime(); // 休出
			String lateTime = attendanceList.get(i).getLateTime(); // 遅刻
			String leavingEarlyTime = attendanceList.get(i).getLeavingEarlyTime(); // 早退

			// それぞれ値があれば分表示に変換して合計変数に足していく。

			sumWorkTime += workTime != "" ? common.changeTimeToMin(workTime) : 0;
			sumEarlyTime += earlyTime != "" ? common.changeTimeToMin(earlyTime) : 0;
			sumNormalOverTime += normalOverTime != "" ? common.changeTimeToMin(normalOverTime) : 0;
			sumLateOverTime += lateOverTime != "" ? common.changeTimeToMin(lateOverTime) : 0;
			sumBreakTime += breakTime != "" ? common.changeTimeToMin(breakTime) : 0;
			sumLateTime += lateTime != "" ? common.changeTimeToMin(lateTime) : 0;
			sumLeavingEarlyTime += leavingEarlyTime != "" ? common.changeTimeToMin(leavingEarlyTime) : 0;
		}

		// 分表示 → 文字列表示に変換してマップにセットする。
		Map<String, String> attendanceMap = new HashMap<String, String>();
		attendanceMap.put("sumWorkTime", common.changeMinToTime(sumWorkTime));
		attendanceMap.put("sumEarlyTime", common.changeMinToTime(sumEarlyTime));
		attendanceMap.put("sumNormalOverTime", common.changeMinToTime(sumNormalOverTime));
		attendanceMap.put("sumLateOverTime", common.changeMinToTime(sumLateOverTime));
		attendanceMap.put("sumBreakTime", common.changeMinToTime(sumBreakTime));
		attendanceMap.put("sumLateTime", common.changeMinToTime(sumLateTime));
		attendanceMap.put("sumLeavingEarlyTime", common.changeMinToTime(sumLeavingEarlyTime));

		return attendanceMap;
	}

	/*
	 *  該当月の日分のレコードを作成し、該当年・月のデータがある日付のみにデータをセットした日付分のレコードを取得する。
	 *	第1引数：該当付きの最終日、第2引数：DBから取得したレコードリスト
	 */

	public List<Attendance> getRecordList(int year, int month, int lastDay, Employee emp) {
		WorkScheduleCommon common = new WorkScheduleCommon();
		AttendanceLogic attendanceLogic = new AttendanceLogic();
		List<Attendance> recordList = new ArrayList<Attendance>();

		// 日付分のレコードを作成。
		for (int i = 1; i <= lastDay; i++) {
			recordList.add(new Attendance(i, 0, "", "", 0, "", "", "", "", "", "", "", ""));
		}

		// // 該当年・月の勤怠データをDBから取得する。
		AttendanceDAO attendanceDAO = new AttendanceDAO();
		List<Attendance> attendanceList = attendanceDAO.getAttendanceData(year, month, lastDay, emp.getId());

		// 該当月のレコードリストにあるそれぞれのAttendanceオブジェクトの日付から日のみを取得する。
		// また、休日区分、出勤時間、退社時間、欠勤区分、備考も取得し、出勤時間、退社時間、備考については加工してセットする。
		for (int i = 0; i < attendanceList.size(); i++) {
			String date = attendanceList.get(i).getDate();
			String[] dateList = date.split("-");
			int day = Integer.parseInt(dateList[2]);

			int holiday = attendanceList.get(i).getHoliday();
			String attendanceTime = attendanceList.get(i).getAttendanceTime();
			String leavingTime = attendanceList.get(i).getLeavingTime();
			int absence = attendanceList.get(i).getAbsence();
			String remark = attendanceList.get(i).getRemark();

			if (attendanceTime == null || ":".equals(attendanceTime)) {
				attendanceTime = "";
			}

			if (leavingTime == null || ":".equals(leavingTime)) {
				leavingTime = "";
			}

			if (remark == null) {
				remark = "";
			}

			// Attendanceオブジェクトに設定する。
			recordList.get(day - 1).setHoliday(holiday);
			recordList.get(day - 1).setAttendanceTime(attendanceTime);
			recordList.get(day - 1).setLeavingTime(leavingTime);
			recordList.get(day - 1).setAbsence(absence);
			recordList.get(day - 1).setRemark(remark);

			// 出勤時間と退社時間に値があるとき、勤怠情報の計算準備をする。
			if ((attendanceTime != null && !("".equals(attendanceTime)))
					&& (leavingTime != null && !("".equals(leavingTime)))) {

				// それぞれの時刻を分表示で取得する。
				final int hour24 = 60 * 24;
				int attendance = common.changeTimeToMin(attendanceTime); // 出勤
				int leave = common.changeTimeToMin(leavingTime); // 退社
				int workTimeFrom = common.changeTimeToMin(emp.getWorkTimeFrom()); // 就業開始
				int workTimeTo = common.changeTimeToMin(emp.getWorkTimeTo()); // 就業終了
				int normalOverTimeFrom = common.changeTimeToMin(emp.getNormalOverTimeFrom()); // 普通残業開始
				int normalOverTimeTo = common.changeTimeToMin(emp.getNormalOverTimeTo()); // 普通残業終了
				int lateOverTimeFrom = common.changeTimeToMin(emp.getLateOverTimeFrom()); // 深夜残業開始
				int lateOverTimeTo = common.changeTimeToMin(emp.getLateOverTimeTo()) + hour24; // 深夜残業終了
				int lunchBreakFrom = common.changeTimeToMin(emp.getLunchBreakFrom()); // 休憩1開始
				int lunchBreakTo = common.changeTimeToMin(emp.getLunchBreakTo()); // 休憩1終了
				int eveningBreakFrom = common.changeTimeToMin(emp.getEveningBreakFrom()); // 休憩2開始
				int eveningBreakTo = common.changeTimeToMin(emp.getEveningBreakTo()); // 休憩2終了
				int nightBreakFrom = common.changeTimeToMin(emp.getNightBreakFrom()) + hour24; // 休憩3開始
				int nightBreakTo = common.changeTimeToMin(emp.getNightBreakTo()) + hour24; // 休憩3終了
				int earlyAppearance = emp.getEarlyAppearance(); // 早出

				String workTime = ""; // 就業時間
				String earlyTime = ""; // 早出残業
				String normalOverTime = ""; // 普通残業
				String lateOverTime = ""; // 深夜残業
				String breakTime = ""; // 休出
				String lateTime = ""; // 遅刻
				String leavingEarlyTime = ""; // 早退

				// 休日区分：0(平日)の時、「就業時間」、「早出残業」、「普通残業」、「遅刻」、「早退」)を算出して、Attendanceオブジェクトに設定する。
				if (holiday == 0) {

					// 退社が0:00 ～ 6:00のとき 24hを足す。
					if (leave >= 0 && leave <= 360) {
						leave += hour24;
					}

					// 退社が出勤より大きい場合、各値を算出する。
					if (attendance < leave) {
						// 時刻表示で取得する。
						workTime = attendanceLogic.calcWorkTime(attendance, leave, workTimeFrom, workTimeTo,
								lunchBreakFrom,
								lunchBreakTo, eveningBreakFrom, eveningBreakTo, lateOverTimeTo);
						earlyTime = attendanceLogic.calcEarlyTime(attendance, workTimeFrom, earlyAppearance);
						normalOverTime = attendanceLogic.calcNormalOverTime(leave, normalOverTimeFrom, normalOverTimeTo,
								lateOverTimeTo);
						lateOverTime = attendanceLogic.calcLateOverTime1(leave, lateOverTimeFrom, lateOverTimeTo,
								nightBreakFrom,
								nightBreakTo);
						lateTime = attendanceLogic.calcLateTime(attendance, workTimeFrom, workTimeTo, lunchBreakFrom,
								lunchBreakTo);
						leavingEarlyTime = attendanceLogic.calcLeavingEarlyTime(leave, workTimeFrom, workTimeTo,
								lunchBreakFrom,
								lunchBreakTo);

						// Attendanceオブジェクトに設定する。
						recordList.get(day - 1).setWorkTime(workTime);
						recordList.get(day - 1).setEarlyTime(earlyTime);
						recordList.get(day - 1).setNormalOverTime(normalOverTime);
						recordList.get(day - 1).setLateOverTime(lateOverTime);
						recordList.get(day - 1).setLateTime(lateTime);
						recordList.get(day - 1).setLeavingEarlyTime(leavingEarlyTime);
					}

					// 休日区分：1(休日)の時、「深夜残業」及び「休出」を算出して、Attendanceオブジェクトに設定する
				} else if (holiday == 1) {
					// 出勤  < 退社のとき
					if (attendance < leave) {
						// 時刻表示で取得する。
						breakTime = attendanceLogic.calcBreakTime1(attendance, leave, lunchBreakFrom, lunchBreakTo,
								eveningBreakFrom,
								eveningBreakTo, lateOverTimeFrom, hour24);
						lateOverTime = attendanceLogic.calcLateOverTime2(attendance, leave, lateOverTimeFrom,
								lateOverTimeTo,
								nightBreakFrom, nightBreakTo, hour24);
						// 出勤 >= 退社のとき
					} else if (attendance >= leave) {
						// 時刻表示で取得する。
						breakTime = attendanceLogic.calcBreakTime2(attendance, leave, workTimeFrom, lunchBreakFrom,
								lunchBreakTo,
								eveningBreakFrom, eveningBreakTo, lateOverTimeFrom, lateOverTimeTo, hour24);
						lateOverTime = attendanceLogic.calcLateOverTime3(attendance, leave, lunchBreakFrom,
								lunchBreakTo,
								eveningBreakFrom, eveningBreakTo, lateOverTimeFrom, lateOverTimeTo, nightBreakFrom,
								nightBreakTo, hour24);
					}

					// Attendanceオブジェクトに設定する。
					recordList.get(day - 1).setBreakTime(breakTime);
					recordList.get(day - 1).setLateOverTime(lateOverTime);

				}
			}
		}

		return recordList;
	}

	/*
	 * 打刻日の勤怠データを配列で取得する。
	 * 第1引数： マップデータ(json返却用)、 第2引数：日付分のレコードリスト、 第3引数：打刻日
	 */
	public Map<String, String> getAttendanceArray(Map<String, String> jsonMap, List<Attendance> recordList,
			int embossDayOfMonth) {
		String workTime = recordList.get(embossDayOfMonth - 1).getWorkTime();
		String earlyTime = recordList.get(embossDayOfMonth - 1).getEarlyTime();
		String normalOverTime = recordList.get(embossDayOfMonth - 1).getNormalOverTime();
		String lateOverTime = recordList.get(embossDayOfMonth - 1).getLateOverTime();
		String breakTime = recordList.get(embossDayOfMonth - 1).getBreakTime();
		String lateTime = recordList.get(embossDayOfMonth - 1).getLateTime();
		String leavingEarlyTime = recordList.get(embossDayOfMonth - 1).getLeavingEarlyTime();

		jsonMap.put("workTime", workTime);
		jsonMap.put("earlyTime", earlyTime);
		jsonMap.put("normalOverTime", normalOverTime);
		jsonMap.put("lateOverTime", lateOverTime);
		jsonMap.put("breakTime", breakTime);
		jsonMap.put("lateTime", lateTime);
		jsonMap.put("leavingEarlyTime", leavingEarlyTime);

		return jsonMap;
	}

}
