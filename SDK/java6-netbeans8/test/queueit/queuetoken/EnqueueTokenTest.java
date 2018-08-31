/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queueit.queuetoken;

import java.util.Date;
import java.util.UUID;
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
        assertTrue(startTime <= token.getIssued().getTime());
        assertTrue(endTime >= token.getIssued().getTime());
        assertEquals(token.getExpires().getTime(), Long.MAX_VALUE);
        assertNull(token.getEventId());
        assertNull(token.getBody());
    }
    
    @Test
    public void factory_withValidity_long() throws Exception {
        long expectedValidity = 3000;
        
        IEnqueueToken token = Token
                .enqueue("ticketania")
                .withValidity(expectedValidity)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(new Date(token.getIssued().getTime() + expectedValidity), token.getExpires());
    }
  
    @Test
    public void factory_withValidity_date() throws Exception {
        Date expectedValidity = new Date(2080, 01, 01, 12, 00, 00);
        
        IEnqueueToken token = Token
                .enqueue("ticketania")
                .withValidity(expectedValidity)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(new Date(token.getIssued().getTime() + expectedValidity.getTime()), token.getExpires());
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
        IEnqueueTokenBody expectedBody = Body.enqueue().withKey("somekey").generate();
        
        IEnqueueToken token = Token
                .enqueue("ticketania")
                .withBody(expectedBody)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(expectedBody, token.getBody());
    }
    
    @Test
    public void factory_withBody_withKey_withRank() throws Exception {
        IEnqueueTokenBody expectedBody = Body.enqueue().withKey("somekey").generate();
        String expectedEventId = "myevent";
        String expectedCustomerId = "ticketania";
        long expectedValidity = 1100;
        IEnqueueToken token = Token
                .enqueue(expectedCustomerId)
                .withBody(expectedBody)
                .withEventId(expectedEventId)
                .withValidity(expectedValidity)
                .generate("5ebbf794-1665-4d48-80d6-21ac34be7faedf9e10b3-551a-4682-bb77-fee59d6355d6");

        assertEquals(expectedCustomerId, token.getCustomerId());
        assertEquals(expectedEventId, token.getEventId());
        assertEquals(expectedValidity, token.getExpires().getTime() - token.getIssued().getTime());
        assertEquals(expectedBody, token.getBody());
    }
}
