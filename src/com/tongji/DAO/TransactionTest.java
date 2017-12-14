/**
* Title: TransactionTest.java
* Description: ����һ���������ݿ���������ļ�
* û�в�ȡ�ʵ��ĸ��뼶�𣬻ᵼ�¸��ֲ������⣻
* 1.�����ĳ���ֶΣ�1�����޸��ˣ�û�ύ��2����Ͷ�ȡ��
* 2.�����ظ�����1��2�����ȡ���ֶΣ�Ȼ��һ������������ֶΣ���һ���ֶζ�������һ���ˡ�
* 3.�ö���һ�������ڶ��ֶΣ�����һ�������¼�¼���ղ�����������ʱ���ֶ����������¼��
* ���ݿ�ĸ��뼶�𣺶�δ�ύ�ģ������ύ��oracleĬ�ϣ������ظ�����MYSQLĬ�ϣ������л�
* 1.��δ�ύ����ʲô�����ܷ���
* 2.�����ύ�ľ��ǲ��ᷢ�����
* 3.���ظ������ǰ��Ǹ��ֶ���������������Ͳ��ܸ�������ֶ��ˣ����ǿ������Ӽ�¼�������лö��Ŀ���
* 4.���л������ǰ������������ˣ������Ͳ�������ˣ����ö������ˣ�����Ч�ʷǳ��ĵ͡�
* 
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date Dec 13, 2017
* @version 1.0
*/
package com.tongji.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;

/**  
* Title: TransactionTest 
* Description:  ����һ���������������������
* @author mdm(computer in lab)  
* @date Dec 13, 2017  
*/
public class TransactionTest {
	/**
	 * ���ܣ�����������ĸ��·���������һ�����ӡ�
	 * ��Ϊ�����У��������������ϵ�Update,���Ƕ��½������ӣ����Ǳ�֤��������һ���Ե�
	 * �����д������Ϊ�����õ����ǲ�ͬ������
	 *   DAO dao = new DAO();
		 String sql = "UPDATE users SET balance = " +
		 "balance - 500 WHERE id = 1";
		 dao.update(sql);
		 int i = 10 / 0;
		 System.out.println(i);

		 sql = "UPDATE users SET balance = " +
		 "balance + 500 WHERE id = 2";
		 dao.update(sql);
	 * @param connection
	 * @param sql
	 * @param object
	 */
	public void update(Connection connection,String sql,Object ... args) {
		PreparedStatement preparedStatement=null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			
			for(int i=0;i<args.length;i++) {
				preparedStatement.setObject(i+1, args[i]);
			}
			
			preparedStatement.executeUpdate();				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, null);
		}
	}
	/**
	 * ���ܣ����������û�֮�以����Ĳ���id=1���˸�id=2���˻��500Ԫ��
	 * ��������: 1. ����������, ÿ������ʹ�õ����Լ��ĵ���������, ���޷���֤����. 2. ���岽��: 1). ���������ʼǰ, ��ʼ����:
	 * ȡ�� Connection ��Ĭ���ύ��Ϊ. connection.setAutoCommit(false); 2). �������Ĳ������ɹ�,
	 * ���ύ����: connection.commit(); 3). �ع�����: �������쳣, ���� catch ���лع�����:
	 * ׼ȷ�ĸ�ʽӦ���������ġ�
	 * try {
		 * 
		 * //��ʼ����: ȡ��Ĭ���ύ. connection.setAutoCommit(false);
		 * 
		 * //...
		 * 
		 * //�ύ���� connection.commit(); } catch (Exception e) { //...
		 * 
		 * //�ع����� try { connection.rollback(); } catch (SQLException e1) {
		 * e1.printStackTrace(); } } finally{ JDBCTools.releaseDB(null, null,
		 * connection); }
	 */
	@Test
	public void testTransaction() {
		Connection connection=null;
		try {
			connection= JDBCTools.getConnection();
			//��ʼ����ȡ��Ĭ�ϵ��ύ
			connection.setAutoCommit(false);
			//�鿴���ݿ������Ĭ�ϸ��뼶��
			System.out.println(connection.getTransactionIsolation());
			String sql="update emp set account=account-500 where id=1";
			update(connection, sql);
			
			sql="update emp set account=account+500 where id=2";
			update(connection, sql);
			
			//�ύ����
			connection.commit();
		} catch (Exception e) {			
			e.printStackTrace();
			//�ع�����
			try {
				connection.rollback();
			} catch (Exception e2) {
				e.printStackTrace();
			}
		} finally {
			JDBCTools.releaseDB(null, null, connection);
		}
	}
}

