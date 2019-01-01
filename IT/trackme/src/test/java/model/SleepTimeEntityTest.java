package model;

import org.junit.Test;

import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;

import static org.junit.Assert.*;

public class SleepTimeEntityTest {
    private static final double DELTA = 1e-15;
    private String taxcode = "ABCD";
    private Date day = new Date(400000);
    private IndividualEntity individual = new IndividualEntity();
    private Time value = new Time(2000);
    private double lat = 3.3;
    private double longi = 4.4;



    @Test
    public void testConstruction() {
        individual.setTaxcode(taxcode);
        SleepTimeEntity sleepTime = new SleepTimeEntity(individual, day, value, lat, longi);

        assertEquals(individual, sleepTime.getIndividual());
        assertEquals(value, sleepTime.getValue());
        assertEquals(lat, sleepTime.getLatitude(), DELTA);
        assertEquals(longi, sleepTime.getLongitude(), DELTA);

        assertEquals(individual.getTaxcode(), sleepTime.getPK().getIndividual());
        assertEquals(day, sleepTime.getPK().getDay());
    }

}