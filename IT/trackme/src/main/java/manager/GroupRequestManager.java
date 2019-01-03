package manager;

import manager.geocoding.FoundLocation;
import manager.geocoding.Geocoder;
import manager.geocoding.GeocoderImpl;
import model.*;
import model.anonymized.*;

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

/**
 * Bean that manages the group requests
 */
@Stateless
public class GroupRequestManager {

    private final static int PRIVACY_NUM = 0;
    private final static Date DEFAULT_MIN_BIRTHDATE = new Date(-5364610705443L);  //1800-01-01
    private final static Date DEFAULT_MAX_BIRTHDATE = new Date(7258170248082L);   //2200-01-01
    private final static Double DEFAULT_MIN_LATITUDE = -90.0;
    private final static Double DEFAULT_MAX_LATITUDE = 90.0;
    private final static Double DEFAULT_MIN_LONGITUDE = -180.0;
    private final static Double DEFAULT_MAX_LONGITUDE = 180.0;

    private boolean locationIsNull;
    private boolean dateMinIsNull;
    private boolean dateMaxIsNull;
    private boolean sexIsNull;

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em = null;


    /**
     * Create a new group data request with the give parameters
     * @param usernameTP The username of the Third Party who issues the request
     * @param name The name that the Third Party has chosen for the request
     * @param frequency The frequency of the updates
     * @param views Indicates which data the Third Party is interested in
     * @param location The location indicated by the Third Party
     * @param age_min The minimum age indicated by the Third Party
     * @param age_max The maximum age indicated by the Third Party
     * @param sex The sex indicated by the Third Party
     * @param birthCountry The country of birth indicated by the Third Party
     * @return
     */
    public String newGroupRequest(String usernameTP, String name, UpdateFrequency frequency, short views, String location, Byte age_min, Byte age_max, Sex sex, String birthCountry) {

        StringBuilder builder = new StringBuilder();
        Date dateMin, dateMax;

        dateMin = getDateFromAge(age_max);
        dateMax = getDateFromAge(age_min);

        //call to the maps API
        Geocoder geocoder = new GeocoderImpl();
        FoundLocation foundLocation = geocoder.getLocation(location);
        System.out.println("is foundLocation null: " + (foundLocation == null));


        //set flag for queries
        setFlags(foundLocation, dateMin, dateMax, sex);



        //Specific queries to the DB based on the 'views' value
        if((views & View.BLOOD_PRESSURE) > 0) {
            System.out.println("ok blood");
            builder.append(bloodPressureQuery(foundLocation, dateMin, dateMax, sex, birthCountry));
        }

        if((views & View.HEARTBEAT ) > 0) {
            builder.append(heartbeatQuery(foundLocation, dateMin, dateMax, sex, birthCountry));
        }

        if((views & View.SLEEP_TIME) > 0) {
            builder.append(sleepTimeQuery(foundLocation, dateMin, dateMax, sex, birthCountry));
        }

        if((views & View.STEPS) > 0) {
            builder.append(stepsQuery(foundLocation, dateMin, dateMax, sex, birthCountry));
        }



        //insert new GroupMonitoring in DB
        ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, usernameTP);
        tp.addGroupMonitorings(name, new Timestamp(Calendar.getInstance().getTimeInMillis()), frequency, views, location, age_min, age_max, sex, birthCountry);
        //GroupMonitoringEntity group = new GroupMonitoringEntity(name, new Timestamp(Calendar.getInstance().getTimeInMillis()), frequency, views, location, age_min, age_max, sex, birthCountry, tp);

        try {
            em.persist(tp);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return builder.toString();
    }

