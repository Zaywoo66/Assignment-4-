# Video Conversion Facade

A Java 17 example of the Facade structural design pattern for a media conversion pipeline (Assignment 4, Option A). The client calls one method, `VideoConversionFacade.convert(input, outputFormat)`, while the facade coordinates codec selection, video transcoding, audio mixing, and container assembly.

## What the example does

The sample keeps representative video frames and audio samples in memory. The bitrate subsystem adjusts estimated frame payload sizes, and the audio subsystem resamples, mixes, and normalizes sample values. The muxer returns a conversion summary with a suggested output filename and estimated size.

This is an educational pipeline simulation, not a real media encoder: it does not decode MP4/AVI bytes or create a playable output video. Real encoding would require a codec engine such as FFmpeg, which is outside this assignment's dependencies.

## Pattern roles

| Role | Project class | Responsibility |
| --- | --- | --- |
| Facade | `VideoConversionFacade` | Offers one conversion operation and coordinates the subsystems. |
| Subsystem | `CodecFactory` | Selects codec settings for a media format. |
| Subsystem | `BitrateReader` | Reads frame metadata and estimates transcoded frame payload sizes. |
| Subsystem | `AudioMixer` | Resamples, mixes, and normalizes audio samples. |
| Subsystem | `MediaMuxer` | Combines stream metadata into a conversion result. |
| Client | `Main` | Uses the facade and never calls subsystem classes directly. |

## Structure

```text
src/main/java/facade/
├── Main.java                         Client demonstration
├── api/                              Public facade API and result
├── model/                            Input and output value types
└── subsystem/                        Independent conversion components
src/test/java/
├── facade/api/                       Facade conversion tests
├── facade/model/                     Input validation tests
└── facade/subsystem/                 Audio mixing tests
docs/
├── uml.puml                          PlantUML class diagram
├── clean-code-notes.md               Five Clean Code principles
└── defense-cheatsheet.md             Demo steps and defense questions
```

Subsystem classes have no reference to the facade. They are public and can be used directly by advanced clients. The demo client uses the facade and the input value types only.

## Build and run

Install JDK 17 or newer and Maven. Run the automatic tests with:

```sh
mvn test
```

To build the project and run the media and video conversion demonstration:

```sh
mvn clean package
mvn -q exec:java -Dexec.mainClass=facade.Main
```

In PowerShell, quote the Maven property: `mvn -q exec:java '-Dexec.mainClass=facade.Main'`.

The simple client-side conversion call is:

```java
ConversionResult result = new VideoConversionFacade()
        .convert(inputVideo, VideoFormat.MP4);
```

`Main` supplies a small in-memory AVI example and runs this conversion for both MP4 and WEBM, then prints each result. The JUnit tests check those output summaries, target bitrate limits, audio mixing and normalization, empty audio input, and invalid video inputs.

## Example output

```text
Output: lecture-converted.mp4
Format: MP4 | video: 4500 kbps | frames: 4
Audio: 48000 Hz | 4 samples | estimated size: 1125263 bytes

Output: lecture-converted.webm
Format: WEBM | video: 2500 kbps | frames: 4
Audio: 48000 Hz | 4 samples | estimated size: 625264 bytes
```

The output is a summary of the simulated conversion, not a video file. See [the UML diagram](docs/uml.puml), [Clean Code notes](docs/clean-code-notes.md), and [defense guide](docs/defense-cheatsheet.md).

GitHub repository: <https://github.com/Zaywoo66/Assignment-4->
