package cn.dao;

import cn.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private ProfTitleDao(){}
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}

	public Collection<ProfTitle> findAll() throws SQLException{
		Collection<ProfTitle> profTitles = new TreeSet<ProfTitle>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象
		ResultSet resultSet = statement.executeQuery("select * from proftitle");
		//若结果集仍然有下一条记录，则执行循环体
		while(resultSet.next()){
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
			profTitles.add(profTitle);
		}
		return profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException {
		Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		Statement statement = connection.createStatement();
		//执行SQL查询语句并获得结果集对象（游标指向结果集的开头）
		ResultSet resultSet = statement.executeQuery("select * from proftitle");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//创建ProfTitle对象，根据遍历结果中的id,description,no,remarks值
			ProfTitle profTitle = new ProfTitle(resultSet.getInt("id"),resultSet.getString("description"),resultSet.getString("no"),resultSet.getString("remarks"));
			//向profTitles集合中添加ProfTitle对象
			profTitles.add(profTitle);
		}
		//关闭资源
		JdbcHelper.close(resultSet,statement,connection);
		ProfTitle desiredProfTitle = null;
		for (ProfTitle profTitle : profTitles) {
			if(id.equals(profTitle.getId())){
				desiredProfTitle =  profTitle;
				break;
			}
		}
		return desiredProfTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException {
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateProfTitle_sql = " update proftitle set description=?,no=?,remarks=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateProfTitle_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	public ProfTitle addWithSP(ProfTitle profTitle) throws SQLException,ClassNotFoundException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addDegree_sql = "insert into proftitle(no,description,remarks) values (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addDegree_sql);
		//为预编译参数赋值
		pstmt.setString(1,"04");
		pstmt.setString(2,"副教授");
		pstmt.setString(3,"");
		//执行预编译对象的executeUpdate方法，获取添加的记录行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("添加了" + affectedRowNum + "条记录");
		//关闭pstmt对象
		pstmt.close();
		//关闭连接
		connection.close();
		return profTitle;
	}
	public boolean add(ProfTitle profTitle) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String addProfTitle_sql = "insert into proftitle(no,description,remarks) values" +
				" (?,?,?)";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(addProfTitle_sql);
		//为预编译参数赋值
		pstmt.setString(1, profTitle.getNo());
		pstmt.setString(2, profTitle.getDescription());
		pstmt.setString(3, profTitle.getRemarks());
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("增加了 " + affectedRowNum + " 条记录");
		//关闭资源
		JdbcHelper.close(pstmt, connection);
		//如果影响的行数大于1，则返回true，否则返回false
		return affectedRowNum > 0;
	}

	public static boolean delete(Integer id) throws SQLException {
		Connection connection = JdbcHelper.getConn();
		//创建sql语句，“？”作为占位符
		String deleteProfTitle_sql = "delete from proftitle where id = ?";
		//创建PreparedStatement接口对象，包装编译后的目标代码（可以设置参数，安全性高）
		PreparedStatement pstmt = connection.prepareStatement(deleteProfTitle_sql);
		//为预编译的语句参数赋值
		pstmt.setInt(1,id);
		//执行预编译对象的executeUpdate()方法，获取删除记录的行数
		int affectedRowNum = pstmt.executeUpdate();
		System.out.println("删除了 "+affectedRowNum+" 条");
		//关闭资源
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}

	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		//找到对应id号的一行profTitle
		ProfTitle profTitle1 = ProfTitleDao.getInstance().find(1);
		//打印信息
		System.out.println(profTitle1);
		profTitle1.setDescription("副教授");
		//修改相应属性
		ProfTitleDao.getInstance().update(profTitle1);
		//再次找到该行
		ProfTitle profTitle2 = ProfTitleDao.getInstance().find(1);
		//打印信息
		System.out.println(profTitle2.getDescription());
	}
}

