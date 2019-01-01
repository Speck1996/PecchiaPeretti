package model;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Entity for individuals registered to trackme services
 */
@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name = "Individual.findByUsername",
                query = "SELECT i FROM IndividualEntity i WHERE i.username = :username")
})
@Table(name = "Individual")
public class IndividualEntity {
    private String taxcode;
    private String username;
    private String name;
    private String surname;
    private String email;
    //needed for REST easy parsing
    private String birthDayString;
    private Date birthDate;
    private Sex sex;
    private String country;
    private String password;


    private List<MonitoringEntity> monitorings = new ArrayList<>();

    @Transient
    public String getBirthDayString() {
        return birthDayString;
    }

    public void setBirthDayString(String birthDayString) {

        try {
            this.birthDayString = birthDayString;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = formatter.parse(birthDayString);
            java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
            birthDate = sqlStartDate;

        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    public IndividualEntity() {
    }

    public IndividualEntity(String taxcode, String username, String name, String surname, String email, Date birthDate, Sex sex, String country, String password) {
        this.taxcode = taxcode;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthDate = birthDate;
        this.sex = sex;
        this.country = country;
        this.password = password;
    }

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

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    public List<MonitoringEntity> getMonitorings() {
        return monitorings;
    }

    private void setMonitorings(List<MonitoringEntity> monitoring) {

    }

    public void addThirdPartyMonitoring(ThirdPartyEntity thirdParty, Timestamp ts, UpdateFrequency frequency, short views, short attributes) {
        MonitoringEntity monitoring = new MonitoringEntity(this, thirdParty, ts, frequency, views, attributes);
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
    public String toString() {
        return "IndividualEntity{" +
                "taxcode='" + taxcode + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", sex=" + sex +
                ", country='" + country + '\'' +
                ", password='" + password + '\'' +
                ", monitorings=" + monitorings +
                '}';
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
