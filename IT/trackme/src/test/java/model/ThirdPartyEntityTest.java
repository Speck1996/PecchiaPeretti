package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThirdPartyEntityTest {
    private String username = "pluto";
    private String name = "nome";
    private String surname = "cognome";
    private String email = "mail@mail.com";
    private String password = "password";


    @Test
    public void testConstructor() {

        ThirdPartyEntity tp = new ThirdPartyEntity(username, email, name, surname, password);

        assertEquals(username, tp.getUsername());
        assertEquals(name, tp.getName());
        assertEquals(surname, tp.getSurname());
        assertEquals(email, tp.getEmail());
        assertEquals(password, tp.getPassword());

        assertEquals(0, tp.getMonitorings().size());

    }

}