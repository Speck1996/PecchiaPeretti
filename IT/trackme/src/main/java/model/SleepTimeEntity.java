package model;

import model.xml.DateAdapter;
import model.xml.TimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

/**
 * Entity for sleep time data
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "SleepTime.requestAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.country like :country and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.requestLocationAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.requestDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.requestLocationDateAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.requestSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.requestLocationSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.requestDateSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.requestLocationDateSexAnonymized",
                query = "SELECT b FROM IndividualEntity i, SleepTimeEntity b WHERE i = b.individual and i.birthDate >= :datemin and i.birthDate <= :datemax and i.country like :country and i.sex = :sex and b.latitude >= :minlat and b.latitude <= :maxlat and b.longitude >= :minlong and b.longitude <= :maxlong and b.id.day <= :lastupdate  ORDER BY b.id.day DESC"),
        @NamedQuery(name = "SleepTime.updateData",
                query = "UPDATE SleepTimeEntity st SET st.value = :sleep, st.latitude = :latitude, st.longitude = :longitude WHERE st.id.day = :day and st.id.individual = :individual"),
        @NamedQuery(name = "SleepTime.requestIndividual", query = "SELECT s FROM SleepTimeEntity s WHERE s.id.individual = :taxcode"),
})
@Table(name = "SleepTime")
public class SleepTimeEntity {
    @EmbeddedId
    private SleepTimeEntityPK id;

    @ManyToOne
    @MapsId("individual")
    @JoinColumn(name = "individual")
    private IndividualEntity individual;

    @Basic
    @Column(name = "value")
    private Time value;

    @Basic
    @Column(name = "latitude")
    private Double latitude;

    @Basic
    @Column(name = "longitude")
    private Double longitude;

    public SleepTimeEntity() {
    }

    public SleepTimeEntity(IndividualEntity individual, Date day, Time value, Double latitude, Double longitude) {
        this.individual = individual;
        this.value = value;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = new SleepTimeEntityPK(individual.getTaxcode(), day);
    }

    public SleepTimeEntityPK getPK() {
        return id;
    }

    @XmlJavaTypeAdapter(DateAdapter.class)
    public Date getDay() {
        return getPK().getDay();
    }

    public void setDay(Date day) {
    }

    @XmlTransient
    public IndividualEntity getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }

    @XmlJavaTypeAdapter(TimeAdapter.class)
    public Time getValue() {
        return value;
    }

    public void setValue(Time value) {
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
        SleepTimeEntity that = (SleepTimeEntity) o;
        return Objects.equals(individual, that.individual) &&
                //Objects.equals(day, that.day) &&
                Objects.equals(value, that.value) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual,/* day,*/ value, latitude, longitude);
    }
}
