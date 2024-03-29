package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Primary key class for heartbeat entity
 * @see HeartbeatEntity
 */
@Embeddable
public class HeartbeatEntityPK implements Serializable {
    private String individual;
    private Timestamp ts;

    public HeartbeatEntityPK() {
        System.out.println("Building void");
    }

    public HeartbeatEntityPK(String individual, Timestamp ts) {
        this.individual = individual;
        this.ts = ts;
    }

    @Column(name = "individual")
    @Id
    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    @Column(name = "ts")
    @Id
    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeartbeatEntityPK that = (HeartbeatEntityPK) o;
        return Objects.equals(individual, that.individual) &&
                Objects.equals(ts, that.ts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual, ts);
    }
}
