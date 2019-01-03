package model;

/**
 * The frequency of updates for new data
 * @see GroupMonitoringEntity
 * @see MonitoringEntity
 */
public enum UpdateFrequency {
    WEEK, MONTH, QUARTER, SEMESTER, YEAR;

    public static UpdateFrequency getFrequency(String freq) {
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
}
