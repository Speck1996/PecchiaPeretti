package model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "BloodPressure")
@IdClass(BloodPressureEntityPK.class)
public class BloodPressureEntity {
    private IndividualEntity individual;
    private Timestamp ts;
    private short value;
    private String latitude;
    private String longitude;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "individual")
    public IndividualEntity getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }

    @Id
    @Column(name = "ts")
    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    @Basic
    @Column(name = "value")
    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @Basic
    @Column(name = "latitude")
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude")
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodPressureEntity that = (BloodPressureEntity) o;
        return value == that.value &&
                Objects.equals(individual, that.individual) &&
                Objects.equals(ts, that.ts) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual, ts, value, latitude, longitude);
    }
}
