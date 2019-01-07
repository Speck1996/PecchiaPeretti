package model.xml;

import model.SleepTimeEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "SleepTimeData")
public class SleepTimeListAnonymized {
    @XmlElement(name = "tuple")
    private List<SleepTimeEntity> entities;

    public SleepTimeListAnonymized() {
    }

    public SleepTimeListAnonymized(List<SleepTimeEntity> entities) {
        this.entities = entities;
    }
}
