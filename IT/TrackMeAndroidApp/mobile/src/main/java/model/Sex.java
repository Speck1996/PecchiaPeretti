package model;

/**
 * Enumeration for the individual's sex attribute
 */
public enum Sex {

    MALE("Mr"), FEMALE("Mrs");

   /**
    * String containing the sex value
    */
    private String sex;

    /**
     * Constructor for the enum
     * @param sex value to be associated to the enum
     */
    Sex(String sex){

        this.sex = sex;
    }

    /**
     * Getter for the string value of  SEX
     * @return the string value of SEX
     */
    public String getString() {

        return sex;
    }

    /**
     * Getter for the sex enum value associated to the given string
     * @param sex string that needs to be converted into the enum
     * @return the sex enum value
     */
    public static Sex getEnum(String sex) {

        switch (sex) {
            case "Mr":
                return MALE;

            case "Mrs":
                return FEMALE;

            default:
                return null;
        }
    }
}

