package tongji.com.cn.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import oracle.jdbc.OracleDriver;

public class JDBCTest_oracle {

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
