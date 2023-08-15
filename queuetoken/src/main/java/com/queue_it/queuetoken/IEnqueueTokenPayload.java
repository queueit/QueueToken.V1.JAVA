package com.queue_it.queuetoken;

import com.queue_it.queuetoken.enums.TokenOrigin;

public interface IEnqueueTokenPayload {
    String getKey();

    Double getRelativeQuality();

    String[] getCustomDataKeys();

    String getCustomDataValue(String key);

    TokenOrigin getTokenOrigin();

    String encryptAndEncode(String secretKey, String tokenIdentifier) throws TokenSerializationException;
}
