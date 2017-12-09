package tongji.com.cn.employee;

import java.awt.Window.Type;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Test;

public class JDBCTest {
	
	
//我们查询的时候查学生和查员工字段完全不一样。我们有办法把这两个方法写成一个统一的方法吗
/**
 * 通用的查询方法：可以根据传入的 SQL、Class 对象返回 SQL 对应的记录的对象
 * @param clazz: 描述对象的类型
 * @param sql: SQL 语句。可能带占位符
 * @param args: 填充占位符的可变参数。
 * @return
 */
	
@Test
public void testGet()
{
	String sql="select employee_id,last_name  from employees where department_id=?";
	ArrayList<Employee> arrayList=get(Employee.class,sql,80);
	for(Employee e:arrayList)
	{
		System.out.println(e);
	}
}
/**
 * 查询方法，以后你只要传类，和sql语句就可以了。 
 * @param <T>
 * @param <T>
 * @param clazz
 * @param sql
 * @param args
 * @return
 */
public  <T> ArrayList<T> get(Class<T> clazz,String sql,Object ... args)
{
	T entity =null;
	Connection connection=null;
	PreparedStatement preparedStatement=null;
	ResultSet resultSet=null;
	//一定要定义在这里，定义在try块里面后面是访问不到的，上面的同理。
	ArrayList<T> arrayList_entity=new ArrayList<T>();
	try {
		//1.得到resultSet对象
		connection=JDBCTools.getConnection();
		preparedStatement =connection.prepareStatement(sql);
		
		for(int i=0;i<args.length;i++)
		{
			preparedStatement.setObject(i+1, args[i]);
		}
		resultSet = preparedStatement.executeQuery();
		//2.得到resultsetMetaDate对象
		ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
		
		
		
		//3. 处理结果集. 利用 ResultSetMetaData 填充 3 对应的 Map 对象
		//可能存在多个条记录，新建一个list用于保存。
		ArrayList<Map<String, Object>> arrayList=new ArrayList<Map<String, Object>>();
		while(resultSet.next())
		{
			//4. 创建一个 Map<String, Object> 对象, 键: SQL 查询的列的别名, 
			Map<String, Object> map=new HashMap<>();
			for(int i=0; i<resultSetMetaData.getColumnCount();i++)
			{
				String columnLabel = resultSetMetaData.getColumnLabel(i+1);
				Object columnValue = resultSet.getObject(i+1);
				map.put(columnLabel, columnValue);
			}
			//那结果保存在list中
			arrayList.add(map);
			
		}
	
		//4. 若 Map 不为空集, 利用反射创建 clazz 对应的对象
		//建立一个list存储反射修改过后的对象。
		//遍历集合，为每个对象赋值。然后把对象保存到最终要返回的对象数组中。
		if(arrayList.size()>0)
		{
			for(Map<String, Object>map_list:arrayList)
			{
				entity =clazz.newInstance();
				//6. 遍历 Map 对象, 利用反射为 Class 对象的对应的属性赋值.
				for(Map.Entry<String, Object> entry:map_list.entrySet())
				{
					String fieldName =entry.getKey().toLowerCase();
					Object value=entry.getValue();
					ReflectionUtils.setFieldValue(entity, fieldName, value);
					
				}
				arrayList_entity.add(entity);
				
			}
			
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}finally
	{
		JDBCTools.release(resultSet,preparedStatement, connection);
	}
	return arrayList_entity;
}
	


//*==============================================================================*/
//*==============================================================================*/	
//*==============================================================================*/	
	
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
		JDBCTools.release( resultSet,preparedStatement, connection);
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
		JDBCTools.release( resultSet,statement, connection);
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
			JDBCTools.release(resultSet,statement, connection);
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
