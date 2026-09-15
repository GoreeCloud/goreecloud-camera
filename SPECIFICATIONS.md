# GoreeCloud Camera — Repository Specifications

> Repository document version: **0.2.1**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Repository: `GoreeCloud/goreecloud-camera`

## 1. Purpose and authority

GoreeCloud Camera is the GoreeCloud-owned mobile capture application for still photography, video, scanning, creative capture, computational imaging, visual utilities, and privacy-controlled acquisition of source media.

This repository now contains the **first native Android implementation foundation**. The implementation is deliberately narrow: it establishes application identity, Android build configuration, runtime camera permission handling, Camera2 preview-session ownership, camera capability discovery, deterministic default-camera selection, lifecycle cleanup, initial unit tests, and exact-revision CI. It does **not** yet establish usable photo/video capture, media persistence, device qualification, or accepted GoreeCloud Platform System integration.

The release lifecycle therefore remains **Concept**. Source presence and a buildable engineering shell do not by themselves establish an Experimental prototype suitable for lifecycle promotion; that transition requires runtime evidence from an actual Android execution environment.

The canonical Drive project specification is `GoreeCloud/Projects/Project Specification — Camera.md` v0.3. The repository `FEATURE-ROADMAP.md` is the canonical editable roadmap source; `GoreeCloud/Feature Roadmap/GoreeCloud Camera/FEATURE-ROADMAP.md` v1.2 is its synchronized Drive representation. This file is the repository-coupled technical specification and must remain materially synchronized with the Drive project specification and roadmap state.

## 2. Verified current implementation state

The current foundation introduces the following repository implementation state:

- A native Android application module and Gradle build exist.
- Canonical Android application ID and namespace: `com.goreecloud.camera`.
- Product version: `0.1.0`; version code: `1`.
- Release lifecycle remains `Concept`.
- Strategic compile/target API: Android 17 / API 37.
- Provisional minimum API: 29. This is an engineering baseline, not a final support-policy commitment.
- Primary language: Kotlin using Android Gradle Plugin built-in Kotlin support.
- Android Gradle Plugin: 9.4.0; CI Gradle: 9.6.0; Java toolchain compatibility: 17.
- Runtime manifest currently requests only `android.permission.CAMERA`.
- No Internet, location, microphone, or media-library permission is requested by this foundation.
- A Camera2 preview session controller owns camera opening, preview-session creation, repeating preview, stop, and shutdown behavior.
- A Capability Registry enumerates cameras, lens direction, hardware level, RAW capability, logical multi-camera capability, and preview sizes.
- A deterministic selector prefers back, external, front, then unknown-facing cameras.
- Initial unit tests cover deterministic camera selection.
- Static source-contract validation and Android CI are defined for exact candidate revisions.
- Pull request #2 was squash-merged to authoritative `main` as signed commit `8de1eac6693f09ef52b0f12886108727352404e1`.
- That exact merged commit passed push-triggered Android Foundation run `35017167769` and Platform Contract run `35017168819`.

Not yet verified or implemented:

- successful preview execution on a real device or emulator with camera hardware;
- still-image capture or MediaStore finalization;
- video/audio recording;
- camera switching and user-facing mode controls;
- device qualification or Camera-specific hardware profiles;
- local settings persistence;
- production signing or packaged release;
- accepted Glaze UI, Privacy Shield, Wardveil Security, Everkeep, Manager, Mesh, or Identity runtime integration;
- a recognized open-source repository license.

## 3. Product scope and boundaries

Camera owns capture-session orchestration and production of source media. It must not become the authoritative media-library, general backup, synchronization, or long-term organizational system.

Planned authority boundaries:

- **GoreeCloud Camera** — capture.
- **GoreeCloud Gallery** — local media viewing and management.
- **GoreeCloud Photos** — backup, synchronization, organization, intelligence, sharing, and preservation.
- **Everkeep** — resilience, preservation, portability, continuity, succession, and recovery requirements.

Extensions, scanners, remote-control clients, and downstream apps must not acquire broader camera or media authority merely because they participate in a capture workflow.

## 4. Platform and technology direction

The first implementation is a native Android application using Kotlin and standard Android/Gradle organization.

### 4.1 Current build baseline

- `compileSdk = 37`
- `targetSdk = 37`
- `minSdk = 29` — provisional engineering minimum
- Android Gradle Plugin `9.4.0`
- Gradle `9.6.0` in CI
- Java source/target compatibility `17`
- Android application ID `com.goreecloud.camera`

The strategic platform target follows GoreeCloud OS Mobile: Android 17 / API 37. The current physical GoreeCloud OS Mobile qualification baseline remains separately governed and does not automatically qualify Camera.

### 4.2 Camera framework strategy

