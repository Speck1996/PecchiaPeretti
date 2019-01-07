package login;

import model.ThirdPartyEntity;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.Iterator;

@Named
public class WebAppSignUp {

    @EJB
    SignUp signupBean;

    private String username;
    private String email;
    private String password;
    private String confPassword;
    private String name;
    private String surname;

    private UIComponent usernameInput;
    private UIComponent emailInput;
    private UIComponent passwordInput;
    private UIComponent confirmInput;

    private boolean error = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public UIComponent getUsernameInput() {
        return usernameInput;
    }

    public void setUsernameInput(UIComponent usernameInput) {
        this.usernameInput = usernameInput;
    }

    public String getErrorUsername() {
        return fieldError(usernameInput);
    }

    public UIComponent getEmailInput() {
        return emailInput;
    }

    public void setEmailInput(UIComponent emailInput) {
        this.emailInput = emailInput;
    }

    public String getErrorEmail() {
        return fieldError(emailInput);
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

    public UIComponent getConfirmInput() {
        return confirmInput;
    }

    public void setConfirmInput(UIComponent confirmInput) {
        this.confirmInput = confirmInput;
    }

    public String getErrorConfirm() {
        return fieldError(confirmInput);
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

    public String signup() {
        if(!password.equals(confPassword)) {
            error = true;
            return "";
        }

        System.out.println("username: " + username + " email: " + email + " password: " + password);
        ThirdPartyEntity tp = new ThirdPartyEntity(username, email, name, surname, password);
        signupBean.registerThirdParty(tp);

        return "login.xhtml?faces-redirect=true";
    }
}
