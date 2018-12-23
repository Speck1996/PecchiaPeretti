package model.anonymized;

import java.sql.Timestamp;

public class BloodPressureAnonymized {

    private Timestamp ts;
    private short value;

    public BloodPressureAnonymized(Timestamp ts, short value) {
        this.ts = ts;
        this.value = value;
    }

    public Timestamp getTs() {
        return ts;
    }

    public short getValue() {
        return value;
    }
}
