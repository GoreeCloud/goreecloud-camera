# GoreeCloud Camera — Repository Specifications

> Repository document version: **0.1.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Repository: `GoreeCloud/goreecloud-camera`

## 1. Purpose and authority

GoreeCloud Camera is the planned GoreeCloud-owned mobile capture application for still photography, video, scanning, creative capture, computational imaging, visual utilities, and privacy-controlled acquisition of source media.

This repository is currently a **Concept-stage specification and governance baseline**. Requirements in this file describe the approved implementation direction unless explicitly identified as verified current state. They do not prove that a runtime capability exists.

The canonical Drive project specification is `GoreeCloud/Projects/Project Specification — Camera.docx`. The canonical planned-feature record is `GoreeCloud/Feature Roadmap/GoreeCloud Camera/FEATURE-ROADMAP.docx`. This file is the repository-coupled technical specification and must remain materially synchronized with those records.

## 2. Verified current implementation state

At this baseline:

- The repository exists and uses `main` as its default branch.
- Before this baseline work, the repository contained only a minimal `README.md`.
- No Android source tree or Gradle build has been verified.
- No application package, release artifact, automated runtime test suite, device-qualification evidence, or platform-integration acceptance has been verified.
- No recognized open-source license has yet been committed.

The product lifecycle therefore remains **Concept**.

## 3. Product scope and boundaries

Camera owns capture-session orchestration and production of source media. It must not become the authoritative media-library, general backup, synchronization, or long-term organizational system.

Planned authority boundaries:

- **GoreeCloud Camera** — capture.
- **GoreeCloud Gallery** — local media viewing and management.
- **GoreeCloud Photos** — backup, synchronization, organization, intelligence, sharing, and preservation.
- **Everkeep** — resilience, preservation, portability, continuity, succession, and recovery requirements.

Extensions, scanners, remote-control clients, and downstream apps must not acquire broader camera or media authority merely because they participate in a capture workflow.

## 4. Platform and technology direction

The first implementation is planned as a native Android application using Kotlin and standard Android/Gradle organization. Platform camera abstractions should be used where they improve lifecycle handling and compatibility, with Camera2 or other lower-level interfaces where advanced manual control, concurrent cameras, RAW, high-speed capture, or device-specific behavior requires them.

The UI must use the current accepted GoreeCloud Glaze UI contract. The current consumer target is **1.4.1**. Unsupported hardware capabilities must remain hidden or unavailable rather than being advertised from theoretical API support.

Exact minimum Android API, device support baseline, CameraX-vs-Camera2 orchestration, codec/HDR matrix, and initial qualified devices remain open engineering decisions.

## 5. Architectural principles

### 5.1 Authorization travels with the operation

Each capture request should carry an effective context containing mode, requested camera set, destination policy, Privacy Shield context, metadata policy, extension authority, remote-control authority, quality profile, and applicable security state. Downstream modules consume that context rather than re-deriving authority from identity alone.

### 5.2 Capability-driven hardware behavior

Camera must not assume uniform Android camera hardware. An effective device profile combines reported capabilities with real-device qualification, including explicit workarounds or blacklists when evidence contradicts nominal capability reporting.

### 5.3 Recoverable media lifecycle

The capture pipeline should validate camera/storage/thermal/battery/microphone/permission prerequisites, run ephemeral preview analysis, stage incomplete output recoverably, apply privacy/metadata policy, finalize media atomically or recoverably, verify committed artifacts, notify only authorized downstream consumers, and remove temporary state after successful commit.

Recoverable completed media must not be silently lost after process death, camera-service restart, storage interruption, battery events, or OS lifecycle interruption when recovery is technically possible.

## 6. Planned major components

- **Camera Session Controller** — lifecycle, camera resource arbitration, and active-session ownership.
- **Capability Registry** — raw platform capability plus qualified device profile.
- **Preview Engine** — viewfinder surfaces and ephemeral analysis-frame routing.
- **Photo Capture Engine** — still capture, burst, RAW, computational pipelines, and finalization.
- **Video Capture Engine** — video/audio capture, encoding, stabilization configuration, monitoring, and recording recovery.
- **Processing Pipeline** — local computational photography and video transformations.
- **Metadata and Provenance Engine** — metadata policy, artifact relationships, processing provenance, and future authenticity records.
- **Storage Coordinator** — staging, MediaStore/protected destinations, atomic commit, storage reserve, and cleanup.
- **Private Capture Controller** — stricter metadata, analysis, backup, Lens, preview, sharing, and storage behavior.
- **Lens Runtime** — signed, sandboxed, permission-scoped, resource-limited creative effects.
- **Scanner and Visual Utilities** — document/code scanning and optional local visual assistance.
- **Remote Viewfinder Controller** — future explicit pairing, session-scoped authority, and visible remote control.
- **Platform Adapters** — narrow integrations with applicable GoreeCloud systems.
- **Diagnostics** — privacy-safe structured events and qualification evidence.

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

All remain planned until verified implementation evidence reclassifies them.

## 8. Data and storage

Ordinary photographs and videos must remain normal user-owned files. GoreeCloud-specific relationships may use portable metadata or sidecars, but primary media must remain usable without GoreeCloud Camera.

Planned capture relationship data should be versioned and capable of representing primary media, RAW, motion sequences, separate camera streams, sidecars, integrity state, privacy policy, and computational-modification provenance.

Private Capture may use a protected destination, but export/recovery rules must remain explicit and user-controlled.

## 9. Interfaces and APIs

