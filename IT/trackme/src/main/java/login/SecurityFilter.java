package login;

import model.ThirdPartyEntity;
import org.glassfish.jersey.internal.util.Base64;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;


/**
 *This class provides a security filter for all the resources under a certain path. To access these resources
 * the user must be authenticated
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    /**
     * Header authentication identifier
     */
    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    /**
     * There are various type of authentication. For this academic project, basic auth will be enough
     */
    //see Basic Auth using RESTapi
    private static final String AUTHORIZATION_HEADER_PREFIX = "Basic ";

    /**
     * Prefix for all the resources to which this filter will be applied
     */
    private static final String SECURED_URL_PREFIX = "secured";

    /**
     * Manager to check for username and password validity
     */
    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em = null;

    /**
     * This methods is in change of the authentication: if the username and the password contained in the header
     * corresponds to the ones stored in the database then the authentication is successfully completed. Otherwise it is denied
     * @param requestContext
     */
    @Override
    public void filter(ContainerRequestContext requestContext){

        //filtering only the secured path resources
        if (requestContext.getUriInfo().getPath().contains(SECURED_URL_PREFIX)) {

            //getting the authorization header
            List<String> authHeader = requestContext.getHeaders().get(AUTHORIZATION_HEADER_KEY);

            //authorization header found
            if (authHeader != null && authHeader.size() > 0) {

                //decoding the header
                String authToken = authHeader.get(0);
                authToken = authToken.replaceFirst(AUTHORIZATION_HEADER_PREFIX, "");
                String decodedString = Base64.decodeAsString(authToken);
                StringTokenizer tokenizer = new StringTokenizer(decodedString, ":");
                String username = tokenizer.nextToken();
                String password = tokenizer.nextToken();

                //finding the user in the DB
                //TODO login for both ANDROID and WEBAPP
                ThirdPartyEntity tp = em.find(ThirdPartyEntity.class, username);

                if (tp.getUsername().equals(username) && tp.getPassword().equals(password)) {
                    return;
                }
            }


            Response unauthorizedStatus = Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Nope")
                    .build();

            requestContext.abortWith(unauthorizedStatus);


        }

    }

}