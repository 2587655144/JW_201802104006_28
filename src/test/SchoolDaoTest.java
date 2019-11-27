//201802104006武秋菊
package test;
import cn.dao.SchoolDao;
import cn.domain.School;

import java.sql.SQLException;

public class SchoolDaoTest {
    public static void main(String[] args) throws SQLException,ClassNotFoundException {
        //创建schoolToAdd对象
        School schoolToAdd = new School("管理","12","");
        //创建Dao对象
        SchoolDao schoolDao = new SchoolDao();
        //执行Dao对象的方法
        School addSchool = schoolDao.addWithSP(schoolToAdd);
        //打印添加后返回的对象
        System.out.println(addSchool);
        System.out.println("添加School成功");
    }
}
