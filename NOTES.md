# GoreeCloud Camera — Repository Notes

> Repository document version: **0.2.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

## Current baseline

- Canonical repository: `GoreeCloud/goreecloud-camera`.
- Product version baseline: `0.1.0`.
- Lifecycle remains Concept pending runtime evidence suitable for promotion.
- Primary platform: native Android/Kotlin.
- Canonical application ID: `com.goreecloud.camera`.
- Compile/target SDK: 37 / Android 17.
- Provisional minimum SDK: 29.
- Android Gradle Plugin: 9.4.0.
- CI Gradle baseline: 9.6.0.
- Platform Contract: 0.2.
- Current Glaze UI consumer target: 1.4.1.
- Integral Platform Systems: exactly seven; GoreeCloud Sync is separate.
- Source now includes Camera2 capability enumeration and a lifecycle-owned preview session controller.
- Runtime preview on a qualified device is not yet verified.

## Resolved engineering decisions for the foundation

- Android application identity: `com.goreecloud.camera`.
- Kotlin/native Android implementation.
- Camera2 is used directly for the first preview/session foundation.
- API 37 is the compile and target baseline.
- API 29 is a provisional development minimum, not a final support promise.
- Current runtime permission scope is camera only.
- Core build uses AGP built-in Kotlin to avoid a redundant Kotlin Android plugin declaration.

## Open engineering decisions

- final minimum Android/API support baseline;
- whether later high-level CameraX abstractions should wrap selected flows while Camera2 remains available for advanced controls;
- initial supported hardware/qualification tiers;
- still-capture persistence and atomic finalization design;
- video capture and recording-recovery design;
- Motion Photo representation;
- codec/HDR policy;
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

No supported real-device Camera preview qualification has been recorded. The OnePlus Nord N200 remains a broader GoreeCloud OS Mobile physical qualification baseline, but Camera support on it must be tested independently.

### User manual synchronization

A central Camera user-manual record has not yet been verified. Repository `USER-MANUAL.md` describes the engineering foundation only and must later be synchronized through the approved central workflow.

## Next engineering milestone

Complete the first useful capture loop: verify the preview on a representative Android 16/17 target, implement a basic still-photo request, stage/finalize the image through MediaStore without legacy broad-storage permission, and add deterministic tests around capture state and failure cleanup.
