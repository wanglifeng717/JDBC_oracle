package tongji.com.cn.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.mysql.jdbc.UpdatableResultSet;

import oracle.jdbc.OracleDriver;

public class JDBCTest_oracle {

	/**
	 * ResultSet: �����. ��װ��ʹ�� JDBC ���в�ѯ�Ľ��. 
	 * 1. ���� Statement ����� executeQuery(sql) ���Եõ������.
	 * 2. ResultSet ���ص�ʵ���Ͼ���һ�����ݱ�. ��һ��ָ��ָ�����ݱ�ĵ�һ����ǰ��.
	 * ���Ե��� next() ���������һ���Ƿ���Ч. ����Ч�÷������� true, ��ָ������. �൱��
	 * Iterator ����� hasNext() �� next() �����Ľ����
	 * 3. ��ָ���λ��һ��ʱ, ����ͨ������ getXxx(index) �� getXxx(columnName)
	 * ��ȡÿһ�е�ֵ. ����: getInt(1), getString("name")
	 * 4. ResultSet ��ȻҲ��Ҫ���йر�. 
	 */
	@Test
	public  void testResultSet()
	{
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		try {
			//1. ��ȡ Connection
			//2. ��ȡ Statement
			connection=JDBCTools.getConnection();
			statement=connection.createStatement();
			//3. ׼�� SQL
			String sql="select employee_id id,first_name name,email from employees where department_id=30";
			//4. ִ�в�ѯ, �õ� ResultSet
			resultSet=statement.executeQuery(sql);
			//5. ���� ResultSet
			while(resultSet.next())
			{
				int id=resultSet.getInt("ID");
				String name =resultSet.getString("name");
				String email=resultSet.getString("email");
				
				System.out.println(id+","+name+","+email+"!");
				
					
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			JDBCTools.release(statement, connection, resultSet);
		}
	}
	
	
	@Test
	public void testUpdate() {
		String sql_insert="insert into dept1 values(1,'wanglifeng','�Ϻ�')";
		String sql_update="update dept1 set dname='zhangtian' where deptno=1";
		String sql_delete="delete from dept1 where deptno=1 ";
		Update(sql_delete);
	}
	/**
	 * ͨ�õĸ��µķ���: ���� INSERT��UPDATE��DELETE
	 * �汾 1.
	 */
	public void Update(String sql)
	{
		Connection connection=null;
		Statement statement=null;
			try {
				connection = JDBCTools.getConnection();
				statement = connection.createStatement();
				statement.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				JDBCTools.release(statement, connection);
			}
	}
	
//	����ͳ�ķ�ʽ����DriverManager�ķ�ʽ��
	@Test
	public void testDriverManager() throws SQLException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
//		׼���������ݿ��4���ַ���
		String driverClass=null;
		String jdbcUrl=null;
		String user =null;
		String password=null;
//		��ȡ�����ļ�
		InputStream in =
			getClass().getClassLoader().getResourceAsStream("jdbc.properties");
//		���������ļ�
		Properties properties= new Properties();
		properties.load(in);
//		���������ļ�����ֵ����������
		driverClass=properties.getProperty("driver");
		jdbcUrl=properties.getProperty("jdbcUrl");
		user=properties.getProperty("user");
		password=properties.getProperty("password");
		//ע������
		//DriverManager.registerDriver((Driver) Class.forName(driverClass).newInstance());
		Class.forName(driverClass);
		Connection connection=
		DriverManager.getConnection(jdbcUrl, user, password);
		System.out.println(connection);
	}
//	�ö�ȡ�����ļ��ķ�ʽȥ��ȡ���ݿ�����
	@Test
	public void testGetConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException
	{
		System.out.println(getConnection());
	}
	public Connection  getConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, IOException 
	{
		String driverClass =null;
		String jdbcUrl=null;
		String user =null;
		String password =null;
		
		InputStream inputStream=
				getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties_jdbc=new Properties();
		properties_jdbc.load(inputStream);
		
		driverClass = properties_jdbc.getProperty("driver");
		jdbcUrl =properties_jdbc.getProperty("jdbcUrl");
		user = properties_jdbc.getProperty("user");
		password= properties_jdbc.getProperty("password");
		
		
		Properties properties=new Properties();
		properties.put("user", user);
		properties.put("password", password);
		
		Driver driver= (Driver) Class.forName(driverClass).newInstance();
		Connection connection = driver.connect(jdbcUrl, properties);
		return connection;
	}
//	ֱ����Driverȥʵ�����ݿ����ӡ����ַ�ʽֻ�ڲ��Ե�ʱ���á�
	@Test
	public void testDriver() throws SQLException
	{
//		�½�������
		Driver driver = new OracleDriver();
//		׼��������Ҫ�ļ����ַ���
		String url="jdbc:oracle:thin:@localhost:1521:orcl";
		Properties info = new Properties();
		info.put("user", "scott");
		info.put("password", "tiger");
//		��ȡ����
		Connection connection =driver.connect(url, info);
		System.out.println(connection);
	}
}
