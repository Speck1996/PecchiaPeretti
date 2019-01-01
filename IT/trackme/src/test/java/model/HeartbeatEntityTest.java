package model;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class HeartbeatEntityTest {

    private static final double DELTA = 1e-15;
    private String taxcode = "ABCD";
    private Timestamp ts = new Timestamp(4000);
    private IndividualEntity individual = new IndividualEntity();
    private short value = 4;
    private double lat = 3.3;
    private double longi = 4.4;



    @Test
    public void testConstruction() {
        individual.setTaxcode(taxcode);
        HeartbeatEntity heartbeat = new HeartbeatEntity(individual, ts, value, lat, longi);

        assertEquals(individual, heartbeat.getIndividual());
        assertEquals(value, heartbeat.getValue());
        assertEquals(lat, heartbeat.getLatitude(), DELTA);
        assertEquals(longi, heartbeat.getLongitude(), DELTA);

        assertEquals(individual.getTaxcode(), heartbeat.getPK().getIndividual());
        assertEquals(ts, heartbeat.getPK().getTs());
    }

}