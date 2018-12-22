package manager.geocoding;



/***
 * This class stores a location, with longitude, latitude, name, and northeast, sudwest coordinates of the box including the area,
 * when the location refers to a district
 * @Author Stefano P.
 */
public class FoundLocation {

    /**
     * latitude value of the location centre point
     */
    private double latitude;

    /**
     * longitude value of the location centre point
     */
    private double longitude;

    /**
     * north coordinate of the box containing the location
     */
    private double boxNorth;

    /**
     * south coordinate of the box containing the location
     */
    private double boxSouth;

    /**
     * east coordinate of the box containing the location
     */
    private double boxEast;

    /**
     * west coordinate of the box containing the location
     */
    private double boxWest;

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
     * Setter for the latitude
     * @param latitude used to fill the latitude attribute
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Setter for the longitude
     * @param longitude used to fill the latitude attribute
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Getter for the latitude value
     * @return the latitude value of the location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Getter for the longitude value
     * @return the longitude value of the location
     */
    public double getLongitude() {
        return longitude;
    }


    /**
     * Getter for the north coordinate of the box containing the location
     * @return north coordinate value of the box containing the location
     */
    public double getBoxNorth() {
        return boxNorth;
    }


    /**
     * Setter for the north coordinate of the box containing the location
     * @param boxNorth  value of the north coordinate
     */
    public void setBoxNorth(double boxNorth) {
        this.boxNorth = boxNorth;
    }

    /**
     * Getter for the south coordinate of the box containing the location
     * @return south coordinate value of the box containing the location
     */
    public double getBoxSouth() {
        return boxSouth;
    }

    /**
     * Setter for the south coordinate of the box containing the location
     * @param boxSouth  value of the north coordinate
     */
    public void setBoxSouth(double boxSouth) {
        this.boxSouth = boxSouth;
    }

    /**
     * Getter for the east coordinate of the box containing the location
     * @return east coordinate value of the box containing the location
     */
    public double getBoxEast() {
        return boxEast;
    }

    /**
     * Setter for the east coordinate of the box containing the location
     * @param boxEast value of the north coordinate
     */
    public void setBoxEast(double boxEast) {
        this.boxEast = boxEast;
    }

    /**
     * Getter for the west coordinate of the box containing the location
     * @return west coordinate value of the box containing the location
     */
    public double getBoxWest() {
        return boxWest;
    }

    /**
     * Setter for the west coordinate of the box containing the location
     * @param boxWest value of the north coordinate
     */
    public void setBoxWest(double boxWest) {
        this.boxWest = boxWest;
    }


    /**
     * Setter for the longitude
     * @param name used to fill the latitude attribute
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Getter for the name value of the location
     * @return the name value of the location
     */
    public String getName() {
        return name;
    }
}
