package model.anonymized;

import java.sql.Date;
import java.sql.Time;

public class SleepTimeAnonymized {
    private Date day;
    private Time value;

    public SleepTimeAnonymized(Date day, Time value) {
        this.day = day;
        this.value = value;
    }

    public Date getDay() {
        return day;
    }

    public Time getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SleepTimeAnonymized{" +
                "day=" + day +
                ", value=" + value +
                '}';
    }
}
