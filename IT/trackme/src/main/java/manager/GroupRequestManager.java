package manager;

import model.BloodPressureEntity;
import model.Sex;
import model.UpdateFrequency;
import model.anonymized.BloodPressureEntityAnonymized;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Iterator;
import java.util.List;

@Stateless
public class GroupRequestManager {

    protected final static int PRIVACY_NUM = 10;

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em = null;


    // Create a new group data request with the specified parameters, asked by 'usernameTP' third party
    public String newGroupRequest(String usernameTP, UpdateFrequency frequency, short views, String location, Byte age_min, Byte age_max, Sex sex, String birthCountry) {


        String s = "SELECT new model.anonymized.BloodPressureEntityAnonymized(h.id.ts, h.value) FROM IndividualEntity i JOIN HeartbeatEntity h";
        TypedQuery<BloodPressureEntityAnonymized> query = em.createQuery(s, BloodPressureEntityAnonymized.class);
        List<BloodPressureEntityAnonymized> res = query.getResultList();

        String ret = "";
        int i=1;
        for(Iterator<BloodPressureEntityAnonymized> iterator = res.iterator(); iterator.hasNext(); i++) {
            BloodPressureEntityAnonymized b = iterator.next();
            ret += "" + i + ")" + b.getTs() + "  " + b.getValue() + "\n";
        }

        return ret;

    }
}
