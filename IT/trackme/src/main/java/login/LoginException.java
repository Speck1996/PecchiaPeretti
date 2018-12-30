package login;

/**
 * Exception thrown by the AuthenticationManager while checking user credentials
 * in the database
 */
class LoginException extends Exception {

    /**
     * Constructor for the exception
     * @param message message to be returned when the exception occurs
     */
    LoginException(String message){
        super(message);
    }
}
