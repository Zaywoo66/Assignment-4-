package facade.api;

import facade.model.VideoAsset;
import facade.model.VideoFormat;
import facade.subsystem.AudioMixer;
import facade.subsystem.BitrateReader;
import facade.subsystem.Codec;
import facade.subsystem.CodecFactory;
import facade.subsystem.ConvertedVideo;
import facade.subsystem.MediaMuxer;
import facade.subsystem.VideoStream;

import java.util.Objects;

/** One-call entry point for the video conversion subsystem. */
public final class VideoConversionFacade {
    private final CodecFactory codecFactory = new CodecFactory();
    private final BitrateReader bitrateReader = new BitrateReader();
    private final AudioMixer audioMixer = new AudioMixer();
    private final MediaMuxer mediaMuxer = new MediaMuxer();

    public ConversionResult convert(VideoAsset input, VideoFormat outputFormat) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(outputFormat, "outputFormat must not be null");

        try {
            Codec sourceCodec = codecFactory.codecFor(input.format());
            Codec targetCodec = codecFactory.codecFor(outputFormat);
            VideoStream decodedVideo = bitrateReader.read(input, sourceCodec);
            VideoStream encodedVideo = bitrateReader.transcode(decodedVideo, targetCodec);
            var mixedAudio = audioMixer.mix(input.audioTracks(), targetCodec.audioSampleRateHz());
            ConvertedVideo converted = mediaMuxer.mux(input.fileName(), encodedVideo, mixedAudio, targetCodec);
            return toResult(converted);
        } catch (RuntimeException exception) {
            throw new ConversionException(
                    "Could not convert " + input.fileName() + " to " + outputFormat + ": "
                            + exception.getMessage(),
                    exception);
        }
    }

    private ConversionResult toResult(ConvertedVideo converted) {
        return new ConversionResult(
                converted.fileName(),
                converted.format(),
                converted.videoBitrateKbps(),
                converted.duration(),
                converted.frameCount(),
                converted.audioSampleRateHz(),
                converted.audioSampleCount(),
                converted.estimatedSizeBytes());
    }
}
