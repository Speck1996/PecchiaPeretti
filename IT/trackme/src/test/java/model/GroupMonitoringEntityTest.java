package model;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class GroupMonitoringEntityTest {

    private String name = "name";
    private Timestamp ts = new Timestamp(27492818);
    private UpdateFrequency frequency = UpdateFrequency.SEMESTER;
    private short views = 2;
    private String location = "ROMA";
    private byte ageMin = 18;
    private byte ageMax = 35;
    private Sex sex = Sex.FEMALE;
    private String country = "ITALY";
    private ThirdPartyEntity tp = new ThirdPartyEntity("tp", "tp@mail.com", "name", "surname", "admin");

    @Test
    public void testConstructor() {
        GroupMonitoringEntity group = new GroupMonitoringEntity(name, ts, frequency, views, location, ageMin, ageMax, sex, country, tp);

        assertEquals(name, group.getPK().getName());
        assertEquals(ts, group.getTs());
        assertEquals(frequency, group.getFrequency());
        assertEquals(views, group.getViews());
        assertEquals(location, group.getLocation());
        assertEquals(ageMin, (byte) group.getAgeMin());
        assertEquals(ageMax, (byte) group.getAgeMax());
        assertEquals(sex, group.getSex());
        assertEquals(country, group.getCountry());
        assertEquals(tp, group.getThirdParty());
    }

}