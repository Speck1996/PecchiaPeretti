package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateFrequencyTest {

    @Test
    public void getFrequency1() {
        assertEquals(UpdateFrequency.MONTH, UpdateFrequency.getFrequency("MonTH"));
    }

    @Test
    public void getFrequency2() {
        assertEquals(UpdateFrequency.YEAR, UpdateFrequency.getFrequency("YEAR"));
    }

    @Test
    public void getFrequency3() {
        assertEquals(UpdateFrequency.SEMESTER, UpdateFrequency.getFrequency("semester"));
    }
}