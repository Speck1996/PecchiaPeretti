package model.xml;

import javax.xml.bind.annotation.XmlValue;

public class XmlTime {
    @XmlValue
    private String time;

    public XmlTime(String time) {
        this.time = time;
    }
}
