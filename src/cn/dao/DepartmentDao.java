//201802104006武秋菊
package cn.dao;

import cn.domain.Department;
import cn.service.DepartmentService;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DepartmentDao {

	private static DepartmentDao departmentDao=new DepartmentDao();

	private DepartmentDao(){}

	public static DepartmentDao getInstance(){
		return departmentDao;
	}

	public Department addWithSP(Department department) throws SQLException,ClassNotFoundException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addDepartment_sql = "insert into department(no,description,remarks) values (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDepartment_sql);
		//为预编译参数赋值
		pstmt.setString(1,"04");
		pstmt.setString(2,"信管");
		pstmt.setString(3,"good");
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了" + affectedRowNum + "条记录");
		//关闭pstmt对象
		pstmt.close();
		//关闭连接
		connection.close();
		return department;
	}

	public Collection<Department> findAll() throws SQLException {
		//创建集合departments
		Collection<Department> departments = new TreeSet<Department>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象
		ResultSet resultSet = statement.executeQuery("select * from department");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()){
			Department department = new Department(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolDao.getInstance().find(resultSet.getInt("school_id")));
			departments.add(department);
		}
		return departments;
	}
	//按学院查询专业
	public Collection<Department> findAllBySchool(Integer schoolId) throws SQLException {
		//创建集合departments
		Collection<Department> departments = new TreeSet<Department>();
		DepartmentService.getInstance().getAll();
		Connection connection = JdbcHelper.getConn();
		String selectDepart_sql = "select * from department where school_id = ?";
		PreparedStatement pstmt = connection.prepareStatement(selectDepart_sql);
		pstmt.setInt(1,schoolId);
		ResultSet resultSet = pstmt.executeQuery();
		while(resultSet.next()){
		Department department = new Department(
					resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"),
					SchoolDao.getInstance().find(resultSet.getInt("school_id")));
					departments.add(department);
		}
		return departments;
	}
	public Department find(Integer id) throws SQLException {
		DepartmentService.getInstance().getAll();
		Connection connection = JdbcHelper.getConn();
		String updateDepartment_sql = "select * from department where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(updateDepartment_sql);
		pstmt.setInt(1,id);
		ResultSet resultSet = pstmt.executeQuery();
		resultSet.next();
		Department department = new Department(
				resultSet.getInt("id"),
				resultSet.getString("description"),
				resultSet.getString("no"),
				resultSet.getString("remarks"),
				SchoolDao.getInstance().find(resultSet.getInt("school_id"))
		);
		return department;
	}
	public boolean add(Department department) throws SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String addDepartment_sql = "INSERT INTO department (description,no,remarks,school_id) VALUES"+" (?,?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(addDepartment_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getSchool().getId());
		//执行预编译语句，获取添加记录行数并赋值给affectedRowNum
		int affectedRowNum=preparedStatement.executeUpdate();
		System.out.println("添加了"+affectedRowNum+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean update(Department department) throws SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDepartment_sql = " update department set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDepartment_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,department.getDescription());
		preparedStatement.setString(2,department.getNo());
		preparedStatement.setString(3,department.getRemarks());
		preparedStatement.setInt(4,department.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean delete(Integer id) throws SQLException {
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteDepartment_sql = "DELETE FROM department WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDepartment_sql);
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
		Department department1 = DepartmentDao.getInstance().find(2);
		System.out.println(department1);
		department1.setDescription("土木");
		DepartmentDao.getInstance().update(department1);
		Department department2 = DepartmentDao.getInstance().find(2);
		System.out.println(department2.getDescription());
	}
}

