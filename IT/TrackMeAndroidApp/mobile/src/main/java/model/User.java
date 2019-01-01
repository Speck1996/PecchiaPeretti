package model;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing the user, used to communicate credentials with the server
 */
public class User {
    /**
     * String containing the username value
     */
    @SerializedName("username")
     private String username;

    /**
     * String containing the password value
     */
    @SerializedName("password")
    String password;

    /**
     * Getter for the username
     * @return the username value
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username value
     * @param username value to be associated to the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the password
     * @return the password value
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password value
     * @param password value to be associated to the password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
