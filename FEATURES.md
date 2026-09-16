# GoreeCloud Camera — Current Features

> Repository document version: **0.3.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

## Verified runtime foundation

The following bounded runtime behavior is verified on the representative Android 16 / API 36 virtual-camera CI configuration:

- application installation and camera-only permission grant;
- Camera2 device discovery;
- selected emulated back-camera opening;
- preview session configuration;
- repeating preview reaching application state `PREVIEWING`.

This evidence does not qualify a physical device.

## Implemented source functionality

The source additionally contains the first still-photo milestone:

- JPEG output capability discovery;
- JPEG `ImageReader` configured as a capture-session output;
- one-shot `TEMPLATE_STILL_CAPTURE` request;
- an engineering **Capture photo** control;
- collision-resistant UTC timestamp naming;
- MediaStore reservation in `DCIM/GoreeCloud Camera`;
- `IS_PENDING=1` while the image is incomplete;
- JPEG write and publish by clearing `IS_PENDING`;
- pending-row deletion on handled capture/write failure;
- no storage, media-library, Internet, location, or microphone permission expansion;
- static source checks for the camera-only manifest and MediaStore/still-capture contract;
- representative emulator CI intended to verify a non-zero published JPEG and JPEG signature.

The photo path is not a supported feature until its exact candidate and authoritative-main runtime qualification passes.

## Other implemented foundation

- native Android application module and Kotlin source;
- Android 17/API 37 compile and target baseline; provisional API 29 minimum;
- deterministic default-camera selection;
- lifecycle-owned Camera2 session cleanup;
- unit tests for camera selection and photo filename generation;
- exact-source lint, unit tests, APK assembly, provenance/digest verification, artifact upload, and Platform Contract validation.

## Not yet implemented or accepted

- physical-device qualification;
- video/audio recording;
- robust interrupted/process-death capture recovery;
- zoom/focus/exposure controls and lens switching UI;
- production Glaze UI components;
- Private Capture;
- richer metadata/provenance controls;
- Gallery/Photos handoff;
- accepted application-specific Platform System integrations;
- production package/release.

## Status rule

Planned capabilities remain in `FEATURE-ROADMAP.md`. Source implementation, emulator qualification, and physical-device/support claims are separate evidence levels and must not be conflated.
