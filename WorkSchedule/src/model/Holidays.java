package model;

import java.io.Serializable;

public class Holidays implements Serializable {
	private String holidayDate;

	public String getHolidayDate() {
		return this.holidayDate;
	}

	public void setHolidayDate(String date) {
		this.holidayDate = date;
	}

}
