package activityhelpers;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryptor {


    private static Encryptor encryptor;

    public static synchronized Encryptor getInstance(){

        if(encryptor== null){
            encryptor = new Encryptor();
        }

        return encryptor;
    }


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