The initial foundation uses direct **Camera2** APIs for preview and capability discovery. This preserves a clear path to advanced manual, RAW, concurrent-camera, high-speed, and device-specific behavior. Future work may introduce higher-level Android camera abstractions where they materially improve lifecycle handling or compatibility, but they must not obscure verified hardware capability or create a second conflicting session authority.

### 4.3 User-interface direction

The current activity is an **engineering preview shell**, not an accepted GoreeCloud Camera user experience. The production UI must use the current accepted GoreeCloud Glaze UI contract. The current consumer target is **1.4.1**. No Glaze UI conformance is claimed by this foundation.

Unsupported hardware capabilities must remain hidden or unavailable rather than being advertised from theoretical API support.

## 5. Architectural principles

### 5.1 Authorization travels with the operation

Each capture request should carry an effective context containing mode, requested camera set, destination policy, Privacy Shield context, metadata policy, extension authority, remote-control authority, quality profile, and applicable security state. Downstream modules consume that context rather than re-deriving authority from identity alone.

The preview-only foundation does not yet construct the full capture authorization context because it does not persist media, use extensions, perform remote control, or invoke downstream services. That contract must be introduced before those capabilities appear.

### 5.2 Capability-driven hardware behavior

Camera must not assume uniform Android camera hardware. The current Capability Registry reads Camera2 characteristics and exposes source-level capability facts. A later effective device profile must combine reported capabilities with real-device qualification, including explicit workarounds or blacklists when evidence contradicts nominal capability reporting.

### 5.3 Single camera-session authority

`CameraSessionController` is the current owner of Camera2 device/session resources. Activities, future modes, extensions, and platform adapters must not open independent camera sessions behind this authority. Session ownership should evolve behind narrow interfaces as additional capture engines are introduced.

### 5.4 Recoverable media lifecycle

The future capture pipeline must validate camera/storage/thermal/battery/microphone/permission prerequisites, run ephemeral preview analysis, stage incomplete output recoverably, apply privacy/metadata policy, finalize media atomically or recoverably, verify committed artifacts, notify only authorized downstream consumers, and remove temporary state after successful commit.

No media is persisted by the current foundation, so recovery/finalization behavior remains unimplemented.

## 6. Major components and implementation status

- **Camera Session Controller — Foundation implemented.** Owns Camera2 lifecycle, device/session resources, preview start/stop, and shutdown.
- **Capability Registry — Foundation implemented.** Enumerates Camera2 capability facts and preview sizes.
- **Preview Engine — Foundation implemented.** `TextureView` host plus Camera2 repeating preview request; runtime qualification still pending.
- **Camera Selector — Foundation implemented.** Deterministic default-camera selection.
- **Photo Capture Engine — Planned.** Still capture, burst, RAW, computational pipelines, and finalization.
- **Video Capture Engine — Planned.** Video/audio capture, encoding, stabilization configuration, monitoring, and recording recovery.
- **Processing Pipeline — Planned.** Local computational photography and video transformations.
- **Metadata and Provenance Engine — Planned.** Metadata policy, artifact relationships, processing provenance, and future authenticity records.
- **Storage Coordinator — Planned.** Staging, MediaStore/protected destinations, atomic commit, storage reserve, and cleanup.
- **Private Capture Controller — Planned.** Stricter metadata, analysis, backup, Lens, preview, sharing, and storage behavior.
- **Lens Runtime — Planned.** Signed, sandboxed, permission-scoped, resource-limited creative effects.
- **Scanner and Visual Utilities — Planned.** Document/code scanning and optional local visual assistance.
- **Remote Viewfinder Controller — Planned.** Explicit pairing, session-scoped authority, and visible remote control.
- **Platform Adapters — Planned.** Narrow integrations with applicable GoreeCloud systems.
- **Diagnostics — Planned.** Privacy-safe structured events and qualification evidence.

## 7. Functional requirements

The canonical Feature Roadmap defines detailed planned feature scope. Major families include:

- Photo, Video, Portrait, Night, Pro Photo, Pro Video, and Cinema modes.
- Burst, Motion Photos, Best Shot, RAW, stabilization, Horizon Lock, slow motion, timelapse, hyperlapse, panorama, macro, and product photography.
- Dual/multi-camera and Director Mode.
- Creative Mode, GoreeCloud Lenses, Lens Studio direction, filters, creator tools, and Multi-Capture.
- Document and code scanning plus privacy-aware visual utilities.
- Selfie, foldable/large-screen, and future Remote Viewfinder workflows.
- Local Camera Intelligence and shot guidance.
- Private Capture and metadata controls.
- Deep but non-mandatory Gallery/Photos/Everkeep integration.

The current implementation provides only the Android application shell, permission flow, capability discovery, camera selection, and preview-session foundation. All user-facing capture modes remain planned until verified implementation evidence reclassifies them.

## 8. Data and storage

