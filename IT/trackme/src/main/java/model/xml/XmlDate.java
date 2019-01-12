package model.xml;

import javax.xml.bind.annotation.XmlValue;

/**
 * Xml date.
 * Used for xml marshalling.
 * @see DateAdapter
 */
public class XmlDate {
    @XmlValue
    private String date;

    public XmlDate(String date) {
        this.date = date;
    }
}
