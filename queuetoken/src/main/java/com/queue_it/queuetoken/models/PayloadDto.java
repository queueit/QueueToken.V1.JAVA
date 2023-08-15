package com.queue_it.queuetoken.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import com.queue_it.queuetoken.EnqueueTokenPayload;
import com.queue_it.queuetoken.TokenSerializationException;
import com.queue_it.queuetoken.security.AesEncryption;
import com.queue_it.queuetoken.security.Base64UrlEncoding;

public class PayloadDto {

    private String key;
    private Double relativeQuality;
    private Map<String, String> customData;

    public PayloadDto(String key, Double relativeQuality, Map<String, String> customData) {
        this.key = key;
        this.relativeQuality = relativeQuality;
        this.customData = customData;
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
            sb.append(key.replaceAll("\"", "\\\""));
            sb.append("\"");
            addComma = true;
        }

        boolean addCustomDataComma = false;
        if (this.customData != null && !this.customData.isEmpty()) {
            if (addComma) {
                sb.append(",");
            }

            sb.append("\"cd\":{");

            for (Object customDataKey : this.customData.keySet()) {
                Object value = this.customData.get(customDataKey);
                if (addCustomDataComma) {
                    sb.append(",");
                }

                sb.append("\"");
                sb.append(customDataKey.toString().replaceAll("\"", "\\\""));
                sb.append("\":\"");
                sb.append(value.toString().replaceAll("\"", "\\\""));
                sb.append("\"");
                addCustomDataComma = true;
            }

            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }

    public static EnqueueTokenPayload deserialize(String input, String secretKey, String tokenIdentifier)
            throws TokenSerializationException, ClassNotFoundException, IOException {
        byte[] headerEncrypted = Base64UrlEncoding.decode(input);
        byte[] decryptedPayload = AesEncryption.decrypt(headerEncrypted, secretKey, tokenIdentifier);
        ByteArrayInputStream in = new ByteArrayInputStream(decryptedPayload);
        ObjectInputStream is = new ObjectInputStream(in);
        return (EnqueueTokenPayload) is.readObject();
    }
}
