package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import model.GroupMonitoringEntity;
import model.View;
import model.anonymized.BloodPressureAnonymized;
import model.anonymized.HeartbeatAnonymized;
import model.anonymized.SleepTimeAnonymized;
import model.anonymized.StepsAnonymized;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Named
public class GroupData {

    @EJB
    private GroupRequestManager requestManager;

    private String usernameTP;
    private String nameReq;
    private GroupMonitoringEntity groupMonitoring;


    @PostConstruct
    public void init() {
        usernameTP = AuthenticationUtils.getUsernameByCookies(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());

        Map<String, String> parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        nameReq = parameters.get(Requests.NAME_PARAM);
        if(nameReq == null) {
            //TODO
            return;
        }

        groupMonitoring = requestManager.getRequest(usernameTP, nameReq);
    }


    public String getUsernameTP() {
        return usernameTP;
    }

    public String getNameReq() {
        return nameReq;
    }

    public boolean wantsBlood() {
        return (groupMonitoring.getViews() & View.BLOOD_PRESSURE) > 0;
    }

    public boolean wantsHeart() {
        return (groupMonitoring.getViews() & View.HEARTBEAT) > 0;
    }

    public boolean wantsSleep() {
        return (groupMonitoring.getViews() & View.SLEEP_TIME) > 0;
    }

    public boolean wantsSteps() {
        return (groupMonitoring.getViews() & View.STEPS) > 0;
    }

    public List<BloodPressureAnonymized> getBloodPressureData() {
        return requestManager.getBloodPressureData(groupMonitoring);
    }

    public List<HeartbeatAnonymized> getHeartBeatData() {
        System.out.println("in");return requestManager.getHeartBeatData(groupMonitoring);
    }

    public List<SleepTimeAnonymized> getSleepTimeData() {
        return requestManager.getSleepTimeData(groupMonitoring);
    }

    public List<StepsAnonymized> getStepsData() {
        return requestManager.getStepsData(groupMonitoring);
    }

}