No public network API is currently implemented or required for core capture. Internal module contracts should be narrow and testable. Future platform adapters, Remote Viewfinder transport, Lens manifests, or integration APIs must be separately versioned when introduced.

Core capture must never depend on a hosted control plane.

## 10. Authentication and authorization

Basic local capture must not require account authentication. Identity may become applicable to optional account-bound, paired-device, sharing, or cross-device workflows, but must not be used to broaden a capture operation beyond the authority carried by its capture context.

Remote control requires explicit pairing, session-scoped authorization, visible active-state indication, and revocation.

## 11. Privacy requirements

- Core capture is offline-first and local-first.
- Viewfinder/analysis frames are ephemeral by default.
- Location and other sensitive metadata are separately controllable.
- Private Capture provides a strong visible state and stricter destination/metadata/analysis/backup/extension behavior.
- Lens packages receive only minimum frame access and must not silently upload frames.
- Sharing/export supports Original, Privacy Safe, and Custom metadata policies when implemented.
- Optional external processing must not become mandatory for normal capture.
- Applicable Privacy Shield contracts fail closed for dependent features while unrelated local capture remains available where policy permits.

## 12. Security requirements

- Centralize camera-session ownership.
- Protect temporary frames, RAW data, cached/in-progress video, Private Capture material, microphone use, location metadata, and remote-session state according to sensitivity and lifetime.
- Lens packages must be signed, sandboxed, permission-scoped, resource-limited, and killable without destabilizing core capture where practical.
- Private Capture temporary data must not leak into ordinary thumbnails, logs, caches, or backup queues.
- Diagnostics must not contain raw frame/media payloads by default.
- Use mature approved cryptographic/platform primitives; do not invent custom cryptography.

## 13. GoreeCloud Platform Contract

The repository declares **Platform Contract 0.2**, exactly seven Integral Platform Systems, and current Glaze UI target **1.4.1**.

At Concept stage every Platform System is **applicable-blocked** because no Camera runtime implementation or acceptance evidence exists:

1. GoreeCloud Manager
2. Privacy Shield
3. Wardveil Security
4. Everkeep
5. Glaze UI
6. GoreeCloud Mesh
7. GoreeCloud Identity

GoreeCloud Sync is separately governed and must not appear as an eighth `platform_systems` key. If Camera later uses Sync, synchronization authorization, datasets, version/change model, conflicts, replication, offline resume, and cross-device behavior must be documented separately.

## 14. Accessibility

Planned requirements include meaningful labels/state, logical focus order, screen-reader announcements, non-color-only critical warnings, large-text and reduced-motion support, adequate contrast/target sizing, hardware/voice/gesture alternatives where appropriate, haptic confirmation, foldable/large-screen operability, and optional privacy-aware visual descriptions.

Accessibility acceptance requires real implementation and device validation.

## 15. Performance, reliability, and continuity

Performance budgets are qualification targets, not current claims. Real-device testing must establish thresholds for launch, preview readiness, shutter responsiveness, lens switching, sustained recording, memory/thermal behavior, storage exhaustion, battery-critical finalization, and interrupted-session recovery.

Correctness, media integrity, privacy, security, and thermal sustainability take precedence over headline latency.

Camera does not own general media backup/restore, but captured files must remain exportable and portable. In-progress capture recovery is a Camera reliability responsibility.

## 16. Dependencies and configuration

No runtime dependency set or runtime configuration format exists yet. Future dependencies must be limited to justified open-source or platform foundations. Core operation must not depend on proprietary hosted control planes, vendor accounts, or commercial cloud processing.

Tracked configuration must contain no reusable secrets. User preferences, device qualification, privacy policy state, presets, extension permissions, diagnostics settings, and build-time configuration should remain clearly separated.

## 17. Observability

Planned diagnostics may record privacy-safe structured events such as session lifecycle, mode, capability decisions, encoder configuration, thermal/storage state classes, error codes, recovery outcomes, and timing metrics.

Ordinary diagnostics must not contain raw camera frames, recognized faces, OCR content, precise location, microphone payloads, or captured media.

## 18. Testing and validation

Future validation must cover unit/instrumentation tests, fake pipeline tests where useful, real-device qualification, media integrity, process death, camera-service restart, lock/unlock, storage exhaustion, thermal escalation, microphone route changes, runtime permission revocation, Lens isolation, Remote Viewfinder disconnect, accessibility, privacy, security, and platform integration acceptance.

## 19. Production acceptance

Camera must not be classified Stable until applicable GoreeCloud production-readiness requirements and Camera-specific gates are satisfied, including an identifiable candidate, qualified real-device matrix, no release-blocking media-loss/corruption/privacy/security defects, verified offline core capture and recovery, accepted applicable Platform System integrations, current Stable Glaze UI application-specific acceptance, synchronized repository/Drive documentation, and required human visual/usability/device acceptance.

## 20. Known boundaries and open decisions

Open decisions include minimum Android/API baseline, CameraX-vs-Camera2 orchestration, Motion Photo representation, codec/HDR policy, Private Capture protected storage, provenance/authenticity format, Lens runtime technology, Remote Viewfinder transport, first qualified device list, performance budgets, and the recognized repository license.

## 21. Immediate next engineering milestone

After repository-governance acceptance, the next milestone is a **real buildable native Android foundation**, not placeholder directories. It should introduce application/package version metadata, lifecycle-aware app shell, camera permission flow, viewfinder host, capability probing, session state, basic test infrastructure, and a defined Android target that can be built and validated.
