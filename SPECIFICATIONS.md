# GoreeCloud Camera — Repository Specifications

> Repository document version: **0.3.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Repository: `GoreeCloud/goreecloud-camera`

## 1. Purpose and authority

GoreeCloud Camera is the GoreeCloud-owned mobile capture application for still photography, video, scanning, creative capture, computational imaging, visual utilities, and privacy-controlled acquisition of source media.

This repository contains the **first native Android implementation foundation**, a verified representative Android-emulator preview path, and the active PR #6 candidate for the first bounded JPEG still-photo + MediaStore publication path. The implementation remains deliberately narrow: application identity, Android build configuration, runtime camera permission handling, single-authority Camera2 preview/session ownership, camera capability discovery, deterministic default-camera selection, lifecycle cleanup, explicit still capture, handled-failure MediaStore cleanup, unit/static validation, exact-revision CI, and Android 16 virtual-camera qualification infrastructure. It does **not** yet establish physical-device qualification, video/audio capture, production UI acceptance, robust process-death capture recovery, or accepted GoreeCloud Platform System integration.

The release lifecycle remains **Concept**. Source implementation and representative runtime evidence are necessary implementation evidence, but neither is by itself a lifecycle promotion. Promotion must be evaluated separately against the authoritative lifecycle standard and the actual maturity of usable capture, persistence, device support, recovery, privacy/security, accessibility, and acceptance evidence.

The canonical Drive project specification is `GoreeCloud/Projects/Project Specification — Camera.md`. The repository `FEATURE-ROADMAP.md` is the canonical editable roadmap source; `GoreeCloud/Feature Roadmap/GoreeCloud Camera/FEATURE-ROADMAP.md` is its synchronized Drive representation. This file is the repository-coupled technical specification and must remain materially synchronized with the Drive project specification and roadmap state.

## 2. Verified current implementation state

Authoritative `main` currently establishes the following verified foundation:

- A native Android application module and Gradle build exist.
- Canonical Android application ID and namespace: `com.goreecloud.camera`.
- Product version: `0.1.0`; version code: `1`.
- Release lifecycle remains `Concept`.
- Strategic compile/target API: Android 17 / API 37.
- Provisional minimum API: 29. This is an engineering baseline, not a final support-policy commitment.
- Primary language: Kotlin using Android Gradle Plugin built-in Kotlin support.
- Android Gradle Plugin: 9.4.0; CI Gradle: 9.6.0; Java toolchain compatibility: 17.
- Runtime manifest requests only `android.permission.CAMERA`.
- No Internet, location, microphone, storage, all-files, or media-library read permission is requested.
- A Camera2 session controller owns camera opening, preview-session creation, repeating preview, stop, and shutdown behavior.
- A Capability Registry enumerates cameras, lens direction, hardware level, RAW capability, logical multi-camera capability, and preview sizes.
- A deterministic selector prefers back, external, front, then unknown-facing cameras.
- Initial unit tests cover deterministic camera selection.
- Static source-contract validation and Android CI are defined for exact candidate revisions.
- Pull request #2 was squash-merged to authoritative `main` as signed commit `8de1eac6693f09ef52b0f12886108727352404e1`, establishing the native Android source foundation.
- Pull request #4, **“Qualify Camera2 preview on Android emulator,”** was squash-merged to authoritative `main` as signed commit `6dbd2c1a5c5e3fb523662f5a6e181c6dbf73644a`.
- Exact PR #4 candidate `cdc59289c607558090c298ee74ae2829268920c1` passed Android Foundation run `35020668795`, including the Android 16 virtual-camera preview qualification job, and Platform Contract run `35020668711`.
- Authoritative merge commit `6dbd2c1a5c5e3fb523662f5a6e181c6dbf73644a` passed push-triggered Android Foundation run `35021425774`, including the same runtime preview qualification, and Platform Contract run `35021428000`.
- Documentation reconciliation PR #5 produced authoritative `main` commit `1687d81a65c037b46a25f98a23e5a0d1453e58de`, whose Android Foundation and Platform Contract workflows also passed.
- The representative Android 16/API 36 emulator uses an emulated back camera and has reached `Session: previewing` with one detected virtual camera while only CAMERA permission is granted.
- Runtime preview evidence includes UI hierarchy, screenshot, CameraService state, package/app-ops state, logcat, and provenance; it explicitly records `physical_device_qualification=false` and no Stable release authority.

