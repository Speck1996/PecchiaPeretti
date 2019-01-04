package model;


/**
 * Definitions of integer constants used for individual's attributes viewed by third parties in Individual Requests.
 * These constants are used in bitwise operations.
 * In individuals requests, the attributes attribute is an integer number whose bits indicates which individual's attributes are requested.
 * Each bit corresponds to an idividual's attribute, if the bit is 1 that attribute is requested, it is 0 otherwise.
 * For example, if the attribute attributes in a request is set equal to 3, it means that the request refers only to Sex and Age attributes,
 * because SEX & AGE = 3
 * @see MonitoringEntity
 */
public class Attribute {
    public static final short SEX = 1;
    public static final short AGE = 2;
    public static final short COUNTRY = 4;
    public static final short POSITION = 8;
    public static final short NAME = 16;
    public static final short SURNAME = 32;

    public static short getNumericAttributes(String[] stringAttr) {
        short attributes = 0;

        if(stringAttr == null)
            return attributes;

        for(String a: stringAttr) {
            switch (a) {
                case "name":
                    attributes = (short) (attributes | NAME);
                    break;
                case "surname":
                    attributes = (short) (attributes | SURNAME);
                    break;
                case "sex":
                    attributes = (short) (attributes | SEX);
                    break;
                case "age":
                    attributes = (short) (attributes | AGE);
                    break;
                case "country":
                    attributes = (short) (attributes | COUNTRY);
                    break;
                case "position":
                    attributes = (short) (attributes | POSITION);
                    break;
                default:
                    System.out.println("Unexpected attribute: " + a);
            }
        }

        return attributes;
    }
}
