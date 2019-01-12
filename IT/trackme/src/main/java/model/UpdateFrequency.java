package model;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * The frequency of updates for new data
 * @see GroupMonitoringEntity
 * @see MonitoringEntity
 */
public enum UpdateFrequency {
    WEEK, MONTH, QUARTER, SEMESTER, YEAR;

    /**
     * Get the instance specified by the given string
     * @param freq The string representing a frequency
     * @return The UpdateFrequency instance desired or null if freq has no correspondence
     */
    public static UpdateFrequency getFrequency(String freq) {
        if(freq == null)
            return null;

        freq = freq.toLowerCase();

        switch (freq) {
            case "week":
                return WEEK;
            case "month":
                return MONTH;
            case "quarter":
                return QUARTER;
            case "semester":
                return SEMESTER;
            case "year":
                return YEAR;
            default:
                return null;
        }
    }

    /**
     * Calculate the Timestamp of the last update base on the the given frequency
     * @param requestTS Timestamp of when the request was issued
     * @param freq Frequency of updates
     * @return Timestamp of the last update
     */
    public static Timestamp getLastUpdate(Timestamp requestTS, UpdateFrequency freq) {
        if(freq == null || requestTS == null)
            return requestTS;

        long begin = requestTS.getTime();
        long today = Calendar.getInstance().getTimeInMillis();
        long diff = (today - begin) / (1000 * 60 * 60 * 24);   //difference in days
        long updates = 0;

        switch (freq) {
            case WEEK:
                updates = diff / 7;
                return new Timestamp(begin + updates * 7 * (24 * 60 * 60 * 1000));
            case MONTH:
                updates = diff / 30;
                return new Timestamp(begin + updates * 30 * (24 * 60 * 60 * 1000));
            case QUARTER:
                updates = diff / 91;
                return new Timestamp(begin + updates * 91 * (24 * 60 * 60 * 1000));
            case SEMESTER:
                updates = diff / 182;
                return new Timestamp(begin + updates * 182 * (24 * 60 * 60 * 1000));
            case YEAR:
                updates = diff / 365;
                return new Timestamp(begin + updates * 365 * (24 * 60 * 60 * 1000));
            default:
                return requestTS;
        }


    }
}
