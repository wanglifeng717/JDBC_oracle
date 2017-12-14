/**
* Title: DBCPTest.java
* Description: 测试DBCP数据库连接池的类
* 需要两个依赖包，一个配置文件，配置文件和JDBC的配置文件不同需要注意
* DBCP，pool ，dbcp.properties
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017年12月14日
* @version 1.0
*/
package com.tongji.connectionPool;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.junit.Test;

/**  
* Title: DBCPTest 
* Description:这个类是测试数据库连接池的类。  
* @author mdm(computer in lab)  
* @date 2017年12月14日  
*/
public class DBCPTest {

	/**
	 * 功能：用加载配置文件的方式获取数据库连接
	 * 这种方式优点就是解耦合，同时我们明白这个可以注册新建多个数据源。方便管理。
	 * 1. 加载 dbcp 的 properties 配置文件: 配置文件中的键需要来自 BasicDataSource
	 * 的属性.
	 * 2. 调用 BasicDataSourceFactory 的 createDataSource 方法创建 DataSource
	 * 实例
	 * 3. 从 DataSource 实例中获取数据库连接. 
	 *
	 * @throws Exception
	 */
	@Test
	public void testDBCPWithDataSourceFactory() throws Exception {
		Properties properties=new Properties();
		InputStream inputStream=DBCPTest.class.getClassLoader().getResourceAsStream("dbcp.properties");
		properties.load(inputStream);
		
		DataSource dataSource=BasicDataSourceFactory.createDataSource(properties);
		
		System.out.println(dataSource.getConnection());
		BasicDataSource basicDataSource=(BasicDataSource) dataSource;
		//查看刚才配置的信息是否正确。
		System.out.println(basicDataSource.getInitialSize());
		System.out.println(basicDataSource.getMaxTotal());
		System.out.println(basicDataSource.getMinIdle());
		System.out.println(basicDataSource.getMaxWaitMillis());
	}
	/**
	 * 功能：使用传统手动配置的方式来获取数据库连接池和数据库连接
	 * 使用 DBCP 数据库连接池
	 * 1. 加入 jar 包(2 个jar 包). 依赖于 Commons Pool
	 * 2. 创建数据库连接池
	 * 3. 为数据源实例指定必须的属性
	 * 4. 从数据源中获取数据库连接
	 * @throws SQLException 
	 */
	@Test
	public void testDBCP() throws SQLException{
		final BasicDataSource dataSource = new BasicDataSource();
		
		//2. 为数据源实例指定必须的属性
		dataSource.setUsername("scott");
		dataSource.setPassword("tiger");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		
		//3. 指定数据源的一些可选的属性.
		//1). 指定数据库连接池中初始化连接数的个数
		dataSource.setInitialSize(5);
		
		//2). 指定最大的连接数: 同一时刻可以同时向数据库申请的连接数
		dataSource.setMaxTotal(5);
		
		//3). 指定小连接数: 在数据库连接池中保存的最少的空闲连接的数量 
		dataSource.setMinIdle(2);
		
		//4).等待数据库连接池分配连接的最长时间. 单位为毫秒. 超出该时间将抛出异常. 
		dataSource.setMaxWaitMillis(1000 * 3);
		
		//4. 从数据源中获取数据库连接
		Connection connection = dataSource.getConnection();
		System.out.println(connection.getClass()); 
		
		connection = dataSource.getConnection();
		System.out.println(connection.getClass()); 
		
		connection = dataSource.getConnection();
		System.out.println(connection.getClass()); 
		
		connection = dataSource.getConnection();
		System.out.println(connection.getClass()); 
		
		Connection connection2 = dataSource.getConnection();
		System.out.println(">" + connection2.getClass()); 
		/*我们之前的连接都没关，我们到第六个的时候，如果3秒还是连接不上就会抛出异常
		 * 我们在3秒内把那个连接释放掉就可以，测试的时候我们用一个线程获取连接，用一个线程释放连接*/
		new Thread(){
			public void run() {
				Connection conn;
				try {
					conn = dataSource.getConnection();
					System.out.println(conn.getClass()); 
				} catch (SQLException e) {
					e.printStackTrace();
				}
			};
		}.start();
		//让当前线程休眠5.5秒，然后另外一个线程由于线程连接已经满了，所以会超时。
		try {
			Thread.sleep(5500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		connection2.close();
	}
}