Ordinary photographs and videos must remain normal user-owned files. GoreeCloud-specific relationships may use portable metadata or sidecars, but primary media must remain usable without GoreeCloud Camera.

No captured media, database, preference store, sidecar, or capture relationship record is created by the current foundation.

Planned capture relationship data should be versioned and capable of representing primary media, RAW, motion sequences, separate camera streams, sidecars, integrity state, privacy policy, and computational-modification provenance.

Private Capture may use a protected destination, but export/recovery rules must remain explicit and user-controlled.

## 9. Interfaces and APIs

No public network API is implemented or required for core capture.

Current internal source interfaces include:

- `CameraDescriptor` and `LensFacing` for neutral camera identity/role representation;
- `CameraSelector` for deterministic default-camera selection;
- `CameraCapabilityRegistry` for Camera2 capability discovery and preview-size selection;
- `CameraSessionController` for lifecycle-owned Camera2 preview resources;
- `CameraSessionState` for the engineering shell's observable session state.

Future platform adapters, capture-result contracts, Remote Viewfinder transport, Lens manifests, or integration APIs must be separately versioned when introduced. Core capture must never depend on a hosted control plane.

## 10. Authentication and authorization

Basic local capture must not require account authentication. Identity may become applicable to optional account-bound, paired-device, sharing, or cross-device workflows, but must not be used to broaden a capture operation beyond the authority carried by its capture context.

The current foundation has no account, network, remote-control, or Identity dependency.

Remote control requires explicit pairing, session-scoped authorization, visible active-state indication, and revocation when implemented.

## 11. Privacy requirements

### 11.1 Current implemented privacy boundary

The Android manifest currently requests only `android.permission.CAMERA`.

The foundation intentionally does **not** request:

- `android.permission.INTERNET`;
- fine or coarse location;
- `android.permission.RECORD_AUDIO`;
- media-library read permissions.

The current preview pipeline does not persist camera frames, upload content, record audio, read location, or access an existing media library. These are current source-level facts, not Privacy Shield acceptance evidence.

### 11.2 Future privacy requirements

- Core capture remains offline-first and local-first.
- Viewfinder/analysis frames are ephemeral by default.
- Location and other sensitive metadata are separately controllable.
- Private Capture provides a strong visible state and stricter destination/metadata/analysis/backup/extension behavior.
- Lens packages receive only minimum frame access and must not silently upload frames.
- Sharing/export supports Original, Privacy Safe, and Custom metadata policies when implemented.
- Optional external processing must not become mandatory for normal capture.
- Applicable Privacy Shield contracts fail closed for dependent features while unrelated local capture remains available where policy permits.

## 12. Security requirements

### 12.1 Current source-level controls

- Camera2 device and capture-session ownership is centralized in `CameraSessionController`.
- Camera access is gated by the Android camera runtime permission.
- Camera/session/surface resources are closed on activity pause, surface loss, stop, and shutdown paths.
- The application disallows cleartext network traffic even though the foundation currently has no Internet permission.
- Android backup is disabled for this engineering foundation.

These controls do not constitute Wardveil Security acceptance.

### 12.2 Future security requirements

- Protect temporary frames, RAW data, cached/in-progress video, Private Capture material, microphone use, location metadata, and remote-session state according to sensitivity and lifetime.
- Lens packages must be signed, sandboxed, permission-scoped, resource-limited, and killable without destabilizing core capture where practical.
- Private Capture temporary data must not leak into ordinary thumbnails, logs, caches, or backup queues.
- Diagnostics must not contain raw frame/media payloads by default.
- Use mature approved cryptographic/platform primitives; do not invent custom cryptography.

## 13. GoreeCloud Platform Contract

The repository declares **Platform Contract 0.2**, exactly seven Integral Platform Systems, and current Glaze UI target **1.4.1**.

All seven remain **applicable-blocked** at this milestone because application-specific runtime acceptance evidence does not exist:

1. GoreeCloud Manager
2. Privacy Shield
3. Wardveil Security
4. Everkeep
5. Glaze UI
6. GoreeCloud Mesh
7. GoreeCloud Identity

The source foundation is relevant evidence of implementation progress but does not satisfy Platform System conformance on its own.

GoreeCloud Sync is separately governed and must not appear as an eighth `platform_systems` key. If Camera later uses Sync, synchronization authorization, datasets, version/change model, conflicts, replication, offline resume, and cross-device behavior must be documented separately.

## 14. Accessibility

The engineering shell provides basic text labels, an explicit camera-permission control, and a content description for the preview host, but no accessibility acceptance is claimed.

Production requirements include meaningful labels/state, logical focus order, screen-reader announcements, non-color-only critical warnings, large-text and reduced-motion support, adequate contrast/target sizing, hardware/voice/gesture alternatives where appropriate, haptic confirmation, foldable/large-screen operability, and optional privacy-aware visual descriptions.

