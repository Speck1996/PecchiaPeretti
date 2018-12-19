package model;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Individual")
public class IndividualEntity {
    private String taxcode;
    private String username;
    private String name;
    private String surname;
    private String email;
    private Date birthDate;
    private String country;
    private String password;


    private List<MonitoringEntity> monitorings = new ArrayList<>();

    @Id
    @Column(name = "taxcode")
    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "birth_date")
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @OneToMany(mappedBy = "individual")
    public List<MonitoringEntity> getMonitorings() {
        return monitorings;
    }

    private void setMonitorings(List<MonitoringEntity> monitoring) {

    }

    public void addThirdPartyMonitoring(ThirdPartyEntity thirdParty) {
        MonitoringEntity monitoring = new MonitoringEntity(this, thirdParty);
        monitorings.add(monitoring);
        thirdParty.getMonitorings().add(monitoring);
    }

    public void removeThirdPartyMonitoring(ThirdPartyEntity thirdPartyEntity) {
        for(Iterator<MonitoringEntity> iterator = monitorings.iterator(); iterator.hasNext(); ) {
            MonitoringEntity monitoring = iterator.next();

            if(monitoring.getIndividual().equals(this) && monitoring.getThirdParty().equals(thirdPartyEntity)) {
                iterator.remove();
                monitoring.getThirdParty().getMonitorings().remove(monitoring);
                monitoring.setIndividual(null);
                monitoring.setThirdParty(null);
            }
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndividualEntity that = (IndividualEntity) o;
        return Objects.equals(taxcode, that.taxcode) &&
                Objects.equals(username, that.username) &&
                Objects.equals(name, that.name) &&
                Objects.equals(surname, that.surname) &&
                Objects.equals(email, that.email) &&
                Objects.equals(birthDate, that.birthDate) &&
                Objects.equals(country, that.country) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taxcode, username, name, surname, email, birthDate, country, password);
    }
}
