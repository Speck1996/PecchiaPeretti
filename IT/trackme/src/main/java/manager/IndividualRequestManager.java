package manager;

import model.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Calendar;

@Stateless
public class IndividualRequestManager {

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em;

    /*
    A new request to access individual data. It creates in the DB a monitoring tuple with status 'PENDING'
     */
    public String newIndividualRequest(String usernameTP, String taxcode, UpdateFrequency frequency, short views, short attributes) {
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
        individual.addThirdPartyMonitoring(thirdParty, new Timestamp(Calendar.getInstance().getTimeInMillis()), frequency, views, attributes);
        System.out.println("Created manytomany");

        em.persist(individual);


        return "Done";

    }


    /*
    Accept an existing individual data request
     */
    public void acceptRequest(String taxcode, String usernameTP) {
        MonitoringEntity monitoring = em.find(MonitoringEntity.class, new MonitoringEntityPK(taxcode, usernameTP));

        if(monitoring == null)
            return;

        monitoring.setStatus(RequestStatus.ACCEPTED);
        em.persist(monitoring);
    }

    /*
    Reject a pending request or block an already accepted request (remove the tuple from the DB)
     */
    public void rejectRequest(String taxcode, String usernameTP) {
        MonitoringEntity monitoring = em.find(MonitoringEntity.class, new MonitoringEntityPK(taxcode, usernameTP));

        if(monitoring == null)
            return;

        em.remove(monitoring);
    }
}
