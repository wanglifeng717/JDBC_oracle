package tongji.com.cn.DAO;


import java.sql.Date;

import org.junit.Test;

public class DAOTest 
{
	DAO dao=new DAO();
	@Test
	public  void testGetForValue() {
		String sql="select last_name from employees where department_id=?";
		Object object=dao.getForValue(sql, 80);
		System.out.println(object);
	}
	@Test
	public void testUpdate()
	{
		String sql="insert into employees(employee_id,last_name,hire_date) values(?,?,?) ";
		dao.Update(sql,45,"ÕÅâùÄþ",new Date(new java.util.Date().getTime()));
		
	}
	@Test
	public  void testGet() {
		String sql="select last_name from employees where department_id=?";
		System.out.println(dao.get(Employee.class, sql, 80));
		

	}
	@Test
	public void testgetForList() {
		String sql="select last_name from employees where department_id=?";
		System.out.println(dao.getForList(Employee.class, sql, 80));
	}
}
