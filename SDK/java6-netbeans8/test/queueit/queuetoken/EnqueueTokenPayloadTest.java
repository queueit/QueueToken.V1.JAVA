package queueit.queuetoken;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mala
 */
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
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\",\"cd\":{\"key3\":\"Value3\",\"key2\":\"Value2\",\"key1\":\"Value1\"}}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("myKey")
            .withRelativeQuality(0.456)
            .withCustomData("key1", "Value1")
            .withCustomData("key2", "Value2")
            .withCustomData("key3", "Value3")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }   

    @Test
    public void serialize_key_relativequality_onecustomdata() {
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\",\"cd\":{\"key1\":\"Value1\"}}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("myKey")
            .withRelativeQuality(0.456)
            .withCustomData("key1", "Value1")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }      
    
    @Test
    public void serialize_key_relativequality() {
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\"}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("myKey")
            .withRelativeQuality(0.456)
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }   
    
    @Test
    public void serialize_key() {
        String expectedJson = "{\"k\":\"myKey\"}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("myKey")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }  
    
    @Test
    public void serialize_key_escaped() {
        String expectedJson = "{\"k\":\"my\"Key\"}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("my\"Key")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    } 
    
    @Test
    public void serialize_relativequality() {
        String expectedJson = "{\"r\":0.456}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withRelativeQuality(0.456)
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }  
    
    @Test
    public void serialize_customdata() {
        String expectedJson = "{\"cd\":{\"key1\":\"Value1\"}}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withCustomData("key1", "Value1")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }  
    
    @Test
    public void serialize_customdata_escaped() {
        String expectedJson = "{\"cd\":{\"ke\"y1\":\"Va\"lue1\"}}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withCustomData("ke\"y1", "Va\"lue1")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    } 
}
