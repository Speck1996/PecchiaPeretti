package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ViewTest {

    @Test
    public void getNumericViews() {
        String[] views = {"HEART", "steps", "SLeeP"};

        assertEquals(View.HEARTBEAT | View.STEPS | View.SLEEP_TIME, View.getNumericViews(views));
    }
}