The active PR #6 candidate additionally implements in source:

- JPEG output-size discovery in the Capability Registry;
- one JPEG `ImageReader` attached to the same Camera2 capture-session authority as preview;
- explicit `CAPTURING` state and engineering-stage **Capture photo** control;
- one-shot `TEMPLATE_STILL_CAPTURE` requests;
- deterministic UTC `GCAM_*.jpg` naming;
- MediaStore reservation under `DCIM/GoreeCloud Camera` using `IS_PENDING=1`;
- JPEG signature validation before publication;
- write-then-publish behavior that clears `IS_PENDING` only after successful output completion;
- deletion of the pending row on handled reserve/capture/write/finalization failures;
- cleanup of `ImageReader`, pending-photo state, and in-flight capture state through the existing session lifecycle;
- filename unit coverage plus strengthened static source-contract validation;
- an Android 16/API 36 emulator qualification path intended to trigger one photo, locate the published MediaStore row, require non-zero size, read the output, and verify its JPEG signature.

These PR #6 items remain **candidate state**, not authoritative `main` state, until the exact final PR head passes required checks and is merged. A prior runtime attempt on an earlier candidate failed because the Android emulator's Quickstep launcher became unresponsive before Camera reached preview; that attempt did not exercise the still-capture or MediaStore path and is not negative capture evidence.

Not yet verified or implemented:

- successful preview/still qualification on a representative physical phone;
- authoritative-main acceptance of the PR #6 JPEG + MediaStore path;
- video/audio recording;
- camera switching and production user-facing mode controls;
- physical-device qualification or Camera-specific hardware profiles/quirk records;
- local settings persistence;
- robust interrupted/process-death capture recovery beyond handled in-process cleanup;
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

The strategic platform target follows GoreeCloud OS Mobile: Android 17 / API 37. The Android 16/API 36 emulator qualification is representative runtime evidence only; the current physical GoreeCloud OS Mobile qualification baseline remains separately governed and does not automatically qualify Camera.

### 4.2 Camera framework strategy

The implementation uses direct **Camera2** APIs for preview, capability discovery, and the bounded JPEG still-capture candidate. This preserves a clear path to advanced manual, RAW, concurrent-camera, high-speed, and device-specific behavior while keeping one explicit session authority. Future work may introduce higher-level Android camera abstractions where they materially improve lifecycle handling or compatibility, but they must not obscure verified hardware capability or create a second conflicting session authority.

### 4.3 User-interface direction

The current activity is an **engineering capture shell**, not an accepted GoreeCloud Camera user experience. The production UI must use the current approved Stable GoreeCloud Glaze UI contract. Portfolio authority is currently **Glaze UI 1.5.0**; the repository's prior 1.4.1 target is therefore a migration item, not acceptance evidence. No Glaze UI conformance is claimed by this foundation or PR #6 candidate.

Unsupported hardware capabilities must remain hidden or unavailable rather than being advertised from theoretical API support.

## 5. Architectural principles

### 5.1 Authorization travels with the operation

Each capture request should carry an effective context containing mode, requested camera set, destination policy, Privacy Shield context, metadata policy, extension authority, remote-control authority, quality profile, and applicable security state. Downstream modules consume that context rather than re-deriving authority from identity alone.

The bounded local JPEG candidate does not yet construct the full GoreeCloud capture authorization context because it has no network, account, extensions, remote control, location, microphone, or downstream-service invocation. The explicit shutter action and narrow local MediaStore destination do not substitute for the future operation-bound privacy/security contract required before more sensitive or connected capabilities appear.

### 5.2 Capability-driven hardware behavior

