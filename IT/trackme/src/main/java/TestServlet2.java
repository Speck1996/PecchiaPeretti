import manager.IndividualRequestManager;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/indreq")
public class TestServlet2 extends HttpServlet {

    @EJB
    private IndividualRequestManager bean;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String un = req.getParameter("un");
            String tc = req.getParameter("tc");

            System.out.println("username: " + un + " taxcode: " + tc);
            String res = bean.newIndividualRequest(un, tc,null, null, (short) 3, (short) 6);

            resp.getWriter().println(res);
        } catch (Exception e) {
            System.out.println(resp.getWriter().printf(e.toString()));
        }
    }
}
