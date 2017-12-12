/**
* Title: TestOfPrimaryKey.java
* Description: 测试自动获取主键的类
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017年12月11日
* @version 1.0
*/
package com.tongji.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import org.junit.Test;



/**  
* Title: TestOfPrimaryKey 
* Description:  主要测试自动获取主键
* @author mdm(computer in lab)  
* @date 2017年12月11日  
*/
public class GetPrimaryKeyTest {
	/**
	 * 功能：在插入一条数据的同时，我得到那个主键值
	 * 		oracle里面没有自增的主键，可以自己定义序列和触发器来完成类似的操作
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
			//使用重载的此方法prepareStatement（sql,flag）
			//preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			preparedStatement = connection.prepareStatement(sql,new String[] {"employee_id"});
			preparedStatement.setObject(1, "zhangzhang");
			preparedStatement.executeUpdate();
			//获取包含新主键的结果集
			ResultSet resultSet=preparedStatement.getGeneratedKeys();
			if(resultSet.next()) {
				//获取方法和SQL不一样
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
