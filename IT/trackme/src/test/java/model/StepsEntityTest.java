package model;

import org.junit.Test;

import java.sql.Date;
import java.sql.Time;

import static org.junit.Assert.*;

public class StepsEntityTest {
    private static final double DELTA = 1e-15;
    private String taxcode = "ABCD";
    private Date day = new Date(400000);
    private IndividualEntity individual = new IndividualEntity();
    private int value = 2000;
    private double lat = 3.3;
    private double longi = 4.4;



    @Test
    public void testConstruction() {
        individual.setTaxcode(taxcode);
        StepsEntity steps = new StepsEntity(individual, day, value, lat, longi);

        assertEquals(individual, steps.getIndividual());
        assertEquals(value, steps.getValue());
        assertEquals(lat, steps.getLatitude(), DELTA);
        assertEquals(longi, steps.getLongitude(), DELTA);

        assertEquals(individual.getTaxcode(), steps.getPK().getIndividual());
        assertEquals(day, steps.getPK().getDay());
    }

}