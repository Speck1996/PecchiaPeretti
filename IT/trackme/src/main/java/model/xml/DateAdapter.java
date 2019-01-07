package model.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Date;

public class DateAdapter extends XmlAdapter<XmlDate, Date> {
    @Override
    public Date unmarshal(XmlDate v) throws Exception {
        return null;
    }

    @Override
    public XmlDate marshal(Date v) throws Exception {
        return new XmlDate(v.toString());
    }
}
