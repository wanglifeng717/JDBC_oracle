package com.tongji.DAO;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBC �Ĺ�����
 * 
 * ���а���: ��ȡ���ݿ�����, �ر����ݿ���Դ�ȷ���.
 */
public class JDBCTools {

	public static Connection getConnection() throws Exception {
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
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
