package ch.fhnw.edu.eaf.eventmgmt;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/*
@Component
public class RoleAuthenticatorFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        boolean hasRightRole = true;
        // Abort request if user is not allowed to do action on resource
        if(!hasRightRole){
            HttpServletResponse resp = (HttpServletResponse) res;
            resp.reset();
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg0) throws ServletException {}

}
*/