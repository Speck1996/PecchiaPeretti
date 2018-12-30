package login;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

/**
 * Class used only for demo purpose
 */
@ApplicationPath("/login")
@Path("/securedresource")
public class SecuredResource extends Application {

    @GET
    @Secured
    @Path("message")
    @Produces(MediaType.TEXT_PLAIN)
    public String securedMethod() {
        return "You got me";
    }

}

