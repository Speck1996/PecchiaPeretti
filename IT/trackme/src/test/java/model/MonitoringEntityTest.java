package model;

import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class MonitoringEntityTest {

    private ThirdPartyEntity tp = new ThirdPartyEntity("tp", "tp@mail.com", "name", "surname", "admin");
    private IndividualEntity individual = new IndividualEntity("taxcode", "username", "name", "surname", "email", new Date(345678), Sex.MALE, "country", "password");
    private Timestamp ts = new Timestamp(123445);
    private UpdateFrequency freq = UpdateFrequency.MONTH;
    private short views = 6;
    private short attributes = 1;

    @Test
    public void testConstructor() {
        MonitoringEntity monitoring = new MonitoringEntity(individual, tp, ts, freq, views, attributes);

        assertEquals(individual, monitoring.getIndividual());
        assertEquals(tp, monitoring.getThirdParty());
        assertEquals(ts, monitoring.getTs());
        assertEquals(freq, monitoring.getFrequency());
        assertEquals(views, monitoring.getViews());
        assertEquals(attributes, monitoring.getAttributes());

        assertEquals(individual.getTaxcode(), monitoring.getPk().getIndividual());
        assertEquals(tp.getUsername(), monitoring.getPk().getThirdParty());
    }

}