package login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import model.ThirdPartyEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;


/**
 * Class that identify user credentials and generate a token
 */
@ApplicationPath("/login")
@Path("/authenticate")
public class AuthenticationManager{

    /**
     * Key used for the encryption algorithm
     */
    static final String TOKENKEY = "TokenKey";

    /**
     * String used to decorate the token
     */
    static final String ISSUER = "Trackme";

    /**
     * Tag used to store user's username in the token
     */
    static final String OWNERTAG = "owner";


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
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(Credentials credentials) {

        try {
            //taking credentials
            String username = credentials.getUsername();
            String password = credentials.getPassword();

            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(username);

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

        //finding the third party
        ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, username);

        //wrong password
        if (!(tp.getPassword().equals(password))){
            throw new LoginException("Wrong Password By user " + username);
        }

        //no user with the given credentials found
        else if(tp == null){

            throw new LoginException("User not found " + username );
        }
    }

    /**
     * Method that generates the token
     * @param username used to personalize, and decorate the token
     * @return the generated token
     * @throws JWTCreationException if some invalid decorations are applied
     */
    private String issueToken(String username) throws JWTCreationException{

        //Claim used to store the username
        Map<String, Object> headerClaims = new HashMap();
        headerClaims.put(OWNERTAG, username);

        //Algorithm used to encrypt the token
        Algorithm algorithm = Algorithm.HMAC256(TOKENKEY);

        //Creating the token
        String token = JWT.create()
                .withHeader(headerClaims)
                .withIssuer(ISSUER)
                .sign(algorithm);
         return token;

    }
}
