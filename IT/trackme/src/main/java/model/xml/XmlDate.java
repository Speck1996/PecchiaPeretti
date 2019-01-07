package model.xml;

import javax.xml.bind.annotation.XmlValue;

public class XmlDate {
    @XmlValue
    private String date;

    public XmlDate(String date) {
        this.date = date;
    }
}
