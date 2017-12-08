package tongji.com.cn.employee;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTools {
	
	
	/**
	 * ִ�� SQL �ķ���,ʹ��preparedstatement��������
	 * 
	 * @param sql: insert, update �� delete�� �������� select
	 */
	public static void update(String sql,Object ... args )
	{//�ÿɱ�������ã���Ȼ�������滹Ҫ��װ����������á�����д�Ļ����㰮�������ʹ�����
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		try {
			connection=JDBCTools.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			JDBCTools.release(preparedStatement, connection);
		}
	}
	
	/**
	 * ִ�� SQL �ķ���
	 * 
	 * @param sql: insert, update �� delete�� �������� select
	 */
	public static void update(String sql) {
		Connection connection = null;
		Statement statement = null;

		try {
			// 1. ��ȡ���ݿ�����
			connection = getConnection();

			// 2. ���� Connection ����� createStatement() ������ȡ Statement ����
			statement = connection.createStatement();

			// 4. ���� SQL ���: ���� Statement ����� executeUpdate(sql) ����
			statement.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5. �ر����ݿ���Դ: ��������ر�.
			release(statement, connection,null);
		}
	}
	
	
	
	/*���Ӹ߼��İ汾���ر�Statement,connection,resultSet*/
	public static void release(Statement statement,Connection connection,ResultSet resultSet)
	{
		if(resultSet!=null)
		 {
			 try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		
		if(statement!=null)
		 {
			 try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 
		 if(connection!=null)
		 {
			 try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
	}

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
				e.printStackTrace();
			}
		 }
		 
		 if(connection!=null)
		 {
			 try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
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
