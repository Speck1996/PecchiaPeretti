package model;

/**
 * Definitions of integer constants used for data type viewed by third parties in Group Data Requests and Individual Requests.
 * These constants are used in bitwise operations.
 * In both group and individuals requests, the views attribute is an integer number whose bits indicates which type of data are requested.
 * Each bit corresponds to a type of data, if the bit is 1 that type is requested, it is 0 otherwise.
 * For example, if the attribute views in a request is set equal to 3, it means that the request refers only to Heartbeat and Blood Pressure data,
 * because HEARTBEAT & BLOOD_PRESSURE = 3
 * @see GroupMonitoringEntity
 * @see MonitoringEntity
 */
public class View {
    public static final int HEARTBEAT = 1;
    public static final int BLOOD_PRESSURE = 2;
    public static final int SLEEP_TIME = 4;
    public static final int STEPS = 8;
}
