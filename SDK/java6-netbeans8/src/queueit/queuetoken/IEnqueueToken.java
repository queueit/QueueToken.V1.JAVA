package queueit.queuetoken;

import java.util.*;

public interface IEnqueueToken {
    TokenVersion getTokenVersion();
    EncryptionType getEncryption();
    Date  getIssued();
    Date getExpires();
    UUID getTokenIdentifier();
    String getCustomerId();
    String getEventId();
    IEnqueueTokenBody getBody();
    
    String getToken(String secret);
    String getSignedToken(String secret);
    String getSignature(String secret);
}
