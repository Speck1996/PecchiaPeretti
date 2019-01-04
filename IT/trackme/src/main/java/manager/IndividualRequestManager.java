package manager;

import model.*;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
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
    public String newIndividualRequest(String usernameTP, String taxcode, String name, UpdateFrequency frequency, short views, short attributes) throws IllegalRequestArgumentException, RequestExistsException {
        System.out.println("NEW INDIVIDUAL REQUEST for: " + taxcode + "by " + usernameTP);

        //Check whether the username is correct
//        if(usernameTP == null)
//            return "wrong username";

        ThirdPartyEntity thirdParty;

        try {
            thirdParty = em.find(ThirdPartyEntity.class, usernameTP);
        } catch (Exception e) {
            throw new IllegalRequestArgumentException();
        }

        if(thirdParty == null)
            throw new IllegalRequestArgumentException();


        //Check whether the taxcode is correct
//        if(taxcode == null)
//            return "wrong taxcode";

        IndividualEntity individual;
        try {
            individual = em.find(IndividualEntity.class, taxcode);
        } catch (Exception e) {
            throw new IllegalRequestArgumentException();
        }

        if(individual == null)
            throw new IllegalRequestArgumentException();


        //check if there exists already a request
        if(em.find(MonitoringEntity.class, new MonitoringEntityPK(taxcode, usernameTP)) != null) {
            throw new RequestExistsException();
        }


        //Create new monitoring request
        individual.addThirdPartyMonitoring(thirdParty, name, new Timestamp(Calendar.getInstance().getTimeInMillis()), frequency, views, attributes);

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

    public MonitoringEntity getRequest(String usernameTP, String taxcode) {
        return em.find(MonitoringEntity.class, new MonitoringEntityPK(taxcode, usernameTP));
    }

    public List<BloodPressureEntity> getBloodPressureData(String taxcode) {
        TypedQuery<BloodPressureEntity> query = em.createNamedQuery("BloodPressure.requestIndividual", BloodPressureEntity.class);
        query.setParameter("taxcode", taxcode);

        List<BloodPressureEntity> results = query.getResultList();
        return results;
    }

    public List<HeartbeatEntity> getHeartBeatData(String taxcode) {
        TypedQuery<HeartbeatEntity> query = em.createNamedQuery("Heartbeat.requestIndividual", HeartbeatEntity.class);
        query.setParameter("taxcode", taxcode);

        List<HeartbeatEntity> results = query.getResultList();
        return results;
    }

    public List<SleepTimeEntity> getSleepTimeData(String taxcode) {
        TypedQuery<SleepTimeEntity> query = em.createNamedQuery("SleepTime.requestIndividual", SleepTimeEntity.class);
        query.setParameter("taxcode", taxcode);

        List<SleepTimeEntity> results = query.getResultList();
        return results;
    }

    public List<StepsEntity> getStepsData(String taxcode) {
        TypedQuery<StepsEntity> query = em.createNamedQuery("Steps.requestIndividual", StepsEntity.class);
        query.setParameter("taxcode", taxcode);

        List<StepsEntity> results = query.getResultList();
        return results;
    }

    public void setFrequency(String usernameTP, String taxcode, UpdateFrequency frequency) {
        System.out.println("freq: " + frequency);
        MonitoringEntity monitoring = em.find(MonitoringEntity.class, new MonitoringEntityPK(taxcode, usernameTP));

        if(monitoring != null) {
            monitoring.setFrequency(frequency);
            em.persist(monitoring);
        }
    }
}
