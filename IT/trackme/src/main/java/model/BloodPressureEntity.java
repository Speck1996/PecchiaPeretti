package model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Entity for the blood pressure data
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "BloodPressure.requestAnonymized",
                query = "SELECT b FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "BloodPressure.requestLocationAnonymized",
                query = "SELECT b FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "BloodPressure.requestDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "BloodPressure.requestLocationDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "BloodPressure.requestSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "BloodPressure.requestLocationSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "BloodPressure.requestDateSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong ORDER BY b.id.ts DESC"),
        @NamedQuery(name = "BloodPressure.updateData",
                query = "UPDATE BloodPressureEntity bp SET bp.valueMin = :bloodpressuremin, bp.valueMax = :bloodpressuremax, bp.latitude = :latitude, bp.longitude = :longitude WHERE bp.id.ts = :ts and bp.id.individual = :individual"),
        @NamedQuery(name = "BloodPressure.requestIndividual", query = "SELECT b FROM BloodPressureEntity b WHERE b.id.individual = :taxcode"),

})
@Table(name = "BloodPressure")
//@IdClass(BloodPressureEntityPK.class)
public class BloodPressureEntity {
    @EmbeddedId
    private BloodPressureEntityPK id;

    @ManyToOne
    @MapsId("individual")
    @JoinColumn(name = "individual")
    private IndividualEntity individual;

    @Basic
    @Column(name = "value_min")
    private short valueMin;

    @Basic
    @Column(name = "value_max")
    private short valueMax;

    @Basic
    @Column(name = "latitude")
    private Double latitude;

    @Basic
    @Column(name = "longitude")
    private Double longitude;

    /**
     * Empty constructor
     */
    public BloodPressureEntity() {
    }

    /**
     * Constructor with attribute values
     * @param individual The individual that produce this data
     * @param ts The timestamp of the inserting
     * @param valueMin The value of the blood pressure
     * @param latitude The latitude of the detection
     * @param longitude The longitude of the detection
     */
    public BloodPressureEntity(IndividualEntity individual, Timestamp ts, short valueMin, short valueMax, Double latitude, Double longitude) {
        this.individual = individual;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = new BloodPressureEntityPK(individual.getTaxcode(), ts);
    }

    /**
     * Return the primary key of this data entry
     * @return The primary key
     * @see BloodPressureEntityPK
     */
    public BloodPressureEntityPK getPK() {
        return id;
    }


    /**
     * Return the individual that produce this data
     * @return The individual that produce this data
     */
    public IndividualEntity getIndividual() {
        return individual;
    }

    /**
     * Set the indivudla that produce this data
     * @param individual the indivudla that produce this data
     */
    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }

//    @Id
//    @Column(name = "ts")
//    public Timestamp getTs() {
//        return ts;
//    }
//
//    public void setTs(Timestamp ts) {
//        this.ts = ts;
//    }

    /**
     * Return this entry's value of the blood pressure
     * @return This entry's value of the blood pressure
     */
    public short getValueMin() {
        return valueMin;
    }

    /**
     * Set this entry's value of the blood pressure
     * @param valueMin This entry's value of the blood pressure
     */
    public void setValueMin(short valueMin) {
        this.valueMin = valueMin;
    }


    /**
     * Return this entry's value of the blood pressure
     * @return This entry's value of the blood pressure
     */
    public short getValueMax() {
        return valueMax;
    }

    /**
     * Set this entry's value of the blood pressure
     * @param valueMax This entry's value of the blood pressure
     */
    public void setValueMax(short valueMax) {
        this.valueMax = valueMax;
    }

    /**
     * Return the latitude of this data
     * @return The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Set the latitude of this data
     * @param latitude The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Return the longitude of this data
     * @return The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Set the longitude of this data
     * @param longitude The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodPressureEntity that = (BloodPressureEntity) o;
        return valueMin == that.valueMin &&
                Objects.equals(individual, that.individual) &&
                //Objects.equals(ts, that.ts) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual,/* ts,*/ valueMin, latitude, longitude);
    }

    @Override
    public String toString() {
        return "BloodPressureEntity{" +
                "id=" + id +
                ", value=" + valueMin +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
