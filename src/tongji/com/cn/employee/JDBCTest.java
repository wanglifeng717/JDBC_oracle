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
		preparedStatement.setString(2, "张甜");
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
 * 在JDBCTools中加入了基于preparedStatement实现的update,测试插入一条数据
 */
@Test
public  void testPreparedStatement2()
{
	String sql="insert into employees(employee_id,last_name,salary,email) values(?,?,?,?)";
	JDBCTools.update(sql, 53,"芙蓉姐姐",99,"tanwanggaidihu@qq.com");
	
}
	
	
/**复制过来的，不一定能跑
 * 使用 PreparedStatement 将有效的解决 SQL 注入问题.
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
			System.out.println("登录成功!");
		} else {
			System.out.println("用户名和密码不匹配或用户名不存在. ");
		}

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		JDBCTools.release( preparedStatement, connection,resultSet);
	}
}

/**
 * SQL 注入.:复制过来的，不一定能跑
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
			System.out.println("登录成功!");
		} else {
			System.out.println("用户名和密码不匹配或用户名不存在. ");
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
	 * 验证一个员工的信息。
	 * 从数据库里面查找，有就打印，没有就返回查无此人
	 * 
	 */
	@Test
	public void testGetEmployee()
	{
		//1.得到查询类型，因为where子句后面必须有判定条件
		int searchType= getSearchTypeFromConsole();
		//2.具体查询员工信息，查询大的员工在赋给对象。这里默认库里面就一条信息
		Employee employee=searchEmployee(searchType);
		//3.打印员工的信息
		printEmployee(employee);
		
	}

	
	/**
	 * 打印员工的的信息
	 * @param employee
	 */
	
	private void printEmployee(Employee employee) {
		if(employee!=null)
			System.out.println(employee);
		else
			System.out.println("查无此人");
		
	}

	/**
	 * 具体查询一个员工的信息
	 * @param searchType
	 * @return
	 */
	
	private Employee searchEmployee(int searchType) {
		String sql="select employee_id,last_name name,email,salary from employees where ";
		
		Scanner scanner=new Scanner(System.in);
		//1.根据用户的输入类型，提示用户补充信息，并完成sql的拼接
		if(searchType==1)
		{
			System.out.println("请输入姓名");
			String name=scanner.next();
			sql=sql+"last_name='"+name+"'";
		}
		else
		{
			System.out.println("请输入工资");
			int salary=scanner.nextInt();
			sql=sql+"salary="+salary;
		}
		System.out.println(sql);
		//2.执行一个查询
		Employee employee=getEmployee(sql);
		return employee;
	}
/**
 * 根据传入的SQL返回员工对象
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
	 * 从控制台读入一个整数, 确定要查询的类型
	 * 
	 * @return: 1.用last_name查询. 2. 用工资salary查询 其他的无效. 并提示请用户重新输入.
	 */
private int getSearchTypeFromConsole() 
{
	System.out.println("请输入查询的类型1，用姓名查询。2，用工资查询");
	
	Scanner scanner = new Scanner(System.in);
	int type=scanner.nextInt();	
	if(type!=1 && type!=2)
	{
		System.out.println("请输入正确的参询类型");
		throw new RuntimeException();
	}
	return type;
}
//*==============================================================================*/
//*==============================================================================*/
//*==============================================================================*/
//*==============================================================================*/
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