Camera must not assume uniform Android camera hardware. The current Capability Registry reads Camera2 characteristics and exposes source-level capability facts, including preview and candidate JPEG output sizes. A later effective device profile must combine reported capabilities with real-device qualification, including explicit workarounds or blacklists when evidence contradicts nominal capability reporting.

### 5.3 Single camera-session authority

`CameraSessionController` is the current owner of Camera2 device/session resources. The candidate still-photo path adds its JPEG output to this same authority rather than opening an independent camera. Activities, future modes, extensions, and platform adapters must not open independent camera sessions behind this authority. Session ownership should evolve behind narrow interfaces as additional capture engines are introduced.

### 5.4 Recoverable media lifecycle

The full capture pipeline must validate camera/storage/thermal/battery/microphone/permission prerequisites, run ephemeral preview analysis, stage incomplete output recoverably, apply privacy/metadata policy, finalize media atomically or recoverably, verify committed artifacts, notify only authorized downstream consumers, and remove temporary state after successful commit.

PR #6 implements the first bounded portion of this model: reserve a pending MediaStore destination, submit one JPEG capture, validate/write the JPEG, publish only after successful completion, and delete the pending row on handled failure. This is not yet a complete crash/process-death journal or production-grade recovery system.

## 6. Major components and implementation status

- **Camera Session Controller — Foundation implemented and representative-emulator preview qualified; still-capture extension in PR #6 candidate.** Owns Camera2 lifecycle, device/session resources, preview start/stop, candidate one-shot JPEG capture, and shutdown. Physical-device qualification remains open.
- **Capability Registry — Foundation implemented; JPEG discovery added in PR #6 candidate.** Enumerates Camera2 capability facts, preview sizes, and candidate JPEG output sizes.
- **Preview Engine — Foundation implemented and representative-emulator qualified.** `TextureView` host plus Camera2 repeating preview request; physical-device and performance qualification remain pending.
- **Camera Selector — Foundation implemented.** Deterministic default-camera selection.
- **Photo Capture Engine — Bounded candidate implementation.** PR #6 adds one explicit JPEG still capture and handled-failure finalization; burst, RAW, computational pipelines, orientation/metadata controls, physical-device acceptance, and production photo UX remain planned.
- **Video Capture Engine — Planned.** Video/audio capture, encoding, stabilization configuration, monitoring, and recording recovery.
- **Processing Pipeline — Planned.** Local computational photography and video transformations.
- **Metadata and Provenance Engine — Planned.** Metadata policy, artifact relationships, processing provenance, and future authenticity records.
- **Storage Coordinator — Partially implemented in PR #6 candidate.** Pending MediaStore JPEG reservation, write, publish, and handled-failure deletion exist; broader storage reserve, protected destinations, journaling, process-death recovery, and richer cleanup remain planned.
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

The authoritative implementation currently provides the Android application shell, permission flow, capability discovery, camera selection, preview-session foundation, and representative Android-emulator preview qualification. PR #6 adds a bounded engineering-stage JPEG shutter and MediaStore publication candidate. Production Photo mode and all richer user-facing capture modes remain planned until their implementation and acceptance evidence reclassifies them.

## 8. Data and storage

Ordinary photographs and videos must remain normal user-owned files. GoreeCloud-specific relationships may use portable metadata or sidecars, but primary media must remain usable without GoreeCloud Camera.

PR #6's bounded still candidate creates only a newly captured JPEG through Android MediaStore. It reserves the row as pending under `DCIM/GoreeCloud Camera`, writes the JPEG, and publishes the item only after the write succeeds. The candidate does not read the user's existing media library, create an application database, persist preferences, create sidecars, or create capture relationship records.

Planned capture relationship data should be versioned and capable of representing primary media, RAW, motion sequences, separate camera streams, sidecars, integrity state, privacy policy, and computational-modification provenance.

Private Capture may use a protected destination, but export/recovery rules must remain explicit and user-controlled.

## 9. Interfaces and APIs

No public network API is implemented or required for core capture.

Current internal source interfaces include:

