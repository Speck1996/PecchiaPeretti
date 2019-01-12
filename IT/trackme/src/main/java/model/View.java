package model;

import manager.GroupRequestManager;

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
    public static final short HEARTBEAT = 1;
    public static final short BLOOD_PRESSURE = 2;
    public static final short SLEEP_TIME = 4;
    public static final short STEPS = 8;

    /**
     * Transform the array of views (string format) into the corresponding number
     * @param stringsView Array of string representing which views are of interest
     * @return The views in numeric format, as expected by {@link GroupRequestManager}
     */
    public static short getNumericViews(String[] stringsView) {
        short views = 0;

        if(stringsView == null)
            return views;

        for(String v: stringsView) {
            v = v.toLowerCase();
            switch (v) {
                case "steps":
                    views = (short) (views | View.STEPS);
                    break;
                case "sleep":
                    views = (short) (views | View.SLEEP_TIME);
                    break;
                case "heart":
                    views = (short) (views | View.HEARTBEAT);
                    break;
                case "blood":
                    views = (short) (views | View.BLOOD_PRESSURE);
                    break;
                default:
                    System.out.println("Unexpected view: " + v);
            }
        }

        return views;
    }
}
