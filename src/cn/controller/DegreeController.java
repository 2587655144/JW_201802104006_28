package cn.controller;

import cn.domain.Degree;
import cn.service.DegreeService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {

    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //请使用以下JSON测试增加功能
    //{"description":"id为5的新学位","id":5,"no":"05","remarks":""}
    //请使用以下JSON测试修改功能
    //{"description":"修改1号学位","id":1,"no":"05","remarks":""}

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
        String degree_json = JSONUtil.getJSON(request);

        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        System.out.println(degreeToAdd);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Degree对象
        try {
            DegreeService.getInstance().add(degreeToAdd);
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
            DegreeService.getInstance().delete(id);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "删除成功");
            message.put("data", null);
        } catch (SQLException e) {
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
        String degree_json = JSONUtil.getJSON(request);
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            DegreeService.getInstance().update(degreeToAdd);
            message.put("statusCode", "200");
            message.put("message", "更新成功");
            message.put("data", null);
        } catch (SQLException e) {
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
        //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
        if(id_str == null){
            try {
                responseDegrees(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseDegree(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //响应一个学位对象
    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        Degree degree = DegreeService.getInstance().find(id);
        String degree_json = JSON.toJSONString(degree);
        //响应
        response.getWriter().println(degree_json);
    }
    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);
        //响应
        response.getWriter().println(degrees_json);
    }

}
