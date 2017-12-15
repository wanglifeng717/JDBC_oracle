
package com.tongji.connectionPool;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
* Title: JDBCTools 
* Description: ���ݿ⹤����
* ���������ݿ����ӣ��������ӹرգ����ݿ����������ع����رյĺ����� 
* @author mdm(computer in lab)  
* @date 2017��12��14��
 */
public class JDBCTools {

	/**
	 * ���ܣ��ύ���ݿ�����
	 * 
	 * @param connection
	 */
	public static void commit(Connection connection)
	{
		if(connection != null)
		{
			try {
				connection.commit();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	/**
	 * ���ܣ��ع����ݿ�����
	 * 
	 * @param connection
	 */
	public static void rollback(Connection connection) {
		if(connection!=null) {
			try {
				connection.rollback();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	/**
	 * ���ܣ��������ݿ�����
	 * 
	 * @param connection
	 */
	public static void beginTx(Connection connection) {
		if(connection!=null) {
			try {
				connection.setAutoCommit(false);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	private static DataSource dataSource = null;
	//���ݿ����ӳ�Ӧֻ����ʼ��һ��. 
		static{
			dataSource = new ComboPooledDataSource("helloc3p0");
		}
		/**
		 * ���ܣ��Ժ����еĻ�ȡ���ӵķ�ʽ�������ݿ����ӳ��л�ȡ����
		 * 
		 * @return
		 * @throws Exception
		 */
		public static Connection getConnection() throws Exception {
			return dataSource.getConnection();
		}
	/**
	 * ���ܣ���ȡ���ݿ�����
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection2() throws Exception {
		Properties properties = new Properties();
		InputStream inStream = JDBCTools.class.getClassLoader()
				.getResourceAsStream("jdbc.properties");
		properties.load(inStream);

		// 1. ׼����ȡ���ӵ� 4 ���ַ���: user, password, jdbcUrl, driverClass
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String driverClass = properties.getProperty("driver");
		// 2. ��������: Class.forName(driverClass)
		Class.forName(driverClass);

		// 3. ����
		// DriverManager.getConnection(jdbcUrl, user, password)
		// ��ȡ���ݿ�����
		Connection connection = DriverManager.getConnection(jdbcUrl, user,
				password);
		return connection;
	}

	/**
	 * ���ܣ��ͷ����ݿ�����
	 * 
	 * @param resultSet
	 * @param statement
	 * @param connection
	 */
	public static void releaseDB(ResultSet resultSet, Statement statement,
			Connection connection) {

		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (connection != null) {
			try {
				//���ݿ����ӳص� Connection ������� close ʱ
				//��������Ľ��йر�, ���ǰѸ����ݿ����ӻ�黹�����ݿ����ӳ���. 
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}