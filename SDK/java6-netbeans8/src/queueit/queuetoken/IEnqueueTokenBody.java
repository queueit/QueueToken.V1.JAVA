package queueit.queuetoken;

public interface IEnqueueTokenBody {
    String getKey();
    Double getRank();
    String getCustomDataValue(String key);
    String serialize();
}
