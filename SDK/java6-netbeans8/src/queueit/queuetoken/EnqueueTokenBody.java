package queueit.queuetoken;

import java.util.*;

class EnqueueTokenBody implements IEnqueueTokenBody {

    private String key;
    private Double rank;
    private Map customData;

    public EnqueueTokenBody() {
        this.customData = new HashMap();
    }

    public EnqueueTokenBody(EnqueueTokenBody body, String key) {
        this.key = key;
        this.rank = body.rank;
        this.customData = body.customData;
    }

    public EnqueueTokenBody(EnqueueTokenBody body, double rank) {
        this.key = body.key;
        this.rank = rank;
        this.customData = body.customData;
    }
    
    public EnqueueTokenBody(EnqueueTokenBody body, String customDataKey, String customDataValue) {
        this.key = body.key;
        this.rank = body.rank;
        this.customData = body.customData;
        this.customData.put(customDataKey, customDataValue);
    }
        
    @Override
    public String getKey() {
        return this.key;
    }
    
    @Override
    public Double getRank() {
        return this.rank;
    }
        
    @Override
    public String getCustomDataValue(String key) {      
        Object value = this.customData.get(key);
        if (value == null)
            return null;
        
        return (String)value;
    }
    
    @Override
    public String serialize() {
        boolean addComma = false;
        
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        
        if (this.rank != null) {
            sb.append("\"r\":");
            sb.append(this.rank);
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
   
}
