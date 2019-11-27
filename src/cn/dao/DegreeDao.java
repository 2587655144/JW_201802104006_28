package cn.dao;

import cn.domain.Degree;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.TreeSet;

public final class DegreeDao {
	private static DegreeDao degreeDao= new DegreeDao();
	private DegreeDao(){}
	public static DegreeDao getInstance(){
		return degreeDao;
	}

	public Collection<Degree> findAll() throws SQLException {
		Collection<Degree> degrees = new TreeSet<Degree>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象
		ResultSet resultSet = statement.executeQuery("select * from degree");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()){
			Degree degree = new Degree(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
			degrees.add(degree);
		}
		return degrees;
	}
	public Degree find(Integer id) throws SQLException{
		Degree degree = null;
		Connection connection = JdbcHelper.getConn();
		String deleteDegree_sql = "select * from degree where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//由于id不能取重复值，故结果集中最多有一条记录
		//若结果集有一条记录，则以当前记录中的id,description,no,remarks值为参数，创建Degree对象
		//若结果集中没有记录，则本方法返回null
		if (resultSet.next()){
			degree = new Degree(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return degree;
	}

	public boolean update(Degree degree) throws SQLException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建预编译语句对象，包装编译后的目标代码
		PreparedStatement preparedStatement = connection.prepareStatement("update degree set description=?,no=?,remarks=? where id=?");
		preparedStatement.setString(1,degree.getDescription());
		preparedStatement.setString(2,degree.getNo());
		preparedStatement.setString(3,degree.getRemarks());
		preparedStatement.setInt(4,degree.getId());
		//执行修改语句
		int affectedRowNum = preparedStatement.executeUpdate();
		System.out.println("修改了 "+affectedRowNum+" 条");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}
	public Degree addWithSP(Degree degree) throws SQLException,ClassNotFoundException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addDegree_sql = "insert into degree(no,description,remarks) values (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1,"04");
		pstmt.setString(2,"硕士");
		pstmt.setString(3,"good");
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了" + affectedRowNum + "条记录");
		//关闭pstmt对象
		pstmt.close();
		//关闭连接
		connection.close();
		return degree;
	}
	public boolean add(Degree degree) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addDegree_sql = "insert into degree(no,description,remarks) values" +
				" (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1, degree.getNo());
		pstmt.setString(2, degree.getDescription());
		pstmt.setString(3, degree.getRemarks());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 " + affectedRowNum + " 条记录");
		//关闭资源
		JdbcHelper.close(pstmt, connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}

	public boolean delete(Degree degree) throws SQLException{
		Collection<Degree> degrees = new TreeSet<Degree>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String deleteDegree_sql="delete from degree where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
		//为预编译参数赋值
		pstmt.setInt(1,degree.getId());
		System.out.println("id"+degree.getId());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 "+affectedRowNum+" 条记录");
		//关闭资源
		JdbcHelper.close(pstmt, connection);
		return degrees.remove(degree);
	}
	//创建main方法，查询数据库中的对象，并输出
	public static void main(String[] args) throws ClassNotFoundException,SQLException{
		//删
		//degreeDao.delete(2);
		//DegreeDao.getInstance().findAll();
		Degree degree1 = DegreeDao.getInstance().find(6);
		System.out.println(degree1);
		degree1.setDescription("硕士");
		DegreeDao.getInstance().update(degree1);
		Degree degree2 = DegreeDao.getInstance().find(6);
		System.out.println(degree2.getDescription());

		Degree degree = new Degree(5,"博士","05","");
		System.out.println(DegreeDao.getInstance().add(degree));

	}
}

