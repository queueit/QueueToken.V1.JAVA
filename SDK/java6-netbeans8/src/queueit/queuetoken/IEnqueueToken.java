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
    
    String getTokenWithoutHash();
    String getToken();
    String getHash();
}
