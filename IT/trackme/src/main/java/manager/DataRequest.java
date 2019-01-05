package manager;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataRequest {
    private String sender;


    private String receiver;

    boolean accepted;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
