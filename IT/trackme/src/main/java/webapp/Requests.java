package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import manager.IndividualRequestManager;
import model.GroupMonitoringEntity;
import model.MonitoringEntity;
import model.RequestStatus;
import model.UpdateFrequency;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed Bean responsible for the display of the requests
 */
@Named
public class Requests {

    public final static String NAME_PARAM = "n";
    public final static String TAXCODE_PARAM = "tc";

    @EJB
    GroupRequestManager groupRequestManager;

    @EJB
    IndividualRequestManager individualRequestManager;

    public List<DisplayGroupReq> retrieveGroupRequests() {
        List<GroupMonitoringEntity> monitorings = groupRequestManager.getRequests(AuthenticationUtils.getUsernameByCookiesMap(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()));

        if(monitorings != null) {
            List<DisplayGroupReq> results = new ArrayList<>();

            for(GroupMonitoringEntity m: monitorings) {
                //String display = m.getPK().getName() + " - Requested on: " + m.getTs().toString();
                DisplayGroupReq display = new DisplayGroupReq(m.getPK().getName(), m.getTs(), m.getFrequency() != null, m.getFrequency());
                results.add(display);
            }

            return results;
        }

        return null;

    }

    public List<DisplayIndividualReq> retrieveIndividualRequests() {
        List<MonitoringEntity> monitorings = individualRequestManager.getRequests(AuthenticationUtils.getUsernameByCookiesMap(FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap()));

        if(monitorings == null)
            return null;

        List<DisplayIndividualReq> results = new ArrayList<>();

        for(MonitoringEntity m: monitorings) {
            //String display = m.getPk().getIndividual() + " - Requested on: " +m.getTs() + " status: " + m.getStatus();
            DisplayIndividualReq display = new DisplayIndividualReq(m.getName(), m.getTs(), m.getStatus(), m.getPk().getIndividual(), m.getFrequency() != null, m.getStatus() == RequestStatus.ACCEPTED, m.getFrequency());
            results.add(display);
        }

        return results;
    }

    public Timestamp lastUpdate(Timestamp ts, UpdateFrequency frequency) {

        System.out.println("ts: " + ts + " freq: " + frequency);

        return UpdateFrequency.getLastUpdate(ts, frequency);
    }

}
