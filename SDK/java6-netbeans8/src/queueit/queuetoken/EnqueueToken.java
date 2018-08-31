package queueit.queuetoken;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

class EnqueueToken implements IEnqueueToken {

    private final String customerId;
    private String eventId;
    private IEnqueueTokenBody body;
    private long issued;
    private long validityMillis = Long.MAX_VALUE;
    private UUID tokenIdentifier;
    private String serializedToken;
    private String signature;

    public EnqueueToken(String customerId) {
        this.customerId = customerId;
        this.issued = System.currentTimeMillis();
    }
    
    public EnqueueToken(EnqueueToken token, long validityMillis) {
        this(token.customerId); 
          
        this.eventId = token.eventId;
        this.body = token.body;    
        this.validityMillis = validityMillis;     
    }
    
    public EnqueueToken(EnqueueToken token, String eventId) {
        this(token.customerId); 
          
        this.eventId = eventId;
        this.body = token.body;    
        this.validityMillis = token.validityMillis;     
    }

    public EnqueueToken(EnqueueToken token, IEnqueueTokenBody body) {
        this(token.customerId); 
          
        this.eventId = token.eventId;
        this.body = body;    
        this.validityMillis = token.validityMillis;     
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
    public Date getIssued() {
        return new Date(this.issued);
    }

    @Override
    public Date getExpires() {
        if (this.validityMillis == Long.MAX_VALUE)
            return new Date(Long.MAX_VALUE);
        return new Date(this.issued + validityMillis);
    }

    @Override
    public UUID getTokenIdentifier() {
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
    public IEnqueueTokenBody getBody() {
        return this.body;
    }

    @Override
    public String getToken(String secret) {
        return this.serializedToken;
    }

    @Override
    public String getSignature(String secret) {
        return this.signature;
    }
    
    @Override
    public String getSignedToken(String secret) {
        return this.serializedToken + "." + this.signature;
    }    
    public void generate(String secretKey) throws TokenSerializationException {
        try {
            this.tokenIdentifier = UUID.randomUUID();
            this.issued = System.currentTimeMillis();
            
            String serialized = serializeHeader();
            if (this.body != null) {
                String bodyJson = this.body.serialize();
                serialized = serialized + "." + this.serializeBody(bodyJson, secretKey);
            }
            this.serializedToken = serialized;
                        
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] signatureBytes = digest.digest((serializedToken + secretKey).getBytes(Charset.forName("UTF-8")));
            this.signature = DatatypeConverter.printBase64Binary(signatureBytes);    
            
        } catch (NoSuchAlgorithmException ex) {
            throw new TokenSerializationException(ex);
        }
    }  
    
    private String serializeHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"typ\":\"QT1\"");
        sb.append(",\"enc\":\"AES256\"");
        sb.append(",\"iss\":");
        sb.append(this.getIssued());
        sb.append(",\"exp\":");
        sb.append(this.getExpires());
        sb.append(",\"ti\":");
        sb.append(this.getTokenIdentifier());
        sb.append(",\"c\":");
        sb.append(this.getCustomerId());
        if (this.getEventId() != null) {
            sb.append(",\"e\":");
            sb.append(this.getEventId());
        }
        sb.append("}");
        
        return DatatypeConverter.printBase64Binary(sb.toString().getBytes(Charset.forName("UTF-8")));        
    }
    
    private String serializeBody(String input, String secretKey) throws TokenSerializationException {   
        try {
            Cipher cipher = Cipher.getInstance("AES");

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] keyBytes = digest.digest(secretKey.getBytes(Charset.forName("UTF-8")));
        
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            
            byte[] inputBytes = input.getBytes(Charset.forName("UTF-8"));
            
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted= new byte[cipher.getOutputSize(inputBytes.length)];
            int enc_len = cipher.update(inputBytes, 0, inputBytes.length, encrypted, 0);
            enc_len += cipher.doFinal(encrypted, enc_len);
            
            return DatatypeConverter.printBase64Binary(encrypted);

        } catch (Exception ex) {
            throw new TokenSerializationException(ex);
        } 
    }
}
