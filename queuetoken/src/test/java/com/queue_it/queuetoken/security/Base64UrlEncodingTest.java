package com.queue_it.queuetoken.security;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Base64UrlEncodingTest {

    @Test
    public void testDecode_PassBase64WithoutTrialingEqualSigns_ShouldReturnValidBase64Value() {
        // Arrange
        // The Actual Base64 String of "queueit" is cXVldWVpdA==
        // Removing the trailing "==" to test the function behavior
        String input = "cXVldWVpdA";
        String expectedOutput = "queueit";

        // Act
        byte[] decodedValue = Base64UrlEncoding.decode(input);
        String actual = new String(decodedValue);

        // Assert
        assertEquals(expectedOutput, actual);
    }

    @Test
    public void testDecode_PassValidBase64Value_ShouldReturnValidBase64DecodedValue() {
        // Arrange
        String input = "cXVldWVpdA==";
        String expectedOutput = "queueit";

        // Act
        byte[] decodedValue = Base64UrlEncoding.decode(input);
        String actual = new String(decodedValue);

        // Assert
        assertEquals(expectedOutput, actual);
    }

    @Test
    public void testEncode() {

        // Arrange
        String input = "queueit";

        // The Actual Base64 String of "queueit" is cXVldWVpdA==
        String expectedOutput = "cXVldWVpdA";

        // Act
        String actual = Base64UrlEncoding.encode(input.getBytes());

        // Assert
        assertEquals(expectedOutput, actual);
    }
}
