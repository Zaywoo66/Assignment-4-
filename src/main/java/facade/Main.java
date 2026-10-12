package facade;

import facade.api.ConversionResult;
import facade.api.VideoConversionFacade;
import facade.model.AudioTrack;
import facade.model.VideoAsset;
import facade.model.VideoFormat;
import facade.model.VideoFrame;

import java.time.Duration;
import java.util.List;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        VideoAsset sample = new VideoAsset(
                "lecture.avi",
                VideoFormat.AVI,
                5_200,
                Duration.ofSeconds(2),
                List.of(
                        new VideoFrame(0, 42_000),
                        new VideoFrame(500, 57_000),
                        new VideoFrame(1_000, 61_000),
                        new VideoFrame(1_500, 48_000)),
                List.of(
                        new AudioTrack("voice", 44_100, List.of(4_000, 12_000, -8_000, 6_000)),
                        new AudioTrack("music", 48_000, List.of(2_000, -3_000, 5_000, 7_000))));

        VideoConversionFacade converter = new VideoConversionFacade();
        printResult(converter.convert(sample, VideoFormat.MP4));
        printResult(converter.convert(sample, VideoFormat.WEBM));
    }

    private static void printResult(ConversionResult result) {
        System.out.printf("Output: %s%n", result.outputFileName());
        System.out.printf("Format: %s | video: %d kbps | frames: %d%n",
                result.format(), result.videoBitrateKbps(), result.frameCount());
        System.out.printf("Audio: %d Hz | %d samples | estimated size: %d bytes%n%n",
                result.audioSampleRateHz(), result.audioSampleCount(), result.estimatedSizeBytes());
    }
}
