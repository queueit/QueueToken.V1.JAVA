package queueit.queuetoken;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

class EnqueueToken implements IEnqueueToken {

    private final String customerId;
    private String eventId;
    private IEnqueueTokenPayload payload;
    private long issued;
    private long expires = Long.MAX_VALUE;
    private String tokenIdentifier;
    private String serializedToken;
    private String signature;

    public EnqueueToken(String customerId) {
        this.customerId = customerId;
        this.issued = System.currentTimeMillis();
        this.tokenIdentifier = UUID.randomUUID().toString();
    }
    
    public EnqueueToken(EnqueueToken token, Long expires) {
        this(token.customerId); 
        
        this.eventId = token.eventId;
        this.payload = token.payload;    
        this.expires = expires;
        this.issued = token.issued;
    }
    
    public EnqueueToken(EnqueueToken token, String eventId) {
        this(token.customerId); 
          
        this.eventId = eventId;
        this.payload = token.payload;    
        this.expires = token.expires;  
        this.issued = token.issued;
    }

    public EnqueueToken(EnqueueToken token, IEnqueueTokenPayload payload) {
        this(token.customerId); 
          
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
    public String getToken() {
        return this.serializedToken;
    }

    @Override
    public String getSignature() {
        return this.signature;
    }
    
    @Override
    public String getSignedToken() {
        return this.serializedToken + "." + this.signature;
    }    

    void generate(String secretKey) throws TokenSerializationException {
        generate(secretKey, true);
    }

    void generate(String secretKey, boolean resetTokenIdentifier) throws TokenSerializationException {
        if (resetTokenIdentifier)
            this.tokenIdentifier = UUID.randomUUID().toString();
        
        AesEncryption aes = new AesEncryption(secretKey, tokenIdentifier);
                        
        String serialized = serializeHeader() + ".";
        if (this.payload != null) {
            String payloadJson = this.payload.serialize();
            serialized = serialized + this.serializePayload(payloadJson, aes);
        }
        this.serializedToken = serialized;

        ShaSignature sha = new ShaSignature(secretKey);

        this.signature = Base64UrlEncoder.encode(sha.GenerateSignature(serialized));    
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
    
    private String serializePayload(String input, AesEncryption aes) throws TokenSerializationException {   
        try {
            byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
                        
            byte[] encrypted = aes.Encrypt(inputBytes);
            
            return Base64UrlEncoder.encode(encrypted);

        } catch (Exception ex) {
            throw new TokenSerializationException(ex);
        } 
    }  
}
