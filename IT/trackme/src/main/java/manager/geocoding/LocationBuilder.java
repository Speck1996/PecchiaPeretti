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
    LocationBuilder setLatitude(String latitude);


    /**
     * Method that sets the longitude for the location to be built, with the given one
     * @param  longitude string containing longitude coordinates
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setLongitude(String longitude);

    /**
     * Method that sets the northeast coordinates of the box containing the location to be built, with the given one
     * @param  northeast string containing northeast coordinates
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setNortheast(String northeast);

    /**
     * Method that sets the southwest coordinates of the box containing the location to be built, with the given one
     * @param  southwest string containing northeast coordinates
     * @return LocationBuilder, in order to continue the building phase
     */
    LocationBuilder setSouthwest(String southwest);

}
