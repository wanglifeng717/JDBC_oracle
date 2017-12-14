
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
* Description: 数据库工具类
* 包含了数据库连接，数据连接关闭，数据库事务开启，回滚，关闭的函数。 
* @author mdm(computer in lab)  
* @date 2017年12月14日
 */
public class JDBCTools {

	/**
	 * 功能：提交数据库事务
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
	 * 功能：回滚数据库事务
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
	 * 功能：开启数据库事务
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
	//数据库连接池应只被初始化一次. 
		static{
			dataSource = new ComboPooledDataSource("helloc3p0");
		}
		/**
		 * 功能：以后所有的获取连接的方式都从数据库连接池中获取即可
		 * 
		 * @return
		 * @throws Exception
		 */
		public static Connection getConnection() throws Exception {
			return dataSource.getConnection();
		}
	/**
	 * 功能：获取数据库连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection2() throws Exception {
		Properties properties = new Properties();
		InputStream inStream = JDBCTools.class.getClassLoader()
				.getResourceAsStream("jdbc.properties");
		properties.load(inStream);

		// 1. 准备获取连接的 4 个字符串: user, password, jdbcUrl, driverClass
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String driverClass = properties.getProperty("driver");
		// 2. 加载驱动: Class.forName(driverClass)
		Class.forName(driverClass);

		// 3. 调用
		// DriverManager.getConnection(jdbcUrl, user, password)
		// 获取数据库连接
		Connection connection = DriverManager.getConnection(jdbcUrl, user,
				password);
		return connection;
	}

	/**
	 * 功能：释放数据库连接
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
				//数据库连接池的 Connection 对象进行 close 时
				//并不是真的进行关闭, 而是把该数据库连接会归还到数据库连接池中. 
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
