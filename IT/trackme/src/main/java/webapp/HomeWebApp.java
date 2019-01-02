package webapp;

import login.AuthenticationUtils;
import login.WebAppLogin;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import java.util.Map;

@Named
public class HomeWebApp {

    public String retrievUsername() {
        String username = null;

        Map<String, Object> cookies = FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap();

        if(cookies != null) {
            Cookie c = (Cookie) cookies.get(WebAppLogin.COOKIE_NAME);
            String token = c.getValue();
            username = AuthenticationUtils.getUsernameFromToken(token);
        }

        return username;
    }
}
