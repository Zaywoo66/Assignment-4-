package facade.subsystem;

import facade.model.VideoFormat;
import facade.model.VideoFrame;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record VideoStream(
        VideoFormat format,
        int bitrateKbps,
        Duration duration,
        List<VideoFrame> frames) {

    public VideoStream {
        Objects.requireNonNull(format, "format must not be null");
        Objects.requireNonNull(duration, "duration must not be null");
        Objects.requireNonNull(frames, "frames must not be null");
        if (bitrateKbps <= 0 || duration.isZero() || duration.isNegative() || frames.isEmpty()) {
            throw new IllegalArgumentException("video stream must have a positive bitrate, duration, and frames");
        }
        frames = List.copyOf(frames);
    }
}
