package facade.subsystem;

import facade.model.AudioTrack;
import facade.model.VideoFormat;

import java.util.Objects;

public final class MediaMuxer {
    public ConvertedVideo mux(String inputFileName, VideoStream video, AudioTrack audio, Codec targetCodec) {
        Objects.requireNonNull(inputFileName, "inputFileName must not be null");
        Objects.requireNonNull(video, "video must not be null");
        Objects.requireNonNull(audio, "audio must not be null");
        Objects.requireNonNull(targetCodec, "targetCodec must not be null");
        if (video.format() != targetCodec.format()) {
            throw new IllegalArgumentException("video stream format does not match the target container");
        }

        long videoBytes = video.frames().stream().mapToLong(frame -> frame.payloadBytes()).sum();
        long audioBytes = audio.samples().size() * (long) Short.BYTES;
        String outputFileName = convertedName(inputFileName, targetCodec.format());
        return new ConvertedVideo(
                outputFileName,
                targetCodec.format(),
                video.bitrateKbps(),
                video.duration(),
                video.frames().size(),
                audio.sampleRateHz(),
                audio.samples().size(),
                videoBytes + audioBytes + 256);
    }

    private String convertedName(String inputFileName, VideoFormat outputFormat) {
        int extensionIndex = inputFileName.lastIndexOf('.');
        String baseName = extensionIndex > 0 ? inputFileName.substring(0, extensionIndex) : inputFileName;
        return baseName + "-converted." + outputFormat.extension();
    }
}
