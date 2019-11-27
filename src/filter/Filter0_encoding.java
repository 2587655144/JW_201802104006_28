//201802104006武秋菊
package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter(filterName = "Filter 0",urlPatterns = "/*")
public class Filter0_encoding implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String path = request.getRequestURI();
        if (path.contains("/login")){
            System.out.println("url中含有\"/login\"，不设置字符编码");
        }else {
            String method = request.getMethod();
            if (method.equals("post") || method.equals("put")){
                //设置请求字符编码为UTF-8
                request.setCharacterEncoding("UTF-8");
                //设置响应字符编码为UTF-8
                response.setContentType("text/html;charset=UTF-8");
            }else {
                //设置响应字符编码为UTF-8
                response.setContentType("text/html;charset=UTF-8");
            }
        }
        System.out.println("Filter 0 begins");
        //执行其他过滤器，如过滤器已执行完毕，则执行原请求资源
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("Filter 0 ends");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
