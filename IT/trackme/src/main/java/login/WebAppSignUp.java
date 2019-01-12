package login;

import model.ThirdPartyEntity;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.Iterator;

/**
 * Managed Bean responsible for the signup of the third parties.
 */
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
    private String errorMsg;

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

    /**
     * Check if there was an error in the username field
     * @return The name of the css class for error if an error occurred, null otherwise
     */
    public String getErrorUsername() {
        return AuthenticationUtils.fieldError(usernameInput);
    }

    public UIComponent getEmailInput() {
        return emailInput;
    }

    public void setEmailInput(UIComponent emailInput) {
        this.emailInput = emailInput;
    }

    /**
     * Check if there was an error in the email field
     * @return The name of the css class for error if an error occurred, null otherwise
     */
    public String getErrorEmail() {
        return AuthenticationUtils.fieldError(emailInput);
    }

    public UIComponent getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(UIComponent passwordInput) {
        this.passwordInput = passwordInput;
    }

    /**
     * Check if there was an error in the password field
     * @return The name of the css class for error if an error occurred, null otherwise
     */
    public String getErrorPassword() {
        return AuthenticationUtils.fieldError(passwordInput);
    }

    public UIComponent getConfirmInput() {
        return confirmInput;
    }

    public void setConfirmInput(UIComponent confirmInput) {
        this.confirmInput = confirmInput;
    }

    /**
     * Check if there was an error in the password confirmation field
     * @return The name of the css class for error if an error occurred, null otherwise
     */
    public String getErrorConfirm() {
        return AuthenticationUtils.fieldError(confirmInput);
    }

    /**
     * Check if an error occurred in form submission.
     * If it is the cast, show an error message on faces
     * @return true if an error occured, false otherwise
     */
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Retrieve the error message
     * @return The error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * Try to signup the third party.
     * @return The redirection page (the same if signup failed)
     */
    public String signup() {
        if(!password.equals(confPassword)) {
            error = true;
            errorMsg = "Passwords do not match";
            return "";
        }

        System.out.println("username: " + username + " email: " + email + " password: " + password);
        ThirdPartyEntity tp = new ThirdPartyEntity(username, email, name, surname, password);
        try {
            signupBean.registerThirdParty(tp);
        } catch (Exception e) {
            error = true;

            System.out.println("Maremma");
            Throwable ee = e.getCause().getCause();

            System.out.println(ee.getMessage());

            if(ee.getMessage().contains("'email'")) {
                errorMsg = "Email already used";
            }
            else if(ee.getMessage().contains("PRIMARY")) {
                errorMsg = "Username already used";
            }
            else
                errorMsg = ee.getMessage();

            return "";
        }

        return "login.xhtml?faces-redirect=true";
    }
}
