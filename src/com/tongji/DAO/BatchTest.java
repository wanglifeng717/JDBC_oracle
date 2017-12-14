/**
* Title: BatchTest.java
* Description: �������ݿ������������
* ���ݿ��������룬�������100000��������
* �����ĵ���statement��ʽ��
* �����preparedstatement ��Ϊ��Ԥ������ģ��ٶ���΢��㣬������Ȼ��һ��һ���ĵ���
* ������batch�����Ĳ�������Ϊ�����ǻ��۵�һ��������SQL��䣬Ȼ���ȥͳһ��ִ��
* ������һ�ֻ����ʵ�֡�
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017��12��14��
* @version 1.0
*/
package com.tongji.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

/**  
* Title: BatchTest 
* Description:  ����һ�����Դ������ݲ�����࣬��batch������
* @author mdm(computer in lab)  
* @date 2017��12��14��  
*/
public class BatchTest {
	@Test
	public void testBatch() {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
	    String sql=null;
	    try {
			connection=JDBCTools.getConnection();
			//��������
			JDBCTools.beginTx(connection);
			
			sql="insert into emp values(?,?)";
			preparedStatement=connection.prepareStatement(sql);
			//���Կ�ʼ��ʱ���ʱ�䡣
			long begin=System.currentTimeMillis();
			for(int i=0;i<10000;i++) {
				preparedStatement.setInt(1, i+1);
				preparedStatement.setInt(2, i+2);
				//����SQL
				preparedStatement.addBatch();
				//���۵�һ���̶�ͳһ��ִ��һ�Σ��������ԭ�Ȼ��۵�SQL
				if((i+1)%300==0) {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
				}		
			}
			//�ⲽ�����Ҫ
			//��ִ�е���������������ֵ��������������Ҫ����ִ��һ��
			if(100000%300 != 0) {
				preparedStatement.executeBatch();
				preparedStatement.clearBatch();
			}
			//������ֹʱ��ʱ��
			long end =System.currentTimeMillis();
			//��ʾ��������Щ�������˶೤ʱ�䡣
			System.out.println("Time:"+(end - begin));
			//�ύ����
			JDBCTools.commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
			JDBCTools.rollback(connection);
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
	}

	
}
