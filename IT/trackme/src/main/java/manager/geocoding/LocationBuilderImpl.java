package manager.geocoding;

/**
 * Class implementing the LocationBuilder interfaces, that actually builds the FoundLocation
 */
public class LocationBuilderImpl implements LocationBuilder {
    /**
     * Temporally stores the object in the building phase
     */
    private FoundLocation tmpLoc;

    /**
     * Initializing the temporal object
     */
    public LocationBuilderImpl(){
        tmpLoc = new FoundLocation();
    }

    /**
     * {@inheritDoc}
     * @return FoundLocation requested
     */
    @Override
    public FoundLocation build(){
        FoundLocation location = new FoundLocation();

        //filling the location object to be returned
        location.setName(tmpLoc.getName());
        location.setLatitude(tmpLoc.getLatitude());
        location.setLongitude(tmpLoc.getLongitude());
        location.setBoxNorth(tmpLoc.getBoxNorth());
        location.setBoxSouth(tmpLoc.getBoxSouth());
        location.setBoxEast(tmpLoc.getBoxEast());
        location.setBoxWest(tmpLoc.getBoxWest());

        return location;
    }


    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setName(String name){
        tmpLoc.setName(name);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setLatitude(double latitude) {
        tmpLoc.setLatitude(latitude);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setLongitude(double longitude) {
        tmpLoc.setLongitude(longitude);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setNorthBox(double north) {
        tmpLoc.setBoxNorth(north);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setSouthBox(double south) {
        tmpLoc.setBoxSouth(south);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setEastBox(double east) {
        tmpLoc.setBoxEast(east);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setWestBox(double west) {
        tmpLoc.setBoxWest(west);
        return this;
    }
}
