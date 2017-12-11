/**
* Title: MetaDataTest.java
* Description: �������ݿ�Ԫ���ݺ�resultSet�������Ԫ���ݲ����ļ�
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017��12��11��
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
* Description:  �������ݿ�Ԫ���ݣ���resultSet�����Ԫ���ݵĲ����ࡣ
* @author mdm(computer in lab)  
* @date 2017��12��11��  
*/
public class MetaDataTest {
	/**
	 * ���ܣ��������ݿ�Ԫ���ݣ�DatabaseMetaData
	 *     ������connection�õ�������һ���õĲ��࣬�˽�̶ȼ���
	 */
	@Test
	public void testDatabaseMetaData() {
		Connection connection=null;	
		ResultSet resultSet=null;
		try {
			connection=JDBCTools.getConnection();
			DatabaseMetaData data=connection.getMetaData();
			
			//���Եõ����ݿⱾ���һЩ������Ϣ
			//1. �õ����ݿ�İ汾��
			int version = data.getDatabaseMajorVersion();
			System.out.println(version);
			
			//2. �õ����ӵ����ݿ���û���
			String user = data.getUserName();
			System.out.println(user);
			
			//3. �õ� ��Щ���ݿ�
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
	 * ���ܣ�
	 * ResultSetMetaData: �����������Ԫ����. 
	 * ���Եõ�������еĻ�����Ϣ: �����������Щ��, ����, �еı�����.
	 * ��Ϸ������д��ͨ�õĲ�ѯ����. 
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
			//1.�õ�ResultSetMetaData����
			ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
			//2.�õ��еĸ���
			int columnCount=resultSetMetaData.getColumnCount();
			System.out.println(columnCount);
			
			for(int i=0;i<columnCount;i++) {
				//3.�õ�����
				String columnName = resultSetMetaData.getColumnName(i+1);
				//4.�õ��еı���
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
