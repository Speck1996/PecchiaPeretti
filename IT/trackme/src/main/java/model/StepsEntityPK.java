package model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

/**
 * Primary key class for steps entity
 * @see StepsEntity
 */
@Embeddable
public class StepsEntityPK implements Serializable {
    private String individual;
    private Date day;

    public StepsEntityPK() {
    }

    public StepsEntityPK(String individual, Date day) {
        this.individual = individual;
        this.day = day;
    }

    @Column(name = "individual")
    @Id
    public String getIndividual() {
        return individual;
    }

    public void setIndividual(String individual) {
        this.individual = individual;
    }

    @Column(name = "day")
    @Id
    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepsEntityPK that = (StepsEntityPK) o;
        return Objects.equals(individual, that.individual) &&
                Objects.equals(day, that.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individual, day);
    }
}
