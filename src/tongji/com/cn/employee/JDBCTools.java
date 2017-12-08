package tongji.com.cn.employee;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTools {
	
	
	/**
	 * 执行 SQL 的方法,使用preparedstatement构成重载
	 * 
	 * @param sql: insert, update 或 delete。 而不包含 select
	 */
	public static void update(String sql,Object ... args )
	{//用可变参数更好，不然程序里面还要封装成数组才能用。这样写的话，你爱传几个就传几个
		Connection connection=null;
		PreparedStatement preparedStatement=null;
		try {
			connection=JDBCTools.getConnection();
			preparedStatement=connection.prepareStatement(sql);
			
			for(int i=0;i<args.length;i++)
			{
				preparedStatement.setObject(i+1, args[i]);
			}
			
			preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			JDBCTools.release(preparedStatement, connection);
		}
	}
	
	/**
	 * 执行 SQL 的方法
	 * 
	 * @param sql: insert, update 或 delete。 而不包含 select
	 */
	public static void update(String sql) {
		Connection connection = null;
		Statement statement = null;

		try {
			// 1. 获取数据库连接
			connection = getConnection();

			// 2. 调用 Connection 对象的 createStatement() 方法获取 Statement 对象
			statement = connection.createStatement();

			// 4. 发送 SQL 语句: 调用 Statement 对象的 executeUpdate(sql) 方法
			statement.executeUpdate(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 5. 关闭数据库资源: 由里向外关闭.
			release(statement, connection,null);
		}
	}
	
	
	
	/*更加高级的版本：关闭Statement,connection,resultSet*/
	public static void release(Statement statement,Connection connection,ResultSet resultSet)
	{
		if(resultSet!=null)
		 {
			 try {
				resultSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		
		if(statement!=null)
		 {
			 try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 
		 if(connection!=null)
		 {
			 try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
	}

	/**
	 * 关闭 Statement 和 Connection
	 * @param statement
	 * @param conn
	 */
	 public static void release(Statement statement,Connection connection)
	 {
		 if(statement!=null)
		 {
			 try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
		 
		 if(connection!=null)
		 {
			 try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }
					 
	 }
	 
	
	
	/**
	 * 1. 获取连接的方法. 通过读取配置文件从数据库服务器获取一个连接.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		// 1. 准备连接数据库的 4 个字符串.
		// 1). 创建 Properties 对象
		Properties properties = new Properties();

		// 2). 获取 jdbc.properties 对应的输入流
		InputStream in = 
				JDBCTools.class.getClassLoader().getResourceAsStream(
				"jdbc.properties");

		// 3). 加载 2） 对应的输入流
		properties.load(in);

		// 4). 具体决定 user, password 等4 个字符串.
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String driver = properties.getProperty("driver");

		// 2. 加载数据库驱动程序(对应的 Driver 实现类中有注册驱动的静态代码块.)
		Class.forName(driver);

		// 3. 通过 DriverManager 的 getConnection() 方法获取数据库连接.
		return DriverManager.getConnection(jdbcUrl, user, password);
	}
}
