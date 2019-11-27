package cn.dao;

import cn.domain.Teacher;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}

	public Collection<Teacher> findAll() throws SQLException {
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象
		ResultSet resultSet = statement.executeQuery("select * from teacher");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()){
			Teacher teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("name"),
					ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
			teachers.add(teacher);
		}
		return teachers;
	}

	public Teacher find(Integer id) throws SQLException{
		//声明一个Teacher类型的变量
		Teacher teacher = null;
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteTeacher_sql = "SELECT * FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Teacher对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("name"),
					ProfTitleDao.getInstance().find(resultSet.getInt("proftitle_id")),
					DegreeDao.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}

	public boolean update(Teacher teacher) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDegree_sql = " update teacher set name=?,proftitle_id=?,degree_id=?,department_id=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,teacher.getName());
		preparedStatement.setInt(2,teacher.getTitle().getId());
		preparedStatement.setInt(3,teacher.getDegree().getId());
		preparedStatement.setInt(4,teacher.getDepartment().getId());
		preparedStatement.setInt(5,teacher.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	
	public boolean add(Teacher teacher) throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addTeacher_sql = "insert into teacher(name,proftitle_id,department_id,degree_id) values" +
				" (?,?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addTeacher_sql);
		//为预编译参数赋值
		pstmt.setString(1, teacher.getName());
		pstmt.setInt(2, teacher.getTitle().getId());
		pstmt.setInt(3, teacher.getDepartment().getId());
		pstmt.setInt(4, teacher.getDegree().getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 " + affectedRowNum + " 条记录");
		//关闭资源
		JdbcHelper.close(pstmt, connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}

	public boolean delete(int id) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteTeacher_sql = "DELETE FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		Teacher teacher1 = TeacherDao.getInstance().find(1);
		System.out.println(teacher1);
		teacher1.setName("李四");
		TeacherDao.getInstance().update(teacher1);
		Teacher teacher2 = TeacherDao.getInstance().find(1);
		System.out.println(teacher2.getName());
	}

}
