/**
* Title: ProcedueOrFunctionTest.java
* Description: ʹ��JDBC���ô洢���̻���
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017��12��15��
* @version 1.0
*/
package com.tongji.DBUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**  
* Title: ProcedueOrFunctionTest 
* Description:  ʹ��JDBC���ô洢���̻���
* @author mdm(computer in lab)  
* @date 2017��12��15��  
*/
public class ProcedueOrFunctionTest {

	/**
	 * ���ʹ�� JDBC ���ô洢�����ݿ��еĺ�����洢����
	 */
	@Test
	public void testCallableStatment() {

		Connection connection = null;
		CallableStatement callableStatement = null;

		try {
			connection = JDBCTools.getConnection();

			// 1. ͨ�� Connection ����� prepareCall()
			// ��������һ�� CallableStatement �����ʵ��.
			// ��ʹ�� Connection ����� preparedCall() ����ʱ,
			// ��Ҫ����һ�� String ���͵��ַ���, ���ַ�������ָ����ε��ô洢����.
			// ���ַ�������ָ����ε��ô洢����
			//{?= call <procedure-name>[(<arg1>,<arg2>, ...)]}
			 // {call <procedure-name>[(<arg1>,<arg2>, ...)]}
			String sql = "{?= call get_sal(?)}";
			callableStatement = connection.prepareCall(sql);

			// 2. ͨ�� CallableStatement ����� 
			//reisterOutParameter() ����ע�� OUT ����.
			callableStatement.registerOutParameter(1, Types.NUMERIC);
			
			
			// 3. ͨ�� CallableStatement ����� setXxx() �����趨 IN �� IN OUT ����. ���뽫����Ĭ��ֵ��Ϊ
			// null, ����ʹ�� setNull() ����.
			callableStatement.setInt(2, 10);
			
			// 4. ͨ�� CallableStatement ����� execute() ����ִ�д洢����
			callableStatement.execute();
			
			// 5. ��������õ��Ǵ����ز����Ĵ洢����, 
			//����Ҫͨ�� CallableStatement ����� getXxx() ������ȡ�䷵��ֵ.
			double sumSalary = callableStatement.getDouble(1);			
			System.out.println(sumSalary);			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, callableStatement, connection);
		}
	}
	@Test
	public void testC3P0() throws SQLException {
		DataSource dataSource = new ComboPooledDataSource("helloc3p0");

		System.out.println(dataSource.getConnection());
	}
}
