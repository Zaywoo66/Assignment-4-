package facade.model;

import java.util.List;
import java.util.Objects;

public record AudioTrack(String name, int sampleRateHz, List<Integer> samples) {
    public AudioTrack {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(samples, "samples must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (sampleRateHz <= 0) {
            throw new IllegalArgumentException("sample rate must be positive");
        }
        if (samples.stream().anyMatch(sample -> sample == null
                || sample < Short.MIN_VALUE || sample > Short.MAX_VALUE)) {
            throw new IllegalArgumentException("audio samples must fit in a signed 16-bit value");
        }
        samples = List.copyOf(samples);
    }
}
