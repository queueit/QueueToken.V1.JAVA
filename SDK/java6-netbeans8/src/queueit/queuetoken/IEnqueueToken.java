package queueit.queuetoken;

public interface IEnqueueToken {
    TokenVersion getTokenVersion();
    EncryptionType getEncryption();
    long getIssued();
    long getExpires();
    String getTokenIdentifier();
    String getCustomerId();
    String getEventId();
    IEnqueueTokenPayload getPayload();
    
    String getToken();
    String getSignedToken();
    String getSignature();
}
