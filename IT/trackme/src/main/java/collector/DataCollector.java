package collector;

import model.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Bean responsible for receiving data and persist it in the DataBase
 */
@Stateless
public class DataCollector {

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em;

    /**
     *
     * @param taxcode The taxcode of the individual
     * @param valueMin The min value of the Blood Pressure to be store
     * @param valueMax The max value of the Blood Pressure to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    public void insertBloodPressureData(String taxcode, short valueMin, short valueMax, Double latitude, Double longitude) {

        IndividualEntity individual = em.find(IndividualEntity.class, taxcode);

        if(individual == null)
            return;

        BloodPressureEntity bloodPressure = new BloodPressureEntity(individual, new Timestamp(Calendar.getInstance().getTimeInMillis()), valueMin, valueMax, latitude, longitude);

        em.persist(bloodPressure);
    }

    /**
     *
     * @param taxcode The taxcode of the individual
     * @param value The value of the Heartbeat to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    public void insertHeartbearData(String taxcode, short value, Double latitude, Double longitude) {

        IndividualEntity individual = em.find(IndividualEntity.class, taxcode);

        if(individual == null)
            return;

        HeartbeatEntity heartbeat = new HeartbeatEntity(individual, new Timestamp(Calendar.getInstance().getTimeInMillis()), value, latitude, longitude);

        em.persist(heartbeat);
    }

    /**
     *
     * @param taxcode The taxcode of the individual
     * @param value The time of sleeping to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    public void insertSleepTimeData(String taxcode, Time value, Double latitude, Double longitude) {

        IndividualEntity individual = em.find(IndividualEntity.class, taxcode);

        if(individual == null)
            return;

        SleepTimeEntity sleepTime = new SleepTimeEntity(individual, new Date(Calendar.getInstance().getTimeInMillis()), value, latitude, longitude);

        em.persist(sleepTime);
    }

    /**
     *
     * @param taxcode The taxcode of the individual
     * @param value The numbers of steps to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    public void insertStepsData(String taxcode, int value, Double latitude, Double longitude) {

        IndividualEntity individual = em.find(IndividualEntity.class, taxcode);

        if(individual == null)
            return;

        StepsEntity steps = new StepsEntity(individual, new Date(Calendar.getInstance().getTimeInMillis()), value, latitude, longitude);

        em.persist(steps);
    }


}
