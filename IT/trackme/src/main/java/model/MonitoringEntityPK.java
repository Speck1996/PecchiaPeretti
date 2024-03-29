package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * Primary key class for individual data requests
 * @see MonitoringEntity
 */
@Embeddable
public class MonitoringEntityPK implements Serializable {
    private String individual;
    private String thirdParty;

    public MonitoringEntityPK() {
    }

    public MonitoringEntityPK(String individual, String thirdParty) {
        System.out.println("Building pk: " + individual + " " + thirdParty);
        this.individual = individual;
        this.thirdParty = thirdParty;
    }

    @Column(name = "individual")
    @Id
    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    @Column(name = "third_party")
    @Id
    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoringEntityPK that = (MonitoringEntityPK) o;
        return Objects.equals(individual, that.individual) &&
                Objects.equals(thirdParty, that.thirdParty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual, thirdParty);
    }
}
