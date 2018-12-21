import manager.GroupRequestManager;

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

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.println("HelloWorld");

        String s = bean.newGroupRequest("mydoc", null, (short) 1, "Milano", (byte) 0, (byte) 100, null, null);
        out.println(s);

    }
}
