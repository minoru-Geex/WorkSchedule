package model;

public class WorkScheduleCommon {
	/*
	 * 数値表示の時刻を分表示に変換する。
	 * 第1引数：数値表示
	 */
	public int changeNumToMin(int timeNumber) {
		// 数値表示の時刻 → 分表示の時刻
		int minute = timeNumber / 100 * 60 + timeNumber % 100; // 分単位に変換

		return minute;
	}

	/*
	 * 時刻表示を分表示の数値にする。
	 * 第1引数： 時刻表示の文字列
	 */
	public int changeTimeToMin(String timeString) {
		// 文字列型の時刻 → 数値型の時刻へ変換
		int timeNumber = changeTimeToNum(timeString);

		int minute = changeNumToMin(timeNumber);

		return minute;
	}

	/*
	 * 分表示の数値を時刻表示に変換する。
	 * 第1引数：分表示の数値
	 */
	public String changeMinToTime(int minute) {
		// 分表示 → 数値表示
		int timeNumber = minute / 60 * 100 + minute % 60;
		String timeString = changeNumToTime(timeNumber);

		return timeString;
	}

	/*
	 * 時刻表示(〇〇:〇〇)を数値表示(int型)に変換する。(ex: 9:00→900)
	 * 第1引数：時刻表示
	 */
	public int changeTimeToNum(String timeString) {
		String[] timeArray = timeString.split(":");

		String result  = "";

		for (String time : timeArray) {
			result += time;
		}

		int timeNumber = Integer.parseInt(result);

		return timeNumber;
	}



	/*
	 * 数値表示で時刻を表現しているものを時刻表示にする。
	 * 第1引数：数値表示
	 */

	public String changeNumToTime(int number) {
		String stringDisplay = Integer.toString(number);
		int length = stringDisplay.length();

		String timeString = "";

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
				timeString = number / 100 + ":" + stringDisplay.substring(stringDisplay.length() -2);
				break;
		}
		return timeString;
	}

}
