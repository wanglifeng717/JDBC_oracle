package com.tongji.DAO;

public class Employee {

	int employee_id;
	String last_name;
	String email;
	int salary;
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Employee(int employee_id, String last_name, String email, int salary) {
		super();
		this.employee_id = employee_id;
		this.last_name = last_name;
		this.email = email;
		this.salary = salary;
	}
	
	@Override
	public String toString() {
		return "Employee [employee_id=" + employee_id + ", last_name=" + last_name + ", email=" + email + ", salary="
				+ salary + "]";
	}

	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	
	
}
