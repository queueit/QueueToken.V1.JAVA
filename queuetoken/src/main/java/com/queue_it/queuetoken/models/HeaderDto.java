package com.queue_it.queuetoken.models;

import java.nio.charset.StandardCharsets;

import com.queue_it.queuetoken.IEnqueueTokenPayload;
import com.queue_it.queuetoken.enums.EncryptionType;
import com.queue_it.queuetoken.enums.TokenVersion;
import com.queue_it.queuetoken.security.Base64UrlEncoding;

public class HeaderDto {

    private final String customerId;
    private String eventId;
    private String ipAddress;
    private String xForwaredFor;
    private IEnqueueTokenPayload payload;
    private long issued;
    private long expires = Long.MAX_VALUE;
    private String tokenIdentifier;
    private String token;
    private String hash;

    public HeaderDto(String tokenIdentifier, String customerId, String eventId, long issued, long expires,
            String ipAddress, String xForwaredFor, IEnqueueTokenPayload payload) {
        this.tokenIdentifier = tokenIdentifier;
        this.customerId = customerId;
        this.eventId = eventId;
        this.issued = issued;
        this.expires = expires;
        this.ipAddress = ipAddress;
        this.xForwaredFor = xForwaredFor;
        this.payload = payload;
    }

    public TokenVersion getTokenVersion() {
        return TokenVersion.QT1;
    }

    public EncryptionType getEncryption() {
        return EncryptionType.AES256;
    }

    public long getIssued() {
        return this.issued;
    }

    public long getExpires() {
        return this.expires;
    }

    public String getTokenIdentifier() {
        return this.tokenIdentifier;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public String getEventId() {
        return this.eventId;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getXForwardedFor() {
        return this.xForwaredFor;
    }

    public IEnqueueTokenPayload getPayload() {
        return this.payload;
    }

    public String getTokenWithoutHash() {
        return this.token;
    }

    public String getHash() {
        return this.hash;
    }

    public String getToken() {
        return this.token + "." + this.hash;
    }

    public String serialize() {

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

        return Base64UrlEncoding.encode(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
