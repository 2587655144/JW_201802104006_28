package test;//201802104006武秋菊
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction {
    public static void main(String[] args) {
        //将school表的no字段添加唯一性约束
        //alter table school add unique(no);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = JdbcHelper.getConn();
            //关闭自动提交,事务开始
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("insert into school(description,no )values(?,?)");
            preparedStatement.setString(1,"管理");
            preparedStatement.setString(2,"02");
            //执行第一条insert语句
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("insert into school(description,no)values(?,?)");
            //将第二条记录的no字段值也设置为“02”，以违反no字段的唯一性约束
            preparedStatement.setString(1,"土木");
            preparedStatement.setString(2,"22");
            //执行第一条insert语句
            preparedStatement.executeUpdate();
            //提交当前连接所做的操作
            connection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage()+ "\"errorCode = " + e.getErrorCode());
            try{
                //回滚当前连接所作的操作
                if(connection != null){
                    connection.rollback();
                }
            }catch (SQLException e1){
                e1.printStackTrace();
            }
        }finally {
            try{
                //恢复自动提交
                if (connection != null){
                    connection.setAutoCommit(true);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            //关闭资源
            JdbcHelper.close(preparedStatement,connection);
        }
    }
}
