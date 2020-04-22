package model;

import java.io.Serializable;

public class Attendance implements Serializable {
	private String date;
	private int day;
	private int holiday;
	private String attendanceTime;
	private String leavingTime;
	private int absence;
	private String remark;
	private String workTime;
	private String earlyTime;
	private String normalOverTime;
	private String lateOverTime;
	private String breakTime;
	private String lateTime;
	private String leavingEarlyTime;


	public Attendance() {

	}

	public Attendance(int day, int holiday, String attendanceTime, String leavingTime, int absence, String remark, String workTime, String earlyTime, String normalOverTime, String lateOverTime, String breakTime, String lateTime, String leavingEarlyTime) {
		this.day = day;
		this.holiday = holiday;
		this.attendanceTime = attendanceTime;
		this.leavingTime = leavingTime;
		this.absence = absence;
		this.remark = remark;
		this.workTime = workTime;
		this.earlyTime = earlyTime;
		this.normalOverTime = normalOverTime;
		this.lateOverTime = lateOverTime;
		this.breakTime = breakTime;
		this.lateTime = lateTime;
		this.leavingEarlyTime = leavingEarlyTime;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDay() {
		return this.day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHoliday() {
		return this.holiday;
	}

	public void setHoliday(int holiday) {
		this.holiday = holiday;
	}

	public String getAttendanceTime() {
		return this.attendanceTime;
	}

	public void setAttendanceTime(String attendanceTime) {
		this.attendanceTime = attendanceTime;
	}

	public String getLeavingTime() {
		return this.leavingTime;
	}

	public void setLeavingTime(String leavingTime) {
		this.leavingTime = leavingTime;
	}

	public int getAbsence() {
		return this.absence;
	}

	public void setAbsence(int absence) {
		this.absence = absence;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWorkTime() {
		return this.workTime;
	}

	public void setWorkTime(String time) {
		this.workTime = time;
	}

	public String getEarlyTime() {
		return this.earlyTime;
	}

	public void setEarlyTime(String time) {
		this.earlyTime = time;
	}

	public String getNormalOverTime() {
		return this.normalOverTime;
	}

	public void setNormalOverTime(String time) {
		this.normalOverTime = time;
	}

	public String getLateOverTime() {
		return this.lateOverTime;
	}

	public void setLateOverTime(String time) {
		this.lateOverTime = time;
	}

	public String getBreakTime() {
		return this.breakTime;
	}

	public void setBreakTime(String time) {
		this.breakTime = time;
	}

	public String getLateTime() {
		return this.lateTime;
	}

	public void setLateTime(String time) {
		this.lateTime = time;
	}

	public String getLeavingEarlyTime() {
		return this.leavingEarlyTime;
	}

	public void setLeavingEarlyTime(String time) {
		this.leavingEarlyTime = time;
	}
}
