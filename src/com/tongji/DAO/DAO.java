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
 * ʵ�ֹ��ܵ�ģ�黯�������ڴ����ά��
 * ���Ա�����̳л���ֱ��ʹ��
 * �������ݵ��࣬�����˶����ݵ�CRUD��create��read��update��delete�����������κ�ҵ����Ϣ
 * 1.insert,update,delete����������
 * 		void update(String sql,Object ... args);
 * 2.��ѯһ����¼�����ض�Ӧ�Ķ���
 * 		<T> T get(class<T> clazz,String sql,object ... args)
 * 3.��ѯ������¼�����ض�Ӧ�Ķ���ļ���
 * 		<T> list<T> getForList(class<T> clazz,String sql,object ... args)
 * 4.����ĳ����¼��ĳһ���ֶε�ֵ��ͳ�Ƶ�ֵ��һ���ж�������¼�ȣ�
 * 		<E> E getForValue(String sql,Object ... args)
 * @author mdm
 *
 */
public class DAO {
	
	
	/**
	 * 4.����ĳ����¼��ĳһ���ֶε�ֵ��ͳ�Ƶ�ֵ��һ���ж�������¼�ȣ�
 * 		<E> E getForValue(String sql,Object ... args)
	 */
public <E> E getForValue(String sql, Object... args) {
		
		//1. �õ������: �ý����Ӧ��ֻ��һ��, ��ֻ��һ��
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			//1. �õ������
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
		//2. ȡ�ý��
		
		return null;
	}
	
	/**
	 * 1.�������ݵ��࣬�����˶����ݵ�CRUD��create��read��update��delete��
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
//			��preparedStatement������ֵ
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			preparedStatement.executeUpdate();	
			
		} catch (Exception e) {
			e.printStackTrace();//һ��Ҫ��ӡ�쳣��Ϣ����Ȼû���κε���ʾ����Ӳ���ȥ��û�κ���ʾ��
		}finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
			
		}
		
	}
	// 1.��ѯһ����¼, ���ض�Ӧ�Ķ���.����ǰ���Ѿ����ɵķ�����
		public <T> T get2(Class<T> clazz, String sql, Object... args) {
			List<T> result = getForList(clazz, sql, args);
			if(result.size() > 0){
				return result.get(0);
			}
			
			return null;
		}
	/**
	 *  1.��ȡconnection
		 2.��ȡpreparedStatement
		 3.���ռλ��
		 4.���в�ѯ���õ�resultSet
		 5.��resultSet���м�¼��
		         ׼��һ��Map<String,Object><������ű�����ֵ>(��ʵӳ���Ǻ��ֶα���֮���ӳ��)
		 6.�õ�resultSetMetaData����
		 7.����resultSetָ�������ƶ�һ����λ
		 8.��resultSetMetaData�õ��������һ�������У�ÿһ�еı�������resultSet�õ�ÿһ�е�ֵ
		 9.���Map����
		 11.�÷��䴴��Class��Ӧ�Ķ���
		 12.����Map�����÷��������������ֵ��������ΪMap�е�key,ֵΪMap�е�value
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
//	2.��ѯһ����¼�����ض�Ӧ�Ķ���
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
	 * ���� SQL ���� Class ����, ���� SQL ����ѯ���ļ�¼��Ӧ�� Class ��Ķ���ļ���
	 * @param clazz: ���������
	 * @param sql: SQL ���
	 * @param args: ��� SQL ����ռλ���Ŀɱ����. 
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
//			Ϊpreparedstatement��ֵ����
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
//			1.�õ������
			resultSet =  preparedStatement.executeQuery();
			//2. ��������, �õ� Map �� List, ����һ�� Map ����
			//����һ����¼. Map �� key Ϊ reusltSet ���еı���, Map �� value
			//Ϊ�е�ֵ. 
			List<Map<String, Object>> values=handleResultSetToMapList(resultSet);
		
			//3. �� Map �� List תΪ clazz ��Ӧ�� List
			//���� Map �� key ��Ϊ clazz ��Ӧ�Ķ���� propertyName, 
			//�� Map �� value ��Ϊ clazz ��Ӧ�Ķ���� propertyValue
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


	/**3.��ѯ������¼�����ض�Ӧ�Ķ���ļ���
	 * ��������, �õ� Map ��һ�� List, ����һ�� Map �����Ӧһ����¼
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
	 * ��ȡ������� ColumnLabel ��Ӧ�� List
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
