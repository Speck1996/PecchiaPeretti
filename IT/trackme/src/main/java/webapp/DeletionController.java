package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import manager.IndividualRequestManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.Map;

@Named
public class DeletionController {

    @EJB
    GroupRequestManager groupRequestManager;

    @EJB
    IndividualRequestManager individualRequestManager;

    private String usernameTP;
    private String nameReq;
    private String taxcode;

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

        System.out.println("tp: " + usernameTP + " name: " + nameReq + " taxcode: " + taxcode);

    }

    public String getUsernameTP() {
        return usernameTP;
    }

    public void setUsernameTP(String usernameTP) {
        this.usernameTP = usernameTP;
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

    public String delete() {

        if(nameReq != null) {
            if(taxcode != null && !taxcode.equals(""))
                individualRequestManager.deleteRequest(taxcode, usernameTP);
            else
                groupRequestManager.deleteRequest(usernameTP, nameReq);
        }

        return "requests.xhtml?faces-redirect=true";
    }
}
