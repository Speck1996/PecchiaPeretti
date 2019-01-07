package login;

import model.ThirdPartyEntity;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Named
public class WebAppLogin {

    public final static String COOKIE_NAME = "auth";

    private final static String FORM_ID = "loginForm";
    private final static String USERNAME_ID = "username";
    private final static String PASSWORD_ID = "password";

    private UIComponent usernameInput;
    private UIComponent passwordInput;

    private boolean error = false;

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em;

    public UIComponent getUsernameInput() {
        return usernameInput;
    }

    public void setUsernameInput(UIComponent usernameInput) {
        this.usernameInput = usernameInput;
    }

    public String getErrorUsername() {
        return fieldError(usernameInput);
    }

    public UIComponent getPasswordInput() {
        return passwordInput;
    }


    public void setPasswordInput(UIComponent passwordInput) {
        this.passwordInput = passwordInput;
    }

    public String getErrorPassword() {
        return fieldError(passwordInput);
    }

    private String fieldError(UIComponent component) {
        FacesContext context = FacesContext.getCurrentInstance();

        String clientId = component.getClientId(context);
        Iterator<FacesMessage> messages = context.getMessages(clientId);

        while (messages.hasNext()) {
            if (messages.next().getSeverity().compareTo(
                    FacesMessage.SEVERITY_ERROR) >= 0) {
                return "input-error";
            }
        }
        return null;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String login() {


        Map<String, String> parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String username = parameters.get(FORM_ID + ":" + USERNAME_ID);
        String password = parameters.get(FORM_ID + ":" + PASSWORD_ID);

        try {
            authenticate(username, password);
            String token = AuthenticationUtils.issueToken(username);


            //create new cookie and store the token in it
            Map<String, Object> properties = new HashMap<>();
            properties.put("path", "/");
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie(COOKIE_NAME, token, properties);

            error = false;

            //return "/login/securedresource/message?faces-redirect=true";
            String r = "webapp/home.xhtml?faces-redirect=true";
            System.out.println("returning: " + r);
            return r;

        } catch (Exception e) {
            e.printStackTrace();
            error = true;
            return "";
        }
    }

    private void authenticate(String username, String password) throws LoginException {
        ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, username);

        if(tp == null) {
            throw new LoginException("User not found: " + username);
        }

        if(!tp.getPassword().equals(password)) {
            throw new LoginException("Wrong password by user: " + username);
        }
    }
}
