package model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "requestAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country"),
        @NamedQuery(name = "requestLocationAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "requestDateAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country"),
        @NamedQuery(name = "requestLocationDateAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "requestSexAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex"),
        @NamedQuery(name = "requestLocationSexAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "requestDateSexAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex"),
        @NamedQuery(name = "requestLocationDateSexAnonymizedHeartbeat",
                query = "SELECT new model.anonymized.HeartbeatAnonymized(b.id.ts, b.value) FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
})
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
    private Double latitude;
    private Double longitude;

    public HeartbeatEntity() {
    }

    public HeartbeatEntity(IndividualEntity individual, Timestamp ts, short value, Double latitude, Double longitude) {
        this.individual = individual;
        this.value = value;
        this.latitude = latitude;
        this.longitude = longitude;
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
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
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
