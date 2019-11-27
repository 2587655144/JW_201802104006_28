//201802104006武秋菊
package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//@WebFilter(filterName = "Filter 10",urlPatterns = "/*")
public class Filter10_date implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String path = request.getRequestURI();
        //创建时间对象
        Date date = new Date();
        //日期格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("请求资源名称:" + path + ",请求时间:" + sdf.format(date));
        System.out.println("Filter 10 begins");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 10 ends");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
