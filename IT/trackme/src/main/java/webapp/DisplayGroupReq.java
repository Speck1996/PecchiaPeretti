package webapp;

import java.sql.Timestamp;

public class DisplayGroupReq {
    private String name;
    private Timestamp ts;
    private String link;
    private boolean subscribed;

    public DisplayGroupReq() {
    }

    public DisplayGroupReq(String name, Timestamp ts, boolean subscribed) {
        this.name = name;
        this.ts = ts;
        this.link = Requests.NAME_PARAM + "=" + name;
        this.subscribed = subscribed;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean getSubscribed() { ;return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
