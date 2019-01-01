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
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country"),
        @NamedQuery(name = "BloodPressure.requestLocationAnonymized",
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "BloodPressure.requestDateAnonymized",
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country"),
        @NamedQuery(name = "BloodPressure.requestLocationDateAnonymized",
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "BloodPressure.requestSexAnonymized",
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex"),
        @NamedQuery(name = "BloodPressure.requestLocationSexAnonymized",
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "BloodPressure.requestDateSexAnonymized",
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex"),
        @NamedQuery(name = "BloodPressure.requestLocationDateSexAnonymized",
                query = "SELECT new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value) FROM IndividualEntity i, BloodPressureEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
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
    //private Timestamp ts;
    private short value;
    private Double latitude;
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
     * @param value The value of the blood pressure
     * @param latitude The latitude of the detection
     * @param longitude The longitude of the detection
     */
    public BloodPressureEntity(IndividualEntity individual, Timestamp ts, short value, Double latitude, Double longitude) {
        this.individual = individual;
        this.value = value;
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
    @Basic
    @Column(name = "value")
    public short getValue() {
        return value;
    }

    /**
     * Set this entry's value of the blood pressure
     * @param value This entry's value of the blood pressure
     */
    public void setValue(short value) {
        this.value = value;
    }

    /**
     * Return the latitude of this data
     * @return The latitude
     */
    @Basic
    @Column(name = "latitude")
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
    @Basic
    @Column(name = "longitude")
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
