package com.queue_it.queuetoken;

import java.util.*;

public class EnqueueTokenGenerator {
    private EnqueueToken token;
    
    public EnqueueTokenGenerator(String customerId) {
         this.token = new EnqueueToken(customerId, null);
    }

    public EnqueueTokenGenerator(String customerId, String tokenIdentifierPrefix) {
         this.token = new EnqueueToken(customerId, tokenIdentifierPrefix);
    }
        
    public EnqueueTokenGenerator withEventId(String eventId) {
        this.token = EnqueueToken.addEventId(this.token, eventId);
        
        return this;
    }
    
    public EnqueueTokenGenerator withIpAddress(String ipAddress) {
        this.token = EnqueueToken.addIPAddress(this.token, ipAddress);
        
        return this;
    }
        
    public EnqueueTokenGenerator withValidity(long validityMillis) {
        this.token = EnqueueToken.addExpires(this.token, this.token.getIssued() + validityMillis);
        
        return this;
    }

    public EnqueueTokenGenerator withValidity(Date expires) {
        this.token = EnqueueToken.addExpires(this.token, expires.getTime());
        
        return this;
    }
        
    public EnqueueTokenGenerator withPayload(IEnqueueTokenPayload payload) {
        this.token = EnqueueToken.addPayload(this.token, payload);
        
        return this;
    }
    
    public IEnqueueToken generate(String secretKey) throws TokenSerializationException {
        token.generate(secretKey, true);
        return token;
    }
}
