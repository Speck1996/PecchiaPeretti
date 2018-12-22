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

        location.setName(tmpLoc.getName());
        location.setLatitude(tmpLoc.getLatitude());
        location.setLongitude(tmpLoc.getLongitude());
        location.setNortheast(tmpLoc.getNortheast());
        location.setSouthwest(tmpLoc.getSouthwest());

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
    public LocationBuilder setLatitude(String latitude) {
        tmpLoc.setLatitude(latitude);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setLongitude(String longitude) {
        tmpLoc.setLongitude(longitude);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setNortheast(String northeast) {
        tmpLoc.setNortheast(northeast);
        return this;
    }

    /**
     * {@inheritDoc}
     * @return LocationBuilder building the FoundLocation requested
     */
    @Override
    public LocationBuilder setSouthwest(String southwest) {
        tmpLoc.setSouthwest(southwest);
        return this;
    }
}
