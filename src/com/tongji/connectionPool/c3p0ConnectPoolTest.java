package com.tongji.connectionPool;
import javax.sql.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
* Title: c3p0ConnectPoolTest.java
* Description: c3p0数据库连接池用的测试类。hibernate就是用这个。Tomcat是用的DBCP 
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017年12月14日
* @version 1.0
*/

/**  
* Title: c3p0ConnectPoolTest 
* Description:  测试使用c3p0数据源
* @author mdm(computer in lab)  
* @date 2017年12月14日  
*/
public class c3p0ConnectPoolTest {

	/**
	 * 1. 创建 c3p0-config.xml 文件, 
	 * 参考帮助文档中 Appendix B: Configuation Files 的内容
	 * 2. 创建 ComboPooledDataSource 实例；
	 * DataSource dataSource = 
	 *			new ComboPooledDataSource("helloc3p0");  
	 * 3. 从 DataSource 实例中获取数据库连接. 
	 */
	@Test
	public void testC3poWithConfigFile() throws Exception{
		DataSource dataSource = 
				new ComboPooledDataSource("helloc3p0");  
		
		System.out.println(dataSource.getConnection()); 
		
		ComboPooledDataSource comboPooledDataSource = 
				(ComboPooledDataSource) dataSource;
		System.out.println(comboPooledDataSource.getMaxStatements()); 
	}
	//测试重构后的获取数据库连接的方法。
	@Test
	public void testJDBCToolsOfConnection() throws Exception
	{
		System.out.println(JDBCTools.getConnection());
	}
}
