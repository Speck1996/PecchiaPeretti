package model;

import com.google.gson.annotations.SerializedName;


/**
 * Class that stores the user registration data
 */
public class Individual {

    /**
    * String that contains the tax code of the individual
     */
    @SerializedName("taxcode")
    private String taxcode;

    /**
     * String that contains the username of the username
     */
    @SerializedName("username")
    private String username;

    /**
     * String that contains the name of the individual
     */
    @SerializedName("name")
    private String name;

    /**
     * String that contains the surname of the individual
     */
    @SerializedName("surname")
    private String surname;

    /**
     * String that contains the email of the individual
     */
    @SerializedName("email")
    private String email;

    /**
     * String that contains the birthdate of the individual
     */
    @SerializedName("birthDayString")
    private String birthDate;

    /**
     * Sex value of the individual
     */
    @SerializedName("sex")
    private Sex sex;

    /**
     * String that contains the birth country of the individual
     */
    @SerializedName("country")
    private String country;

    /**
     * String that contains the password of the individual
     */
    @SerializedName("password")
    private String password;

    /**
     * Getter for the tax code
     * @return tax code string associated to the individual
     */
    public String getTaxcode() {
        return taxcode;
    }

    /**
     * Setter for the tax code
     * @param taxcode to be associated to the individual
     */
    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    /**
     * Getter for the username
     * @return username string associated to the individual
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for the username
     * @param username to be associated to the individual
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter for the name
     * @return name string associated to the individual
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name
     * @param name to be associated to the individual
     */
    public void setName(String name) {
        this.name = name;
    }


     /**
     * Getter for the surname
     * @return surname string associated to the individual
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setter for the surname
     * @param surname to be associated to the individual
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Getter for the email
     * @return email string associated to the individual
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the email
     * @param email to be associated to the individual
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter for the birhdate
     * @return birthdate string associated to the individual
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Setter for the birthdate
     * @param birthDate to be associated to the individual
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Getter for the sex
     * @return sex associated to the individual
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * Setter for the sex
     * @param sex to be associated to the individual
     */
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    /**
     * Getter for the country
     * @return country string associated to the individual
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter for the country
     * @param country to be associated to the individual
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter for the password
     * @return password string associated to the individual
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for the password
     * @param password to be associated to the individual
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     * @return string associated to the object
     */
    @Override
    public String toString() {
        return "Individual{" +
                "taxcode='" + taxcode + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", sex=" + sex +
                ", country='" + country + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
