/**
* Title: DBCPTest.java
* Description: ����DBCP���ݿ����ӳص���
* ��Ҫ������������һ�������ļ��������ļ���JDBC�������ļ���ͬ��Ҫע��
* DBCP��pool ��dbcp.properties
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017��12��14��
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
* Description:������ǲ������ݿ����ӳص��ࡣ  
* @author mdm(computer in lab)  
* @date 2017��12��14��  
*/
public class DBCPTest {

	/**
	 * ���ܣ��ü��������ļ��ķ�ʽ��ȡ���ݿ�����
	 * ���ַ�ʽ�ŵ���ǽ���ϣ�ͬʱ���������������ע���½��������Դ���������
	 * 1. ���� dbcp �� properties �����ļ�: �����ļ��еļ���Ҫ���� BasicDataSource
	 * ������.
	 * 2. ���� BasicDataSourceFactory �� createDataSource �������� DataSource
	 * ʵ��
	 * 3. �� DataSource ʵ���л�ȡ���ݿ�����. 
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
		//�鿴�ղ����õ���Ϣ�Ƿ���ȷ��
		System.out.println(basicDataSource.getInitialSize());
		System.out.println(basicDataSource.getMaxTotal());
		System.out.println(basicDataSource.getMinIdle());
		System.out.println(basicDataSource.getMaxWaitMillis());
	}
	/**
	 * ���ܣ�ʹ�ô�ͳ�ֶ����õķ�ʽ����ȡ���ݿ����ӳغ����ݿ�����
	 * ʹ�� DBCP ���ݿ����ӳ�
	 * 1. ���� jar ��(2 ��jar ��). ������ Commons Pool
	 * 2. �������ݿ����ӳ�
	 * 3. Ϊ����Դʵ��ָ�����������
	 * 4. ������Դ�л�ȡ���ݿ�����
	 * @throws SQLException 
	 */
	@Test
	public void testDBCP() throws SQLException{
		final BasicDataSource dataSource = new BasicDataSource();
		
		//2. Ϊ����Դʵ��ָ�����������
		dataSource.setUsername("scott");
		dataSource.setPassword("tiger");
		dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		
		//3. ָ������Դ��һЩ��ѡ������.
		//1). ָ�����ݿ����ӳ��г�ʼ���������ĸ���
		dataSource.setInitialSize(5);
		
		//2). ָ������������: ͬһʱ�̿���ͬʱ�����ݿ������������
		dataSource.setMaxTotal(5);
		
		//3). ָ��С������: �����ݿ����ӳ��б�������ٵĿ������ӵ����� 
		dataSource.setMinIdle(2);
		
		//4).�ȴ����ݿ����ӳط������ӵ��ʱ��. ��λΪ����. ������ʱ�佫�׳��쳣. 
		dataSource.setMaxWaitMillis(1000 * 3);
		
		//4. ������Դ�л�ȡ���ݿ�����
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
		/*����֮ǰ�����Ӷ�û�أ����ǵ���������ʱ�����3�뻹�����Ӳ��Ͼͻ��׳��쳣
		 * ������3���ڰ��Ǹ������ͷŵ��Ϳ��ԣ����Ե�ʱ��������һ���̻߳�ȡ���ӣ���һ���߳��ͷ�����*/
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
		//�õ�ǰ�߳�����5.5�룬Ȼ������һ���߳������߳������Ѿ����ˣ����Իᳬʱ��
		try {
			Thread.sleep(5500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		connection2.close();
	}
}
