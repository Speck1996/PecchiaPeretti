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
    public static final int SEX = 1;
    public static final int AGE = 2;
    public static final int COUNTRY = 4;
    public static final int POSITION = 8;
}
