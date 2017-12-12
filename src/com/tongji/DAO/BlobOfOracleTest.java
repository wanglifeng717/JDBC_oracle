/**
* Title: BlobOfOracleTest.java
* Description: oracle�Ĵ��ļ�BLOB����Ͷ�ȡ�Ĳ�����
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
* Description:  oracle�Ĵ��ļ�����Ͷ�ȡ�Ĳ�����
* @author mdm(computer in lab)  
* @date Dec 12, 2017  
*/
public class BlobOfOracleTest {
	
	/**
	 * ��ȡ blob ����: 
	 * 1. ʹ�� getBlob ������ȡ�� Blob ����
	 * 2. ���� Blob �� getBinaryStream() �����õ�����������ʹ�� IO ��������. 
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
	 * ���� BLOB ���͵����ݱ���ʹ�� PreparedStatement����Ϊ BLOB ����
	 * ������ʱ�޷�ʹ���ַ���ƴд�ġ�
	 * 
	 * ���� setBlob(int index, InputStream inputStream)
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
