package manager;

import manager.geocoding.FoundLocation;
import manager.geocoding.Geocoder;
import manager.geocoding.GeocoderImpl;
import model.*;
import model.anonymized.BloodPressureAnonymized;
import model.anonymized.HeartbeatAnonymized;
import model.anonymized.SleepTimeAnonymized;
import model.anonymized.StepsAnonymized;

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

    private final static int PRIVACY_NUM = 3;
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


    /*
     Create a new group data request with the specified parameters, asked by 'usernameTP' third party
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


        //flag for queries
        locationIsNull = (foundLocation == null);
        dateMinIsNull = (dateMin == null);
        dateMaxIsNull = (dateMax == null);
        sexIsNull = (sex == null);



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
        GroupMonitoringEntity group = new GroupMonitoringEntity(name, new Timestamp(Calendar.getInstance().getTimeInMillis()), frequency, views, location, age_min, age_max, sex, birthCountry, tp);

        try {
            em.persist(group);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return builder.toString();
    }






    /*
    Get a date of birth given an age (and the current date)
     */
    private Date getDateFromAge(Byte age) {
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
    private String bloodPressureQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        System.out.println("blood req: " + location + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        StringBuilder builder = new StringBuilder();

        TypedQuery<BloodPressureAnonymized> query = null;



        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestLocationAnonymized", BloodPressureAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestDateAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestLocationDateAnonymized", BloodPressureAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestSexAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestLocationSexAnonymized", BloodPressureAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestDateSexAnonymized", BloodPressureAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestLocationDateSexAnonymized", BloodPressureAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<BloodPressureAnonymized> results = query.getResultList();

        builder.append("\nBLOOD PRESSURE\n");

        if(results.size() < PRIVACY_NUM) {
            builder.append("Not enough data about Blood Pressure, size: ");
            builder.append(results.size());
            builder.append("\n");
        }
        else {
            int i = 1;
            for(Iterator<BloodPressureAnonymized> iterator = results.iterator(); iterator.hasNext(); i++) {
                BloodPressureAnonymized b = iterator.next();
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
    private String heartbeatQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        System.out.println("heart req: " + location + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        StringBuilder builder = new StringBuilder();

        TypedQuery<HeartbeatAnonymized> query = null;


        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestAnonymizedHeartbeat", HeartbeatAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestLocationAnonymizedHeartbeat", HeartbeatAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestDateAnonymizedHeartbeat", HeartbeatAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestLocationDateAnonymizedHeartbeat", HeartbeatAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestSexAnonymizedHeartbeat", HeartbeatAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestLocationSexAnonymizedHeartbeat", HeartbeatAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestDateSexAnonymizedHeartbeat", HeartbeatAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestLocationDateSexAnonymizedHeartbeat", HeartbeatAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<HeartbeatAnonymized> results = query.getResultList();

        builder.append("\nHEARTBEAT\n");

        if(results.size() < PRIVACY_NUM) {
            builder.append("Not enough data about Heartbeat, size: ");
            builder.append(results.size());
            builder.append("\n");
        }
        else {
            int i = 1;
            for(Iterator<HeartbeatAnonymized> iterator = results.iterator(); iterator.hasNext(); i++) {
                HeartbeatAnonymized h = iterator.next();
                builder.append(i);
                builder.append(") Timestamp: ");
                builder.append(h.getTs());
                builder.append(" value: ");
                builder.append(h.getValue());
                builder.append("\n");
            }
        }


        return builder.toString();

    }

    /*
    Perform a query for Sleep Time data with the given constraints
     */
    private String sleepTimeQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        System.out.println("sleep req: " + location + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        StringBuilder builder = new StringBuilder();

        TypedQuery<SleepTimeAnonymized> query = null;



        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestAnonymizedSleeptime", SleepTimeAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestLocationAnonymizedSleeptime", SleepTimeAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestDateAnonymizedSleeptime", SleepTimeAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestLocationDateAnonymizedSleeptime", SleepTimeAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestSexAnonymizedSleeptime", SleepTimeAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestLocationSexAnonymizedSleeptime", SleepTimeAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestDateSexAnonymizedSleeptime", SleepTimeAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestLocationDateSexAnonymizedSleeptime", SleepTimeAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<SleepTimeAnonymized> results = query.getResultList();

        builder.append("\nSLEEP TIME\n");

        if(results.size() < PRIVACY_NUM) {
            builder.append("Not enough data about Sleep Time, size: ");
            builder.append(results.size());
            builder.append("\n");
        }
        else {
            int i = 1;
            for(Iterator<SleepTimeAnonymized> iterator = results.iterator(); iterator.hasNext(); i++) {
                SleepTimeAnonymized b = iterator.next();
                builder.append(i);
                builder.append(") Day: ");
                builder.append(b.getDay());
                builder.append(" value: ");
                builder.append(b.getValue());
                builder.append("\n");
            }
        }


        return builder.toString();

    }

    /*
    Perform a query for Steps data with the given constraints
     */
    private String stepsQuery(FoundLocation location, Date dateMin, Date dateMax, Sex sex, String birthCountry) {
        System.out.println("steps req: " + location + " " + dateMin + " " + dateMax + " " + sex + " " + birthCountry);

        StringBuilder builder = new StringBuilder();

        TypedQuery<StepsAnonymized> query = null;



        /*
        Choose the right query
         */
        if(locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestAnonymizedSteps", StepsAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && sexIsNull)
            query = em.createNamedQuery("requestLocationAnonymizedSteps", StepsAnonymized.class);

        if(locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestDateAnonymizedSteps", StepsAnonymized.class);

        if(!locationIsNull && (!dateMaxIsNull || !dateMinIsNull) & sexIsNull)
            query = em.createNamedQuery("requestLocationDateAnonymizedSteps", StepsAnonymized.class);

        if(locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestSexAnonymizedSteps", StepsAnonymized.class);

        if(!locationIsNull && dateMaxIsNull && dateMinIsNull && !sexIsNull)
            query = em.createNamedQuery("requestLocationSexAnonymizedSteps", StepsAnonymized.class);

        if(locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestDateSexAnonymizedSteps", StepsAnonymized.class);

        if(!locationIsNull  && (!dateMaxIsNull || !dateMinIsNull) && !sexIsNull)
            query = em.createNamedQuery("requestLocationDateSexAnonymizedSteps", StepsAnonymized.class);


        //Set parameters
        setParam(query, location, dateMin, dateMax, sex, birthCountry);



        /*
        Run the query and construct results
         */
        List<StepsAnonymized> results = query.getResultList();

        builder.append("\nSTEPS\n");

        if(results.size() < PRIVACY_NUM) {
            builder.append("Not enough data about Steps, size: ");
            builder.append(results.size());
            builder.append("\n");
        }
        else {
            int i = 1;
            for(Iterator<StepsAnonymized> iterator = results.iterator(); iterator.hasNext(); i++) {
                StepsAnonymized b = iterator.next();
                builder.append(i);
                builder.append(") Day: ");
                builder.append(b.getDay());
                builder.append(" value: ");
                builder.append(b.getValue());
                builder.append("\n");
            }
        }


        return builder.toString();

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
