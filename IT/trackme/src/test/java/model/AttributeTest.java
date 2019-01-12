package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void getNumericAttributes() {

        String[] attr = {"AGE", "position", "nAmE"};

        assertEquals(Attribute.AGE | Attribute.POSITION | Attribute.NAME, Attribute.getNumericAttributes(attr));
    }
}