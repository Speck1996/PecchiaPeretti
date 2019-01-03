package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import model.UpdateFrequency;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;

@Named
public class SubscriptionController {

    @EJB
    GroupRequestManager requestManager;

    private String username;
    private String nameReq;
    private String freq;

    @PostConstruct
    public void init() {

        username = AuthenticationUtils.getUsernameByCookies(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());
        String par = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(Requests.NAME_PARAM);
        if(par != null)
            nameReq = par;

    }

    public String getNameReq() {
        return nameReq;
    }

    public void setNameReq(String nameReq) {
        this.nameReq = nameReq;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public void unsubscribe() {

        requestManager.setFrequency(username, nameReq, null);

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("requests.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribe() {
                requestManager.setFrequency(username, nameReq, UpdateFrequency.getFrequency(freq));


        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("requests.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
