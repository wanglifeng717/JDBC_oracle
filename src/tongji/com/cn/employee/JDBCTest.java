package tongji.com.cn.employee;

import java.util.Scanner;

import org.junit.Test;

public class JDBCTest {

	
    /*�������ķ�ʽ����������һ������
     * 1.�����ͱ��Ӧ����
     * 2.�½��࣬��ֵ
     * 3.����update���������ݲ��뵽������
     */
	@Test
	public void testAddNewEmployee()
	{
		Employee employee=getEmployeeFromConsole();
		addNewEmployee(employee);
	}
	//�ӿ���̨����һ��Ա������Ϣ
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
	
	//���һ��Ա������Ϣ
	public void addNewEmployee(Employee employee)
	{
        // 1. ׼��һ�� SQL ���:
		String sql = "INSERT INTO employees(employee_id,last_name,email,salary) VALUES(" + employee.getEmployee_id()
				+ ",'" + employee.getLast_name() + "','" + employee.getEmail()+ "',"
				+ employee.getSalary()
				+ ")";

		System.out.println(sql);
		
		JDBCTools.update(sql);
				
	}
	
}
