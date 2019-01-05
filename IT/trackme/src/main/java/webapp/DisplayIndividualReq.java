package webapp;

import model.RequestStatus;

import java.sql.Timestamp;

public class DisplayIndividualReq {
    private String name;
    private Timestamp ts;
    private RequestStatus status;
    private String link;
    private boolean subscribed;
    private boolean accepted;

    public DisplayIndividualReq() {
    }

    public DisplayIndividualReq(String name, Timestamp ts, RequestStatus status, String taxcode, boolean subscribed, boolean accepted) {
        this.name = name;
        this.ts = ts;
        this.status = status;
        this.link = Requests.TAXCODE_PARAM + "=" + taxcode + "&" + Requests.NAME_PARAM + "=" + name;
        this.subscribed = subscribed;
        this.accepted = accepted;
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
}