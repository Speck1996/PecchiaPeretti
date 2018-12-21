package model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Heartbeat")
//@IdClass(HeartbeatEntityPK.class)
public class HeartbeatEntity {

    @EmbeddedId
    private HeartbeatEntityPK id;

    @ManyToOne
    @MapsId("individual")
    @JoinColumn(name = "individual")
    private IndividualEntity individual;
    //private Timestamp ts;

    private short value;
    private String latitude;
    private String longitude;

    public HeartbeatEntity() {
    }

    public HeartbeatEntity(IndividualEntity individual, Timestamp ts) {
        this.individual = individual;
        //this.ts = ts;
        this.id = new HeartbeatEntityPK(individual.getTaxcode(), ts);
    }

    public HeartbeatEntityPK getPK() {
        return id;
    }


    public IndividualEntity getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }


//    @Id
//    @MapsId("ts")
//    @Column(name = "ts")
//    public Timestamp getTs() {
//        return ts;
//    }
//
//    public void setTs(Timestamp ts) {
//        this.ts = ts;
//    }

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
        HeartbeatEntity that = (HeartbeatEntity) o;
        return value == that.value &&
                Objects.equals(individual, that.individual) &&
                //Objects.equals(ts, that.ts) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual,/* ts,*/ value, latitude, longitude);
    }
}
