package com.queue_it.queuetoken;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class AesEncryption {

    public static byte[] Encrypt(byte[] input, String secretKey, String tokenIdentifier) throws TokenSerializationException {
        SecretKeySpec key = generateKey(secretKey);
        IvParameterSpec iv = generateIV(tokenIdentifier);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encrypted= new byte[cipher.getOutputSize(input.length)];
            int enc_len = cipher.update(input, 0, input.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);
            return encrypted;
        } catch (Exception ex) {
            throw new TokenSerializationException(ex);
        }
    }
   
    private static SecretKeySpec generateKey(String secretKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(secretKey.getBytes(Charset.forName("UTF-8")));
            return new SecretKeySpec(keyBytes, "AES");
            
        } catch (NoSuchAlgorithmException ex) {
            //will not happen
            return null;
        }
    }

    private static IvParameterSpec generateIV(String tokenIdentifier)
    {       
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] ivBytes = md5.digest(tokenIdentifier.getBytes(Charset.forName("UTF-8")));
            return new IvParameterSpec(ivBytes);  
        } catch (NoSuchAlgorithmException ex) {
            // Will not happen
            return null;
        } 
    }
}
