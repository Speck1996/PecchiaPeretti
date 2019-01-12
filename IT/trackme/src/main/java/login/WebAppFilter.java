package login;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter that prevents access to webapp's resources to all not authorized requester.
 */
@WebFilter(urlPatterns = "/webapp/*")
public class WebAppFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Cookie[] cookies = req.getCookies();


        boolean found = false;
        if(cookies != null) {
            for (Cookie c : cookies) {

                if (c.getName().equals("auth")) {

                    try {
                        AuthenticationUtils.validateToken(c.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                    filterChain.doFilter(servletRequest, servletResponse);
                    found = true;
                    break;
                }
            }
        }

        if(!found) {
            System.out.println("token not found");
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
