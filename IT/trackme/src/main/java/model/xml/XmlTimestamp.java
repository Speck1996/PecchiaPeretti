package model.xml;

import javax.xml.bind.annotation.XmlValue;

/**
 * Xml timestamp.
 * Used for xml marshalling.
 * @see TimestampAdapter
 */
public class XmlTimestamp {
    @XmlValue
    String ts;

    public XmlTimestamp(String ts) {
        this.ts = ts;
    }
}
