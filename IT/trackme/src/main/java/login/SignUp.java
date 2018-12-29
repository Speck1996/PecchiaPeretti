package login;

import model.ThirdPartyEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;


/**
 * This class implements the singup process
 */
@Stateless
@Path("/home")
@ApplicationPath("/register")
public class SignUp extends Application {

    /**
     * Entity manager needed to add data to the DB
     */
    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em = null;

    /**
     * Method that adds the given user to the DB
     * @param sentTp the user to be added to the DB
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/signup")
    public void registerThirdParty(ThirdPartyEntity sentTp){
        em.persist(sentTp);
    }
}
