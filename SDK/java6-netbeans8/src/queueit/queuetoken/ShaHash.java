package queueit.queuetoken;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

class ShaSignature {
    private final String secretKey;

    public ShaSignature(String secretKey)
    {
        this.secretKey = secretKey;
    }

    public byte[] GenerateSignature(String tokenString)
    {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest((tokenString + secretKey).getBytes(Charset.forName("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
            //will not happen
            return null;
        }
    }
   
}
