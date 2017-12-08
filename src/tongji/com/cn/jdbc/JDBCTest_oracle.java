package tongji.com.cn.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.mysql.jdbc.UpdatableResultSet;

import oracle.jdbc.OracleDriver;

public class JDBCTest_oracle {

	@Test
	public void testUpdate() {
		String sql_insert="insert into dept1 values(1,'wanglifeng','上海')";
		String sql_update="update dept1 set dname='zhangtian' where deptno=1";
		String sql_delete="delete from dept1 where deptno=1 ";
		Update(sql_delete);
	}
	/**
	 * 通用的更新的方法: 包括 INSERT、UPDATE、DELETE
	 * 版本 1.
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
	
//	最正统的方式，用DriverManager的方式。
	@Test
	public void testDriverManager() throws SQLException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException
	{
//		准备连接数据库的4个字符串
		String driverClass=null;
		String jdbcUrl=null;
		String user =null;
		String password=null;
//		读取配置文件
		InputStream in =
			getClass().getClassLoader().getResourceAsStream("jdbc.properties");
//		加载配置文件
		Properties properties= new Properties();
		properties.load(in);
//		解析配置文件，赋值给几个参数
		driverClass=properties.getProperty("driver");
		jdbcUrl=properties.getProperty("jdbcUrl");
		user=properties.getProperty("user");
		password=properties.getProperty("password");
		//注册驱动
		//DriverManager.registerDriver((Driver) Class.forName(driverClass).newInstance());
		Class.forName(driverClass);
		Connection connection=
		DriverManager.getConnection(jdbcUrl, user, password);
		System.out.println(connection);
	}
//	用读取配置文件的方式去获取数据库连接
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
//	直接用Driver去实现数据库连接。这种方式只在测试的时候用。
	@Test
	public void testDriver() throws SQLException
	{
//		新建驱动类
		Driver driver = new OracleDriver();
//		准备连接需要的几个字符串
		String url="jdbc:oracle:thin:@localhost:1521:orcl";
		Properties info = new Properties();
		info.put("user", "scott");
		info.put("password", "tiger");
//		获取连接
		Connection connection =driver.connect(url, info);
		System.out.println(connection);
	}
}
