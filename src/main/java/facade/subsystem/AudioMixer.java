package facade.subsystem;

import facade.model.AudioTrack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class AudioMixer {
    public AudioTrack mix(List<AudioTrack> tracks, int outputSampleRateHz) {
        Objects.requireNonNull(tracks, "tracks must not be null");
        if (outputSampleRateHz <= 0) {
            throw new IllegalArgumentException("output sample rate must be positive");
        }
        if (tracks.isEmpty()) {
            return new AudioTrack("mixed audio", outputSampleRateHz, List.of());
        }

        List<List<Integer>> resampledTracks = tracks.stream()
                .map(track -> resample(Objects.requireNonNull(track, "track must not be null"), outputSampleRateHz))
                .toList();
        int sampleCount = resampledTracks.stream().mapToInt(List::size).max().orElse(0);
        List<Integer> mixedSamples = new ArrayList<>(sampleCount);

        for (int sampleIndex = 0; sampleIndex < sampleCount; sampleIndex++) {
            long sum = 0;
            int activeTracks = 0;
            for (List<Integer> samples : resampledTracks) {
                if (sampleIndex < samples.size()) {
                    sum += samples.get(sampleIndex);
                    activeTracks++;
                }
            }
            mixedSamples.add((int) (sum / activeTracks));
        }
        return new AudioTrack("mixed audio", outputSampleRateHz, normalize(mixedSamples));
    }

    private List<Integer> resample(AudioTrack track, int outputSampleRateHz) {
        if (track.sampleRateHz() == outputSampleRateHz || track.samples().isEmpty()) {
            return track.samples();
        }
        int outputCount = Math.max(1, (int) Math.round(
                track.samples().size() * outputSampleRateHz / (double) track.sampleRateHz()));
        List<Integer> samples = new ArrayList<>(outputCount);
        for (int index = 0; index < outputCount; index++) {
            int sourceIndex = Math.min(track.samples().size() - 1,
                    (int) (index * track.sampleRateHz() / (double) outputSampleRateHz));
            samples.add(track.samples().get(sourceIndex));
        }
        return samples;
    }

    private List<Integer> normalize(List<Integer> samples) {
        int peak = samples.stream().mapToInt(Math::abs).max().orElse(0);
        if (peak == 0) {
            return List.copyOf(samples);
        }
        double gain = Short.MAX_VALUE / (double) peak;
        return samples.stream()
                .map(sample -> (int) Math.round(sample * gain))
                .toList();
    }
}