- `CameraDescriptor` and `LensFacing` for neutral camera identity/role representation;
- `CameraSelector` for deterministic default-camera selection;
- `CameraCapabilityRegistry` for Camera2 capability discovery plus preview/JPEG output-size selection;
- `CameraSessionController` for lifecycle-owned Camera2 preview and candidate still-capture resources;
- `CameraSessionState` for observable engineering-shell session state;
- `PhotoCaptureOutcome` for bounded capture completion/failure reporting;
- `PhotoFileNamer` for deterministic UTC JPEG naming;
- `PhotoMediaStoreCommitter` and `PendingPhoto` for candidate pending-row reservation, JPEG commit/publication, and discard cleanup.

Future platform adapters, capture-result contracts, Remote Viewfinder transport, Lens manifests, or integration APIs must be separately versioned when introduced. Core capture must never depend on a hosted control plane.

## 10. Authentication and authorization

Basic local capture must not require account authentication. Identity may become applicable to optional account-bound, paired-device, sharing, or cross-device workflows, but must not be used to broaden a capture operation beyond the authority carried by its capture context.

The current foundation and PR #6 candidate have no account, network, remote-control, or Identity dependency.

Remote control requires explicit pairing, session-scoped authorization, visible active-state indication, and revocation when implemented.

## 11. Privacy requirements

### 11.1 Current implemented privacy boundary

The Android manifest requests only `android.permission.CAMERA`.

The foundation and PR #6 candidate intentionally do **not** request:

- `android.permission.INTERNET`;
- fine or coarse location;
- `android.permission.RECORD_AUDIO`;
- legacy storage permission;
- all-files access;
- media-library read permissions.

The qualified preview pipeline does not persist preview frames, upload content, record audio, read location, or access an existing media library. The PR #6 candidate persists only the JPEG created by an explicit shutter operation into a newly reserved MediaStore item. Runtime qualification grants only CAMERA permission. These are current implementation/evidence facts, not Privacy Shield acceptance evidence.

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
- Camera/session/surface/`ImageReader` resources are closed on activity pause, surface loss, stop, and shutdown paths.
- Candidate still output is held as a pending MediaStore item until a valid JPEG is written successfully.
- Handled reserve/capture/write/finalization failures attempt to delete the pending MediaStore item instead of intentionally publishing incomplete output.
- The application disallows cleartext network traffic even though it has no Internet permission.
- Android backup is disabled for this engineering foundation.
- Exact-revision runtime qualification records package/app-ops state and CameraService evidence without granting broader runtime permission authority.

These controls do not constitute Wardveil Security acceptance.

### 12.2 Future security requirements

- Protect temporary frames, RAW data, cached/in-progress video, Private Capture material, microphone use, location metadata, and remote-session state according to sensitivity and lifetime.
- Add process-death/interrupted-operation recovery so incomplete capture state cannot rely only on in-process cleanup.
- Lens packages must be signed, sandboxed, permission-scoped, resource-limited, and killable without destabilizing core capture where practical.
- Private Capture temporary data must not leak into ordinary thumbnails, logs, caches, or backup queues.
- Diagnostics must not contain raw frame/media payloads by default.
- Use mature approved cryptographic/platform primitives; do not invent custom cryptography.

## 13. GoreeCloud Platform Contract

The repository declares **Platform Contract 0.2** and exactly seven Integral Platform Systems. The current approved Stable Glaze UI consumer target is **1.5.0**; Camera remains `applicable-blocked` and has no application-specific Glaze acceptance.

All seven remain **applicable-blocked** at this milestone because application-specific runtime acceptance evidence does not exist:

1. GoreeCloud Manager
2. Privacy Shield
3. Wardveil Security
4. Everkeep
5. Glaze UI
6. GoreeCloud Mesh
7. GoreeCloud Identity

Camera preview/still-capture evidence is relevant product implementation evidence but does not satisfy any Platform System acceptance contract on its own.

GoreeCloud Sync is separately governed and must not appear as an eighth `platform_systems` key. If Camera later uses Sync, synchronization authorization, datasets, version/change model, conflicts, replication, offline resume, and cross-device behavior must be documented separately.

