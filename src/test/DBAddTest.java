package test;//201802104006武秋菊
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBAddTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
       //加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
       //url
        String url = "jdbc:mysql://localhost:3306/bysj" +
                "?useUnicode=true&characterEncoding=utf8" +
                "&serverTimezone=Asia/Shanghai";
       //用户名和密码
        String usename = "root";
        String password = "123456";
       //获得连接对象
        Connection connection = DriverManager.getConnection(url,usename,password);
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
    }
}
