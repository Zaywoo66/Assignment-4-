package facade.api;

import facade.model.AudioTrack;
import facade.model.VideoAsset;
import facade.model.VideoFormat;
import facade.model.VideoFrame;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VideoConversionFacadeTest {
    private final VideoConversionFacade facade = new VideoConversionFacade();

    @Test
    void convertsAviInputToMp4AndReturnsConversionSummary() {
        ConversionResult result = facade.convert(sampleVideo(), VideoFormat.MP4);

        assertEquals("lecture-converted.mp4", result.outputFileName());
        assertEquals(VideoFormat.MP4, result.format());
        assertEquals(4_500, result.videoBitrateKbps());
        assertEquals(Duration.ofSeconds(2), result.duration());
        assertEquals(4, result.frameCount());
        assertEquals(48_000, result.audioSampleRateHz());
        assertEquals(4, result.audioSampleCount());
        assertTrue(result.estimatedSizeBytes() > 0);
    }

    @Test
    void convertsAviInputToWebmUsingTargetBitrateLimit() {
        ConversionResult result = facade.convert(sampleVideo(), VideoFormat.WEBM);

        assertEquals("lecture-converted.webm", result.outputFileName());
        assertEquals(VideoFormat.WEBM, result.format());
        assertEquals(2_500, result.videoBitrateKbps());
        assertEquals(4, result.frameCount());
    }

    @Test
    void rejectsNullInputBeforeStartingConversion() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> facade.convert(null, VideoFormat.MP4));

        assertEquals("input must not be null", exception.getMessage());
    }

    @Test
    void rejectsNullOutputFormatBeforeStartingConversion() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> facade.convert(sampleVideo(), null));

        assertEquals("outputFormat must not be null", exception.getMessage());
    }

    private VideoAsset sampleVideo() {
        return new VideoAsset(
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
    }
}
