package model;

import model.xml.TimestampAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Entity for the heartbeat data
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Heartbeat.requestAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "Heartbeat.requestLocationAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "Heartbeat.requestDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "Heartbeat.requestLocationDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "Heartbeat.requestSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "Heartbeat.requestLocationSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "Heartbeat.requestDateSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "Heartbeat.requestLocationDateSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, HeartbeatEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.ts <= :lastupdate  ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "HeartBeat.updateData",
                query = "UPDATE HeartbeatEntity hb SET hb.value = :heartbeat, hb.latitude = :latitude, hb.longitude = :longitude WHERE hb.id.ts = :ts and hb.id.individual = :individual"),
        @NamedQuery(name = "Heartbeat.requestIndividual", query = "SELECT h FROM HeartbeatEntity h WHERE h.id.individual = :taxcode"),
})
@Table(name = "Heartbeat")
public class HeartbeatEntity {

    @EmbeddedId
    private HeartbeatEntityPK id;

    @ManyToOne
    @MapsId("individual")
    @JoinColumn(name = "individual")
    private IndividualEntity individual;

    @Basic
    @Column(name = "value")
    private short value;

    @Basic
    @Column(name = "latitude")
    private Double latitude;

    @Basic
    @Column(name = "longitude")
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

    @XmlJavaTypeAdapter(TimestampAdapter.class)
    public Timestamp getTs() {
        return getPK().getTs();
    }

    public void setTs(Timestamp ts) {
    }

    @XmlTransient
    public IndividualEntity getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }


    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    @XmlTransient
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @XmlTransient
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
