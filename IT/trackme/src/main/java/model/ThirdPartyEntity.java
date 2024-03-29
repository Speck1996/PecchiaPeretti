package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity for Third Party registered to trackme services
 */
@XmlRootElement
@Entity
@Table(name = "ThirdParty")
public class ThirdPartyEntity {
    private String username;
    private String email;
    private String name;
    private String surname;
    private String password;


    private List<MonitoringEntity> monitorings = new ArrayList<>();
    private List<GroupMonitoringEntity> groupMonitorings = new ArrayList<>();

    public ThirdPartyEntity() {
    }

    public ThirdPartyEntity(String username, String email, String name, String surname, String password) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
    }

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "thirdParty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<MonitoringEntity> getMonitorings() {
        return monitorings;
    }

    private void setMonitorings(List<MonitoringEntity> monitorings) {
        this.monitorings = monitorings;
    }

    @OneToMany(mappedBy = "thirdParty", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<GroupMonitoringEntity> getGroupMonitorings() {
        return groupMonitorings;
    }

    private void setGroupMonitorings(List<GroupMonitoringEntity> groupMonitorings) {
        this.groupMonitorings = groupMonitorings;
    }

    public GroupMonitoringEntity addGroupMonitorings(String name, Timestamp ts, UpdateFrequency frequency, short views, String location, Byte ageMin, Byte ageMax, Sex sex, String birthCountry) {
        GroupMonitoringEntity monitoring = new GroupMonitoringEntity(name, ts, frequency, views, location, ageMin, ageMax, sex, birthCountry, this);
        groupMonitorings.add(monitoring);

        return monitoring;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThirdPartyEntity that = (ThirdPartyEntity) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, name, surname, password);
    }
}
