package collector;

import login.Secured;
import model.*;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Bean responsible for receiving data and persist it in the DataBase
 */

@Stateless
@Secured
@Path("/sendata")
@ApplicationPath("/datareceiver")
public class DataCollector extends Application {

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em = null;

    /**
     *
     * @param username The username of the individual
     * @param value The value of the Blood Pressure to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    private void insertBloodPressureData(String username, short value, Double latitude, Double longitude) {

        //finding the individual
        IndividualEntity individual = em.createNamedQuery("Individual.findByUsername", IndividualEntity.class)
                .setParameter("username", username)
                .getSingleResult();

        if(individual == null)
            return;

        //checking if an entity with a matching primary key is already present
        BloodPressureEntityPK primaryKey = new BloodPressureEntityPK();
        primaryKey.setTs(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        primaryKey.setIndividual(individual.getTaxcode());

        BloodPressureEntity oldBloodPressure = em.find(BloodPressureEntity.class, primaryKey);

        //no entity with matching primary key
        if(oldBloodPressure == null){

            //setting up a new entity
            BloodPressureEntity bloodPressure = new BloodPressureEntity(individual, primaryKey.getTs(), value, latitude, longitude);
            em.persist(bloodPressure);

        }else{

            //updating the already present entity
            Query query = em.createNamedQuery("BloodPressure.updateData", BloodPressureEntity.class)
                    .setParameter("bloodpressure", value)
                    .setParameter("latitude", latitude)
                    .setParameter("longitude", longitude)
                    .setParameter("ts", primaryKey.getTs())
                    .setParameter("individual",primaryKey.getIndividual());

            query.executeUpdate();
        }

    }

    /**
     *
     * @param username The username of the individual
     * @param value The value of the Heartbeat to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    private void insertHeartbeatData(String username, short value, Double latitude, Double longitude) {


        //finding the individual
        IndividualEntity individual = em.createNamedQuery("Individual.findByUsername", IndividualEntity.class)
                .setParameter("username", username)
                .getSingleResult();

        if(individual == null)
            return;


        HeartbeatEntityPK primaryKey = new HeartbeatEntityPK();
        primaryKey.setTs(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        primaryKey.setIndividual(individual.getTaxcode());

        HeartbeatEntity oldHeartBeat = em.find(HeartbeatEntity.class, primaryKey);

        if(oldHeartBeat == null){


            HeartbeatEntity heartBeat = new HeartbeatEntity(individual, primaryKey.getTs(), value, latitude, longitude);
            em.persist(heartBeat);
        }else{


            Query query = em.createNamedQuery("HeartBeat.updateData", HeartbeatEntity.class)
                    .setParameter("heartbeat", value)
                    .setParameter("latitude", latitude)
                    .setParameter("longitude", longitude)
                    .setParameter("ts", primaryKey.getTs())
                    .setParameter("individual",primaryKey.getIndividual());

            query.executeUpdate();
        }




    }

    /**
     *
     * @param username The taxcode of the individual
     * @param value The time of sleeping to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    private void insertSleepTimeData(String username, Time value, Double latitude, Double longitude) {

        IndividualEntity individual = em.createNamedQuery("Individual.findByUsername", IndividualEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();

        if(individual == null)
            return;

        SleepTimeEntityPK primaryKey = new SleepTimeEntityPK();
        primaryKey.setDay(new Date(Calendar.getInstance().getTimeInMillis()));
        primaryKey.setIndividual(individual.getTaxcode());
        SleepTimeEntity oldSleepTime = em.find(SleepTimeEntity.class, primaryKey);

        if(oldSleepTime == null) {
            SleepTimeEntity sleepTime = new SleepTimeEntity(individual, primaryKey.getDay(), value, latitude, longitude);
            em.persist(sleepTime);
        }else{
            Query query = em.createNamedQuery("SleepTime.updateData", SleepTimeEntity.class)
                          .setParameter("sleep", value)
                          .setParameter("latitude", latitude)
                          .setParameter("longitude", longitude)
                          .setParameter("day", primaryKey.getDay())
                          .setParameter("individual",primaryKey.getIndividual());

            query.executeUpdate();
        }
    }

    /**
     *
     * @param username The taxcode of the individual
     * @param value The numbers of steps to be store
     * @param latitude The latitude of the data collection
     * @param longitude The longitude of the data collection
     */
    private void insertStepsData(String username, int value, Double latitude, Double longitude) {

        //finding the individual
        IndividualEntity individual = em.createNamedQuery("Individual.findByUsername", IndividualEntity.class)
                .setParameter("username", username)
                .getSingleResult();

        if(individual == null)
            return;

        StepsEntityPK primaryKey = new StepsEntityPK();
        primaryKey.setDay(new Date(Calendar.getInstance().getTimeInMillis()));
        primaryKey.setIndividual(individual.getTaxcode());
        StepsEntity oldSteps = em.find(StepsEntity.class, primaryKey);

        if(oldSteps == null) {
            StepsEntity steps = new StepsEntity(individual, primaryKey.getDay(), value, latitude, longitude);
            em.persist(steps);
        }else{
            Query query = em.createNamedQuery("Steps.updateData", SleepTimeEntity.class)
                    .setParameter("step", value)
                    .setParameter("latitude", latitude)
                    .setParameter("longitude", longitude)
                    .setParameter("day", primaryKey.getDay())
                    .setParameter("individual",primaryKey.getIndividual());

            query.executeUpdate();
        }
    }


