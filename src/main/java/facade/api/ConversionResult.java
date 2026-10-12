package facade.api;

import facade.model.VideoFormat;

import java.time.Duration;
import java.util.Objects;

public record ConversionResult(
        String outputFileName,
        VideoFormat format,
        int videoBitrateKbps,
        Duration duration,
        int frameCount,
        int audioSampleRateHz,
        int audioSampleCount,
        long estimatedSizeBytes) {

    public ConversionResult {
        Objects.requireNonNull(outputFileName, "outputFileName must not be null");
        Objects.requireNonNull(format, "format must not be null");
        Objects.requireNonNull(duration, "duration must not be null");
        if (videoBitrateKbps <= 0 || frameCount <= 0 || estimatedSizeBytes <= 0) {
            throw new IllegalArgumentException("conversion result values must be positive");
        }
        if (audioSampleRateHz < 0 || audioSampleCount < 0) {
            throw new IllegalArgumentException("audio result values must not be negative");
        }
    }
}
