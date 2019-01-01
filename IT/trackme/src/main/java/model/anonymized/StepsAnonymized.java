package model.anonymized;

import java.util.Date;

public class StepsAnonymized {
    private Date day;
    private int value;

    public StepsAnonymized(Date day, int value) {
        this.day = day;
        this.value = value;
    }

    public Date getDay() {
        return day;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StepsAnonymized{" +
                "day=" + day +
                ", value=" + value +
                '}';
    }
}
