package model.xml;

import model.HeartbeatEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "HeartbeatData")
public class HeartbeatListAnonymized {
    @XmlElement(name = "tuple")
    private List<HeartbeatEntity> entities;

    public HeartbeatListAnonymized() {
    }

    public HeartbeatListAnonymized(List<HeartbeatEntity> entities) {
        this.entities = entities;
    }
}
