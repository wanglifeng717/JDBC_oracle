/**
* Title: DBUtilsTest.java
* Description: 这是一个DBUtils类方法的测试类。测试如何使用DButils
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017年12月14日
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
* Description:  测试使用DButils类的方法
* 在很多时候需要性能的时候，我们不用hibernate，我们用原生的JDBC但是
* DBUtils是在JDBC上封装了一层，方便写代码，同时性能优异。
* *
	 * 1. ResultSetHandler 的作用: QueryRunner 的 query 方法的返回值最终取决于
	 * query 方法的 ResultHandler 参数的 hanlde 方法的返回值. 
	 * 
	 * 2. BeanListHandler: 把结果集转为一个 Bean 的 List, 并返回. Bean 的类型在
	 * 创建 BeanListHanlder 对象时以 Class 对象的方式传入. 可以适应列的别名来映射 
	 * JavaBean 的属性名: 
	 * String sql = "SELECT id, name EmployeeName, email, birth " +
	 *			"FROM Employees WHERE id = ?";
	 * 
	 * BeanListHandler(Class<T> type)
	 * 
	 * 3. BeanHandler: 把结果集转为一个 Bean, 并返回. Bean 的类型在创建 BeanHandler
	 * 对象时以 Class 对象的方式传入
	 * BeanHandler(Class<T> type) 
	 * 
	 * 4. MapHandler: 把结果集转为一个 Map 对象, 并返回. 若结果集中有多条记录, 仅返回
	 * 第一条记录对应的 Map 对象. Map 的键: 列名(而非列的别名), 值: 列的值
	 * 
	 * 5. MapListHandler: 把结果集转为一个 Map 对象的集合, 并返回. 
	 * Map 的键: 列名(而非列的别名), 值: 列的值
	 * 
	 * 6. ScalarHandler: 可以返回指定列的一个值或返回一个统计函数的值. 
	 
* @author mdm(computer in lab)  
* @date 2017年12月14日  
*/
public class DBUtilsTest {

	/**
	 * 功能：ScalarHandler: 可以返回指定列的一个值或返回一个统计函数的值.
	 * 默认就返回一个值
	 */
	@Test
	public void testScalarHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select last_name from employees where employee_id=? ";
			//如果多条，只是返回第一条记录。
			Object name=  queryRunner.query(connection, sql, new ScalarHandler(),135);
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	/**
	 * 功能：MapListHandler:返回结果集对象map的集合list
	 *键：SQL参询的列名（不是别名），值
	 */
	@Test
	public void testMapListHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees  ";
			//如果多条，只是返回第一条记录。
			List<Map<String, Object>> map=  queryRunner.query(connection, sql, new MapListHandler());
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	/**
	 * 功能：MapHandler:返回SQL对应的第一条记录对应的Map对象
	 *键：SQL参询的列名（不是别名），值
	 */
	@Test
	public void testMapHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees  ";
			//如果多条，只是返回第一条记录。
			Map<String, Object> map=  queryRunner.query(connection, sql, new MapHandler());
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	/**
	 * 功能：BeanListHandler把结果集转化为一个List,该List不为null.但可能为空集合（size()方法返回0）
	 *
	 */
	@Test
	public void testBeanListHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees ";
			//如果多条，只是返回第一条记录。
			 List<Employee>list= queryRunner.query(connection, sql, new BeanListHandler<Employee>(Employee.class));
			System.out.println(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * 功能：BeanHandler:把结果集的第一条记录转为创建BeanHandler对象传入的class参数对应的对象。
	 * 就是把生成一个JavaBean对象。
	 */
	@Test
	public void testBeanHandler() {
		QueryRunner queryRunner = new QueryRunner();
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			String sql="select employee_id,last_name,email,salary from employees where employee_id=? ";
			//如果多条，只是返回第一条记录。
			Employee employee= (Employee) queryRunner.query(connection, sql, new BeanHandler<Employee>(Employee.class),134);
			System.out.println(employee);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
	
	/**
	 * 功能：测试QueryRunner类的update方法
	 * 该方法用于insert，update，delete
	 *
	 */
	@Test
	public void testQueryRunnerUdate() {
		//1.创建queryRunner的实现类
		QueryRunner queryRunner=new QueryRunner();
		
		String sql="delete from emp2 where employee_id in(?,?)";
		Connection connection=null;
		try {
			connection=JDBCTools.getConnection();
			//2.使用其update方法。
			queryRunner.update(connection,sql,200,201);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
		
	}
	
	
	
	/**
	 * 这个只是为了熟悉内部原理，平时也基本不用这种方式调用。
	 * BDutils已经给我们实现和很多的Handler让我们用了。
	 * 测试 QueryRunner 的 query 方法
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testResultSetHandler(){
		String sql = "SELECT last_name " +
				"FROM Employees";
		
		//1. 创建 QueryRunner 对象
		QueryRunner queryRunner = new QueryRunner();
		
		Connection conn = null;
		
		try {
			conn = JDBCTools.getConnection();
			/**
			 * 2. 调用 query 方法:通过匿名内部类的方式类调用的。
			 * ResultSetHandler 参数的作用: query 方法的返回值直接取决于 
			 * ResultSetHandler 的 hanlde(ResultSet rs) 是如何实现的. 实际上, 在
			 * QueryRunner 类的 query 方法中也是调用了 ResultSetHandler 的 handle()
			 * 方法作为返回值的。
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
