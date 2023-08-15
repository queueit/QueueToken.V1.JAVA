package com.queue_it.queuetoken.security;

import static java.security.MessageDigest.getInstance;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.queue_it.queuetoken.TokenSerializationException;

public class AesEncryption {

    private AesEncryption() {
    }

    public static byte[] encrypt(byte[] input, String secretKey, String tokenIdentifier)
            throws TokenSerializationException {
        SecretKeySpec key = generateKey(secretKey);
        IvParameterSpec iv = generateIV(tokenIdentifier);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encrypted = new byte[cipher.getOutputSize(input.length)];
            int enc_len = cipher.update(input, 0, input.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);
            return encrypted;
        } catch (Exception ex) {
            throw new TokenSerializationException(ex);
        }
    }

    public static byte[] decrypt(byte[] input, String secretKey, String tokenIdentifier)
            throws TokenSerializationException {
        SecretKeySpec key = generateKey(secretKey);
        IvParameterSpec iv = generateIV(tokenIdentifier);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            return cipher.doFinal(input);

        } catch (Exception ex) {
            throw new TokenSerializationException(ex);
        }
    }

    private static SecretKeySpec generateKey(String secretKey) {
        try {
            MessageDigest digest = getInstance("SHA-256");
            byte[] keyBytes = digest.digest(secretKey.getBytes(Charset.forName(StandardCharsets.UTF_8.name())));
            return new SecretKeySpec(keyBytes, "AES");

        } catch (NoSuchAlgorithmException ex) {
            // will not happen
            return null;
        }
    }

    private static IvParameterSpec generateIV(String tokenIdentifier) {
        try {
            MessageDigest md5 = getInstance("MD5");
            byte[] ivBytes = md5.digest(tokenIdentifier.getBytes(Charset.forName(StandardCharsets.UTF_8.name())));
            return new IvParameterSpec(ivBytes);
        } catch (NoSuchAlgorithmException ex) {
            // Will not happen
            return null;
        }
    }
}
