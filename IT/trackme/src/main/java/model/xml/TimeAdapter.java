package model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Time;

public class TimeAdapter extends XmlAdapter<XmlTime, Time> {
    @Override
    public Time unmarshal(XmlTime v) throws Exception {
        return null;
    }

    @Override
    public XmlTime marshal(Time v) throws Exception {
        return new XmlTime(v.toString());
    }
}
