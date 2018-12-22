package manager.geocoding;

/**
 * Interface used to build the FoundLocation, in a fancy way
 */
public interface LocationBuilder {

    /**
     * Method that actually returns the location built
     * @return FoundLocation
     * @see FoundLocation
     */
    FoundLocation build();



    /**
     * Method that sets the name for the location to be built, with the given one
     * @param name string containing name of the location
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setName(String name);


    /**
     * Method that sets the latitude for the location to be built, with the given one
     * @param latitude string containing latitude coordinates
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setLatitude(double latitude);


    /**
     * Method that sets the longitude for the location to be built, with the given one
     * @param  longitude string containing longitude coordinates
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setLongitude(double longitude);

    /**
     * Method that sets the north coordinate of the box containing the location to be built, with the given one
     * @param  north value of the north coordinate to be set
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setNorthBox(double north);

    /**
     * Method that sets the south coordinate of the box containing the location to be built, with the given one
     * @param  south value of the north coordinate to be set
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setSouthBox(double south);

    /**
     * Method that sets the east coordinate of the box containing the location to be built, with the given one
     * @param  east value of the north coordinate to be set
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setEastBox(double east);

    /**
     * Method that sets the west coordinate of the box containing the location to be built, with the given one
     * @param  west value of the north coordinate to be set
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setWestBox(double west);

}
