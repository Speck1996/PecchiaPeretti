package model;

import com.google.gson.annotations.SerializedName;

/**
 * Class representing the third party request
 */
public class ThirdPartyRequest {

    /**
     * Third party's username
     */
    @SerializedName("sender")
    private String sender;

    /**
     * Individual's username
     */
    @SerializedName("receiver")
    private String receiver;

    /**
     * true if the request is accepted false if the is pending (or refused)
     */
    @SerializedName("accepted")
    private boolean accepted;

    /**
     * Getter for the third party username
     * @return third party username associated to the request
     */
    public String getSender() {
        return sender;
    }

    /**
     * Setter for the third party username
     * @param sender third party that will be associated to the request
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Getter for individual username
     * @return individual username
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * Setter for the individual username
     * @param receiver individual username that will be associated to the request
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * Getter for the accepted boolean
     * @return the accepted boolean value
     */
    public boolean isAccepted() {
        return accepted;
    }

    /**
     * Setter for the accepted boolean
     * @param accepted the value that will be associated to the accepted attribute
     */
    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * {@inheritDoc}
     * @return string representing the received data
     */
    @Override
    public String toString() {
        return "ThirdPartyRequest{" +
                "sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", accepted=" + accepted +
                '}';
    }
}
