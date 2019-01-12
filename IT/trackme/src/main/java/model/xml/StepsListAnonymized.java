package model.xml;

import model.StepsEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper for a list of steps data.
 * Used for xml marshalling.
 */
@XmlRootElement(name = "StepsData")
public class StepsListAnonymized {
    @XmlElement(name = "tuple")
    private List<StepsEntity> entities;

    public StepsListAnonymized() {
    }

    public StepsListAnonymized(List<StepsEntity> entities) {
        this.entities = entities;
    }
}
