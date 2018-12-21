package model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "SleepTime")
//@IdClass(SleepTimeEntityPK.class)
public class SleepTimeEntity {
    @EmbeddedId
    private SleepTimeEntityPK id;

    @ManyToOne
    @MapsId("individual")
    @JoinColumn(name = "individual")
    private IndividualEntity individual;
    //private Date day;
    private Time value;
    private String latitude;
    private String longitude;

    public SleepTimeEntity() {
    }

    public SleepTimeEntity(IndividualEntity individual, Date day) {
        this.individual = individual;
        this.id = new SleepTimeEntityPK(individual.getTaxcode(), day);
    }

    public SleepTimeEntityPK getId() {
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
    public Time getValue() {
        return value;
    }

    public void setValue(Time value) {
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
