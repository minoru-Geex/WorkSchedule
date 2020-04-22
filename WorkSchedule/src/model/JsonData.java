package model;

import java.io.Serializable;
import java.util.Map;

public class JsonData implements Serializable {
	private Attendance attendance;
	private Map<String, String> attendanceMap;

	public Attendance getAttendance() {
		return this.attendance;
	}

	public void setAttendance(Attendance obj) {
		this.attendance = obj;
	}

	public Map<String, String> getAttendanceMap() {
		return this.attendanceMap;
	}

	public void setAttendanceMap(Map<String, String> attendanceMap) {
		this.attendanceMap = attendanceMap;
	}
}
