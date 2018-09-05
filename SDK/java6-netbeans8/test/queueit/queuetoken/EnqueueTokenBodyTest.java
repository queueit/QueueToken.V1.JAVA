package queueit.queuetoken;

import java.util.*;
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
public class EnqueueTokenBodyTest {
    
    public EnqueueTokenBodyTest() {
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
        assertNull(instance.getRank());
        assertNull(instance.getCustomDataValue("key"));
    }

    @Test
    public void factory_key_rank() {
        String expectedKey = "myKey";
        Double expectedRank = 0.456;
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey(expectedKey)
            .withRank(expectedRank)
            .generate();
        String actualKey = instance.getKey();
        Double actualRank = instance.getRank();
        assertEquals(expectedKey, actualKey);
        assertEquals(expectedRank, actualRank);
        assertNull(instance.getCustomDataValue("key"));    
    }

    @Test
    public void factory_key_rank_customdata() {
        String expectedKey = "myKey";
        Double expectedRank = 0.456;
        String expectedCustomDataValue = "Value";
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey(expectedKey)
            .withRank(expectedRank)
            .withCustomData("key", expectedCustomDataValue)
            .generate();
        String actualKey = instance.getKey();
        Double actualRank = instance.getRank();
        String actualCustomData = instance.getCustomDataValue("key");
        assertEquals(expectedKey, actualKey);
        assertEquals(expectedRank, actualRank);
        assertEquals(expectedCustomDataValue, actualCustomData);
    }    

    @Test
    public void factory_rank() {
        Double expectedRank = 0.456;
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withRank(expectedRank)
            .generate();
        String actualKey = instance.getKey();
        Double actualRank = instance.getRank();
        String actualCustomData = instance.getCustomDataValue("key");
        assertNull(actualKey);
        assertEquals(expectedRank, actualRank);
        assertNull(actualCustomData);
    }      
    @Test
    public void factory_rank_customdata() {
        Double expectedRank = 0.456;
        String expectedCustomDataValue = "Value";
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withRank(expectedRank)
            .withCustomData("key", expectedCustomDataValue)
            .generate();
        String actualKey = instance.getKey();
        Double actualRank = instance.getRank();
        String actualCustomData = instance.getCustomDataValue("key");
        assertNull(actualKey);
        assertEquals(expectedRank, actualRank);
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
        Double actualRank = instance.getRank();
        String actualCustomData = instance.getCustomDataValue("key");
        assertNull(actualKey);
        assertNull(actualRank);
        assertEquals(expectedCustomDataValue, actualCustomData);
    }  
    
    @Test
    public void serialize_key_rank_multicustomdata() {
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\",\"cd\":{\"key3\":\"Value3\",\"key2\":\"Value2\",\"key1\":\"Value1\"}}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("myKey")
            .withRank(0.456)
            .withCustomData("key1", "Value1")
            .withCustomData("key2", "Value2")
            .withCustomData("key3", "Value3")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }   

    @Test
    public void serialize_key_rank_onecustomdata() {
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\",\"cd\":{\"key1\":\"Value1\"}}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("myKey")
            .withRank(0.456)
            .withCustomData("key1", "Value1")
            .generate();
        String actualJson = ((EnqueueTokenPayload)instance).serialize();
        
        assertEquals(expectedJson, actualJson);
    }      
    
    @Test
    public void serialize_key_rank() {
        String expectedJson = "{\"r\":0.456,\"k\":\"myKey\"}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withKey("myKey")
            .withRank(0.456)
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
    public void serialize_rank() {
        String expectedJson = "{\"r\":0.456}";
        
        IEnqueueTokenPayload instance = Payload
            .enqueue()
            .withRank(0.456)
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
