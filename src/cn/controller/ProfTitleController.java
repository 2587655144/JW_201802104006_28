package cn.controller;

import cn.domain.ProfTitle;
import cn.service.ProfTitleService;
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

@WebServlet("/profTitle.ctl")
public class ProfTitleController extends HttpServlet {
    /**
     * http://129.211.41.23:8080/profTitle.ctl
     * 增加一个学位对象：将来自前端请求的JSON对象，增加到数据库表中
     * @param request
     * @param response
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
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        System.out.println(profTitleToAdd);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Department对象
        try {
            ProfTitleService.getInstance().add(profTitleToAdd);
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
     * 删除一个学位对象：根据来自前端请求的id，删除数据库表中id的对应记录
     * @param request
     * @param response
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
            ProfTitleService.getInstance().delete(id);
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
     * 修改一个学位对象：将来自前端请求的JSON对象，更新数据库表中相同id的记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        //设置请求字符编码为UTF-8
        request.setCharacterEncoding("UTF-8");
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        ProfTitle profTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //增加加Degree对象
        try {
            ProfTitleService.getInstance().update(profTitleToAdd);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "更新成功");
            message.put("data", null);
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message", "更新失败");
        }
        //响应
        response.setContentType("html/text;charset=UTF8");

        //响应message到前端
        response.getWriter().println(message);
    }

    /**
     * 响应一个或所有学位对象
     * @param request
     * @param response
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
                responseProfTitles(response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            int id = Integer.parseInt(id_str);
            try {
                responseProfTitle(id, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //响应一个学位对象
    private void responseProfTitle(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找学院
        ProfTitle profTitle = ProfTitleService.getInstance().find(id);
        String proTitle_json = JSON.toJSONString(profTitle);
        //响应
        response.getWriter().println(proTitle_json);
    }
    //响应所有学位对象
    private void responseProfTitles(HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //获得所有学院
        Collection<ProfTitle> profTitles = ProfTitleService.getInstance().getAll();
        String profTitles_json = JSON.toJSONString(profTitles);
        //响应
        response.getWriter().println(profTitles_json);
    }
}
