package webapp;

import login.AuthenticationUtils;

import javax.faces.context.FacesContext;
import javax.inject.Named;


@Named
public class HomeWebApp {

    public String retrieveUsername() {
        return AuthenticationUtils.getUsernameByCookies(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());
    }
}
