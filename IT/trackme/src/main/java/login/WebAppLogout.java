package login;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Managed Bean responsible for the logout of the third parties.
 */
@Named
public class WebAppLogout {

    /**
     * Logout the third party and redirect to the login page.
     */
    public void logout() {

        System.out.println("logging out");

        Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        if(cookies != null) {
            Cookie c = (Cookie) cookies.get(WebAppLogin.COOKIE_NAME);

            if(c != null) {
                c.setMaxAge(0);
                c.setPath("/");
                response.addCookie(c);
            }

        }

        try {
            response.sendRedirect("../login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
