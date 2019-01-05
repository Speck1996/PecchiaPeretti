package collector;


/**
 * Class used to receive data from android app clients
 */
public class ReceivedData {

    /**
     *
     */
    private String username;

    /**
     *
     */
    private int heartRate;

    /**
     *
     */
    private String sleep;

    /**
     *
     */
    private int bloodPressureMin;

    private int bloodPressureMax;


    /**
     *
     */
    private int steps;

    /**
     *
     */
    private String latitude;

    /**
     *
     */
    private String longitude;


    /**
     *
     * @return
     */
    public int getHeartRate() {
        return heartRate;
    }

    /**
     *
     * @param heartRate
     */
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    /**
     *
     * @return
     */
    public String getSleep() {
        return sleep;
    }

    /**
     *
     * @param sleep
     */
    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    /**
     *
     * @return
     */
    public int getBloodPressureMin() {
        return bloodPressureMin;
    }

    /**
     *
     * @param bloodPressureMin
     */
    public void setBloodPressureMin(int bloodPressureMin) {
        this.bloodPressureMin = bloodPressureMin;
    }

    /**
     *
     * @return
     */
    public int getBloodPressureMax() {
        return bloodPressureMax;
    }

    /**
     *
     * @param bloodPressureMax
     */
    public void setBloodPressureMax(int bloodPressureMax) {
        this.bloodPressureMin = bloodPressureMax;
    }


    /**
     *
     * @return
     */
    public int getSteps() {
        return steps;
    }


    /**
     *
     * @param steps
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }


    /**
     *
     * @return
     */
    public String getLatitude() {
        return latitude;
    }


    /**
     *
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    /**
     *
     * @return
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }


    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
