package model.xml;

import model.BloodPressureEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper for a list of blood pressure data.
 * Used for xml marshalling.
 */
@XmlRootElement(name = "BloodPressureData")
public class BloodPressureListAnonymized {
    @XmlElement(name = "tuple")
    private List<BloodPressureEntity> entities;

    public BloodPressureListAnonymized() {
    }

    public BloodPressureListAnonymized(List<BloodPressureEntity> entities) {
        this.entities = entities;
    }
}