Accessibility acceptance requires real implementation and device validation.

## 15. Performance, reliability, and continuity

Performance budgets are qualification targets, not current claims. Real-device testing must establish thresholds for launch, preview readiness, shutter responsiveness, lens switching, sustained recording, memory/thermal behavior, storage exhaustion, battery-critical finalization, and interrupted-session recovery.

The current session controller explicitly closes resources across activity/surface lifecycle transitions, but no performance or recovery qualification has been performed.

Correctness, media integrity, privacy, security, and thermal sustainability take precedence over headline latency.

Camera does not own general media backup/restore, but captured files must remain exportable and portable. In-progress capture recovery is a Camera reliability responsibility once capture persistence exists.

## 16. Dependencies and configuration

The current implementation deliberately minimizes dependencies:

- Android platform/framework APIs for runtime behavior;
- Android Gradle Plugin 9.4.0 as build tooling;
- Gradle 9.6.0 in CI;
- JUnit 4.13.2 for local unit testing.

There is no third-party runtime camera library, hosted SDK, telemetry SDK, networking SDK, account SDK, or proprietary control-plane dependency in the foundation.

Future dependencies must remain limited to justified open-source or platform foundations. Core operation must not depend on proprietary hosted control planes, vendor accounts, or commercial cloud processing.

Tracked configuration must contain no reusable secrets. User preferences, device qualification, privacy policy state, presets, extension permissions, diagnostics settings, and build-time configuration should remain clearly separated.

## 17. Observability

The current engineering shell exposes non-persistent session state and a coarse camera-count capability summary to aid bring-up. No telemetry or remote diagnostics are implemented.

Future diagnostics may record privacy-safe structured events such as session lifecycle, mode, capability decisions, encoder configuration, thermal/storage state classes, error codes, recovery outcomes, and timing metrics.

Ordinary diagnostics must not contain raw camera frames, recognized faces, OCR content, precise location, microphone payloads, or captured media.

## 18. Testing and validation

Current repository validation includes:

- static source-contract validation of application ID, API levels, product version, AGP pin, lifecycle declaration, required foundation files, camera permission, and absence of additional sensitive permissions;
- local pure-Kotlin unit tests for deterministic camera selection;
- Android lint, unit tests, and debug APK assembly in exact-revision CI;
- Platform Contract validation through the repository's pinned reusable contract workflow;
- post-merge exact-main Android Foundation run `35017167769` and Platform Contract run `35017168819`, both successful for commit `8de1eac6693f09ef52b0f12886108727352404e1`.

Passing source/build validation proves only those checks. It does not prove camera preview works on supported hardware.

Future validation must cover instrumentation tests, fake pipeline tests where useful, representative emulator/runtime checks, real-device qualification, media integrity, process death, camera-service restart, lock/unlock, storage exhaustion, thermal escalation, microphone route changes, runtime permission revocation, Lens isolation, Remote Viewfinder disconnect, accessibility, privacy, security, and platform integration acceptance.

## 19. Production acceptance

Camera must not be classified Stable until applicable GoreeCloud production-readiness requirements and Camera-specific gates are satisfied, including an identifiable candidate, qualified real-device matrix, no release-blocking media-loss/corruption/privacy/security defects, verified offline core capture and recovery, accepted applicable Platform System integrations, current Stable Glaze UI application-specific acceptance, synchronized repository/Drive documentation, and required human visual/usability/device acceptance.

This foundation is not a release candidate and is not production accepted.

## 20. Known boundaries and open decisions

Open decisions include:

- final minimum Android/API support baseline beyond the provisional `minSdk 29` engineering baseline;
- whether selected future subsystems should use higher-level Android camera abstractions while Camera2 remains the advanced-control/session foundation;
- first real-device qualification list and tier model;
- still-capture and MediaStore finalization design;
- Motion Photo representation;
- codec/HDR and recording matrix;
- Private Capture protected storage;
- provenance/authenticity format;
- Lens runtime technology;
- Remote Viewfinder transport;
- final performance budgets;
- recognized repository license.

## 21. Immediate next engineering milestone

With the native Android foundation merged and build-verified on authoritative `main`, the next milestone is to prove the camera path in a representative Android runtime and implement the first real **Photo capture** path:

1. run/qualify the preview and lifecycle controller on a representative emulator or device with camera support;
2. add a still-capture request and image output path;
3. stage and atomically finalize the image into MediaStore;
4. verify capture completion and failure handling;
5. add automated tests around output selection/state transitions where feasible;
6. record device/runtime evidence without overstating support;
7. keep location, networking, audio, and media-library authority absent until a feature actually requires and justifies it.

Lifecycle promotion from Concept must be evaluated separately after runtime evidence exists; it must not be inferred from the presence of source code or a CI-built APK.
