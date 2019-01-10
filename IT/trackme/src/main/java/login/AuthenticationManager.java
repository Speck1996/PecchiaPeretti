package login;


import model.IndividualEntity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



/**
 * Class that identify user credentials and generate a token
 */
@ApplicationPath("/rest")
@Path("/authenticate")
public class AuthenticationManager{

    /**
     * Manager to check for username and password validity
     */
    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em = null;


    /**
     * Method that check if the given credentials are valid. If they are an access token is given back to the requester
     * @param credentials Class representing the credentials of the user
     * @return Response containing the token if the credentials are valid, an error state if they are not
     */
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {

        try {
            //taking credentials
            String username = credentials.getUsername();
            String password = credentials.getPassword();

            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = AuthenticationUtils.issueToken(username);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            //an error occurred while checking credentials validity
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    /**
     * Method that authenticate the user, by checking if the given credentials are present in the
     * Database
     * @param username Given username to be checked in the database
     * @param password Given password to be checked in the database
     * @throws Exception if at least one of the credentials is not found, or if there is a mismatch with the password
     * in the database.
     */
    private void authenticate(String username, String password) throws Exception {

        //finding the individual
        IndividualEntity in = em.createNamedQuery("Individual.findByUsername", IndividualEntity.class)
                .setParameter("username", username)
                .getSingleResult();

        //no user with the given credentials found
        if(in == null){
            throw new LoginException("User not found " + username );
        }
        //wrong password
        else if (!(in.getPassword().equals(password))){
            throw new LoginException("Wrong Password By user " + username);
        }

    }


}
