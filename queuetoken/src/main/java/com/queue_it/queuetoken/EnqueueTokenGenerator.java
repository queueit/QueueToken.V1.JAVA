package com.queue_it.queuetoken;

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
        this.token = new EnqueueToken(this.token, this.token.getIssued() + validityMillis);
        
        return this;
    }

    public EnqueueTokenGenerator withValidity(Date expires) {
        this.token = new EnqueueToken(this.token, expires.getTime());
        
        return this;
    }
        
    public EnqueueTokenGenerator withPayload(IEnqueueTokenPayload payload) {
        this.token = new EnqueueToken(this.token, payload);
        
        return this;
    }
    
    public IEnqueueToken generate(String secretKey) throws TokenSerializationException {
        token.generate(secretKey, true);
        return token;
    }
}
