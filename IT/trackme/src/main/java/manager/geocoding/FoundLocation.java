package manager.geocoding;



/***
 * This class stores a location, with longitude, latitude, name, and northeast, sudwest coordinates of the box including the area,
 * when the location refers to a district
 * @Author Stefano P.
 */
public class FoundLocation {

    /**
     * String containing the latitude value of the location centre point
     */
    private String latitude;

    /**
     * String containing the longitude value of the location centre point
     */
    private String longitude;

    /**
     * String containing the northeast coordinates of the box containing the location
     */
    private String northeast;

    /**
     * String containing the southwest coordinates of the box containing the location
     */
    private String southwest;

    /**
     * String containing the name of the location
     */
    private String name;

    /**
     * Empty constructor, builder design pattern used to create object of this class
     */
    public FoundLocation() {

    }

    /**
     * Getter for the latitude value
     * @return the latitude value of the location
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Getter for the longitude value
     * @return the longitude value of the location
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Setter for the latitude
     * @param latitude used to fill the latitude attribute
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * Setter for the longitude
     * @param longitude used to fill the latitude attribute
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * Setter for the longitude
     * @param northeast used to fill the latitude attribute
     */
    public void setNortheast(String northeast) {
        this.northeast = northeast;
    }

    /**
     * Setter for the longitude
     * @param southwest used to fill the latitude attribute
     */
    public void setSouthwest(String southwest) {
        this.southwest = southwest;
    }

    /**
     * Setter for the longitude
     * @param name used to fill the latitude attribute
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the northeast coordinates
     * @return the northeast coordinates of the location
     */
    public String getNortheast() {
        return northeast;
    }

    /**
     * Getter for the southwest coordinates
     * @return the southwest coordinates of the location
     */
    public String getSouthwest() {
        return southwest;
    }

    /**
     * Getter for the name value of the location
     * @return the name value of the location
     */
    public String getName() {
        return name;
    }
}
