package webapp;

import model.RequestStatus;
import model.UpdateFrequency;

import java.sql.Timestamp;

public class DisplayIndividualReq {
    private String name;
    private Timestamp ts;
    private RequestStatus status;
    private String link;
    private boolean subscribed;
    private boolean accepted;
    private UpdateFrequency frequency;

    public DisplayIndividualReq() {
    }

    public DisplayIndividualReq(String name, Timestamp ts, RequestStatus status, String taxcode, boolean subscribed, boolean accepted, UpdateFrequency frequency) {
        this.name = name;
        this.ts = ts;
        this.status = status;
        this.link = Requests.TAXCODE_PARAM + "=" + taxcode + "&" + Requests.NAME_PARAM + "=" + name;
        this.subscribed = subscribed;
        this.accepted = accepted;
        this.frequency = frequency;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public UpdateFrequency getFrequency() {
        return frequency;
    }

    public void setFrequency(UpdateFrequency frequency) {
        this.frequency = frequency;
    }
}