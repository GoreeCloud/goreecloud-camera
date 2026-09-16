# GoreeCloud Camera — Repository Notes

> Repository document version: **0.3.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

## Current baseline

- Canonical repository: `GoreeCloud/goreecloud-camera`.
- Product version baseline: `0.1.0`.
- Lifecycle remains Concept; source/build progress does not establish promotion.
- Primary platform: native Android/Kotlin.
- Canonical application ID: `com.goreecloud.camera`.
- Compile/target SDK: 37 / Android 17.
- Provisional minimum SDK: 29.
- Android Gradle Plugin: 9.4.0.
- CI Gradle baseline: 9.6.0.
- Platform Contract: 0.2.
- Current Stable Glaze UI consumer target: 1.5.0.
- Integral Platform Systems: exactly seven; GoreeCloud Sync is separate.
- Source includes Camera2 capability discovery, lifecycle-owned preview/still/video session control, bounded JPEG still capture, and bounded Camera2/MediaRecorder MP4 recording with H.264 video plus AAC microphone audio.
- Representative Android 16 / API 36 emulator evidence qualifies preview and one JPEG + MediaStore still-capture path; it does not qualify video/audio recording or microphone routing.
- Authoritative PR #8 source implementation was squash-merged as signed `main` commit `f6d414049f6ad4792a70da00903fead403dde58c` after exact-head Android Foundation run `35101834080` and Platform Contract run `35101835061` passed.

## Resolved engineering decisions for the foundation

- Android application identity: `com.goreecloud.camera`.
- Kotlin/native Android implementation.
- Camera2 remains the direct camera/session authority for the current foundation.
- MediaRecorder provides the bounded current MP4 encoder/recorder path.
- API 37 is the compile and target baseline.
- API 29 is a provisional development minimum, not a final support promise.
- Camera permission is required for preview/capture.
- `RECORD_AUDIO` is declared only because the bounded video-with-audio path requires it; the app requests microphone authority just in time after an explicit Record-video action, and permission grant does not auto-start recording.
- Internet, location, broad storage, all-files, and media-library read permissions remain absent.
- Core build uses AGP built-in Kotlin to avoid a redundant Kotlin Android plugin declaration.

## Open engineering decisions

- final minimum Android/API support baseline;
- whether later high-level CameraX abstractions should wrap selected flows while Camera2 remains available for advanced controls;
- initial supported hardware/qualification tiers;
- robust still/video process-death recovery and stale pending-media reconciliation;
- video/audio runtime qualification matrix and microphone-routing behavior;
- Motion Photo representation;
- codec/HDR policy beyond the bounded H.264/AAC foundation;
- Private Capture protected-storage design;
- provenance/content-authenticity format;
- Lens runtime and future authoring model;
- Remote Viewfinder transport/pairing;
- initial performance budgets.

## Governance blockers

### License

The public repository does not yet contain a recognized open-source license. An approved license must be selected before the repository is treated as licensing-complete or approved open-source distribution. Do not infer a license from repository visibility or from unrelated GoreeCloud projects.

### Runtime conformance

All seven Integral Platform Systems remain blocked/unaccepted. Source-level privacy/security boundaries and a buildable Camera foundation do not establish application-specific Platform System acceptance.

### Device qualification

No supported real-device Camera preview/capture qualification has been recorded. Representative virtual-camera evidence is not a supported-device claim. Video/audio runtime and microphone-routing qualification also remain open.

### User manual synchronization

The central Camera user-manual record exists at `GoreeCloud/User Manuals/User Manual — GoreeCloud Camera.md` and must remain synchronized with verified repository behavior. It must preserve the distinction between source/build implementation and runtime/device qualification.

## Next engineering milestone

Obtain explicit video/audio runtime evidence on an audio-capable representative target without conflating that result with physical-device support. Verify recording start/stop/finalization, MediaStore publication, microphone permission behavior, audio track presence/routing where the target can support it, handled failure cleanup, and preview recovery. Physical-device/device-profile qualification, local settings, process-death recovery, licensing, production Glaze UI, and Platform System acceptance remain separate open gates.
