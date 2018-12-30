package login;

import java.io.Serializable;

/**
 * Class representing user credentials, used to authenticate users
 * @See AuthenticationManager
 */
public class Credentials implements Serializable {

    /**
     * String representing the username
     */
    private String username;

    /**
     * String representing the password
     */
    private String password;

    /**
     * Getter for the username
     * @return the username associated with the credentials
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username
     * @param username used to set credentials username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password
     * @return the password associated with the credentials
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password
     * @param password used to set credentials password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
