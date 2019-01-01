package model.anonymized;

import java.sql.Timestamp;

public class HeartbeatAnonymized {
    private Timestamp ts;
    private short value;

    public HeartbeatAnonymized(Timestamp ts, short value) {
        this.ts = ts;
        this.value = value;
    }

    public Timestamp getTs() {
        return ts;
    }

    public short getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HeartbeatAnonymized{" +
                "ts=" + ts +
                ", value=" + value +
                '}';
    }
}
