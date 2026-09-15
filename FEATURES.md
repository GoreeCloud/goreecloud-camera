# GoreeCloud Camera — Current Features

> Repository document version: **0.2.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

## Verified source-level functionality

The repository now contains an actual native Android Camera foundation. The following capabilities are implemented in source and are subject to exact-revision CI build/test validation:

- Android application module with `com.goreecloud.camera` identity.
- Android 17 / API 37 compile and target baseline.
- Provisional API 29 minimum SDK.
- Native Kotlin application source using Android platform APIs.
- Runtime camera permission request and denied/granted UI state.
- Camera2 camera enumeration.
- Capability profiles recording lens facing, hardware level, RAW capability, logical multi-camera capability, and available preview sizes.
- Deterministic default-camera selection preferring rear, then external, then front cameras.
- Lifecycle-owned Camera2 preview session controller.
- Preview resource cleanup on activity pause and surface destruction.
- Minimal engineering viewfinder shell that explicitly states Glaze UI integration is pending.
- Unit tests for pure camera-selection behavior.
- Static source-contract checks covering application identity, SDK targets, version identity, required source, camera permission, and absence of additional sensitive permissions.
- Android CI for lint, unit tests, debug APK assembly, exact-source verification, provenance, digest verification, and artifact upload.

## Runtime qualification state

No real-device or emulator preview acceptance has yet been recorded. Therefore the repository must not claim that preview works correctly on any supported device even if the build succeeds.

## Not yet implemented

- still-photo capture;
- photo persistence or atomic finalization;
- video or microphone capture;
- MediaStore writes;
- recording recovery;
- zoom/focus/exposure controls;
- lens switching UI;
- Glaze UI runtime components;
- Private Capture;
- metadata/provenance controls;
- device-specific quirk profiles;
- Gallery/Photos handoff;
- accepted Platform System integrations;
- qualified devices;
- production package/release.

## Status rule

Planned capabilities remain in `FEATURE-ROADMAP.md`. A source implementation is not promoted to a supported feature until the appropriate build, runtime, real-device, privacy, security, accessibility, and acceptance evidence exists.
