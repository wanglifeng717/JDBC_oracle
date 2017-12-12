/**
* Title: TestOfPrimaryKey.java
* Description: �����Զ���ȡ��������
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017��12��11��
* @version 1.0
*/
package com.tongji.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import org.junit.Test;



/**  
* Title: TestOfPrimaryKey 
* Description:  ��Ҫ�����Զ���ȡ����
* @author mdm(computer in lab)  
* @date 2017��12��11��  
*/
public class GetPrimaryKeyTest {
	/**
	 * ���ܣ��ڲ���һ�����ݵ�ͬʱ���ҵõ��Ǹ�����ֵ
	 * 		oracle����û�������������������Լ��������кʹ�������������ƵĲ���
	 *
	 */
	@Test
	public void testGetKeyValue()
	{
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
		try {
			connection=JDBCTools.getConnection();
			String  sql="insert into employees (last_name) values(?)";
			//preparedStatement = connection.prepareStatement(sql);
			//ʹ�����صĴ˷���prepareStatement��sql,flag��
			//preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement = connection.prepareStatement(sql,new String[] {"employee_id"});
			preparedStatement.setObject(1, "zhangzhang");
			preparedStatement.executeUpdate();
			//��ȡ�����������Ľ����
			ResultSet resultSet=preparedStatement.getGeneratedKeys();
			if(resultSet.next()) {
				//��ȡ������SQL��һ��
				System.out.println(resultSet.getString(1));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
	}

}
