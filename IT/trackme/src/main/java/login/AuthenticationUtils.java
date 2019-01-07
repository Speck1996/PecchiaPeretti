package login;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

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


    static String getUsernameFromToken(String token) {
        if(token == null)
            return null;
        DecodedJWT jwt = JWT.decode(token);
        Claim claim = jwt.getHeaderClaim(OWNERTAG);
        String username = claim.asString();

        return username;
    }

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
}
