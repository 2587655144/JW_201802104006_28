package test;//201802104006武秋菊
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBDeleteTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //url为数据库连接字串，其中jdbc为协议，mysql为子协议
        // localhost:3306为数据库服务器的地址和窗口
        String url = "jdbc:mysql://localhost:3306/bysj" +
                "?useUnicode=true&characterEncoding=utf8" +
                "&serverTimezone=Asia/Shanghai";
        //用户名和密码
        String usename = "root";
        String password = "123456";
        //获得连接对象
        Connection connection = DriverManager.getConnection(url,usename,password);
        //创建sql语句
        String deleteDegree_sql = "delete from Degree where id = ?";
        //在该连接上创建预编译语句对象
        PreparedStatement pstmt = connection.prepareStatement(deleteDegree_sql);
        //为预编译参数赋值
        pstmt.setInt(1,5);
        //执行预编译对象的executeUpdate方法，获取删除的记录行数
        int affectedRowNum = pstmt.executeUpdate();
        System.out.println("删除了" + affectedRowNum + "条记录");
        //关闭pstmt对象
        pstmt.close();
        //关闭connection对象，只要建立了连接，就必须关闭
        connection.close();
    }
}
