package model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
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
    private String latitude;
    private String longitude;

    public StepsEntity() {
    }

    public StepsEntity(IndividualEntity individual, Date day) {
        this.individual = individual;
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
