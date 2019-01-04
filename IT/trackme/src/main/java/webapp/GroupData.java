package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Named
public class GroupData {

    @EJB
    private GroupRequestManager requestManager;

    private String usernameTP;
    private String nameReq;
    private GroupMonitoringEntity groupMonitoring;

    private List<BloodPressureEntity> bloodData;
    private List<HeartbeatEntity> heartData;
    private List<SleepTimeEntity> sleepData;
    private List<StepsEntity> stepsData;

    private boolean bloodError;
    private boolean heartError;
    private boolean sleepError;
    private boolean stepsError;


    @PostConstruct
    public void init() {
        usernameTP = AuthenticationUtils.getUsernameByCookiesMap(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap());

        Map<String, String> parameters = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        String param = parameters.get(Requests.NAME_PARAM);
        if(param == null) {
            System.out.println("nameRew == null");
            return;
        }

        nameReq = param;

        groupMonitoring = requestManager.getRequest(usernameTP, nameReq);

        if((groupMonitoring.getViews() & View.BLOOD_PRESSURE) > 0) {
            bloodData = requestManager.getBloodPressureData(groupMonitoring);

            if(bloodData == null || bloodData.size() == 0)
                bloodError = true;
        }

        if((groupMonitoring.getViews() & View.HEARTBEAT) > 0) {
            heartData = requestManager.getHeartBeatData(groupMonitoring);

            if(heartData == null || heartData.size() == 0)
                heartError = true;
        }

        if((groupMonitoring.getViews() & View.SLEEP_TIME) > 0) {
            sleepData = requestManager.getSleepTimeData(groupMonitoring);

            if(sleepData == null || sleepData.size() == 0)
                sleepError = true;
        }

        if((groupMonitoring.getViews() & View.STEPS) > 0) {
            stepsData = requestManager.getStepsData(groupMonitoring);

            if(stepsData == null || stepsData.size() == 0)
                stepsError = true;
        }


    }


    public String getUsernameTP() {
        return usernameTP;
    }

    public String getNameReq() {
        return nameReq;
    }

    public boolean isBloodError() {
        return bloodError;
    }

    public void setBloodError(boolean bloodError) {
        this.bloodError = bloodError;
    }

    public boolean isHeartError() {
        return heartError;
    }

    public void setHeartError(boolean heartError) {
        this.heartError = heartError;
    }

    public boolean isSleepError() {
        return sleepError;
    }

    public void setSleepError(boolean sleepError) {
        this.sleepError = sleepError;
    }

    public boolean isStepsError() {
        return stepsError;
    }

    public void setStepsError(boolean stepsError) {
        this.stepsError = stepsError;
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

    public List<BloodPressureEntity> getBloodPressureData() {
        return bloodData;
    }

    public List<HeartbeatEntity> getHeartBeatData() {
        return heartData;
    }

    public List<SleepTimeEntity> getSleepTimeData() {
        return sleepData;
    }

    public List<StepsEntity> getStepsData() {
        return stepsData;
    }

}
