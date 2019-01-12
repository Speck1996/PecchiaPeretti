package login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utilities class that offers static method regarding authentication and authorization issue.
 */
public class AuthenticationUtils {

    /**
     * Key used for the encryption algorithm
     */
    private static final String TOKENKEY = "TokenKey";

    /**
     * String used to decorate the token
     */
    private static final String ISSUER = "Trackme";

    /**
     * Tag used to store user's username in the token
     */
    private static final String OWNERTAG = "owner";

    /**
     * Method that generates the token
     * @param username used to personalize, and decorate the token
     * @return the generated token
     * @throws JWTCreationException if some invalid decorations are applied
     */
    static String issueToken(String username) throws JWTCreationException{

        //Claim used to store the username
        Map<String, Object> headerClaims = new HashMap<>();
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


    /**
     * Method used to validate the token.
     * The validation is based uniquely on the issuer and the key for the encoding
     * //TODO consider time validation
     * @param token the token to be validated
     * @throws JWTVerificationException if the token cannot be validated
     */
    public static void validateToken(String token) throws JWTVerificationException {

        Algorithm algorithm = Algorithm.HMAC256(TOKENKEY);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build(); //Reusable verifier instance

        DecodedJWT jwt = verifier.verify(token);
    }


    /**
     * Retrieve the username from the given token
     * @param token The token
     * @return The username contained in the token
     */
    static String getUsernameFromToken(String token) {
        if(token == null)
            return null;
        DecodedJWT jwt = JWT.decode(token);
        Claim claim = jwt.getHeaderClaim(OWNERTAG);
        String username = claim.asString();

        return username;
    }

    /**
     * Retrieve the username from a map of cookies.
     * The map must contained an authentication cookie with a valid token.
     * @param cookies The map of cookies
     * @return The username contained in the token, or null if the map is null, the map does not contain an authorization cookie or the token is invalid
     */
    public static String getUsernameByCookiesMap(Map<String, Object> cookies) {
        if(cookies != null) {
            Cookie c = (Cookie) cookies.get(WebAppLogin.COOKIE_NAME);
            if(c != null) {
                String token = c.getValue();
                return AuthenticationUtils.getUsernameFromToken(token);
            }
        }

        return null;
    }

    /**
     * Retrieve the username from an array of cookies.
     * The array must contained an authentication cookie witha a valid token.
     * @param cookies The array of cookies
     * @return The username contained in the token, or null if the array is null, the array does not contain an authorization cookie or the token is invalid
     */
    public static String getUsernameByCookiesArray(Cookie[] cookies) {
        if(cookies == null)
            return null;

        for(Cookie c: cookies) {
            if(c.getName().equals(WebAppLogin.COOKIE_NAME)) {
                return AuthenticationUtils.getUsernameFromToken(c.getValue());
            }
        }

        return null;
    }

    /**
     * Check if an error has occurred in the given faces component
     * @param component The faces component
     * @return The css class for error if an error occurred, null otherwise
     */
    public static String fieldError(UIComponent component) {
        FacesContext context = FacesContext.getCurrentInstance();

        String clientId = component.getClientId(context);
        Iterator<FacesMessage> messages = context.getMessages(clientId);

        while (messages.hasNext()) {
            if (messages.next().getSeverity().compareTo(
                    FacesMessage.SEVERITY_ERROR) >= 0) {
                return "input-error";
            }
        }
        return null;
    }
}