## 14. Accessibility

The engineering shell provides basic text labels, an explicit camera-permission control, a content description for the preview host, and a stable content description for the candidate shutter so runtime automation and assistive technology can identify it. No accessibility acceptance is claimed.

Production requirements include meaningful labels/state, logical focus order, screen-reader announcements, non-color-only critical warnings, large-text and reduced-motion support, adequate contrast/target sizing, hardware/voice/gesture alternatives where appropriate, haptic confirmation, foldable/large-screen operability, and optional privacy-aware visual descriptions.

Accessibility acceptance requires real implementation and device validation.

## 15. Performance, reliability, and continuity

Performance budgets are qualification targets, not current claims. Real-device testing must establish thresholds for launch, preview readiness, shutter responsiveness, lens switching, sustained recording, memory/thermal behavior, storage exhaustion, battery-critical finalization, and interrupted-session recovery.

The current session controller explicitly closes resources across activity/surface lifecycle transitions, and the representative emulator path has verified a successful open/configure/repeating-preview flow. The PR #6 candidate adds handled in-process still-capture cleanup but does not establish process-death recovery or physical-device capture reliability.

Correctness, media integrity, privacy, security, and thermal sustainability take precedence over headline latency.

Camera does not own general media backup/restore, but captured files must remain exportable and portable. In-progress capture recovery is a Camera reliability responsibility once capture persistence exists.

## 16. Dependencies and configuration

The current implementation deliberately minimizes dependencies:

- Android platform/framework APIs for runtime behavior;
- Android Gradle Plugin 9.4.0 as build tooling;
- Gradle 9.6.0 in CI;
- JUnit 4.13.2 for local unit testing;
- pinned `reactivecircus/android-emulator-runner` only as CI/runtime-qualification tooling, not as an application runtime dependency.

There is no third-party runtime camera library, hosted SDK, telemetry SDK, networking SDK, account SDK, or proprietary control-plane dependency in the application foundation.

Future dependencies must remain limited to justified open-source or platform foundations. Core operation must not depend on proprietary hosted control planes, vendor accounts, or commercial cloud processing.

Tracked configuration must contain no reusable secrets. User preferences, device qualification, privacy policy state, presets, extension permissions, diagnostics settings, and build-time configuration should remain clearly separated.

## 17. Observability

The current engineering shell exposes non-persistent session state, a coarse camera-count capability summary, and bounded photo-saved/failure status for bring-up. No telemetry or remote diagnostics are implemented.

CI runtime qualification captures local disposable-emulator evidence including UI hierarchy, screenshot, CameraService state, package/app-ops state, MediaStore query results, logcat, provenance, and—when the capture gate succeeds—the captured JPEG payload for signature verification. This evidence is qualification output rather than product telemetry and is tied to an exact source revision.

Future diagnostics may record privacy-safe structured events such as session lifecycle, mode, capability decisions, encoder configuration, thermal/storage state classes, error codes, recovery outcomes, and timing metrics.

Ordinary diagnostics must not contain raw camera frames, recognized faces, OCR content, precise location, microphone payloads, or captured user media. Test-only runtime evidence containing a disposable emulator JPEG remains CI qualification material and must not be treated as normal product diagnostics.

## 18. Testing and validation

Current repository validation includes:

- static source-contract validation of application ID, API levels, product version, AGP pin, lifecycle declaration, required foundation/capture files, the camera-only permission boundary, Camera2 JPEG still-capture primitives, and MediaStore pending/finalization semantics;
- local pure-Kotlin unit tests for deterministic camera selection and deterministic photo filename generation;
- Android lint, unit tests, and debug APK assembly in exact-revision CI;
- Platform Contract validation through the repository's pinned reusable contract workflow;
- authoritative preview qualification on Android 16/API 36 with an emulated back camera;
- an active PR #6 exact-revision runtime gate that installs the APK, grants only CAMERA permission, requires preview readiness, locates the shutter through its stable accessibility description, triggers one capture, requires a `Photo saved` result, locates the matching non-zero JPEG in `MediaStore.Images/external_primary/DCIM/GoreeCloud Camera`, reads the item back, verifies the JPEG signature, and preserves local runtime evidence;
- successful PR #4 candidate validation in Android Foundation run `35020668795` and Platform Contract run `35020668711` for `cdc59289c607558090c298ee74ae2829268920c1`;
- successful post-merge exact-main Android Foundation run `35021425774` and Platform Contract run `35021428000` for `6dbd2c1a5c5e3fb523662f5a6e181c6dbf73644a`;
- successful documentation-reconciled authoritative-main validation after PR #5.

