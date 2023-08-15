package com.queue_it.queuetoken;

import com.queue_it.queuetoken.enums.TokenOrigin;

public class EnqueueTokenPayloadGenerator {
    private EnqueueTokenPayload payload;

    public EnqueueTokenPayloadGenerator() {
        this.payload = new EnqueueTokenPayload();
    }

    public EnqueueTokenPayloadGenerator withKey(String key) {
        this.payload = new EnqueueTokenPayload(this.payload, key);
        return this;
    }

    public EnqueueTokenPayloadGenerator withRelativeQuality(double relativeQuality) {
        this.payload = new EnqueueTokenPayload(this.payload, relativeQuality);
        return this;
    }

    public EnqueueTokenPayloadGenerator withCustomData(String key, String value) {
        this.payload = new EnqueueTokenPayload(this.payload, key, value);
        return this;
    }

    public EnqueueTokenPayloadGenerator withTokenOrigin(String origin) {
        this.payload = new EnqueueTokenPayload(this.payload, Enum.valueOf(TokenOrigin.class, origin));
        return this;
    }

    public IEnqueueTokenPayload generate() {
        return this.payload;
    }
}
