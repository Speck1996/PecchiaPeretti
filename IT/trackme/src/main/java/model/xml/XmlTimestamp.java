package model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

public class XmlTimestamp {
    @XmlValue
    String ts;

    public XmlTimestamp(String ts) {
        this.ts = ts;
    }
}