    private List<?> getData(GroupMonitoringEntity groupMonitoring, short view) {
        Date dateMin, dateMax;

        dateMin = getDateFromAge(groupMonitoring.getAgeMax());
        dateMax = getDateFromAge(groupMonitoring.getAgeMin());

        FoundLocation foundLocation = getLocation(groupMonitoring.getLocation());
        System.out.println("is foundLocation null: " + (foundLocation == null));

        setFlags(foundLocation, dateMin, dateMax, groupMonitoring.getSex());

        switch (view) {
            case View.BLOOD_PRESSURE:
                return bloodPressureQuery(foundLocation, dateMin, dateMax, groupMonitoring.getSex(), groupMonitoring.getCountry());
            case View.HEARTBEAT:
                return heartbeatQuery(foundLocation, dateMin, dateMax, groupMonitoring.getSex(), groupMonitoring.getCountry());
            case View.SLEEP_TIME:
                return sleepTimeQuery(foundLocation, dateMin, dateMax, groupMonitoring.getSex(), groupMonitoring.getCountry());
            case View.STEPS:
                return stepsQuery(foundLocation, dateMin, dateMax, groupMonitoring.getSex(), groupMonitoring.getCountry());
        }

        return null;
    }

    public List<BloodPressureAnonymized> getBloodPressureData(GroupMonitoringEntity groupMonitoring) {
        return (List<BloodPressureAnonymized>) getData(groupMonitoring, View.BLOOD_PRESSURE);
    }

    public List<HeartbeatAnonymized> getHeartBeatData(GroupMonitoringEntity groupMonitoring) {
        return (List<HeartbeatAnonymized>) getData(groupMonitoring, View.HEARTBEAT);
    }

    public List<SleepTimeAnonymized> getSleepTimeData(GroupMonitoringEntity groupMonitoring) {
        return (List<SleepTimeAnonymized>) getData(groupMonitoring, View.SLEEP_TIME);
    }

    public List<StepsAnonymized> getStepsData(GroupMonitoringEntity groupMonitoring) {
        return (List<StepsAnonymized>) getData(groupMonitoring, View.STEPS);
    }


    private FoundLocation getLocation(String location) {
        Geocoder geocoder = new GeocoderImpl();
        return geocoder.getLocation(location);
    }

    private void setFlags(FoundLocation foundLocation, Date dateMin, Date dateMax, Sex sex) {
        locationIsNull = (foundLocation == null);
        dateMinIsNull = (dateMin == null);
        dateMaxIsNull = (dateMax == null);
        sexIsNull = (sex == null);
    }


    /*
    Get a date of birth given an age (and the current date)
     */
    private Date getDateFromAge(Byte age) {
        if(age == null)
            return null;

        if(age == 0)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - age);

        return new Date(calendar.getTimeInMillis());
    }

    /*
    Set the given values as parameters of the given query
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

    private void setLocation(Query query, FoundLocation location) {

        query.setParameter("maxlat", location.getBoxNorth());
        query.setParameter("minlat", location.getBoxSouth());
        query.setParameter("maxlong", location.getBoxEast());
        query.setParameter("minlong", location.getBoxWest());

    }

    private void setCountry(Query query, String birthCountry) {
        if(birthCountry == null)
            query.setParameter("country", "%%");
        else
            query.setParameter("country", birthCountry);
    }

    private void setParam(Query query, FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        if(!locationIsNull)
            setLocation(query, location);

        if(!dateMaxIsNull || !dateMinIsNull)
            setDates(query, dateMin, dateMax);

        if(!sexIsNull)
            query.setParameter("sex", sex);

        setCountry(query, birthCountry);
    }




    /*
    Perform a query for Blood Pressure data with the given constraints
     */
    private List<?> bloodPressureQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        //System.out.println("blood query: " + location.getName() + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        TypedQuery<BloodPressureAnonymized> query = null;



        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestLocationAnonymized", BloodPressureAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestDateAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestLocationDateAnonymized", BloodPressureAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestSexAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestLocationSexAnonymized", BloodPressureAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestDateSexAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("BloodPressure.requestLocationDateSexAnonymized", BloodPressureAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<BloodPressureAnonymized> results = query.getResultList();
        if(isNotAnonymized(results)) {
            //TODO
            return null;
        }

        return results;
    }

    /*
    Perform a query for Heartbeat data with the given constraints
     */
    private List<?> heartbeatQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        //System.out.println("heart query: " + location.getName() + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        TypedQuery<HeartbeatAnonymized> query = null;


        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestAnonymized", HeartbeatAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestLocationAnonymized", HeartbeatAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestDateAnonymized", HeartbeatAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestLocationDateAnonymized", HeartbeatAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestSexAnonymized", HeartbeatAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestLocationSexAnonymized", HeartbeatAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestDateSexAnonymized", HeartbeatAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("Heartbeat.requestLocationDateSexAnonymized", HeartbeatAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<HeartbeatAnonymized> results = query.getResultList();
        if(isNotAnonymized(results)) {
            return null;
        }

        return results;
    }

    /*
    Perform a query for Sleep Time data with the given constraints
     */
    private List<?> sleepTimeQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        //System.out.println("sleep query: " + location.getName() + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

