package facade.subsystem;

import facade.model.VideoFormat;

import java.util.Objects;

public record Codec(
        VideoFormat format,
        String videoCodec,
        String audioCodec,
        int maximumVideoBitrateKbps,
        int audioSampleRateHz) {

    public Codec {
        Objects.requireNonNull(format, "format must not be null");
        Objects.requireNonNull(videoCodec, "videoCodec must not be null");
        Objects.requireNonNull(audioCodec, "audioCodec must not be null");
        if (maximumVideoBitrateKbps <= 0 || audioSampleRateHz <= 0) {
            throw new IllegalArgumentException("codec limits must be positive");
        }
    }
}
