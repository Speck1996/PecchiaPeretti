package login;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;
import java.util.Map;

/**
 * Filter applied to all the resources that need authentication
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    /**
     * This string represents the path that will be secured
     */
    private static final String REALM = "login";


    /**
     * String representing the type of authentication chosen
     */
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        System.out.println("Filtering...");

        String token = null;

        // Try to extract the token from the cookies
        token = tryCookieToken(requestContext);

        try {
            AuthenticationUtils.validateToken(token);

            postAuthorization(requestContext, token);
            return;
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Not WebApp, let's try for Android");
        }

        // Try to extract the token from the Authorization header
        token = tryHeaderToken(requestContext);

        try {

            // Validate the token
            AuthenticationUtils.validateToken(token);
            postAuthorization(requestContext, token);

        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
        }



    }

    private String tryCookieToken(ContainerRequestContext requestContext) {
        String s = "Cookies:\n";

        Map<String, Cookie> cookies = requestContext.getCookies();
        for(String c: cookies.keySet()) {
            s += c + " -> " + cookies.get(c).getValue() + "\n";
        }

        System.out.println(s);


        Cookie authCookie = cookies.get(WebAppLogin.COOKIE_NAME);
        String token = null;
        if(authCookie != null)
            token = authCookie.getValue();
        System.out.println(token);
        return token;
    }

    private String tryHeaderToken(ContainerRequestContext requestContext) {
        // Get the Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Validate the Authorization header
        if (!isTokenBasedAuthentication(authorizationHeader)) {
            return null;
        }

        return authorizationHeader
                .substring(AUTHENTICATION_SCHEME.length()).trim();
    }

    /**
     *Check if the Authorization header is valid
     *It must not be null and must be prefixed with "Bearer" plus a whitespace
     * The authentication scheme comparison must be case-insensitive
     * @param authorizationHeader string representing the header
     * @return true if the header is valid, false otherwise
     * */
    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }


    /**
     * Abort the filter chain with a 401 status code response
     * The WWW-Authenticate header is sent along with the response
     * @param requestContext the context applying the filter
     */
    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE,
                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
    }


    //this part of the code is used to bind the user with the a principal that can be used to access the username
    // itself in every path that is secured
    private void postAuthorization(ContainerRequestContext requestContext, String token) {

        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                //retrieving username from the token and binding it to the principal
                String username = AuthenticationUtils.getUsernameFromToken(token);
                return () -> username;
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEME;
            }
        });
    }
}
