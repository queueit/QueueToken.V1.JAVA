package com.queue_it.queuetoken;

public interface IEnqueueToken {
    TokenVersion getTokenVersion();
    EncryptionType getEncryption();
    long getIssued();
    long getExpires();
    String getTokenIdentifier();
    String getCustomerId();
    String getEventId();
    String getIpAddress();
    String getXForwardedFor();
    IEnqueueTokenPayload getPayload();
    
    String getTokenWithoutHash();
    String getToken();
    String getHash();
}
