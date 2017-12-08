package tongji.com.cn.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;








public class JDBCTools {

	

	/**
	 * �ر� Statement �� Connection
	 * @param statement
	 * @param conn
	 */
	 public static void release(Statement statement,Connection connection)
	 {
		 if(statement!=null)
		 {
			 try {
				statement.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		 }
		 
		 if(connection!=null)
		 {
			 try {
				connection.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		 }
					 
	 }
	 
	
	
	/**
	 * 1. ��ȡ���ӵķ���. ͨ����ȡ�����ļ������ݿ��������ȡһ������.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		// 1. ׼���������ݿ�� 4 ���ַ���.
		// 1). ���� Properties ����
		Properties properties = new Properties();

		// 2). ��ȡ jdbc.properties ��Ӧ��������
		InputStream in = 
				JDBCTools.class.getClassLoader().getResourceAsStream(
				"jdbc.properties");

		// 3). ���� 2�� ��Ӧ��������
		properties.load(in);

		// 4). ������� user, password ��4 ���ַ���.
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String driver = properties.getProperty("driver");

		// 2. �������ݿ���������(��Ӧ�� Driver ʵ��������ע�������ľ�̬�����.)
		Class.forName(driver);

		// 3. ͨ�� DriverManager �� getConnection() ������ȡ���ݿ�����.
		return DriverManager.getConnection(jdbcUrl, user, password);
	}
}
