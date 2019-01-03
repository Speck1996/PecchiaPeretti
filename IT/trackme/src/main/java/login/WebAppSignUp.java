package login;

import model.ThirdPartyEntity;

import javax.ejb.EJB;
import javax.inject.Named;

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
