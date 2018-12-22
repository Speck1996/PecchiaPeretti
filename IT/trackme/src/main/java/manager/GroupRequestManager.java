package manager;

import model.*;
import model.anonymized.BloodPressureEntityAnonymized;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Stateless
public class GroupRequestManager {

    private final static int PRIVACY_NUM = 2;
    private final static Date DEFAULT_MIN_BIRTHDATE = new Date(-5364610705443L);  //1800-01-01
    private final static Date DEFAULT_MAX_BIRTHDATE = new Date(7258170248082L);   //2200-01-01

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em = null;


    /*
     Create a new group data request with the specified parameters, asked by 'usernameTP' third party
      */
    public String newGroupRequest(String usernameTP, String name, UpdateFrequency frequency, short views, String location, Byte age_min, Byte age_max, Sex sex, String birthCountry) {

        StringBuilder builder = new StringBuilder();
        Date dateMin, dateMax;

        dateMin = getDateFromAge(age_max);
        dateMax = getDateFromAge(age_min);


        //TODO call to the maps service



        //TODO specified location constraints
        //Specific queries to the db
        if((views & View.BLOOD_PRESSURE) > 0) {
            System.out.println("ok blood");
            builder.append(bloodPressureQuery(location, dateMin, dateMax, sex, birthCountry));
        }

        if((views & View.HEARTBEAT ) > 0) {
            builder.append(heartbeatQuery(location, dateMin, dateMax, sex, birthCountry));
        }

        if((views & View.SLEEP_TIME) > 0) {
            builder.append(sleepTimeQuery(location, dateMin, dateMax, sex, birthCountry));
        }

        if((views & View.STEPS) > 0) {
            builder.append(stepsQuery(location, dateMin, dateMax, sex, birthCountry));
        }




        //TODO insert new GroupMonitoring in DB

        ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, usernameTP);
        GroupMonitoringEntity group = new GroupMonitoringEntity(name, new Timestamp(Calendar.getInstance().getTimeInMillis()), frequency, views, location, age_min, age_max, sex, birthCountry, tp);

        try {
//            em.getTransaction().begin();
            em.persist(group);
//            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }




        return builder.toString();

    }






    /*
    Get a date o birth given an age (and the current date)
     */
    private Date getDateFromAge(Byte age) {
        if(age == 0)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - age);

        return new Date(calendar.getTimeInMillis());
    }

    /*
    Set the given dates as parameters of the given query
     */
    private void setDates(Query query, Date dateMin, Date dateMax) {
        if(dateMin == null)
            query.setParameter("datemin", DEFAULT_MIN_BIRTHDATE);
        else
            query.setParameter("datemin", dateMin);

        if(dateMax == null)
            query.setParameter("datemax", DEFAULT_MAX_BIRTHDATE);
        else
            query.setParameter("datemax", dateMax);
    }








    /*
    Perform a query for Blood Pressure data with the given constraints
     */
    private String bloodPressureQuery(String location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        System.out.println("blood req: " + location + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        StringBuilder builder = new StringBuilder();

        TypedQuery<BloodPressureEntityAnonymized> query = null;

        /*
        Set parameters
         */
        if(dateMax == null && dateMin == null && sex == null)
            query = em.createNamedQuery("requestAnonymized", BloodPressureEntityAnonymized.class);

        if(sex != null && (dateMin != null || dateMax != null)) {
            query = em.createNamedQuery("requestDateSexAnonymized", BloodPressureEntityAnonymized.class);

            query.setParameter("sex", sex);
            setDates(query, dateMin, dateMax);
        }

        if(sex == null && (dateMin != null || dateMax != null)) {
            query = em.createNamedQuery("requestDateAnonymized", BloodPressureEntityAnonymized.class);

            setDates(query, dateMin, dateMax);
        }

        if(sex != null && (dateMin == null && dateMax == null)) {
            query = em.createNamedQuery("requestSexAnonymized", BloodPressureEntityAnonymized.class);
            query.setParameter("sex", sex);
        }

        if(birthCountry == null)
            query.setParameter("country", "%%");
        else
            query.setParameter("country", birthCountry);


        /*
        Run the query and construct results
         */


        List<BloodPressureEntityAnonymized> results = query.getResultList();

        builder.append("\nBLOOD PRESSURE\n");

        if(results.size() < PRIVACY_NUM) {
            builder.append("Not enough data about Blood Pressure, size: ");
            builder.append(results.size());
            builder.append("\n");
        }
        else {
            int i = 1;
            for(Iterator<BloodPressureEntityAnonymized> iterator = results.iterator(); iterator.hasNext(); i++) {
                BloodPressureEntityAnonymized b = iterator.next();
                builder.append(i);
                builder.append(") Timestamp: ");
                builder.append(b.getTs());
                builder.append(" value: ");
                builder.append(b.getValue());
                builder.append("\n");
            }
        }



        return builder.toString();

    }

    /*
    Perform a query for Heartbeat data with the given constraints
     */
    private String heartbeatQuery(String location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        StringBuilder builder = new StringBuilder();


        builder.append("\nHeartbeat\n");

        return builder.toString();

    }

    /*
    Perform a query for Sleep Time data with the given constraints
     */
    private String sleepTimeQuery(String location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        StringBuilder builder = new StringBuilder();


        builder.append("\nSleep Time\n");

        return builder.toString();

    }

    /*
    Perform a query for Steps data with the given constraints
     */
    private String stepsQuery(String location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        StringBuilder builder = new StringBuilder();


        builder.append("\nSteps\n");

        return builder.toString();

    }




    public String test() {
        /*new model.anonymized.BloodPressureEntityAnonymized(b.id.ts, b.value)*/
        String s;
        s = "SELECT i.taxcode, b.id.ts, b.value  FROM IndividualEntity as i , BloodPressureEntity as b WHERE i = b.individual and i.sex = :p";

//        TypedQuery<BloodPressureEntityAnonymized> query = em.createQuery(s, BloodPressureEntityAnonymized.class);
        Query query = em.createQuery(s);
        query.setParameter("p", null);
        List res = query.getResultList();

        String ret = "";
        int i=1;
//        for(Iterator<BloodPressureEntityAnonymized> iterator = res.iterator(); iterator.hasNext(); i++) {
//            BloodPressureEntityAnonymized b = iterator.next();
//            ret += "" + i + ")" + b.getTs() + "  " + b.getValue() + "\n";
//        }

        for(Iterator<Object[]> it = res.iterator(); it.hasNext(); i++) {
            Object[] o = it.next();
            ret += o[0] + " " + o[1] + " " + o[2] + "\n";
        }

//        ret += "size: " + res.size() + "subzise: " + res.get(0);

        return ret;
    }
}
