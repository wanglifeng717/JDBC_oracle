package tongji.com.cn.employee;

import java.util.Scanner;

import org.junit.Test;

public class JDBCTest {

	
    /*面向对象的方式向表里面插入一条数据
     * 1.建立和表对应的类
     * 2.新建类，赋值
     * 3.调用update方法吧数据插入到表里面
     */
	@Test
	public void testAddNewEmployee()
	{
		Employee employee=getEmployeeFromConsole();
		addNewEmployee(employee);
	}
	//从控制台输入一个员工的信息
	private Employee getEmployeeFromConsole() {
		Employee employee=new Employee();
		Scanner scanner= new Scanner(System.in);
		
		System.out.println("employee_id:");
		employee.setEmployee_id(scanner.nextInt());
		
		System.out.println("last_name:");
		employee.setLast_name(scanner.next());
		
		System.out.println("email:");
		employee.setEmail(scanner.next());
		
		System.out.println("salary:");
		employee.setSalary(scanner.nextInt());
		
		scanner.close();		
		return employee;		
	}
	
	//添加一个员工的信息
	public void addNewEmployee(Employee employee)
	{
        // 1. 准备一条 SQL 语句:
		String sql = "INSERT INTO employees(employee_id,last_name,email,salary) VALUES(" + employee.getEmployee_id()
				+ ",'" + employee.getLast_name() + "','" + employee.getEmail()+ "',"
				+ employee.getSalary()
				+ ")";

		System.out.println(sql);
		
		JDBCTools.update(sql);
				
	}
	
}
