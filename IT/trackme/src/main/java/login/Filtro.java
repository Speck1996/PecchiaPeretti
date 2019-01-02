package login;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/webapp/*")
public class Filtro implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        Cookie[] cookies = req.getCookies();




        boolean trovato = false;
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
                    trovato = true;
                    break;
                }
            }
        }

        if(!trovato) {
            System.out.println("token not found");
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
