package com.xttc.config;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
/**
 * 自定义一个拦截器类
 */

@Component
public class MyInterceptor1 implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 用户请求/admin 开头路径时，判断用户是否登录
        String uri = request.getRequestURI();
        Object loginUser = request.getSession().getAttribute("loginUser");
        Object registerUser = request.getSession().getAttribute("registerUser");
        if((uri.startsWith("/main1")||uri.startsWith("/information")||uri.startsWith("/safe")||uri.startsWith("/selectbook1")
                ||uri.startsWith("/selectsub")||uri.startsWith("/complete")||uri.startsWith("/dingyue")||uri.startsWith("/quxiao")) && loginUser == null)
        {
            response.sendRedirect("/toLoginpage");
            return false;
        }
        if(uri.startsWith("/register1")&&registerUser==null){
            response.sendRedirect("/toLoginpage");
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        // 向 request 域中存放当前年份用于页面动态展示
        request.setAttribute("currentYear", Calendar.getInstance().get(Calendar.YEAR));
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse
            response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
