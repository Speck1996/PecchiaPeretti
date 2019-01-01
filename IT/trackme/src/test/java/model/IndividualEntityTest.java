package model;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.*;

public class IndividualEntityTest {

    private IndividualEntity individual;
    private String taxcode = "ABCDE";
    private String username = "pippo";
    private String name = "nome";
    private String surname = "cognome";
    private String email = "mail@mail.com";
    private Date bd = new Date(1002020);
    private Sex sex = Sex.FEMALE;
    private String country = "Gondor";
    private String password = "password";

    @Before
    public void setup() {

    }


    @Test
    public void testConstructor() {

        individual = new IndividualEntity(taxcode, username, name, surname, email, bd, sex, country, password);

        assertEquals(taxcode, individual.getTaxcode());
        assertEquals(username, individual.getUsername());
        assertEquals(name, individual.getName());
        assertEquals(surname, individual.getSurname());
        assertEquals(email, individual.getEmail());
        assertEquals(bd, individual.getBirthDate());
        assertEquals(sex, individual.getSex());
        assertEquals(country, individual.getCountry());
        assertEquals(password, individual.getPassword());

        assertEquals(0, individual.getMonitorings().size());
    }

    @Test
    public void testNewMonitoring() {
        individual = new IndividualEntity(taxcode, username, name, surname, email, bd, sex, country, password);
        ThirdPartyEntity tp = new ThirdPartyEntity("tp", "tp@mail.com", "name", "surname", "admin");

        Timestamp ts = new Timestamp(202030);
        UpdateFrequency frequency = UpdateFrequency.QUARTER;
        short views = 3;
        short attributes = 7;

        individual.addThirdPartyMonitoring(tp, ts, frequency, views, attributes);

        boolean found = false;
        for(MonitoringEntity monitoring: individual.getMonitorings()) {
            assertEquals(individual, monitoring.getIndividual());

            if(monitoring.getThirdParty().equals(tp)) {
                found = true;
                assertEquals(ts, monitoring.getTs());
                assertEquals(frequency, monitoring.getFrequency());
                assertEquals(views, monitoring.getViews());
                assertEquals(attributes, monitoring.getAttributes());

                assertEquals(RequestStatus.PENDING, monitoring.getStatus());

                boolean found2 = false;
                for(MonitoringEntity monitoring2: tp.getMonitorings()) {
                    if(monitoring2.equals(monitoring)) {
                        found2 = true;
                    }

                    assertTrue(found2);
                }
            }

        }

        assertTrue(found);
    }

}