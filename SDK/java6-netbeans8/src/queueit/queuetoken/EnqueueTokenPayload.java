package queueit.queuetoken;

import java.nio.charset.Charset;
import java.util.*;

class EnqueueTokenPayload implements IEnqueueTokenPayload {

    private String key;
    private Double relativeQuality;
    private Map customData;

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
        
        return (String)value;
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
        if (this.customData.size() > 0) {
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
    
    public String encryptAndEncode(String secretKey, String tokenIdentifier) throws TokenSerializationException {   
        try {
            byte[] inputBytes = this.serialize().getBytes(Charset.forName("UTF-8"));
                        
            byte[] encrypted = AesEncryption.Encrypt(inputBytes, secretKey, tokenIdentifier);
            
            return Base64UrlEncoder.encode(encrypted);

        } catch (Exception ex) {
            throw new TokenSerializationException(ex);
        } 
    } 
}