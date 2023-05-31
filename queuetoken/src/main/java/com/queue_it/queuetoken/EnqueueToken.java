package com.queue_it.queuetoken;

import java.nio.charset.Charset;
import java.util.*;

import com.queue_it.queuetoken.models.EncryptionType;
import com.queue_it.queuetoken.models.TokenVersion;

class EnqueueToken implements IEnqueueToken {

    private final String customerId;
    private String eventId;
    private String ipAddress;
    private String xForwaredFor;
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

    public EnqueueToken(String tokenIdentifier, String customerId, String eventId, long issued, long expires,
            String ipAddress, String xForwaredFor, IEnqueueTokenPayload payload) {
        this.tokenIdentifier = tokenIdentifier;
        this.customerId = customerId;
        this.eventId = eventId;
        this.ipAddress = ipAddress;
        this.xForwaredFor = xForwaredFor;
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
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public String getXForwardedFor() {
        return this.xForwaredFor;
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

    private String getTokenIdentifier(String tokenIdentifierPrefix1) {
        return tokenIdentifierPrefix1 == null || tokenIdentifierPrefix1.isEmpty() ? UUID.randomUUID().toString()
                : tokenIdentifierPrefix1 + "~" + UUID.randomUUID().toString();
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

        this.hash = Base64UrlEncoding.encode(ShaHash.generateHash(serialized, secretKey));
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
        if (this.getIpAddress() != null) {
            sb.append(",\"ip\":\"");
            sb.append(this.getIpAddress());
            sb.append("\"");
        }
        if (this.getXForwardedFor() != null) {
            sb.append(",\"xff\":\"");
            sb.append(this.getXForwardedFor());
            sb.append("\"");
        }
        sb.append("}");

        return Base64UrlEncoding.encode(sb.toString().getBytes(Charset.forName("UTF-8")));
    }

    static EnqueueToken addIPAddress(EnqueueToken token, String ipAddress, String xForwaredFor) {
        return new EnqueueToken(token.getTokenIdentifier(), token.getCustomerId(), token.getEventId(),
                token.getIssued(), token.getExpires(), ipAddress, xForwaredFor, token.getPayload());
    }

    static EnqueueToken addEventId(EnqueueToken token, String eventId) {
        return new EnqueueToken(token.getTokenIdentifier(), token.getCustomerId(), eventId, token.getIssued(),
                token.getExpires(), token.getIpAddress(), token.getXForwardedFor(), token.getPayload());
    }

    static EnqueueToken addExpires(EnqueueToken token, Long expires) {
        return new EnqueueToken(token.getTokenIdentifier(), token.getCustomerId(), token.getEventId(),
                token.getIssued(), expires, token.getIpAddress(), token.getXForwardedFor(), token.getPayload());
    }

    static EnqueueToken addPayload(EnqueueToken token, IEnqueueTokenPayload payload) {
        return new EnqueueToken(token.getTokenIdentifier(), token.getCustomerId(), token.getEventId(),
                token.getIssued(), token.getExpires(), token.getIpAddress(), token.getXForwardedFor(), payload);
    }
}