//        StringBuilder builder = new StringBuilder();

        TypedQuery<SleepTimeAnonymized> query = null;



        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("SleepTime.requestAnonymized", SleepTimeAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("SleepTime.requestLocationAnonymized", SleepTimeAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("SleepTime.requestDateAnonymized", SleepTimeAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("SleepTime.requestLocationDateAnonymized", SleepTimeAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("SleepTime.requestSexAnonymized", SleepTimeAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("SleepTime.requestLocationSexAnonymized", SleepTimeAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("SleepTime.requestDateSexAnonymized", SleepTimeAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("SleepTime.requestLocationDateSexAnonymized", SleepTimeAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<SleepTimeAnonymized> results = query.getResultList();
        if(isNotAnonymized(results)) {
            return null;
        }

        return results;

    }

    /*
    Perform a query for Steps data with the given constraints
     */
    private List<?> stepsQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        //System.out.println("steps query: " + location.getName() + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        TypedQuery<StepsAnonymized> query = null;



        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("Steps.requestAnonymized", StepsAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("Steps.requestLocationAnonymized", StepsAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("Steps.requestDateAnonymized", StepsAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("Steps.requestLocationDateAnonymized", StepsAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("Steps.requestSexAnonymized", StepsAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("Steps.requestLocationSexAnonymized", StepsAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("Steps.requestDateSexAnonymized", StepsAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("Steps.requestLocationDateSexAnonymized", StepsAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<StepsAnonymized> results = query.getResultList();
        if(isNotAnonymized(results)) {
            return null;
        }

        return results;
    }

    private boolean isNotAnonymized(List<?> results) {
        return results.size() < PRIVACY_NUM;
    }


    public List<GroupMonitoringEntity> getRequests(String usernameTP) {
        ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, usernameTP);

        if(tp == null)
            return null;

        return tp.getGroupMonitorings();
    }

    public GroupMonitoringEntity getRequest(String usernameTP, String name) {
        return em.find(GroupMonitoringEntity.class, new GroupMonitoringEntityPK(usernameTP, name));
    }


    public void setFrequency(String usernameTP, String name, UpdateFrequency frequency) {
        if(name == null) {
            System.out.println("NULLLLLL");
            return;
        }

        TypedQuery<GroupMonitoringEntity> query = em.createNamedQuery("GroupMonitoring.findByTPandName", GroupMonitoringEntity.class);
        query.setParameter("usernameTP", usernameTP);
        query.setParameter("name", name);

        System.out.println("tp: " +usernameTP + " name: " + name);

        GroupMonitoringEntity monitoring = query.getSingleResult();

        if(monitoring == null)
            return;

        monitoring.setFrequency(frequency);
        em.persist(monitoring);
    }






    public String getRequestsTest(String usernameTP) {
        ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, usernameTP);
        if(tp == null)
            return "null";

        List<GroupMonitoringEntity> list = tp.getGroupMonitorings();
        String s = "";
        for(GroupMonitoringEntity m: list) {
            s += "\t" + m.toString() + "\n";
        }

        return s;
    }

    public String test() {
        /*new model.anonymized.BloodPressureAnonymized(b.id.ts, b.value)*/
        String s;
        s = "SELECT i.taxcode, b.id.ts, b.value  FROM IndividualEntity as i , BloodPressureEntity as b WHERE i = b.individual and i.sex = :p";

//        TypedQuery<BloodPressureAnonymized> query = em.createQuery(s, BloodPressureAnonymized.class);
        Query query = em.createQuery(s);
        query.setParameter("p", null);
        List res = query.getResultList();

        String ret = "";
        int i=1;
//        for(Iterator<BloodPressureAnonymized> iterator = res.iterator(); iterator.hasNext(); i++) {
//            BloodPressureAnonymized b = iterator.next();
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
