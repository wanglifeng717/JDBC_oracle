/**
* Title: BlobOfOracleTest.java
* Description: oracle的大文件BLOB插入和读取的操作。
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date Dec 12, 2017
* @version 1.0
*/
package com.tongji.DAO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;


/**  
* Title: BlobOfOracleTest 
* Description:  oracle的大文件插入和读取的操作。
* @author mdm(computer in lab)  
* @date Dec 12, 2017  
*/
public class BlobOfOracleTest {
	
	/**
	 * 读取 blob 数据: 
	 * 1. 使用 getBlob 方法读取到 Blob 对象
	 * 2. 调用 Blob 的 getBinaryStream() 方法得到输入流。再使用 IO 操作即可. 
	 */
	@Test
	public void readBlob(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "select id,pic from lobtab";
			preparedStatement = connection.prepareStatement(sql);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()){
				int id = resultSet.getInt(1);
				Blob picture = resultSet.getBlob(2);
				
				InputStream in = picture.getBinaryStream();
				System.out.println(in.available()); 
				
				OutputStream out = new FileOutputStream("picOfRead.jpg");
				
				byte [] buffer = new byte[1024];
				int len = 0;
				while((len = in.read(buffer)) != -1){
					out.write(buffer, 0, len);
				}
				
				in.close();
				out.close();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.releaseDB(resultSet, preparedStatement, connection);
		}
	}
	/**
	 * 插入 BLOB 类型的数据必须使用 PreparedStatement：因为 BLOB 类型
	 * 的数据时无法使用字符串拼写的。
	 * 
	 * 调用 setBlob(int index, InputStream inputStream)
	 */
	@Test
	public void testInsertBlob(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			connection = JDBCTools.getConnection();
			String sql = "insert into lobtab values(?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, 1);
			InputStream inputStream = new FileInputStream("C:\\Users\\mdm\\Desktop\\ss.png");
			preparedStatement.setBlob(2, inputStream);
			
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
	}
	
}
