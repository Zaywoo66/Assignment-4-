package facade.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VideoAssetTest {
    @Test
    void rejectsFileExtensionThatDoesNotMatchItsFormat() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VideoAsset(
                        "lecture.mp4",
                        VideoFormat.AVI,
                        2_000,
                        Duration.ofSeconds(1),
                        List.of(new VideoFrame(0, 1_000)),
                        List.of()));

        assertEquals("file extension does not match its format", exception.getMessage());
    }

    @Test
    void rejectsFramesOutsideTheVideoDuration() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new VideoAsset(
                        "lecture.avi",
                        VideoFormat.AVI,
                        2_000,
                        Duration.ofSeconds(1),
                        List.of(new VideoFrame(1_000, 1_000)),
                        List.of()));

        assertEquals("frame timestamps must increase within the video duration", exception.getMessage());
    }
}
