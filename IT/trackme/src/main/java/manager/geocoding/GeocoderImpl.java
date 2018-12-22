package manager.geocoding;

import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;
import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import fr.dudie.nominatim.model.Address;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GeocoderImpl implements Geocoder {

    /**
     * Parameter needed for nominatimAPI in order to sign the request
     */
    final String signingEmail = "trackme";


    /**
     * {@inheritDoc}
     * @param query The string representing the location to be found
     * @return the location found
     */
    @Override
    public FoundLocation getLocation(String query) {

        //class storing the request
        NominatimSearchRequest searchRequest = new NominatimSearchRequest();

        //includes a bounding box in the Address returned
        searchRequest.setBounded(true);

        //set the query
        searchRequest.setQuery(query);

        //needed to communicate with nominatim server
        HttpClient client = HttpClientBuilder.create().build();

        //makes and stores the request
        NominatimClient request = new JsonNominatimClient(client,signingEmail);

        //Address contains the information needed (latitude/longitute, name, boundingbox)
        List<Address> addresses = new ArrayList<>();

        //making the request
        try {
            addresses = request.search(query);
        }catch (IOException e){
            System.out.println(e);
        }

        //filtering the results, taking the most relevant result
        Address address = addresses.get(0);

        //Builder needed to make the object containing all the location details
        LocationBuilder locBuilder = new LocationBuilderImpl();

        //building the location
        FoundLocation requestedLocation = locBuilder.setName(address.getDisplayName())
                                                    .setLatitude(address.getLatitude())
                                                    .setLongitude(address.getLongitude())
                                                    .setNorthBox(address.getBoundingBox().getNorth())
                                                    .setSouthBox(address.getBoundingBox().getSouth())
                                                    .setEastBox(address.getBoundingBox().getEast())
                                                    .setWestBox(address.getBoundingBox().getWest())
                                                    .build();

        return requestedLocation;
    }

}
