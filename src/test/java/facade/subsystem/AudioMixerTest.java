package facade.subsystem;

import facade.model.AudioTrack;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AudioMixerTest {
    private final AudioMixer mixer = new AudioMixer();

    @Test
    void mixesTracksAndNormalizesThePeakSample() {
        AudioTrack first = new AudioTrack("voice", 48_000, List.of(1_000, -1_000));
        AudioTrack second = new AudioTrack("music", 48_000, List.of(3_000, 1_000));

        AudioTrack mixed = mixer.mix(List.of(first, second), 48_000);

        assertEquals("mixed audio", mixed.name());
        assertEquals(48_000, mixed.sampleRateHz());
        assertEquals(List.of(32_767, 0), mixed.samples());
    }

    @Test
    void returnsEmptyAudioWhenThereAreNoTracks() {
        AudioTrack mixed = mixer.mix(List.of(), 44_100);

        assertEquals(44_100, mixed.sampleRateHz());
        assertEquals(List.of(), mixed.samples());
    }
}
