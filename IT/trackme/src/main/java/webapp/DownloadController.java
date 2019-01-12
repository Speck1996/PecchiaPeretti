package webapp;

import login.AuthenticationUtils;
import manager.GroupRequestManager;
import manager.IndividualRequestManager;
import model.*;
import model.xml.BloodPressureListAnonymized;
import model.xml.HeartbeatListAnonymized;
import model.xml.SleepTimeListAnonymized;
import model.xml.StepsListAnonymized;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Servlet responsible to create and return data in xml format
 */
@WebServlet(urlPatterns = "/webapp/download")
public class DownloadController extends HttpServlet {


    private static final String TYPE_PARAM_NAME = "t";
    private static final String GROUP_PARAM_VALUE = "g";
    private static final String INDI_PARAM_VALUE = "i";
    private static final String NAME_PARAM_NAME = "n";

    private static final String VIEW_PARAM_NAME = "v";
    private static final String BLOOD_PARAM_VALUE = "b";
    private static final String HEART_PARAM_VALUE = "h";
    private static final String SLEEP_PARAM_VALUE = "s";
    private static final String STEPS_PARAM_VALUE = "st";

    @PersistenceContext
    EntityManager em;

    @EJB
    GroupRequestManager groupRequestManager;

    @EJB
    IndividualRequestManager individualRequestManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String usernameTP = AuthenticationUtils.getUsernameByCookiesArray(req.getCookies());
        String type = req.getParameter(TYPE_PARAM_NAME);
        String nameReq = req.getParameter(NAME_PARAM_NAME);
        String view = req.getParameter(VIEW_PARAM_NAME);

        if(usernameTP == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if(type == null || nameReq == null || view == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }


        resp.setContentType("text/xml");
        PrintWriter out = resp.getWriter();



        //fetch group data
        if(type.equals(GROUP_PARAM_VALUE)) {
            GroupMonitoringEntity monitoring = em.find(GroupMonitoringEntity.class, new GroupMonitoringEntityPK(usernameTP, nameReq));

            if(monitoring == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            StringWriter sw = new StringWriter();

            switch (view) {
                case BLOOD_PARAM_VALUE:
                    List<BloodPressureEntity> bloodPressureDataEntities = groupRequestManager.getBloodPressureData(monitoring);
                    JAXB.marshal(new BloodPressureListAnonymized(bloodPressureDataEntities), sw);
                    break;
                case HEART_PARAM_VALUE:
                    List<HeartbeatEntity> heartbeatEntities = groupRequestManager.getHeartBeatData(monitoring);
                    JAXB.marshal(new HeartbeatListAnonymized(heartbeatEntities), sw);
                    break;
                case SLEEP_PARAM_VALUE:
                    List<SleepTimeEntity> sleepTimeEntities = groupRequestManager.getSleepTimeData(monitoring);
                    JAXB.marshal(new SleepTimeListAnonymized(sleepTimeEntities), sw);
                    break;
                case STEPS_PARAM_VALUE:
                    List<StepsEntity> stepsEntities= groupRequestManager.getStepsData(monitoring);
                    JAXB.marshal(new StepsListAnonymized(stepsEntities), sw);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
            }

            out.println(sw.toString());
            return;
        }

        //fetch individual data
        if(type.equals(INDI_PARAM_VALUE)) {
            StringWriter sw = new StringWriter();

            switch (view) {
                case BLOOD_PARAM_VALUE:
                    List<BloodPressureEntity> bloodPressureDataEntities = individualRequestManager.getBloodPressureData(nameReq);
                    JAXB.marshal(new BloodPressureListAnonymized(bloodPressureDataEntities), sw);
                    break;
                case HEART_PARAM_VALUE:
                    List<HeartbeatEntity> heartbeatEntities = individualRequestManager.getHeartBeatData(nameReq);
                    JAXB.marshal(new HeartbeatListAnonymized(heartbeatEntities), sw);
                    break;
                case SLEEP_PARAM_VALUE:
                    List<SleepTimeEntity> sleepTimeEntities = individualRequestManager.getSleepTimeData(nameReq);
                    JAXB.marshal(new SleepTimeListAnonymized(sleepTimeEntities), sw);
                    break;
                case STEPS_PARAM_VALUE:
                    List<StepsEntity> stepsEntities= individualRequestManager.getStepsData(nameReq);
                    JAXB.marshal(new StepsListAnonymized(stepsEntities), sw);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
            }

            out.println(sw.toString());
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

    }
}
