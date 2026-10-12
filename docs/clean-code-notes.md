# Clean Code in the Video Conversion Facade

The examples below show the design decisions used in this project. The `before` snippets are deliberately simplified examples of designs to avoid; the `after` snippets come from or match the project API.

## 1. Meaningful, intention-revealing names

The public operation states the task and its inputs. Subsystem method names state whether they select, read, transcode, mix, or assemble data, so a reader can follow the conversion order without decoding generic names.

Before:

```java
Object doIt(Object value, Object option);
```

After:

```java
ConversionResult convert(VideoAsset input, VideoFormat outputFormat);
```

## 2. Single Responsibility

The facade coordinates the pipeline; each subsystem owns its own operation. This keeps codec lookup, bitrate estimation, audio processing, and container assembly out of the public facade method's implementation details.

Before:

```java
public ConversionResult convert(...) {
    // look up codecs, resample audio, resize frames, and assemble output
}
```

After:

```java
Codec source = codecFactory.codecFor(input.format());
VideoStream decoded = bitrateReader.read(input, source);
AudioTrack mixed = audioMixer.mix(input.audioTracks(), target.audioSampleRateHz());
```

## 3. Do not leak subsystem details through the facade API

The client passes a media value and a target format, then receives a conversion summary. It does not pass a `CodecFactory`, `BitrateReader`, or intermediate stream into the public method, so internal pipeline changes do not force client changes.

Before:

```java
convert(VideoAsset input, CodecFactory factory, BitrateReader reader,
        AudioMixer mixer, MediaMuxer muxer);
```

After:

```java
ConversionResult result = facade.convert(input, VideoFormat.MP4);
```

## 4. Keep subsystem classes focused

Each subsystem has one clear responsibility and can be used directly by an advanced client. `AudioMixer` resamples the input tracks, combines their samples, and normalizes the result; it does not choose codecs or assemble a container.

Before:

```java
class MediaProcessor {
    // codec selection, video conversion, audio mixing, and output assembly
}
```

After:

```java
AudioTrack mixed = audioMixer.mix(tracks, targetSampleRateHz);
```

## 5. Handle coordination failures with context

Subsystems report invalid stream or format data at the point where it is detected. The facade catches pipeline runtime failures and adds the input filename and requested target format, making the error useful to the client without moving validation into each caller.

Before:

```java
throw new IllegalArgumentException("bad data");
```

After:

```java
throw new ConversionException(
        "Could not convert " + input.fileName() + " to " + outputFormat
                + ": " + exception.getMessage(),
        exception);
```
