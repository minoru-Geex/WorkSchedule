package model;

import java.io.Serializable;

public class RegisterEmployee implements Serializable {
	private String id;
	private String firstName;
	private String lastName;
	private String department;
	private String pass;

	public RegisterEmployee() {

	}

	public RegisterEmployee(String id, String lastName, String firstName, String department, String pass) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
		this.department = department;
		this.pass = pass;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
}
