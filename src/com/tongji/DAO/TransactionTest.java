/**
* Title: TransactionTest.java
* Description: 这是一个测试数据库事务的类文件
* 没有采取适当的隔离级别，会导致各种并发问题；
* 1.脏读：某个字段：1事务修改了，没提交，2事务就读取了
* 2.不可重复读：1，2事务读取了字段，然后一个事务更新了字段，另一个字段读出来不一样了。
* 3.幻读：一个事务在读字段，另外一个加入新记录，刚才是事务读表的时候又多出来几条记录。
* 数据库的隔离级别：读未提交的，读已提交（oracle默认），可重复读（MYSQL默认），串行化
* 1.读未提交就是什么都可能发生
* 2.读已提交的就是不会发生脏读
* 3.可重复读就是把那个字段锁定，其他事务就不能更新这个字段了，但是可以增加记录，还是有幻读的可能
* 4.串行化：就是把整个表都锁定了，这样就不能添加了，连幻读避免了，但是效率非常的低。
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
* Description:  这是一个用来测试数据事务的类
* @author mdm(computer in lab)  
* @date Dec 13, 2017  
*/
public class TransactionTest {
	/**
	 * 功能：适用于事务的更新方法，就用一个连接。
	 * 因为事务中，我们用两次以上的Update,我们都新建了连接，这是保证不了数据一致性的
	 * 错误的写法，因为他们拿到的是不同的连接
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
	 * 功能：测试两个用户之间互相汇款的测试id=1的人给id=2的人汇款500元。
	 * 关于事务: 1. 如果多个操作, 每个操作使用的是自己的单独的连接, 则无法保证事务. 2. 具体步骤: 1). 事务操作开始前, 开始事务:
	 * 取消 Connection 的默认提交行为. connection.setAutoCommit(false); 2). 如果事务的操作都成功,
	 * 则提交事务: connection.commit(); 3). 回滚事务: 若出现异常, 则在 catch 块中回滚事务:
	 * 准确的格式应该是这样的。
	 * try {
		 * 
		 * //开始事务: 取消默认提交. connection.setAutoCommit(false);
		 * 
		 * //...
		 * 
		 * //提交事务 connection.commit(); } catch (Exception e) { //...
		 * 
		 * //回滚事务 try { connection.rollback(); } catch (SQLException e1) {
		 * e1.printStackTrace(); } } finally{ JDBCTools.releaseDB(null, null,
		 * connection); }
	 */
	@Test
	public void testTransaction() {
		Connection connection=null;
		try {
			connection= JDBCTools.getConnection();
			//开始事务：取消默认的提交
			connection.setAutoCommit(false);
			//查看数据库事务的默认隔离级别
			System.out.println(connection.getTransactionIsolation());
			String sql="update emp set account=account-500 where id=1";
			update(connection, sql);
			
			sql="update emp set account=account+500 where id=2";
			update(connection, sql);
			
			//提交事务
			connection.commit();
		} catch (Exception e) {			
			e.printStackTrace();
			//回滚事务
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

