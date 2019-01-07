package model;

import model.xml.DateAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Date;
import java.util.Objects;

/**
 * Entity for steps data
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Steps.requestAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.requestLocationAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.requestDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.requestLocationDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.requestSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.requestLocationSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.requestDateSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.requestLocationDateSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, StepsEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "Steps.updateData",
                query = "UPDATE StepsEntity step SET step.value = :step, step.latitude = :latitude, step.longitude = :longitude WHERE step.id.day = :day and step.id.individual = :individual"),
        @NamedQuery(name = "Steps.requestIndividual", query = "SELECT s FROM StepsEntity s WHERE s.id.individual = :taxcode"),
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

    @Basic
    @Column(name = "value")
    private int value;

    @Basic
    @Column(name = "latitude")
    private Double latitude;

    @Basic
    @Column(name = "longitude")
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

    public StepsEntityPK getPK() {
        return id;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDay() {
        return getPK().getDay();
    }

    public void setDay() {

    }

    @XmlTransient
    public IndividualEntity getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
