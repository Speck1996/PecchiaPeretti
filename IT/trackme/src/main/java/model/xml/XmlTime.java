package model.xml;

import javax.xml.bind.annotation.XmlValue;

/**
 * Xml time.
 * Used for xml marshalling.
 * @see TimeAdapter
 */
public class XmlTime {
    @XmlValue
    private String time;

    public XmlTime(String time) {
        this.time = time;
    }
}
