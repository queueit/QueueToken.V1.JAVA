package com.queue_it.queuetoken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.queue_it.queuetoken.models.PayloadDto;

public class EnqueueTokenPayloadTest {

    public EnqueueTokenPayloadTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void factory_key() {
        String expectedKey = "myKey";
        IEnqueueTokenPayload instance = Payload
                .enqueue()
                .withKey(expectedKey)
                .generate();
        String actualKey = instance.getKey();
        assertEquals(expectedKey, actualKey);
        assertNull(instance.getRelativeQuality());
        assertNull(instance.getCustomDataValue("key"));
    }

    @Test
    public void factory_key_relativequality() {
        String expectedKey = "myKey";
        Double expectedRelativeQuality = 0.456;
        IEnqueueTokenPayload instance = Payload
                .enqueue()
                .withKey(expectedKey)
                .withRelativeQuality(expectedRelativeQuality)
                .generate();
        String actualKey = instance.getKey();
        Double actualRelativeQuality = instance.getRelativeQuality();
        assertEquals(expectedKey, actualKey);
        assertEquals(expectedRelativeQuality, actualRelativeQuality);
        assertNull(instance.getCustomDataValue("key"));
    }

    @Test
    public void factory_key_relativequality_customdata() {
        String expectedKey = "myKey";
        Double expectedRelativeQuality = 0.456;
        String expectedCustomDataValue = "Value";
        IEnqueueTokenPayload instance = Payload
                .enqueue()
                .withKey(expectedKey)
                .withRelativeQuality(expectedRelativeQuality)
                .withCustomData("key", expectedCustomDataValue)
                .generate();
        String actualKey = instance.getKey();
        Double actualRelativeQuality = instance.getRelativeQuality();
        String actualCustomData = instance.getCustomDataValue("key");
        assertEquals(expectedKey, actualKey);
        assertEquals(expectedRelativeQuality, actualRelativeQuality);
        assertEquals(expectedCustomDataValue, actualCustomData);
    }

    @Test
    public void factory_relativequality() {
        Double expectedRelativeQuality = 0.456;
        IEnqueueTokenPayload instance = Payload
                .enqueue()
                .withRelativeQuality(expectedRelativeQuality)
                .generate();
        String actualKey = instance.getKey();
        Double actualRelativeQuality = instance.getRelativeQuality();
        String actualCustomData = instance.getCustomDataValue("key");
        assertNull(actualKey);
        assertEquals(expectedRelativeQuality, actualRelativeQuality);
        assertNull(actualCustomData);
    }

    @Test
    public void factory_relativequality_customdata() {
        Double expectedRelativeQuality = 0.456;
        String expectedCustomDataValue = "Value";
        IEnqueueTokenPayload instance = Payload
                .enqueue()
                .withRelativeQuality(expectedRelativeQuality)
                .withCustomData("key", expectedCustomDataValue)
                .generate();
        String actualKey = instance.getKey();
        Double actualRelativeQuality = instance.getRelativeQuality();
        String actualCustomData = instance.getCustomDataValue("key");
        assertNull(actualKey);
        assertEquals(expectedRelativeQuality, actualRelativeQuality);
        assertEquals(expectedCustomDataValue, actualCustomData);
    }

    @Test
    public void factory_customdata() {
        String expectedCustomDataValue = "value";
        IEnqueueTokenPayload instance = Payload
                .enqueue()
                .withCustomData("key", expectedCustomDataValue)
                .generate();
        String actualKey = instance.getKey();
        Double actualRelativeQuality = instance.getRelativeQuality();
        String actualCustomData = instance.getCustomDataValue("key");
        assertNull(actualKey);
        assertNull(actualRelativeQuality);
        assertEquals(expectedCustomDataValue, actualCustomData);
    }

    @Test
    public void serialize_key_relativequality_multicustomdata() {
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\",\"cd\":{\"key1\":\"Value1\",\"key2\":\"Value2\",\"key3\":\"Value3\"}}";

        HashMap<String, String> customData = new HashMap<String, String>();
        customData.put("key1", "Value1");
        customData.put("key2", "Value2");
        customData.put("key3", "Value3");

        PayloadDto payload = new PayloadDto("myKey", 0.456, customData);

        String actualJson = payload.serialize();

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void serialize_key_relativequality_onecustomdata() {

        // Arrange
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\",\"cd\":{\"key1\":\"Value1\"}}";
        HashMap<String, String> customData = new HashMap<String, String>();
        customData.put("key1", "Value1");
        PayloadDto payload = new PayloadDto("myKey", 0.456, customData);

        // Act
        String actualJson = payload.serialize();

        // Assert
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void serialize_key_relativequality() {
        // Arrange
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\"}";
        PayloadDto payload = new PayloadDto("myKey", 0.456, null);

        // Act
        String actualJson = payload.serialize();

        // Assert
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void serialize_key() {
        // Arrage
        String expectedJson = "{\"k\":\"myKey\"}";
        PayloadDto payload = new PayloadDto("myKey", null, null);

        // Act
        String actualJson = payload.serialize();

        // Assert
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void serialize_key_escaped() {
        // Arrage
        String expectedJson = "{\"k\":\"my\"Key\"}";
        PayloadDto payload = new PayloadDto("my\"Key", null, null);

        // Act
        String actualJson = payload.serialize();

        // Assert
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void serialize_relativequality() {
        // Arrage
        String expectedJson = "{\"r\":0.456}";
        PayloadDto payload = new PayloadDto(null, 0.456, null);

        // Act
        String actualJson = payload.serialize();

        // Assert
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void serialize_customdata() {
        // Arrage
        String expectedJson = "{\"cd\":{\"key1\":\"Value1\"}}";
        HashMap<String, String> customData = new HashMap<String, String>();
        customData.put("key1", "Value1");
        PayloadDto payload = new PayloadDto(null, null, customData);

        // Act
        String actualJson = payload.serialize();

        // Assert
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void serialize_customdata_escaped() {
        // Arrage
        String expectedJson = "{\"cd\":{\"ke\"y1\":\"Va\"lue1\"}}";
        HashMap<String, String> customData = new HashMap<String, String>();
        customData.put("ke\"y1", "Va\"lue1");
        PayloadDto payload = new PayloadDto(null, null, customData);

        // Act
        String actualJson = payload.serialize();

        // Assert
        assertEquals(expectedJson, actualJson);
    }
}
