package model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;

public class TimestampAdapter extends XmlAdapter<XmlTimestamp, Timestamp> {
    @Override
    public Timestamp unmarshal(XmlTimestamp v) throws Exception {
        return null;
    }

    @Override
    public XmlTimestamp marshal(Timestamp v) throws Exception {
        return new XmlTimestamp(v.toString());
    }
}
