package activityhelpers;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Ckass used to encrypt password with a SHA-256 algorithm
 */
public class Encryptor {

    /**
     * Encryptor that will be in charge of applying the encription
     */
    private static Encryptor encryptor;


    /**
     * Method used to get the encryptor
     * @return the encryptor
     */
    public static synchronized Encryptor getInstance(){

        //singleton patter: if there is no encryptor it is created
        if(encryptor== null){
            encryptor = new Encryptor();
        }

        return encryptor;
    }

    /**
     * Method used to encrypt password with SHA-256 algorithm
     * @param baseString the string to be encrypted
     * @return the digest
     */
    public String encrypt(String baseString){

            try {
                // Static getInstance method is called with hashing SHA
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // digest() method called
                // to calculate message digest of an input
                // and return array of byte
                byte[] messageDigest = md.digest(baseString.getBytes());

                // Convert byte array into signum representation
                BigInteger no = new BigInteger(1, messageDigest);

                // Convert message digest into hex value
                String hashtext = no.toString(16);

                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }

                return hashtext;
            }

            // For specifying wrong message digest algorithms
            catch (NoSuchAlgorithmException e) {
                System.out.println("Exception thrown"
                        + " for incorrect algorithm: " + e);

                return null;
            }
        }
}
