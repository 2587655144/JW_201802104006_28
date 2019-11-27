package cn.dao;

import cn.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();

	public School addWithSP(School school) throws SQLException,ClassNotFoundException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addSchool_sql = "insert into school(no,description,remarks) values (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译参数赋值
		pstmt.setString(1,"04");
		pstmt.setString(2,"管理");
		pstmt.setString(3,"");
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了" + affectedRowNum + "条记录");
		//关闭pstmt对象
		pstmt.close();
		//关闭连接
		connection.close();
		return school;
	}
	public SchoolDao(){}
	
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Collection<School> findAll() throws SQLException{
		Collection<School> schools = new TreeSet<School>();
		Connection connection = JdbcHelper.getConn();
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from school");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()){
			School school = new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
			schools.add(school);
		}
		return schools;
	}

	public School find(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		String updateDegree_sql = "select * from school where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDegree_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		School school = new School(
				resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks")
		);
		return school;
	}

	public boolean update(School school) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateSchool_sql = " update school set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateSchool_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	
	public boolean add(School school) throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addSchool_sql = "insert into school(no,description,remarks) values" +
				" (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addSchool_sql);
		//为预编译参数赋值
		pstmt.setString(1, school.getNo());
		pstmt.setString(2, school.getDescription());
		pstmt.setString(3, school.getRemarks());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 " + affectedRowNum + " 条记录");
		//关闭资源
		JdbcHelper.close(pstmt, connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}

	//delete方法，根据school的id值，删除数据库中对应的school对象
	public boolean delete(int id) throws ClassNotFoundException,SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteSchool_sql = "DELETE FROM school WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteSchool_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
}
