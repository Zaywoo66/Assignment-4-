package facade.model;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public record VideoAsset(
        String fileName,
        VideoFormat format,
        int bitrateKbps,
        Duration duration,
        List<VideoFrame> frames,
        List<AudioTrack> audioTracks) {

    public VideoAsset {
        Objects.requireNonNull(fileName, "fileName must not be null");
        Objects.requireNonNull(format, "format must not be null");
        Objects.requireNonNull(duration, "duration must not be null");
        Objects.requireNonNull(frames, "frames must not be null");
        Objects.requireNonNull(audioTracks, "audioTracks must not be null");
        if (fileName.isBlank()) {
            throw new IllegalArgumentException("fileName must not be blank");
        }
        if (!fileName.toLowerCase(Locale.ROOT).endsWith("." + format.extension())) {
            throw new IllegalArgumentException("file extension does not match its format");
        }
        if (bitrateKbps <= 0) {
            throw new IllegalArgumentException("bitrate must be positive");
        }
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }
        if (frames.isEmpty()) {
            throw new IllegalArgumentException("video must contain at least one frame");
        }

        frames = List.copyOf(frames);
        audioTracks = List.copyOf(audioTracks);
        validateFrameOrder(frames, duration.toMillis());
    }

    private static void validateFrameOrder(List<VideoFrame> frames, long durationMillis) {
        long previousTimestamp = -1;
        for (VideoFrame frame : frames) {
            Objects.requireNonNull(frame, "frame must not be null");
            if (frame.timestampMillis() <= previousTimestamp
                    || frame.timestampMillis() >= durationMillis) {
                throw new IllegalArgumentException("frame timestamps must increase within the video duration");
            }
            previousTimestamp = frame.timestampMillis();
        }
    }
}
