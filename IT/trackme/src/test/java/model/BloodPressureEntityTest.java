package model;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class BloodPressureEntityTest {

    private static final double DELTA = 1e-15;
    private String taxcode = "ABCD";
    private Timestamp ts = new Timestamp(4000);
    private IndividualEntity individual = new IndividualEntity();
    private short valueMin = 4;
    private short valueMax = 10;
    private double lat = 3.3;
    private double longi = 4.4;



    @Test
    public void testConstruction() {
        individual.setTaxcode(taxcode);
        BloodPressureEntity bloodPressure = new BloodPressureEntity(individual, ts, valueMin, valueMax, lat, longi);

        assertEquals(individual, bloodPressure.getIndividual());
        assertEquals(valueMin, bloodPressure.getValueMin());
        assertEquals(lat, bloodPressure.getLatitude(), DELTA);
        assertEquals(longi, bloodPressure.getLongitude(), DELTA);

        assertEquals(individual.getTaxcode(), bloodPressure.getPK().getIndividual());
        assertEquals(ts, bloodPressure.getPK().getTs());
    }

}