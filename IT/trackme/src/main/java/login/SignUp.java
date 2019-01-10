package login;

import model.IndividualEntity;
import model.ThirdPartyEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;


/**
 * This class implements the singup process
 */
@Stateless
@Path("/signup")
@ApplicationPath("/rest")
public class SignUp {

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
    @Path("/signuptp")
    public void registerThirdParty(ThirdPartyEntity sentTp){
        em.persist(sentTp);
    }


    /**
     * Method that adds the given user to the DB
     * @param sentIndv the user to be added to the DB
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/individual")
    public void registerIndividual(IndividualEntity sentIndv){
        em.persist(sentIndv);
    }
}
