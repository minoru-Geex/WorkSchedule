package model;

import java.io.Serializable;

public class Employee implements Serializable {
	private int id;
	private String pass;
	private String name;
	private String department;
	private String workPeriodFrom;
	private String workPeriodTo;
	private String workTimeFrom;
	private String workTimeTo;
	private String normalOverTimeFrom;
	private String normalOverTimeTo;
	private String lateOverTimeFrom;
	private String lateOverTimeTo;
	private String lunchBreakFrom;
	private String lunchBreakTo;
	private String eveningBreakFrom;
	private String eveningBreakTo;
	private String nightBreakFrom;
	private String nightBreakTo;
	private int earlyAppearance;

	public Employee() {

	}

	public Employee(int id, String pass) {
		this.id = id;
		this.pass = pass;
	}

	public Employee(int id, String workPeriodFrom, String workPeriodTo, String workTimeFrom, String workTimeTo, String normalOverTimeFrom, String normalOverTimeTo, String lunchBreakFrom, String lunchBreakTo,  String eveningBreakFrom, String eveningBreakTo, int earlyAppearance) {
		this.id = id;
		this.workPeriodFrom = workPeriodFrom;
		this.workPeriodTo = workPeriodTo;
		this.workTimeFrom = workTimeFrom;
		this.workTimeTo = workTimeTo;
		this.normalOverTimeFrom = normalOverTimeFrom;
		this.normalOverTimeTo = normalOverTimeTo;
		this.lunchBreakFrom = lunchBreakFrom;
		this.lunchBreakTo = lunchBreakTo;
		this.eveningBreakFrom = eveningBreakFrom;
		this.eveningBreakTo = eveningBreakTo;
		this.earlyAppearance = earlyAppearance;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getWorkPeriodFrom() {
		return workPeriodFrom;
	}

	public void setWorkPeriodFrom(String workPeriodFrom) {
		this.workPeriodFrom = workPeriodFrom;
	}

	public String getWorkPeriodTo() {
		return workPeriodTo;
	}

	public void setWorkPeriodTo(String workPeriodTo) {
		this.workPeriodTo = workPeriodTo;
	}

	public String getWorkTimeFrom() {
		return this.workTimeFrom;
	}

	public void setWorkTimeFrom(String from) {
		this.workTimeFrom = from;
	}

	public String getWorkTimeTo() {
		return this.workTimeTo;
	}

	public void setWorkTimeTo(String to) {
		this.workTimeTo = to;
	}

	public String getNormalOverTimeFrom() {
		return this.normalOverTimeFrom;
	}

	public void setNormalOverTimeFrom(String from) {
		this.normalOverTimeFrom = from;
	}

	public String getNormalOverTimeTo() {
		return this.normalOverTimeTo;
	}

	public void setNormalOverTimeTo(String to) {
		this.normalOverTimeTo = to;
	}

	public String getLateOverTimeFrom() {
		return this.lateOverTimeFrom;
	}

	public void setLateOverTimeFrom(String from) {
		this.lateOverTimeFrom = from;
	}

	public String getLateOverTimeTo() {
		return this.lateOverTimeTo;
	}

	public void setLateOverTimeTo(String to) {
		this.lateOverTimeTo = to;
	}

	public String getLunchBreakFrom() {
		return this.lunchBreakFrom;
	}

	public void setLunchBreakFrom(String from) {
		this.lunchBreakFrom = from;
	}

	public String getLunchBreakTo() {
		return this.lunchBreakTo;
	}

	public void setLunchBreakTo(String to) {
		this.lunchBreakTo = to;
	}

	public String getEveningBreakFrom() {
		return this.eveningBreakFrom;
	}

	public void setEveningBreakFrom(String from) {
		this.eveningBreakFrom = from;
	}

	public String getEveningBreakTo() {
		return this.eveningBreakTo;
	}

	public void setEveningBreakTo(String to) {
		this.eveningBreakTo = to;
	}

	public String getNightBreakFrom() {
		return this.nightBreakFrom;
	}

	public void setNightBreakFrom(String from) {
		this.nightBreakFrom = from;
	}

	public String getNightBreakTo() {
		return this.nightBreakTo;
	}

	public void setNightBreakTo(String to) {
		this.nightBreakTo = to;
	}

	public int getEarlyAppearance() {
		return this.earlyAppearance;
	}

	public void setEarlyAppearance(int minute) {
		this.earlyAppearance = minute;
	}

}
