package fid.platform.master.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 添加Controller对跨域访问的支持
 * @author Drx
 *
 */
@Component  
public class CorsFilter implements Filter {  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
  
    }  
  
    @Override  
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {  
        HttpServletResponse response = (HttpServletResponse) servletResponse;  
        response.setHeader("Access-Control-Allow-Origin", "*");  
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
        response.setHeader("Access-Control-Max-Age", "3600");  
        response.setHeader("Access-Control-Allow-Headers", "Content-type");  
        response.setHeader("Access-Control-Allow-Credentials", "true");  
        filterChain.doFilter(servletRequest, servletResponse);  
    }  
  
    @Override  
    public void destroy() {  
  
    }

} 