    /**
     * Method called to send data to the collector.
     * TODO replace -1 with something more elegant if data is not sent
     * @param data object containing eHealth data and some value decorating it, such as the sender username
     *             and coordinates
     * @return a response to confirm the receipt of data
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendData(ReceivedData data) {

        //boolean set to true if the data has location attributes set
        boolean locationSet = false;

        //temporally store the coordinates
        double latitude = 0.0;
        double longitude = 0.0;

        //data has coordinates attributes filled
        if (data.getLatitude() != null && data.getLongitude() != null) {
            latitude = Double.parseDouble(data.getLatitude());
            longitude = Double.parseDouble(data.getLongitude());
            locationSet = true;
        }

        //heart rate data accessible with location
        if (data.getHeartRate() != -1 && locationSet) {

            this.insertHeartbeatData(data.getUsername(), (short) data.getHeartRate(), latitude, longitude);

            //heart rate data accessible but without location
        } else if (data.getHeartRate() != -1 && !locationSet) {

            this.insertHeartbeatData(data.getUsername(), (short) data.getHeartRate(), null, null);
        }

        //blood pressure data accessible with location
        if (data.getBloodPressure() != -1 && locationSet){


            this.insertBloodPressureData(data.getUsername(), (short) data.getBloodPressure(), latitude, longitude);

            //blood pressure data accessible but without location
        }else if(data.getBloodPressure() != -1 && !locationSet){

            this.insertBloodPressureData(data.getUsername(), (short)data.getBloodPressure(), null,null);
        }

        //steps data accessible with location
        if(data.getSteps()!=-1 && locationSet) {


            this.insertStepsData(data.getUsername(), data.getSteps(), latitude, longitude);

            //steps data accessible without location
        }else if(data.getSteps() != -1 && !locationSet){
            this.insertStepsData(data.getUsername(), data.getSteps(), null, null);
        }

        //sleep data accessible with location
        if(data.getSleep() != null && locationSet){

            //converting time string in sql format
            Time sleepTime = Time.valueOf(data.getSleep());
            this.insertSleepTimeData(data.getUsername(), sleepTime,latitude, longitude);

            //sleep data accessible without location
        }else if(data.getSleep() != null ){

            Time sleepTime = Time.valueOf(data.getSleep());
            this.insertSleepTimeData(data.getUsername(), sleepTime,null, null);
        }


        return Response.ok().build();
    }


}
