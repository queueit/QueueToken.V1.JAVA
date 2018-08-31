/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package queueit.queuetoken;

import java.util.*;

public class EnqueueTokenGenerator {
    private EnqueueToken token;
    
    public EnqueueTokenGenerator(String customerId) {
         this.token = new EnqueueToken(customerId);
    }

    public EnqueueTokenGenerator withEventId(String eventId) {
        this.token = new EnqueueToken(this.token, eventId);
        
        return this;
    }
    
    public EnqueueTokenGenerator withValidity(long validityMillis) {
        this.token = new EnqueueToken(this.token, validityMillis);
        
        return this;
    }

    public EnqueueTokenGenerator withValidity(Date validity) {
        this.token = new EnqueueToken(this.token, validity.getTime());
        
        return this;
    }
        
    public EnqueueTokenGenerator withBody(IEnqueueTokenBody body) {
        this.token = new EnqueueToken(this.token, body);
        
        return this;
    }
    
    public IEnqueueToken generate(String secretKey) throws TokenSerializationException {
        token.generate(secretKey);
        return token;
    }
}
