package webapp;

import model.RequestStatus;

import java.sql.Timestamp;

public class DisplayIndividualReq {
    private String name;
    private Timestamp ts;
    private RequestStatus status;

    public DisplayIndividualReq() {
    }

    public DisplayIndividualReq(String name, Timestamp ts, RequestStatus status) {
        this.name = name;
        this.ts = ts;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getTs() {
        return ts;
    }

    public void setTs(Timestamp ts) {
        this.ts = ts;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}