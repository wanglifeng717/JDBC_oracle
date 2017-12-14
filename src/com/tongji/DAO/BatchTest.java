/**
* Title: BatchTest.java
* Description: 测试数据库批量导入的类
* 数据库批量插入，例如插入100000万条数据
* 最慢的的是statement方式，
* 其次是preparedstatement 因为是预编译过的，速度稍微快点，但是仍然是一条一条的导的
* 最快的是batch批量的操作，因为我们是积累到一定数量的SQL语句，然后才去统一的执行
* 类似以一种缓存的实现。
* Copyright: Copyright (c) 2017
* Company: TongjiUniversity
* @author mdm(computer in lab)
* @date 2017年12月14日
* @version 1.0
*/
package com.tongji.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

/**  
* Title: BatchTest 
* Description:  这是一个测试大量数据插入的类，用batch操作。
* @author mdm(computer in lab)  
* @date 2017年12月14日  
*/
public class BatchTest {
	@Test
	public void testBatch() {
		Connection connection=null;
		PreparedStatement preparedStatement=null;
	    String sql=null;
	    try {
			connection=JDBCTools.getConnection();
			//开启事务
			JDBCTools.beginTx(connection);
			
			sql="insert into emp values(?,?)";
			preparedStatement=connection.prepareStatement(sql);
			//测试开始的时候的时间。
			long begin=System.currentTimeMillis();
			for(int i=0;i<10000;i++) {
				preparedStatement.setInt(1, i+1);
				preparedStatement.setInt(2, i+2);
				//积累SQL
				preparedStatement.addBatch();
				//积累到一定程度统一的执行一次，并且清空原先积累的SQL
				if((i+1)%300==0) {
					preparedStatement.executeBatch();
					preparedStatement.clearBatch();
				}		
			}
			//这步骤很重要
			//若执行的条数不是批量数值的整数倍，还需要额外执行一次
			if(100000%300 != 0) {
				preparedStatement.executeBatch();
				preparedStatement.clearBatch();
			}
			//测试终止时的时间
			long end =System.currentTimeMillis();
			//显示：插入这些数据用了多长时间。
			System.out.println("Time:"+(end - begin));
			//提交事务
			JDBCTools.commit(connection);
		} catch (Exception e) {
			e.printStackTrace();
			JDBCTools.rollback(connection);
		} finally {
			JDBCTools.releaseDB(null, preparedStatement, connection);
		}
	}

	
}
