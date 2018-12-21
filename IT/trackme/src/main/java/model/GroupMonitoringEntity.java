package model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "GroupMonitoring")
public class GroupMonitoringEntity {
    private int id;
    private Timestamp ts;
    private UpdateFrequency frequency;
    private short views;
    private String location;
    private Byte ageMin;
    private Byte ageMax;
    private Sex sex;
    private String country;
    private ThirdPartyEntity thirdParty;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ts")
    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    public UpdateFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(UpdateFrequency frequency) {
        this.frequency = frequency;
    }

    @Basic
    @Column(name = "views")
    public short getViews() {
        return views;
    }

    public void setViews(short views) {
        this.views = views;
    }

    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "age_min")
    public Byte getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Byte ageMin) {
        this.ageMin = ageMin;
    }

    @Basic
    @Column(name = "age_max")
    public Byte getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Byte ageMax) {
        this.ageMax = ageMax;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "birth_country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @ManyToOne
    @JoinColumn(name = "third_party")
    public ThirdPartyEntity getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdPartyEntity thirdParty) {
        this.thirdParty = thirdParty;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMonitoringEntity that = (GroupMonitoringEntity) o;
        return id == that.id &&
                views == that.views &&
                Objects.equals(ts, that.ts) &&
                Objects.equals(frequency, that.frequency) &&
                Objects.equals(location, that.location) &&
                Objects.equals(ageMin, that.ageMin) &&
                Objects.equals(ageMax, that.ageMax) &&
                Objects.equals(sex, that.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ts, frequency, views, location, ageMin, ageMax, sex);
    }
}
