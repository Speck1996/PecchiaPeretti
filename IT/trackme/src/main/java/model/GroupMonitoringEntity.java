package model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Entity for the accepted group data requests
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "GroupMonitoring.findByTPandName", query = "SELECT m FROM GroupMonitoringEntity m WHERE m.id.thirdParty = :usernameTP and m.id.name = :name")
})
@Table(name = "GroupMonitoring")
public class GroupMonitoringEntity {

    @EmbeddedId
    private GroupMonitoringEntityPK id;

    @ManyToOne
    @MapsId("thirdParty")
    @JoinColumn(name = "third_party")
    private ThirdPartyEntity thirdParty;

    //private int id;
    //private String name;
    @Basic
    @Column(name = "ts")
    private Timestamp ts;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private UpdateFrequency frequency;

    @Basic
    @Column(name = "views")
    private short views;

    @Basic
    @Column(name = "location")
    private String location;

    @Basic
    @Column(name = "age_min")
    private Byte ageMin;

    @Basic
    @Column(name = "age_max")
    private Byte ageMax;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    @Basic
    @Column(name = "birth_country")
    private String country;


    public GroupMonitoringEntity() {
    }

    GroupMonitoringEntity(String name, Timestamp ts, UpdateFrequency frequency, short views, String location, Byte ageMin, Byte ageMax, Sex sex, String country, ThirdPartyEntity thirdParty) {
        this.id = new GroupMonitoringEntityPK(thirdParty.getUsername(), name);

        this.ts = ts;
        this.frequency = frequency;
        this.views = views;
        this.location = location;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.sex = sex;
        this.country = country;
        this.thirdParty = thirdParty;
    }

    public GroupMonitoringEntityPK getPK() {
        return id;
    }

    private void setPK(GroupMonitoringEntityPK id) {
        this.id = id;
    }


    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public UpdateFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(UpdateFrequency frequency) {
        this.frequency = frequency;
    }

    public short getViews() {
        return views;
    }

    public void setViews(short views) {
        this.views = views;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Byte getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(Byte ageMin) {
        this.ageMin = ageMin;
    }

    public Byte getAgeMax() {
        return ageMax;
    }

    public void setAgeMax(Byte ageMax) {
        this.ageMax = ageMax;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    @Override
    public String toString() {
        return "GroupMonitoringEntity{" +
                "id=" + id +
                ", thirdParty=" + thirdParty +
                ", ts=" + ts +
                ", frequency=" + frequency +
                ", views=" + views +
                ", location='" + location + '\'' +
                ", ageMin=" + ageMin +
                ", ageMax=" + ageMax +
                ", sex=" + sex +
                ", country='" + country + '\'' +
                '}';
    }
}
