package manager.geocoding;

import org.junit.Test;
import static org.junit.Assert.*;


public class GeocoderImplTest {

    @Test
    public void getLocation() {
        Geocoder demoGeocoder = new GeocoderImpl();

        assertEquals(demoGeocoder.getLocation("Milano Città Studi").getLatitude(),"45.4770557");
        assertEquals(demoGeocoder.getLocation("Milano Città Studi").getLongitude(),"9.2265746");

        System.out.println(demoGeocoder.getLocation("Milano Città Studi").getNortheast());
        System.out.println(demoGeocoder.getLocation("Milano Città Studi").getSouthwest());

        assertEquals(demoGeocoder.getLocation("Roma").getLatitude(),"41.894802");
        assertEquals(demoGeocoder.getLocation("Roma").getLongitude(),"12.4853384");


    }
}