package com.tongji.connectionPool;
import javax.sql.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
* Title: c3p0ConnectPoolTest.java
* Description: c3p0���ݿ����ӳ��õĲ����ࡣhibernate�����������Tomcat���õ�DBCP 
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017��12��14��
* @version 1.0
*/

/**  
* Title: c3p0ConnectPoolTest 
* Description:  ����ʹ��c3p0����Դ
* @author mdm(computer in lab)  
* @date 2017��12��14��  
*/
public class c3p0ConnectPoolTest {

	/**
	 * 1. ���� c3p0-config.xml �ļ�, 
	 * �ο������ĵ��� Appendix B: Configuation Files ������
	 * 2. ���� ComboPooledDataSource ʵ����
	 * DataSource dataSource = 
	 *			new ComboPooledDataSource("helloc3p0");  
	 * 3. �� DataSource ʵ���л�ȡ���ݿ�����. 
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
	//�����ع���Ļ�ȡ���ݿ����ӵķ�����
	@Test
	public void testJDBCToolsOfConnection() throws Exception
	{
		System.out.println(JDBCTools.getConnection());
	}
}
