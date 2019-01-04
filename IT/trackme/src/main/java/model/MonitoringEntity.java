package model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Entity for accepted or pending individual data requests
 */
@Entity
@Table(name = "Monitoring")
//@IdClass(MonitoringEntityPK.class)
public class MonitoringEntity {

    @EmbeddedId
    private MonitoringEntityPK id;

//    private String individual;
//    private String thirdParty;

    @ManyToOne
    @MapsId("individual")
    @JoinColumn(name = "individual")
    private IndividualEntity individual;

    @ManyToOne
    @MapsId("thirdParty")
    @JoinColumn(name = "third_party")
    private ThirdPartyEntity thirdParty;

    private String name;
    private Timestamp ts;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "frequency")
    private UpdateFrequency frequency;
    private short views;
    private short attributes;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RequestStatus status;
    //private ThirdPartyEntity thirdPartyByThirdParty;


    public MonitoringEntity() {
    }

    public MonitoringEntity(IndividualEntity individual, ThirdPartyEntity thirdParty, String name, Timestamp ts, UpdateFrequency frequency, short views, short attributes) {
        this.individual = individual;
        this.thirdParty = thirdParty;
        this.name = name;
        this.ts = ts;
        this.frequency = frequency;
        this.views = views;
        this.attributes = attributes;
        this.status = RequestStatus.PENDING;
        this.id = new MonitoringEntityPK(individual.getTaxcode(), thirdParty.getUsername());

        System.out.println("STATUS IN CONSTRUCTOR: " + this.status);
    }

    public MonitoringEntityPK getPk() {
        return id;
    }

    /*@Id
    @Column(name = "individual")
    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    @Id
    @Column(name = "third_party")
    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }
    */


    public IndividualEntity getIndividual() {
        return individual;
    }


    public void setIndividual(IndividualEntity individual) {
        this.individual = individual;
    }


    public ThirdPartyEntity getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdPartyEntity thirdParty) {
        this.thirdParty = thirdParty;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "ts")
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

    @Basic
    @Column(name = "views")
    public short getViews() {
        return views;
    }

    public void setViews(short views) {
        this.views = views;
    }

    @Basic
    @Column(name = "attributes")
    public short getAttributes() {
        return attributes;
    }

    public void setAttributes(short attributes) {
        this.attributes = attributes;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus requestStatus) {
        this.status = requestStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonitoringEntity that = (MonitoringEntity) o;
        return views == that.views &&
                Objects.equals(individual, that.individual) &&
                Objects.equals(thirdParty, that.thirdParty) &&
                Objects.equals(ts, that.ts) &&
                Objects.equals(frequency, that.frequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual, thirdParty, ts, frequency, views);
    }

    /*
    @ManyToOne
    @JoinColumn(name = "third_party", referencedColumnName = "username", nullable = false)
    public ThirdPartyEntity getThirdPartyByThirdParty() {
        return thirdPartyByThirdParty;
    }

    public void setThirdPartyByThirdParty(ThirdPartyEntity thirdPartyByThirdParty) {
        this.thirdPartyByThirdParty = thirdPartyByThirdParty;
    }*/
}
