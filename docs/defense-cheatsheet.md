# Facade Defense Guide

## One-minute explanation

The Facade pattern gives a client one simple entry point to a subsystem that otherwise requires several coordinated operations. In this project, `VideoConversionFacade.convert(input, outputFormat)` selects codecs, processes video frames, resamples and mixes audio samples, and assembles a conversion summary. The client does not need to know the order or the classes that perform those steps.

## Live demonstration

1. Run `facade.Main` and point out that it calls `VideoConversionFacade.convert` for MP4 and WEBM.
2. Show the result summaries. They have different codec profiles, estimated bitrates, and sizes.
3. Open `VideoConversionFacade.convert` and trace the sequence: `CodecFactory`, `BitrateReader`, `AudioMixer`, then `MediaMuxer`.
4. Show that `Main` does not import or create any subsystem class.
5. Explain the scope: frame payloads and audio samples are transformed in memory to demonstrate the pattern. The sample does not produce a playable video or claim to implement real codecs.

## Likely questions

1. **What problem does the Facade solve?** It hides the call order and coordination across multiple subsystem classes behind one operation.
2. **Where is the Facade?** `VideoConversionFacade` in `facade.api`.
3. **What does it hide?** Codec selection, frame bitrate adjustment, audio resampling/mixing, and muxing details.
4. **What remains accessible?** Subsystem classes are public and have no back-reference to the facade, so an advanced client can call them directly.
5. **Why does Main use only the facade?** That demonstrates the simplified interface the pattern is meant to provide.
6. **Why use separate subsystem classes?** Each class owns one operation, which keeps the responsibilities clear and lets those operations be reused independently.
7. **How are failures reported?** Subsystems reject invalid data; the facade wraps pipeline runtime failures in `ConversionException` with the source filename and target format.
8. **Does this create a real MP4 or WEBM?** No. It transforms representative frame and sample data in memory and returns a summary. A real encoder would require an external codec engine such as FFmpeg.
9. **What does the Facade not do?** It does not replace subsystem classes or make them inaccessible; it only offers a convenient route through their common workflow.
10. **What is the tradeoff?** The facade adds a coordination class and hides some control from simple clients, while advanced clients can still bypass it.
