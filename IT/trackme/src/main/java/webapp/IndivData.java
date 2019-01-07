package webapp;

import login.AuthenticationUtils;
import manager.IndividualRequestManager;
import model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
public class IndivData {

    @EJB
    IndividualRequestManager requestManager;

    private String usernameTP;
    private String taxcode;
    private MonitoringEntity monitoring;

    @PostConstruct
    public void init() {
        usernameTP = AuthenticationUtils.getUsernameByCookiesMap(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());

        Map<String, String> parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String param = parameters.get(Requests.TAXCODE_PARAM);
        if(param == null) {
            System.out.println("taxcode == null");
            return;
        }

        taxcode = param;

        monitoring = requestManager.getRequest(usernameTP, taxcode);
    }

    public String getNameRequest() {
        return monitoring.getName();
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public boolean wantsBlood() {
        return (monitoring.getViews() & View.BLOOD_PRESSURE) > 0;
    }

    public boolean wantsHeart() {
        return (monitoring.getViews() & View.HEARTBEAT) > 0;
    }

    public boolean wantsSleep() {
        return (monitoring.getViews() & View.SLEEP_TIME) > 0;
    }

    public boolean wantsSteps() {
        return (monitoring.getViews() & View.STEPS) > 0;
    }

    public List<BloodPressureEntity> getBloodPressureData() {
        return requestManager.getBloodPressureData(taxcode);
    }

    public List<HeartbeatEntity> getHeartBeatData() {
        return requestManager.getHeartBeatData(taxcode);
    }

    public List<SleepTimeEntity> getSleepTimeData() {
        return requestManager.getSleepTimeData(taxcode);
    }

    public List<StepsEntity> getStepsData() {
        return requestManager.getStepsData(taxcode);
    }
}
