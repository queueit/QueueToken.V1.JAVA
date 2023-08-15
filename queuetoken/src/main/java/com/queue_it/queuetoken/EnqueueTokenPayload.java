package com.queue_it.queuetoken;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.queue_it.queuetoken.enums.TokenOrigin;
import com.queue_it.queuetoken.models.PayloadDto;
import com.queue_it.queuetoken.security.AesEncryption;
import com.queue_it.queuetoken.security.Base64UrlEncoding;

public class EnqueueTokenPayload implements IEnqueueTokenPayload {

    private String key;
    private Double relativeQuality;
    private Map customData;
    private TokenOrigin origin;
    private PayloadDto payloadDto;

    public EnqueueTokenPayload() {
        this.customData = new HashMap();
    }

    public EnqueueTokenPayload(EnqueueTokenPayload payload, String key) {
        this.key = key;
        this.relativeQuality = payload.relativeQuality;
        this.customData = payload.customData;
    }

    public EnqueueTokenPayload(EnqueueTokenPayload payload, double relativeQuality) {
        this.key = payload.key;
        this.relativeQuality = relativeQuality;
        this.customData = payload.customData;
    }

    public EnqueueTokenPayload(EnqueueTokenPayload payload, String customDataKey, String customDataValue) {
        this.key = payload.key;
        this.relativeQuality = payload.relativeQuality;
        this.customData = payload.customData;
        this.customData.put(customDataKey, customDataValue);
    }

    public EnqueueTokenPayload(String key, double relativeQuality, Map<String, String> customData) {
        this.key = key;
        this.relativeQuality = relativeQuality;
        this.customData = customData;
        setPayloadDto(this.key, this.relativeQuality, this.customData);
    }

    public EnqueueTokenPayload(EnqueueTokenPayload payload, TokenOrigin origin) {
        this.key = payload.key;
        this.relativeQuality = payload.relativeQuality;
        this.customData = payload.customData;
        this.origin = origin;
        setPayloadDto(this.key, this.relativeQuality, this.customData);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Double getRelativeQuality() {
        return this.relativeQuality;
    }

    @Override
    public String[] getCustomDataKeys() {
        String[] keys = new String[this.customData.size()];

        int index = 0;
        for (Object key : customData.keySet()) {
            keys[index] = key.toString();
        }

        return keys;
    }

    @Override
    public String getCustomDataValue(String key) {
        Object value = this.customData.get(key);
        if (value == null)
            return null;

        return (String) value;
    }

    @Override
    public TokenOrigin getTokenOrigin() {
        return this.origin;
    }

    @Override
    public String encryptAndEncode(String secretKey, String tokenIdentifier) throws TokenSerializationException {
        try {
            byte[] inputBytes = this.serialize().getBytes(Charset.forName(StandardCharsets.UTF_8.name()));

            byte[] encrypted = AesEncryption.encrypt(inputBytes, secretKey, tokenIdentifier);

            return Base64UrlEncoding.encode(encrypted);

        } catch (Exception ex) {
            throw new TokenSerializationException(ex);
        }
    }

    public String serialize() {
        boolean addComma = false;

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        if (this.relativeQuality != null) {
            sb.append("\"r\":");
            sb.append(this.relativeQuality);
            addComma = true;
        }

        if (this.key != null) {
            if (addComma) {
                sb.append(",");
            }
            sb.append("\"k\":\"");
            sb.append(this.key.replaceAll("\"", "\\\""));
            sb.append("\"");
            addComma = true;
        }

        boolean addCustomDataComma = false;
        if (!this.customData.isEmpty()) {
            if (addComma) {
                sb.append(",");
            }
            sb.append("\"cd\":{");

            for (Object key : customData.keySet()) {
                Object value = this.customData.get(key);
                if (addCustomDataComma) {
                    sb.append(",");
                }

                sb.append("\"");
                sb.append(key.toString().replaceAll("\"", "\\\""));
                sb.append("\":\"");
                sb.append(value.toString().replaceAll("\"", "\\\""));
                sb.append("\"");
                addCustomDataComma = true;
            }

            sb.append("}");
            addComma = true;
        }
        sb.append("}");
        return sb.toString();
    }

    private void setPayloadDto(String key, Double relativeQuality, Map<String, String> customData) {
        payloadDto = new PayloadDto(key, relativeQuality, customData);
    }
}