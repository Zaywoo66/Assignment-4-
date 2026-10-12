package facade.subsystem;

import facade.model.VideoFormat;

import java.util.Objects;

public final class CodecFactory {
    public Codec codecFor(VideoFormat format) {
        Objects.requireNonNull(format, "format must not be null");
        return switch (format) {
            case MP4 -> new Codec(format, "H.264", "AAC", 4_500, 48_000);
            case MOV -> new Codec(format, "ProRes", "AAC", 8_000, 48_000);
            case AVI -> new Codec(format, "MPEG-4", "MP3", 3_200, 44_100);
            case WEBM -> new Codec(format, "VP9", "Opus", 2_500, 48_000);
        };
    }
}
