package queueit.queuetoken;

public interface IEnqueueTokenPayload {
    String getKey();
    Double getRank();
    String getCustomDataValue(String key);
    String serialize();
}
