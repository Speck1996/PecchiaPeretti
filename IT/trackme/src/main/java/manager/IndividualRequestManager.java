package manager;

import model.*;
import webapp.IndividualReq;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

/**
 * Bean that manages individual data requests
 */
@Stateless
public class IndividualRequestManager {

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em;


    /**
     * A new request to access individual data. It creates in the DB a monitoring tuple with status 'PENDING'
     * @param usernameTP The username of the Third Party that issues the request
     * @param taxcode The taxcode of the Individual
     * @param frequency The frequency of the updates
     * @param views Indicates which data the Third Party is interested in
     * @param attributes Indicates which attributes of the Individual the Third Party is interested in
     * @return
     */
    public String newIndividualRequest(String usernameTP, String taxcode, String name, UpdateFrequency frequency, short views, short attributes) {
        System.out.println("NEW REQUEST");

        //Check whether the username is correct
        if(usernameTP == null)
            return "wrong username";

        ThirdPartyEntity thirdParty = em.find(ThirdPartyEntity.class, usernameTP);
        if(thirdParty == null)
            return "wrong username";


        //Check whether the taxcode is correct
        if(taxcode == null)
            return "wrong taxcode";

        IndividualEntity individual = em.find(IndividualEntity.class, taxcode);
        if(individual == null)
            return "Wrong taxcode";


        //Create new monitoring request
        individual.addThirdPartyMonitoring(thirdParty, name, new Timestamp(Calendar.getInstance().getTimeInMillis()), frequency, views, attributes);
        System.out.println("Created manytomany");

        em.persist(individual);


        return "Done";

    }


    /**
     * Accept an existing individual data request
     * @param taxcode The taxcode of the individual that accept the request
     * @param usernameTP The username of the Third Party that issued the request
     */
    public void acceptRequest(String taxcode, String usernameTP) {
        MonitoringEntity monitoring = em.find(MonitoringEntity.class, new MonitoringEntityPK(taxcode, usernameTP));

        if(monitoring == null)
            return;

        monitoring.setStatus(RequestStatus.ACCEPTED);
        em.persist(monitoring);
    }

    /**
     * Reject a pending request or block an already accepted request (remove the tuple from the DB)
     * @param taxcode The taxcode of the individual that reject or block the request
     * @param usernameTP The username of the Third Party that issued the request
     */
    public void rejectRequest(String taxcode, String usernameTP) {
        MonitoringEntity monitoring = em.find(MonitoringEntity.class, new MonitoringEntityPK(taxcode, usernameTP));

        if(monitoring == null)
            return;

        em.remove(monitoring);
    }

    public List<MonitoringEntity> getRequests(String usernameTP) {
        ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, usernameTP);
        IndividualEntity ind = em.find(IndividualEntity.class, "FGHI");

        if(tp == null)
            return null;

        return tp.getMonitorings();
    }
}
