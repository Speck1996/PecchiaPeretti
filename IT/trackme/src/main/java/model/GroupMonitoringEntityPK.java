package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Primary key class for Group Monitoring entity
 * @see GroupMonitoringEntity
 */
@Embeddable
public class GroupMonitoringEntityPK implements Serializable {

    private String thirdParty;
    private String name;

    public GroupMonitoringEntityPK() {
    }

    public GroupMonitoringEntityPK(String thirdParty, String name) {
        this.thirdParty = thirdParty;
        this.name = name;
    }

    @Column(name = "third_party")
    @Id
    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    @Column(name = "name")
    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMonitoringEntityPK that = (GroupMonitoringEntityPK) o;
        return thirdParty.equals(that.thirdParty) &&
                name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thirdParty, name);
    }
}
