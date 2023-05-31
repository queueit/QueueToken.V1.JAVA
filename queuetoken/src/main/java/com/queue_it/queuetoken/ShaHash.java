package com.queue_it.queuetoken;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaHash {
    public static byte[] generateHash(String tokenString, String secretKey) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest((tokenString + secretKey).getBytes(Charset.forName("UTF-8")));
        } catch (NoSuchAlgorithmException ex) {
            // will not happen
            return null;
        }
    }
}
