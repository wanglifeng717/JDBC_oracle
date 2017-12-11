/**
* Title: MetaDataTest.java
* Description: 测试数据库元数据和resultSet结果集的元数据测试文件
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017年12月11日
* @version 1.0
*/
package tongji.com.cn.DAO;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

/**  
* Title: MetaDataTest 
* Description:  测试数据库元数据，和resultSet结果集元数据的测试类。
* @author mdm(computer in lab)  
* @date 2017年12月11日  
*/
public class MetaDataTest {
	/**
	 * 功能：测试数据库元数据，DatabaseMetaData
	 *     可以由connection得到，但是一般用的不多，了解程度即可
	 */
	@Test
	public void testDatabaseMetaData() {
		Connection connection=null;	
		ResultSet resultSet=null;
		try {
			connection=JDBCTools.getConnection();
			DatabaseMetaData data=connection.getMetaData();
			
			//可以得到数据库本身的一些基本信息
			//1. 得到数据库的版本号
			int version = data.getDatabaseMajorVersion();
			System.out.println(version);
			
			//2. 得到连接到数据库的用户名
			String user = data.getUserName();
			System.out.println(user);
			
			//3. 得到 哪些数据库
			resultSet = data.getCatalogs();
			while(resultSet.next()){
				System.out.println(resultSet.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(resultSet, null, connection);
		}
	}
	/**
	 * 功能：
	 * ResultSetMetaData: 描述结果集的元数据. 
	 * 可以得到结果集中的基本信息: 结果集中有哪些列, 列名, 列的别名等.
	 * 结合反射可以写出通用的查询方法. 
	 */
	@Test
	public void testResultSetMetaData() {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name last_name from employees where department_id=?";
			preparedStatement=connection.prepareStatement(sql);
			preparedStatement.setInt(1, 80);;
			resultSet = preparedStatement.executeQuery();
			//1.得到ResultSetMetaData对象
			ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
			//2.得到列的个数
			int columnCount=resultSetMetaData.getColumnCount();
			System.out.println(columnCount);
			
			for(int i=0;i<columnCount;i++) {
				//3.得到列名
				String columnName = resultSetMetaData.getColumnName(i+1);
				//4.得到列的别名
				String columnLabel =resultSetMetaData.getColumnLabel(i+1);
				System.out.println(columnName+"="+columnLabel);
			}	
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			// TODO: handle finally clause
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}
	}
	
	
}
