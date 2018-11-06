package com.queue_it.queuetoken;

import java.util.Date;
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
public class EnqueueTokenTest {
    
    public EnqueueTokenTest() {
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
    public void factory_simple() throws Exception {
      
        long startTime = System.currentTimeMillis();
        String expectedCustomerId = "ticketania";
        IEnqueueToken token = Token
                .enqueue(expectedCustomerId)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");
        long endTime = System.currentTimeMillis();
        
        assertEquals(expectedCustomerId, token.getCustomerId());
        assertNotNull(token.getTokenIdentifier());
        assertEquals(TokenVersion.QT1, token.getTokenVersion());
        assertEquals(EncryptionType.AES256, token.getEncryption());
        assertTrue(startTime <= token.getIssued());
        assertTrue(endTime >= token.getIssued());
        assertEquals(token.getExpires(), Long.MAX_VALUE);
        assertNull(token.getEventId());
        assertNull(token.getPayload());
    }
    
        @Test
    public void factory_tokenidentifierprefix() throws Exception {
      
        long startTime = System.currentTimeMillis();
        String expectedCustomerId = "ticketania";
        String expectedTokenIdentifierPrefix = "SomePrefix";
        IEnqueueToken token = Token
                .enqueue(expectedCustomerId, expectedTokenIdentifierPrefix)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");
        
        String[] tokenIdentifierParts = token.getTokenIdentifier().split("~");
        assertEquals(expectedTokenIdentifierPrefix, tokenIdentifierParts[0]);        
    }
    
    @Test
    public void factory_withValidity_long() throws Exception {
        long expectedValidity = 3000;
        
        IEnqueueToken token = Token
                .enqueue("ticketania")
                .withValidity(expectedValidity)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(token.getIssued() + expectedValidity, token.getExpires());
    }
  
    @Test
    public void factory_withValidity_date() throws Exception {
        Date expectedValidity = new Date(3471336000000L);
        
        IEnqueueToken token = Token
                .enqueue("ticketania")
                .withValidity(expectedValidity)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(3471336000000L, token.getExpires());
    }
    
    @Test
    public void factory_withEventId() throws Exception {
        String expectedEventId = "myevent";
        
        IEnqueueToken token = Token
                .enqueue("ticketania")
                .withEventId(expectedEventId)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(expectedEventId, token.getEventId());
    }
    
    @Test
    public void factory_withBody() throws Exception {
        IEnqueueTokenPayload expectedPayload = Payload.enqueue().withKey("somekey").generate();
        
        IEnqueueToken token = Token
                .enqueue("ticketania")
                .withPayload(expectedPayload)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(expectedPayload, token.getPayload());
    }
    
    @Test
    public void factory_withBody_withKey_withRelativeQuality() throws Exception {
        String expectedEventId = "myevent";
        String expectedCustomerId = "ticketania";
        long expectedValidity = 1100;

        IEnqueueTokenPayload expectedPayload = Payload
            .enqueue()
            .withKey("somekey")
            .generate();
               
        IEnqueueToken token = Token
                .enqueue(expectedCustomerId)
                .withPayload(expectedPayload)
                .withEventId(expectedEventId)
                .withValidity(expectedValidity)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(expectedCustomerId, token.getCustomerId());
        assertEquals(expectedEventId, token.getEventId());
        assertEquals(expectedValidity, token.getExpires() - token.getIssued());
        assertEquals(expectedPayload, token.getPayload());
    }
    
    @Test
    public void token_withPayload() throws Exception {

            String expectedSignedToken = "eyJ0eXAiOiJRVDEiLCJlbmMiOiJBRVMyNTYiLCJpc3MiOjE1MzQ3MjMyMDAwMDAsImV4cCI6MTUzOTEyOTYwMDAwMCwidGkiOiJhMjFkNDIzYS00M2ZkLTQ4MjEtODRmYS00MzkwZjZhMmZkM2UiLCJjIjoidGlja2V0YW5pYSIsImUiOiJteWV2ZW50In0.0rDlI69F1Dx4Twps5qD4cQrbXbCRiezBd6fH1PVm6CnVY456FALkAhN3rgVrh_PGCJHcEXN5zoqFg65MH8WZc_CQdD63hJre3Sedu0-9zIs.aZgzkJm57etFaXjjME_-9LjOgPNTTqkp1aJ057HuEiU";

            IEnqueueTokenPayload payload = Payload
                    .enqueue()
                    .withKey("somekey")
                    .withRelativeQuality(0.45678663514)
                    .withCustomData("color", "blue")
                    .withCustomData("size", "medium")
                    .generate();

            EnqueueToken token = new EnqueueToken(
                "a21d423a-43fd-4821-84fa-4390f6a2fd3e", 
                "ticketania", 
                "myevent", 
                1534723200000L, 
                1539129600000L, 
                payload);
            token.generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6", false);

            String actualSignedToken = token.getToken();

            assertEquals(expectedSignedToken, actualSignedToken);
    }   
    
    @Test
    public void token_withoutPayload() throws Exception {

        String expectedSignedToken = "eyJ0eXAiOiJRVDEiLCJlbmMiOiJBRVMyNTYiLCJpc3MiOjE1MzQ3MjMyMDAwMDAsImV4cCI6MTUzOTEyOTYwMDAwMCwidGkiOiJhMjFkNDIzYS00M2ZkLTQ4MjEtODRmYS00MzkwZjZhMmZkM2UiLCJjIjoidGlja2V0YW5pYSIsImUiOiJteWV2ZW50In0..nN4Q5wIYKktChsK1_UEuP_tjiZj9xYOd65iYv4E9hbY";

        EnqueueToken token = new EnqueueToken(
            "a21d423a-43fd-4821-84fa-4390f6a2fd3e", 
            "ticketania", 
            "myevent", 
            1534723200000L, 
            1539129600000L, 
            null);
        token.generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6", false);

        String actualSignedToken = token.getToken();

        assertEquals(expectedSignedToken, actualSignedToken);
    }   
    
    @Test
    public void token_minimalHeader() throws Exception {

            String expectedSignedToken = "eyJ0eXAiOiJRVDEiLCJlbmMiOiJBRVMyNTYiLCJpc3MiOjE1MzQ3MjMyMDAwMDAsInRpIjoiYTIxZDQyM2EtNDNmZC00ODIxLTg0ZmEtNDM5MGY2YTJmZDNlIiwiYyI6InRpY2tldGFuaWEifQ..ChCRF4bTbt4zlOcvXLjQYouhgqgiNNNZqcci8VWoZIU";

            EnqueueToken token = new EnqueueToken(
                "a21d423a-43fd-4821-84fa-4390f6a2fd3e", 
                "ticketania", 
                null, 
                1534723200000L, 
                Long.MAX_VALUE, 
                null);
            token.generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6", false);

            String actualSignedToken = token.getToken();

            assertEquals(expectedSignedToken, actualSignedToken);
    }   
}
