package login;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class WebAppFilterRedirect implements Filter {

    private final String loginPath = "/login.xhtml";
    private final String signupPath = "/signup.xhtml";
    private final String homePath = "webapp/home.xhtml";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("in filter");

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        System.out.println(req.getRequestURI());

        boolean redirect = false;

        if(req.getRequestURI().contains(loginPath) || req.getRequestURI().contains(signupPath)) {
            Cookie[] cookies = req.getCookies();

            if(cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals(WebAppLogin.COOKIE_NAME)) {
                        try {
                            AuthenticationUtils.validateToken(c.getValue());
                            redirect = true;
                        } catch (Exception e) {
                            redirect = false;
                        }
                    }
                }
            }

            if(redirect) {
                HttpServletResponse resp = (HttpServletResponse) servletResponse;
                resp.sendRedirect(homePath);
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
