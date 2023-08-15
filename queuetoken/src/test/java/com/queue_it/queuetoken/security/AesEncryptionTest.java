package com.queue_it.queuetoken.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.queue_it.queuetoken.TokenSerializationException;

public class AesEncryptionTest {

    @Test
    public void testDecrypt_PassSameDataForEncryptionAndDecryption_ShouldPassAndBeEqual()
            throws TokenSerializationException {

        String plainText = "My Random Text";
        String secretKey = "secret";
        String tokenIdentifier = "TokenId";

        byte[] encryptedData = AesEncryption.encrypt(plainText.getBytes(), secretKey, tokenIdentifier);
        byte[] decryptedData = AesEncryption.decrypt(encryptedData, secretKey, tokenIdentifier);
        String actualDecryptedText = new String(decryptedData);
        assertEquals(plainText, actualDecryptedText);
    }

    @Test(expected = TokenSerializationException.class)
    public void testDecrypt_PassDifferentSecretKeys_ExceptionIsExpected()
            throws TokenSerializationException {
        // Arange
        String plainText = "My Random Text";
        String secretKeyForEncryption = "secret";
        String tokenIdentifier = "TokenId";
        byte[] encryptedData = AesEncryption.encrypt(plainText.getBytes(), secretKeyForEncryption, tokenIdentifier);

        // Act
        AesEncryption.decrypt(encryptedData, "AnotherSecret", tokenIdentifier);
    }

    @Test(expected = TokenSerializationException.class)
    public void testDecrypt_PassDifferentTokenIdentifiers_ExceptionIsExpected()
            throws TokenSerializationException {
        // Arange
        String plainText = "My Random Text";
        String secretKey = "secret";
        String tokenIdentifierForEncryption = "TokenId";
        byte[] encryptedData = AesEncryption.encrypt(plainText.getBytes(), secretKey, tokenIdentifierForEncryption);

        // Act
        AesEncryption.decrypt(encryptedData, secretKey, "AnotherTokenId");
    }

    @Test
    public void testEncrypt_PassValidData_EncryptedDataShouldntBeNull() throws TokenSerializationException {

        String plainText = "My Random Text";
        String secretKey = "secret";
        String tokenIdentifier = "TokenId";

        byte[] encryptedData = AesEncryption.encrypt(plainText.getBytes(), secretKey, tokenIdentifier);

        assertNotNull(encryptedData);
    }

    @Test
    public void testEncrypt_PassEmptyTokenIdentifier_EncryptedDataShouldNotBeNull() throws TokenSerializationException {

        String plainText = "My Random Text";
        String secretKey = "secret";
        String tokenIdentifier = "";

        byte[] encryptedData = AesEncryption.encrypt(plainText.getBytes(), secretKey, tokenIdentifier);

        assertNotNull(encryptedData);
    }

    @Test(expected = NullPointerException.class)
    public void testEncrypt_PassNullTokenIdentifier_ExceptionIsExpected() throws TokenSerializationException {

        String plainText = "My Random Text";
        String secretKey = "secret";

        AesEncryption.encrypt(plainText.getBytes(), secretKey, null);
    }

    @Test
    public void testEncrypt_PassEmptyInput_NoExceptionIsExpected() throws TokenSerializationException {

        // Arrange
        String plainText = "";
        String secretKey = "secret";
        String tokenIdentifier = "TokenId";

        // Act
        AesEncryption.encrypt(plainText.getBytes(), secretKey, tokenIdentifier);
    }

    @Test(expected = TokenSerializationException.class)
    public void testEncrypt_PassNullInput_TokenSerializationExceptionIsExpected() throws TokenSerializationException {

        // Arrange
        String secretKey = "secret";
        String tokenIdentifier = "TokenId";

        // Act
        AesEncryption.encrypt(null, secretKey, tokenIdentifier);
    }
}
