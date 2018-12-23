import collector.DataCollector;
import manager.GroupRequestManager;
import manager.IndividualRequestManager;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/test")
public class TestServlet extends HttpServlet {

    @EJB
    private GroupRequestManager bean;

    @EJB
    private IndividualRequestManager bean2;

    @EJB
    private DataCollector bean3;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.println("HelloWorld\n");

//        String s = bean.test();
//        out.println(s);

        String s = bean.newGroupRequest("mydoc", "Polimiers", null , (short) 15, "Milano", (byte) 10, (byte) 127, null, null);
        out.println(s);


//        bean2.acceptRequest("FGHI", "cardiologist");
//        bean2.rejectRequest("FGHI", "cardiologist");
//        bean3.insertBloodPressureData("FGHI", (short) 150, null, null);
//        bean3.insertStepsData("ABCDE", 2000, null, null);
        out.println("ok");
    }
}
