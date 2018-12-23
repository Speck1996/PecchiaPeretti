package model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = "requestAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country"),
        @NamedQuery(name = "requestLocationAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "requestDateAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country"),
        @NamedQuery(name = "requestLocationDateAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "requestSexAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex"),
        @NamedQuery(name = "requestLocationSexAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
        @NamedQuery(name = "requestDateSexAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex"),
        @NamedQuery(name = "requestLocationDateSexAnonymizedSteps",
                query = "SELECT new model.anonymized.StepsAnonymized(b.id.day, b.value) FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong"),
})
@Table(name = "Steps")
//@IdClass(StepsEntityPK.class)
public class StepsEntity {

    @EmbeddedId
    private StepsEntityPK id;

    @ManyToOne
    @MapsId("individual")
    @JoinColumn(name = "individual")
    private IndividualEntity individual;
    //private Date day;
    private int value;
    private Double latitude;
    private Double longitude;

    public StepsEntity() {
    }

    public StepsEntity(IndividualEntity individual, Date day, int value, Double latitude, Double longitude) {
        this.individual = individual;
        this.value = value;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = new StepsEntityPK(individual.getTaxcode(), day);
    }

    public StepsEntityPK getId() {
        return id;
    }

    public IndividualEntity getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }

//    @Id
//    @Column(name = "day")
//    public Date getDay() {
//        return day;
//    }
//
//    public void setDay(Date day) {
//        this.day = day;
//    }

    @Basic
    @Column(name = "value")
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
        StepsEntity that = (StepsEntity) o;
        return value == that.value &&
                Objects.equals(individual, that.individual) &&
                //Objects.equals(day, that.day) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual,/* day,*/ value, latitude, longitude);
    }
}
