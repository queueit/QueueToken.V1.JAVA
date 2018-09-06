package queueit.queuetoken;

public interface IEnqueueTokenPayload {
    String getKey();
    Double getRank();
    String[] getCustomDataKeys();
    String getCustomDataValue(String key);
    String serialize();
}
