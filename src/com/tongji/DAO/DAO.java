package com.tongji.DAO;


import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;






/**
 * DAO:data access Object
 * 实现功能的模块化，更利于代码的维护
 * 可以被子类继承或者直接使用
 * 访问数据的类，包含了对数据的CRUD（create，read，update，delete）而不包含任何业务信息
 * 1.insert,update,delete操作都可以
 * 		void update(String sql,Object ... args);
 * 2.查询一条记录，返回对应的对象
 * 		<T> T get(class<T> clazz,String sql,object ... args)
 * 3.查询多条记录，返回对应的对象的集合
 * 		<T> list<T> getForList(class<T> clazz,String sql,object ... args)
 * 4.返回某条记录的某一个字段的值或统计的值（一共有多少条记录等）
 * 		<E> E getForValue(String sql,Object ... args)
 * @author mdm
 *
 */
public class DAO {
	
	
	/**
	 * 4.返回某条记录的某一个字段的值或统计的值（一共有多少条记录等）
 * 		<E> E getForValue(String sql,Object ... args)
	 */
public <E> E getForValue(String sql, Object... args) {
		
		//1. 得到结果集: 该结果集应该只有一行, 且只有一列
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			//1. 得到结果集
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}

			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				return (E) resultSet.getObject(1);
			}
		} catch(Exception ex){
			ex.printStackTrace();
		} finally{
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}
		//2. 取得结果
		
		return null;
	}
	
	/**
	 * 1.访问数据的类，包含了对数据的CRUD（create，read，update，delete）
	 * @param sql
	 * @param args
	 */
	public void Update(String sql,Object... args)
	{
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		
		try {
			connection=JDBCTools.getConnection();
			preparedStatement=connection.prepareStatement(sql);
//			给preparedStatement参数赋值
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			preparedStatement.executeUpdate();	
			
		} catch (Exception e) {
			e.printStackTrace();//一定要打印异常信息，不然没有任何的提示，你加不进去都没任何提示。
		}finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
			
		}
		
	}
	// 1.查询一条记录, 返回对应的对象.利用前面已经生成的方法。
		public <T> T get2(Class<T> clazz, String sql, Object... args) {
			List<T> result = getForList(clazz, sql, args);
			if(result.size() > 0){
				return result.get(0);
			}
			
			return null;
		}
	/**
	 *  1.获取connection
		 2.获取preparedStatement
		 3.填充占位符
		 4.进行查询，得到resultSet
		 5.若resultSet中有记录：
		         准备一个Map<String,Object><键：存放别名，值>(其实映射是和字段别名之间的映射)
		 6.得到resultSetMetaData对象
		 7.处理resultSet指针向下移动一个单位
		 8.有resultSetMetaData得到结果集中一共多少列，每一列的别名，又resultSet得到每一列的值
		 9.填充Map对象
		 11.用反射创建Class对应的对象
		 12.遍历Map对象，用反射填充对象的属性值，属性名为Map中的key,值为Map中的value
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
//	2.查询一条记录，返回对应的对象
	 public	<T> T get(Class<T> clazz,String sql,Object ... args)
	 {
		T entity=null;
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		 
		try {
			connection=JDBCTools.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet=preparedStatement.executeQuery();
			
			if(resultSet.next())
			{
				Map<String, Object> map=new HashMap<>();
				ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
				int columnCount=resultSetMetaData.getColumnCount();
				for(int i=0;i<columnCount;i++)
				{
					String columnLabel=resultSetMetaData.getColumnLabel(i+1);
					Object columnValue=resultSet.getObject(i+1);
					map.put(columnLabel, columnValue);
				}
				
				entity=clazz.newInstance();
				
				for(Map.Entry<String, Object> entry:map.entrySet())
				{
					String propertyName=entry.getKey().toLowerCase();
					Object value= entry.getValue();
					System.out.println(propertyName+value);
					BeanUtils.setProperty(entity, propertyName, value);
				}
			}	
		} catch (Exception e) { 
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}

		return entity;
		 
	 }
	/**
	 * 传入 SQL 语句和 Class 对象, 返回 SQL 语句查询到的记录对应的 Class 类的对象的集合
	 * @param clazz: 对象的类型
	 * @param sql: SQL 语句
	 * @param args: 填充 SQL 语句的占位符的可变参数. 
	 * @return
	 */
	public <T> List<T> getForList(Class<T> clazz,String sql,Object...args)
	{
		List<T> list = new ArrayList<T>();
		
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		ResultSet resultSet=null;
		
		try {
			
			connection = JDBCTools.getConnection();
			preparedStatement=connection.prepareStatement(sql);
//			为preparedstatement赋值参数
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
//			1.得到结果集
			resultSet =  preparedStatement.executeQuery();
			//2. 处理结果集, 得到 Map 的 List, 其中一个 Map 对象
			//就是一条记录. Map 的 key 为 reusltSet 中列的别名, Map 的 value
			//为列的值. 
			List<Map<String, Object>> values=handleResultSetToMapList(resultSet);
		
			//3. 把 Map 的 List 转为 clazz 对应的 List
			//其中 Map 的 key 即为 clazz 对应的对象的 propertyName, 
			//而 Map 的 value 即为 clazz 对应的对象的 propertyValue
			list =transferMapListToBeanList(clazz,values);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}
		return list;
	}


	private <T> List<T> transferMapListToBeanList(Class<T> clazz, List<Map<String, Object>> values) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		List<T> result = new ArrayList<>();
		T bean =null;
		
		if(values.size()>0)
		{			
			for(Map<String, Object> m:values)
			{
				bean = clazz.newInstance();
				for(Map.Entry<String, Object> entry:m.entrySet())
				{
					String propertyName = entry.getKey().toLowerCase();
					Object value =entry.getValue();
					BeanUtils.setProperty(bean,propertyName,value);
				}
				result.add(bean);
			}
		}
		return result;
	}


	/**3.查询多条记录，返回对应的对象的集合
	 * 处理结果集, 得到 Map 的一个 List, 其中一个 Map 对象对应一条记录
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	private List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) throws SQLException {
		List<Map<String, Object>> values=new ArrayList<>();
		List<String> columnLabels =getColumnLabels(resultSet);
		Map<String, Object> map=null;
		while(resultSet.next())
		{
			map=new HashMap<>();
			for(String columnLabel:columnLabels)
			{
				Object value=resultSet.getObject(columnLabel);
				map.put(columnLabel, value);
			}
			values.add(map);
		}
		return values;
	}

	/**
	 * 获取结果集的 ColumnLabel 对应的 List
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<String> getColumnLabels(ResultSet resultSet) throws SQLException {
		List<String> labels = new ArrayList<>();
		ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
		for(int i=0;i<resultSetMetaData.getColumnCount();i++)
		{
			labels.add(resultSetMetaData.getColumnLabel(i+1));
		}
		return labels;
	}


	
}
