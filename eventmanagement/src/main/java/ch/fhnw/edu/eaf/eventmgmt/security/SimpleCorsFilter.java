package ch.fhnw.edu.eaf.eventmgmt.security;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lukasschonbachler on 22.05.17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

    public SimpleCorsFilter() {
        System.out.println("SimpleCorsFilter init");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("SimpleCorsFilter doFilter");

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;


        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8100");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, PATCH, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, x-xsrf-token");


        System.out.println("SimpleCorsFilter method " + request.getMethod());

        //if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
        //    response.setStatus(HttpServletResponse.SC_OK);
        //} else {
        chain.doFilter(req, res);
        //}
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}