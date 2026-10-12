package facade.subsystem;

import facade.model.VideoAsset;
import facade.model.VideoFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BitrateReader {
    public VideoStream read(VideoAsset input, Codec sourceCodec) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(sourceCodec, "sourceCodec must not be null");
        if (input.format() != sourceCodec.format()) {
            throw new IllegalArgumentException("source codec does not match the input format");
        }
        return new VideoStream(input.format(), input.bitrateKbps(), input.duration(), input.frames());
    }

    public VideoStream transcode(VideoStream input, Codec targetCodec) {
        Objects.requireNonNull(input, "input must not be null");
        Objects.requireNonNull(targetCodec, "targetCodec must not be null");
        int outputBitrate = Math.min(input.bitrateKbps(), targetCodec.maximumVideoBitrateKbps());
        long sourceBytes = input.frames().stream().mapToLong(VideoFrame::payloadBytes).sum();
        long targetBytes = Math.max(input.frames().size(),
                Math.round(outputBitrate * input.duration().toMillis() / 8.0));
        double sizeRatio = targetBytes / (double) sourceBytes;

        List<VideoFrame> transcodedFrames = new ArrayList<>(input.frames().size());
        for (VideoFrame frame : input.frames()) {
            long adjustedSize = Math.max(1, Math.round(frame.payloadBytes() * sizeRatio));
            transcodedFrames.add(new VideoFrame(frame.timestampMillis(), adjustedSize));
        }
        return new VideoStream(targetCodec.format(), outputBitrate, input.duration(), transcodedFrames);
    }
}
