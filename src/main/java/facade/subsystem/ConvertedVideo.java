package facade.subsystem;

import facade.model.VideoFormat;

import java.time.Duration;
import java.util.Objects;

public record ConvertedVideo(
        String fileName,
        VideoFormat format,
        int videoBitrateKbps,
        Duration duration,
        int frameCount,
        int audioSampleRateHz,
        int audioSampleCount,
        long estimatedSizeBytes) {

    public ConvertedVideo {
        Objects.requireNonNull(fileName, "fileName must not be null");
        Objects.requireNonNull(format, "format must not be null");
        Objects.requireNonNull(duration, "duration must not be null");
    }
}
