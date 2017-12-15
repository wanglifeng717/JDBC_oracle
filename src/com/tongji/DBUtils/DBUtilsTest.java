/**
* Title: DBUtilsTest.java
* Description: ����һ��DBUtils�෽���Ĳ����ࡣ�������ʹ��DButils
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017��12��14��
* @version 1.0
*/
package com.tongji.DBUtils;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import com.tongji.connectionPool.JDBCTools;

/**  
* Title: DBUtilsTest 
* Description:  ����ʹ��DButils��ķ���
* �ںܶ�ʱ����Ҫ���ܵ�ʱ�����ǲ���hibernate��������ԭ����JDBC����
* DBUtils����JDBC�Ϸ�װ��һ�㣬����д���룬ͬʱ�������졣
* *
	 * 1. ResultSetHandler ������: QueryRunner �� query �����ķ���ֵ����ȡ����
	 * query ������ ResultHandler ������ hanlde �����ķ���ֵ. 
	 * 
	 * 2. BeanListHandler: �ѽ����תΪһ�� Bean �� List, ������. Bean ��������
	 * ���� BeanListHanlder ����ʱ�� Class ����ķ�ʽ����. ������Ӧ�еı�����ӳ�� 
	 * JavaBean ��������: 
	 * String sql = "SELECT id, name EmployeeName, email, birth " +
	 *			"FROM Employees WHERE id = ?";
	 * 
	 * BeanListHandler(Class<T> type)
	 * 
	 * 3. BeanHandler: �ѽ����תΪһ�� Bean, ������. Bean �������ڴ��� BeanHandler
	 * ����ʱ�� Class ����ķ�ʽ����
	 * BeanHandler(Class<T> type) 
	 * 
	 * 4. MapHandler: �ѽ����תΪһ�� Map ����, ������. ����������ж�����¼, ������
	 * ��һ����¼��Ӧ�� Map ����. Map �ļ�: ����(�����еı���), ֵ: �е�ֵ
	 * 
	 * 5. MapListHandler: �ѽ����תΪһ�� Map ����ļ���, ������. 
	 * Map �ļ�: ����(�����еı���), ֵ: �е�ֵ
	 * 
	 * 6. ScalarHandler: ���Է���ָ���е�һ��ֵ�򷵻�һ��ͳ�ƺ�����ֵ. 
	 
* @author mdm(computer in lab)  
* @date 2017��12��14��  
*/
public class DBUtilsTest {

	/**
	 * ���ܣ�ScalarHandler: ���Է���ָ���е�һ��ֵ�򷵻�һ��ͳ�ƺ�����ֵ.
	 * Ĭ�Ͼͷ���һ��ֵ
	 */
	@Test
	public void testScalarHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select last_name from employees where employee_id=? ";
			//���������ֻ�Ƿ��ص�һ����¼��
			Object name=  queryRunner.query(connection, sql, new ScalarHandler(),135);
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	/**
	 * ���ܣ�MapListHandler:���ؽ��������map�ļ���list
	 *����SQL��ѯ�����������Ǳ�������ֵ
	 */
	@Test
	public void testMapListHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees  ";
			//���������ֻ�Ƿ��ص�һ����¼��
			List<Map<String, Object>> map=  queryRunner.query(connection, sql, new MapListHandler());
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	/**
	 * ���ܣ�MapHandler:����SQL��Ӧ�ĵ�һ����¼��Ӧ��Map����
	 *����SQL��ѯ�����������Ǳ�������ֵ
	 */
	@Test
	public void testMapHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees  ";
			//���������ֻ�Ƿ��ص�һ����¼��
			Map<String, Object> map=  queryRunner.query(connection, sql, new MapHandler());
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	/**
	 * ���ܣ�BeanListHandler�ѽ����ת��Ϊһ��List,��List��Ϊnull.������Ϊ�ռ��ϣ�size()��������0��
	 *
	 */
	@Test
	public void testBeanListHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees ";
			//���������ֻ�Ƿ��ص�һ����¼��
			 List<Employee>list= queryRunner.query(connection, sql, new BeanListHandler<Employee>(Employee.class));
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * ���ܣ�BeanHandler:�ѽ�����ĵ�һ����¼תΪ����BeanHandler�������class������Ӧ�Ķ���
	 * ���ǰ�����һ��JavaBean����
	 */
	@Test
	public void testBeanHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees where employee_id=? ";
			//���������ֻ�Ƿ��ص�һ����¼��
			Employee employee= (Employee) queryRunner.query(connection, sql, new BeanHandler<Employee>(Employee.class),134);
			System.out.println(employee);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * ���ܣ�����QueryRunner���update����
	 * �÷�������insert��update��delete
	 *
	 */
	@Test
	public void testQueryRunnerUdate() {
		//1.����queryRunner��ʵ����
		QueryRunner queryRunner=new QueryRunner();
		
		String sql="delete from emp2 where employee_id in(?,?)";
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			//2.ʹ����update������
			queryRunner.update(connection,sql,200,201);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
		
	}
	
	
	
	/**
	 * ���ֻ��Ϊ����Ϥ�ڲ�ԭ��ƽʱҲ�����������ַ�ʽ���á�
	 * BDutils�Ѿ�������ʵ�ֺͺܶ��Handler���������ˡ�
	 * ���� QueryRunner �� query ����
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testResultSetHandler(){
		String sql = "SELECT last_name " +
				"FROM Employees";
		
		//1. ���� QueryRunner ����
		QueryRunner queryRunner = new QueryRunner();
		
		Connection conn = null;
		
		try {
			conn = JDBCTools.getConnection();
			/**
			 * 2. ���� query ����:ͨ�������ڲ���ķ�ʽ����õġ�
			 * ResultSetHandler ����������: query �����ķ���ֱֵ��ȡ���� 
			 * ResultSetHandler �� hanlde(ResultSet rs) �����ʵ�ֵ�. ʵ����, ��
			 * QueryRunner ��� query ������Ҳ�ǵ����� ResultSetHandler �� handle()
			 * ������Ϊ����ֵ�ġ�
			 */
			Object object = queryRunner.query(conn, sql, 
					new ResultSetHandler(){
						@Override
						public Object handle(ResultSet rs) throws SQLException {
							List<Employee> Employees = new ArrayList<>();
							
							while(rs.next()){
								String last_name=rs.getString(1);
								
								Employee employee = 
										new Employee();
								employee.setLast_name(last_name);
								Employees.add(employee);
							}
							
							return Employees;
						}
					}
			
					);			
			
			System.out.println(object); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.releaseDB(null, null, conn);
		}
		
	}
}
