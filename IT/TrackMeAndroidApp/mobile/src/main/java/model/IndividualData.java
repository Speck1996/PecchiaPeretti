package model;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing the data sent to the server
 */
public class IndividualData {

    /**
     * Username of the individual
     */
    @SerializedName("username")
    private String username;

    /**
     * Heart rate value
     */
    @SerializedName("heartRate")
    private int heartRate;

    /**
     * Sleep time value
     */
    @SerializedName("sleep")
    private String sleepTime;

    /**
     * Steps value
     */
    @SerializedName("steps")
    private int steps;

    /**
     * Blood pressure min value
     */
    @SerializedName("bloodPressureMin")
    private int bloodPressureMin;

    /**
     * Blood pressure max value
     */
    @SerializedName("bloodPressureMax")
    private int bloodPressureMax;

    /**
     * Latitude value
     */
    @SerializedName("latitude")
    private String latitude;

    /**
     * Longitude value
     */
    @SerializedName("longitude")
    private String longitude;


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
     * Getter for the heartbeat
     * @return the heartbeat value
     */
    public int getHeartRate() {
        return heartRate;
    }


    /**
     * Setter for the heartbeat value
     * @param heartRate value to be associated to heartbeat field
     */
    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    /**
     * Getter for the sleeptime
     * @return the sleeptime value
     */
    public String getSleepTime() {
        return sleepTime;
    }

    /**
     * Setter for the sleepTime value
     * @param sleepTime value to be associated to sleepTime field
     */
    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }


    /**
     * Getter for the steps
     * @return the steps value
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Setter for the steps value
     * @param steps value to be associated to steps field
     */
    public void setSteps(int steps) {
        this.steps = steps;
    }


    /**
     * Getter for the bloodPressure min
     * @return the bloodPressure min value
     */
    public int getBloodPressureMin() {
        return bloodPressureMin;
    }

    /**
     * Setter for the bloodPressure value
     * @param bloodPressureMin value to be associated to bloodPressureMin field
     */
    public void setBloodPressureMin(int bloodPressureMin) {
        this.bloodPressureMin = bloodPressureMin;
    }

    /**
     * Getter for the bloodPressure max
     * @return the bloodPressure max value
     */
    public int getBloodPressureMax() {
        return bloodPressureMax;
    }

    /**
     * Setter for the bloodPressure max value
     * @param bloodPressureMax value to be associated to bloodPressureMax field
     */
    public void setBloodPressureMax(int bloodPressureMax) {
        this.bloodPressureMax = bloodPressureMax;
    }

    /**
     * Getter for the Latitude
     * @return the latitude value
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Setter for the latitude
     * @param latitude the latitude value to be associated to the latitude field
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Getter for the Longitude
     * @return the longitude value
     */
    public String getLongitude() {
        return longitude;
    }


    /**
     * Setter for the longitude
     * @param longitude the longitude value to be associated to the longitude field
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
