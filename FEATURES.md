# GoreeCloud Camera — Current Features

> Repository document version: **0.4.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

## Verified runtime foundation

The following bounded runtime behavior is verified on the representative Android 16 / API 36 virtual-camera CI configuration for both final PR #6 candidate `a378a17d3e6cf28a16521428fa3d77c1910e2e53` and signed authoritative `main` commit `d5272da9877b47bfca1551784f32b1031a084a8e`:

- application installation and camera-only permission grant;
- Camera2 device discovery;
- selected emulated back-camera opening;
- preview session configuration;
- repeating preview reaching application state `PREVIEWING`;
- one explicit JPEG still-capture request;
- publication of a matching non-zero MediaStore JPEG under `DCIM/GoreeCloud Camera`;
- readback and JPEG-signature verification of the published artifact.

This evidence verifies the bounded path on the configured virtual camera only; it does not qualify a physical device or establish production support.

## Implemented source functionality

The first still-photo milestone now includes:

- JPEG output capability discovery;
- JPEG `ImageReader` configured as a capture-session output;
- one-shot `TEMPLATE_STILL_CAPTURE` request;
- an engineering **Capture photo** control kept clear of bottom system UI through system-bar inset handling;
- collision-resistant UTC timestamp naming;
- MediaStore reservation in `DCIM/GoreeCloud Camera`;
- `IS_PENDING=1` while the image is incomplete;
- JPEG write and publish by clearing `IS_PENDING`;
- pending-row deletion on handled capture/write failure;
- no storage, media-library, Internet, location, or microphone permission expansion;
- static source checks for the camera-only manifest, system-inset handling, and MediaStore/still-capture contract;
- exact-revision representative-emulator qualification of the bounded JPEG publication path.

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
