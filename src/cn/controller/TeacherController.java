package cn.controller;

import cn.domain.Teacher;
import cn.service.TeacherService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/teacher.ctl")
public class TeacherController extends HttpServlet {

    /**
     * POST,  http://129.211.41.23:8080/department.ctl, 增加学院
     *Administrator
     * 增加一个学院对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        //根据request对象，获得代表参数的JSON字串
        String teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        System.out.println(teacherToAdd);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Department对象
        try {
            TeacherService.getInstance().add(teacherToAdd);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "增加成功");
            message.put("data", null);
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message", "增加失败");
        }

        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * Delete,  http://129.211.41.23:8080/department.ctl, 删除学院
     * Administrator
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //到数据库表中删除对应的学院
        try {
            TeacherService.getInstance().delete(id);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "删除成功");
            message.put("data", null);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            message.put("message", "删除失败");
        }

        //响应
        response.setContentType("html/text;charset=UTF8");
        //响应message到前端
        response.getWriter().println(message);
    }



    /**
     *PUT,  http://129.211.41.23:8080/department.ctl, 修改学院
     * Administrator
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String teacher_json = JSONUtil.getJSON(request);
        Teacher teacherToAdd = JSON.parseObject(teacher_json, Teacher.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            TeacherService.getInstance().update(teacherToAdd);
            message.put("statusCode", "200");
            message.put("message", "更新成功");
            message.put("data", null);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            message.put("message", "更新失败");
        }
        response.setContentType("html/text;charset=UTF8");
        response.getWriter().println(message);
    }

    /**
     * GET,  http://129.211.41.23:8080/department.ctl, 查询学院
     * Administrator
     * @param request 请求对象
     * @param response 响应对象
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        //如果id = null, 表示响应所有专业对象，否则响应id指定的专业对象
        if(id_str == null){
            try {
                responseTeachers(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseTeacher(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //响应一个专业对象
    private void responseTeacher(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找专业
        Teacher teacher = TeacherService.getInstance().find(id);
        String teacher_json = JSON.toJSONString(teacher);
        //响应
        response.getWriter().println(teacher_json);
    }
    //响应所有专业对象
    private void responseTeachers(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有专业
        Collection<Teacher> teachers = TeacherService.getInstance().findAll();
        String teachers_str = JSON.toJSONString(teachers, SerializerFeature.DisableCircularReferenceDetect);
        //响应
        response.getWriter().println(teachers_str);
    }
}
