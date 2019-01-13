package webapp;

import login.AuthenticationUtils;
import manager.IndividualRequestManager;
import model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * Managed Bean responsible for the display of individual data
 */
@Named
public class IndivData {

    @EJB
    IndividualRequestManager requestManager;

    @PersistenceContext
    EntityManager em;

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

    public String getAttributes() {
            if(monitoring == null)
                return null;

            StringBuilder sb = new StringBuilder();
            short attr = monitoring.getAttributes();
            if(attr != 0) {
                IndividualEntity indiv;

                try {
                    indiv = em.find(IndividualEntity.class, monitoring.getPk().getIndividual());

                    if(indiv == null)
                        return null;

                    if ((attr & Attribute.NAME) != 0)
                        sb.append(" ").append(indiv.getName());

                    if((attr & Attribute.SURNAME) != 0)
                        sb.append(" ").append(indiv.getSurname());

                    if((attr & Attribute.SEX) != 0 && indiv.getSex() != null)
                        sb.append(" ").append(indiv.getSex().toString().toLowerCase());

                    if((attr & Attribute.AGE) != 0 && indiv.getBirthDate() != null)
                        sb.append(" ").append(indiv.getBirthDate().toString());

                    if((attr & Attribute.COUNTRY) != 0)
                        sb.append(" born in ").append(indiv.getCountry());


                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Individual not found: " + monitoring.getPk().getIndividual());
                    return null;
                }


            }

            return sb.toString();


    }
}
