package webapp;

import login.AuthenticationUtils;

import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Managed Bean responsible for the homepage
 */
@Named
public class HomeWebApp {

    public String retrieveUsername() {
        return AuthenticationUtils.getUsernameByCookiesMap(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());
    }
}
