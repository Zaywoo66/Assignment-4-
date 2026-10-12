package facade.model;

public record VideoFrame(long timestampMillis, long payloadBytes) {
    public VideoFrame {
        if (timestampMillis < 0) {
            throw new IllegalArgumentException("timestamp must not be negative");
        }
        if (payloadBytes <= 0) {
            throw new IllegalArgumentException("frame payload must be positive");
        }
    }
}