The PR #6 runtime gate must pass on the exact final candidate head before merge, and the authoritative merged revision must be independently revalidated before the still-photo path is described as verified authoritative behavior. An emulator infrastructure failure before the application reaches preview is not evidence that Camera2 still capture or MediaStore publication failed.

Passing representative emulator qualification proves only the configured virtual-camera behavior for that exact revision. It does not prove physical-device support, OEM camera behavior, production UI quality, process-death recovery, application-specific Platform System acceptance, or production acceptance.

Future validation must cover instrumentation tests, fake pipeline tests where useful, physical-device qualification, media integrity, process death, camera-service restart, lock/unlock, storage exhaustion, thermal escalation, microphone route changes, runtime permission revocation, Lens isolation, Remote Viewfinder disconnect, accessibility, privacy, security, and platform integration acceptance.

## 19. Production acceptance

Camera must not be classified Stable until applicable GoreeCloud production-readiness requirements and Camera-specific gates are satisfied, including an identifiable candidate, qualified real-device matrix, no release-blocking media-loss/corruption/privacy/security defects, verified offline core capture and recovery, accepted applicable Platform System integrations, current Stable Glaze UI application-specific acceptance, synchronized repository/Drive documentation, and required human visual/usability/device acceptance.

This foundation and PR #6 candidate are not a release candidate and are not production accepted.

## 20. Known boundaries and open decisions

Open decisions include:

- final minimum Android/API support baseline beyond the provisional `minSdk 29` engineering baseline;
- whether selected future subsystems should use higher-level Android camera abstractions while Camera2 remains the advanced-control/session foundation;
- first physical-device qualification list and tier model;
- robust interrupted/process-death still-capture recovery and stale-pending-row reconciliation;
- JPEG orientation and richer metadata policy;
- Motion Photo representation;
- codec/HDR and recording matrix;
- Private Capture protected storage;
- provenance/authenticity format;
- Lens runtime technology;
- Remote Viewfinder transport;
- final performance budgets;
- recognized repository license.

## 21. Immediate next engineering milestone

PR #6 is the current bounded milestone and must first satisfy exact-head CI, exact-head representative emulator JPEG + MediaStore qualification, merge review, and authoritative-main revalidation.

After that acceptance, the next Phase 1 implementation slice should remain bounded and should advance **basic Video/audio capture** without coupling in Pro Video, Cinema, streaming, remote control, or advanced processing. That follow-on slice should:

1. preserve the single camera-session authority and capability-driven hardware selection;
2. introduce `android.permission.RECORD_AUDIO` only when a real audio-recording path is implemented and justified, while preserving a usable video-without-audio path where technically appropriate;
3. use an explicit recording state machine rather than implicit UI state;
4. write ordinary user-owned media through Android-supported scoped-storage/MediaStore behavior;
5. stage/finalize recording output recoverably and avoid publishing known-incomplete media as completed output;
6. add bounded duration/stop/error handling and lifecycle cleanup before advanced video controls;
7. extend exact-revision emulator validation where representative virtual hardware can exercise the path, while retaining physical-device recording qualification as a separate required gate;
8. preserve Concept lifecycle and all application-specific Platform System acceptance blockers until separately satisfied.

Local settings, physical-device qualification, process-death recovery, licensing, current Stable Glaze UI implementation, and Platform System acceptance remain separate Phase 1/qualification obligations and must not be silently folded into a broader unreviewable change.
