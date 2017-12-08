package tongji.com.cn.employee;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import org.junit.Test;

public class JDBCTest {
	
	
	
	
	
	
	
	
	
@Test
public  void testPreparedStatement()
{
	Connection connection=null;
	PreparedStatement preparedStatement=null;
	try 
	{
		String sql="insert into employees (employee_id,last_name,salary,email,hire_date) values(?,?,?,?,?)";
		connection = JDBCTools.getConnection();
		preparedStatement = connection.prepareStatement(sql);
		
		preparedStatement.setInt(1, 52);
		preparedStatement.setString(2, "����");
		preparedStatement.setInt(3, 998);
		preparedStatement.setString(4, "hahia@qq.com");
		preparedStatement.setDate(5, new Date(new java.util.Date().getTime()));
		
		preparedStatement.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally
	{
		JDBCTools.release(preparedStatement, connection);
	}
}
/**
 * ��JDBCTools�м����˻���preparedStatementʵ�ֵ�update,���Բ���һ������
 */
@Test
public  void testPreparedStatement2()
{
	String sql="insert into employees(employee_id,last_name,salary,email) values(?,?,?,?)";
	JDBCTools.update(sql, 53,"ܽ�ؽ��",99,"tanwanggaidihu@qq.com");
	
}
	
	
/**���ƹ����ģ���һ������
 * ʹ�� PreparedStatement ����Ч�Ľ�� SQL ע������.
 */
@Test
public void testSQLInjection2() {
	String username = "a' OR PASSWORD = ";
	String password = " OR '1'='1";

	String sql = "SELECT * FROM users WHERE username = ? "
			+ "AND password = ?";

	Connection connection = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	try {
		connection = JDBCTools.getConnection();
		preparedStatement = connection.prepareStatement(sql);

		preparedStatement.setString(1, username);
		preparedStatement.setString(2, password);

		resultSet = preparedStatement.executeQuery();

		if (resultSet.next()) {
			System.out.println("��¼�ɹ�!");
		} else {
			System.out.println("�û��������벻ƥ����û���������. ");
		}

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		JDBCTools.release( preparedStatement, connection,resultSet);
	}
}

/**
 * SQL ע��.:���ƹ����ģ���һ������
 */
@Test
public void testSQLInjection() {
	String username = "a' OR PASSWORD = ";
	String password = " OR '1'='1";

	String sql = "SELECT * FROM users WHERE username = '" + username
			+ "' AND " + "password = '" + password + "'";

	System.out.println(sql);

	Connection connection = null;
	Statement statement = null;
	ResultSet resultSet = null;

	try {
		connection = JDBCTools.getConnection();
		statement = connection.createStatement();
		resultSet = statement.executeQuery(sql);

		if (resultSet.next()) {
			System.out.println("��¼�ɹ�!");
		} else {
			System.out.println("�û��������벻ƥ����û���������. ");
		}

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		JDBCTools.release( statement, connection,resultSet);
	}
}	
	
	
//*==============================================================================*/
//*==============================================================================*/	
//*==============================================================================*/
	/**
	 * ��֤һ��Ա������Ϣ��
	 * �����ݿ�������ң��оʹ�ӡ��û�оͷ��ز��޴���
	 * 
	 */
	@Test
	public void testGetEmployee()
	{
		//1.�õ���ѯ���ͣ���Ϊwhere�Ӿ����������ж�����
		int searchType= getSearchTypeFromConsole();
		//2.�����ѯԱ����Ϣ����ѯ���Ա���ڸ�����������Ĭ�Ͽ������һ����Ϣ
		Employee employee=searchEmployee(searchType);
		//3.��ӡԱ������Ϣ
		printEmployee(employee);
		
	}

	
	/**
	 * ��ӡԱ���ĵ���Ϣ
	 * @param employee
	 */
	
	private void printEmployee(Employee employee) {
		if(employee!=null)
			System.out.println(employee);
		else
			System.out.println("���޴���");
		
	}

	/**
	 * �����ѯһ��Ա������Ϣ
	 * @param searchType
	 * @return
	 */
	
	private Employee searchEmployee(int searchType) {
		String sql="select employee_id,last_name name,email,salary from employees where ";
		
		Scanner scanner=new Scanner(System.in);
		//1.�����û����������ͣ���ʾ�û�������Ϣ�������sql��ƴ��
		if(searchType==1)
		{
			System.out.println("����������");
			String name=scanner.next();
			sql=sql+"last_name='"+name+"'";
		}
		else
		{
			System.out.println("�����빤��");
			int salary=scanner.nextInt();
			sql=sql+"salary="+salary;
		}
		System.out.println(sql);
		//2.ִ��һ����ѯ
		Employee employee=getEmployee(sql);
		return employee;
	}
/**
 * ���ݴ����SQL����Ա������
 * @param sql
 * @return
 */
	private Employee getEmployee(String sql) {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		Employee employee=null;
		try {
			connection=JDBCTools.getConnection();
			statement=connection.createStatement();
			resultSet=statement.executeQuery(sql);
			
			if(resultSet.next())
			{
				employee=new Employee(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getInt(4));
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		finally {
			JDBCTools.release(statement, connection,resultSet);
		}
		
				
		return employee;
	}

	/**
	 * �ӿ���̨����һ������, ȷ��Ҫ��ѯ������
	 * 
	 * @return: 1.��last_name��ѯ. 2. �ù���salary��ѯ ��������Ч. ����ʾ���û���������.
	 */
private int getSearchTypeFromConsole() 
{
	System.out.println("�������ѯ������1����������ѯ��2���ù��ʲ�ѯ");
	
	Scanner scanner = new Scanner(System.in);
	int type=scanner.nextInt();	
	if(type!=1 && type!=2)
	{
		System.out.println("��������ȷ�Ĳ�ѯ����");
		throw new RuntimeException();
	}
	return type;
}
//*==============================================================================*/
//*==============================================================================*/
//*==============================================================================*/
//*==============================================================================*/
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
