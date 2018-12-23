package manager.geocoding;

import org.junit.Test;

import java.sql.SQLOutput;

import static org.junit.Assert.*;


public class GeocoderImplTest {
    private static final double DELTA = 1e-15;
    private String demoCity = "Roma";
    //Molise doesn't exists
    private String demoRegion = "Molise";

    @Test
    public void getLocation() {

        Geocoder demoGeocoder = new GeocoderImpl();

        assertEquals(demoGeocoder.getLocation("Milano Città Studi").getLatitude(),45.4770557,DELTA);
        assertEquals(demoGeocoder.getLocation("Milano Città Studi").getLongitude(),9.2265746, DELTA);

        System.out.println("Testing: " +  demoCity);
        System.out.println(demoGeocoder.getLocation(demoCity).getBoxNorth());
        System.out.println(demoGeocoder.getLocation(demoCity).getBoxSouth());
        System.out.println(demoGeocoder.getLocation(demoCity).getBoxWest());
        System.out.println(demoGeocoder.getLocation(demoCity).getBoxEast());

        System.out.println("Testing: " +  demoRegion);
        System.out.println(demoGeocoder.getLocation(demoRegion).getBoxNorth());
        System.out.println(demoGeocoder.getLocation(demoRegion).getBoxSouth());
        System.out.println(demoGeocoder.getLocation(demoRegion).getBoxWest());
        System.out.println(demoGeocoder.getLocation(demoRegion).getBoxEast());


        assertEquals(demoGeocoder.getLocation("Roma").getLatitude(),41.894802, DELTA);
        assertEquals(demoGeocoder.getLocation("Roma").getLongitude(),12.4853384, DELTA);

        assertNull(demoGeocoder.getLocation("qwertmcaonvsjbdhjsbawaksaslij"));
        assertNull(demoGeocoder.getLocation(null));
    }
}