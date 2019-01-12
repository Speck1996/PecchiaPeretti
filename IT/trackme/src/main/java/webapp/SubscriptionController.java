package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import manager.IndividualRequestManager;
import model.UpdateFrequency;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.IOException;
import java.util.Map;

/**
 * Managed Bean responsible for subscribe/unsubscribe to a request
 */
@Named
public class SubscriptionController {

    @EJB
    GroupRequestManager groupRequestManager;

    @EJB
    IndividualRequestManager individualRequestManager;

    private String usernameTP;
    private String nameReq = null;
    private String taxcode = null;
    private String freq;

    @PostConstruct
    public void init() {

        usernameTP = AuthenticationUtils.getUsernameByCookiesMap(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());
        Map<String, String> parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();


        if(parameters != null) {
            String par = parameters.get(Requests.NAME_PARAM);

            if (par != null)
                nameReq = par;

            par = parameters.get(Requests.TAXCODE_PARAM);
            if(par != null)
                taxcode = par;
        }
    }

    public String getNameReq() {
        return nameReq;
    }

    public void setNameReq(String nameReq) {
        this.nameReq = nameReq;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    /**
     * Unsubscribe from the request
     */
    public void unsubscribe() {

        if(nameReq != null) {
            if(taxcode != null && !taxcode.equals(""))
                individualRequestManager.setFrequency(usernameTP, taxcode, null);
            else
                groupRequestManager.setFrequency(usernameTP, nameReq, null);
        }

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("requests.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subscribe to the request with the frequency in the form
     */
    public void subscribe() {

        if(nameReq != null) {
            if (taxcode != null && !taxcode.equals(""))
                individualRequestManager.setFrequency(usernameTP, taxcode, UpdateFrequency.getFrequency(freq));
            else
                groupRequestManager.setFrequency(usernameTP, nameReq, UpdateFrequency.getFrequency(freq));
        }

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("requests.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
