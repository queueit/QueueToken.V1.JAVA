package com.queue_it.queuetoken;

import java.nio.charset.Charset;
import java.util.*;

class EnqueueToken implements IEnqueueToken {

    private final String customerId;
    private String eventId;
    private IEnqueueTokenPayload payload;
    private long issued;
    private long expires = Long.MAX_VALUE;    
    private String tokenIdentifierPrefix;
    private String tokenIdentifier;
    private String token;
    private String hash;

    public EnqueueToken(String customerId, String tokenIdentifierPrefix) {
        this.customerId = customerId;
        this.issued = System.currentTimeMillis();
        this.tokenIdentifier = getTokenIdentifier(tokenIdentifierPrefix);
        this.tokenIdentifierPrefix = tokenIdentifierPrefix;
    }

    private String getTokenIdentifier(String tokenIdentifierPrefix1) {
        return tokenIdentifierPrefix1 == null || tokenIdentifierPrefix1.isEmpty() ? UUID.randomUUID().toString() : tokenIdentifierPrefix1 + "~" + UUID.randomUUID().toString();
    }
    
    public EnqueueToken(EnqueueToken token, Long expires) {
        this(token.customerId, token.tokenIdentifierPrefix); 
        
        this.eventId = token.eventId;
        this.payload = token.payload;    
        this.expires = expires;
        this.issued = token.issued;
    }
    
    public EnqueueToken(EnqueueToken token, String eventId) {
        this(token.customerId, token.tokenIdentifierPrefix); 
          
        this.eventId = eventId;
        this.payload = token.payload;    
        this.expires = token.expires;  
        this.issued = token.issued;
    }

    public EnqueueToken(EnqueueToken token, IEnqueueTokenPayload payload) {
        this(token.customerId, token.tokenIdentifierPrefix); 
          
        this.eventId = token.eventId;
        this.payload = payload;    
        this.expires = token.expires; 
        this.issued = token.issued;
    }
    
    public EnqueueToken(String tokenIdentifier, String customerId, String eventId, long issued, long expires, IEnqueueTokenPayload payload)
    {
        this.tokenIdentifier = tokenIdentifier;
        this.customerId = customerId;
        this.eventId = eventId;
        this.issued = issued;
        this.expires = expires;
        this.payload = payload;
    }
        
    @Override
    public TokenVersion getTokenVersion() {
        return TokenVersion.QT1;
    }

    @Override
    public EncryptionType getEncryption() {
        return EncryptionType.AES256;
    }

    @Override
    public long getIssued() {
        return this.issued;
    }

    @Override
    public long getExpires() {
        return this.expires;
    }

    @Override
    public String getTokenIdentifier() {
        return this.tokenIdentifier;
    }

    @Override
    public String getCustomerId() {
        return this.customerId;
    }

    @Override
    public String getEventId() {
        return this.eventId;
    }

    @Override
    public IEnqueueTokenPayload getPayload() {
        return this.payload;
    }

    @Override
    public String getTokenWithoutHash() {
        return this.token;
    }

    @Override
    public String getHash() {
        return this.hash;
    }
    
    @Override
    public String getToken() {
        return this.token + "." + this.hash;
    }    

    void generate(String secretKey) throws TokenSerializationException {
        generate(secretKey, true);
    }

    void generate(String secretKey, boolean resetTokenIdentifier) throws TokenSerializationException {
        if (resetTokenIdentifier)
            this.tokenIdentifier = getTokenIdentifier(this.tokenIdentifierPrefix);
                              
        String serialized = serializeHeader() + ".";
        if (this.payload != null) {
            serialized = serialized + this.payload.encryptAndEncode(secretKey, tokenIdentifier);
        }
        this.token = serialized;

        this.hash = Base64UrlEncoder.encode(ShaHash.GenerateHash(serialized, secretKey));    
    }
    
    private String serializeHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"typ\":\"QT1\"");
        sb.append(",\"enc\":\"AES256\"");
        sb.append(",\"iss\":");
        sb.append(this.getIssued());
        if (this.getExpires() != Long.MAX_VALUE) {
            sb.append(",\"exp\":");
            sb.append(this.getExpires());
        }
        sb.append(",\"ti\":\"");
        sb.append(this.getTokenIdentifier());
        sb.append("\",\"c\":\"");
        sb.append(this.getCustomerId());
        sb.append("\"");
        if (this.getEventId() != null) {
            sb.append(",\"e\":\"");
            sb.append(this.getEventId());
            sb.append("\"");
        }
        sb.append("}");
        
        return Base64UrlEncoder.encode(sb.toString().getBytes(Charset.forName("UTF-8")));        
    } 
}
