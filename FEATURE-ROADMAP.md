# GoreeCloud Camera — Feature Roadmap

> Repository document version: **0.3.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Drive synchronized representation: **GoreeCloud/Feature Roadmap/GoreeCloud Camera/FEATURE-ROADMAP.md**

The repository `FEATURE-ROADMAP.md` is the **canonical editable roadmap source** under current GoreeCloud governance. The corresponding Drive Markdown record is the synchronized GoreeCloud-wide representation. Both must remain materially consistent with authoritative project documentation and verified implementation evidence.

Items remain **planned unless explicitly reclassified by verified implementation evidence**. The native Android foundation is currently in progress as recorded below; this does not promote the product beyond Concept.

## Product direction

GoreeCloud Camera is planned as one native application combining **Professional Camera**, **Creative Camera**, and **Private Camera**. Core capture must remain offline-first, local, user-owned, and independent of mandatory accounts or cloud services.

## Delivery sequence

### Phase 0 — Governance and architecture baseline
Status: **Substantially complete; licensing blocker remains**

Completed repository work includes the canonical Drive project specification, synchronized Drive roadmap, repository documentation baseline, product version identity, Platform Contract 0.2 declaration, current lifecycle classification, and exact-revision Platform Contract validation. A recognized repository license remains unresolved.

### Phase 1 — Native Android capture foundation
Status: **In progress**

Verified on authoritative `main` through documentation-reconciled commit `1687d81a65c037b46a25f98a23e5a0d1453e58de`:

- real Android/Gradle project;
- canonical app identity and version metadata;
- Android 17/API 37 compile and target baseline;
- provisional API 29 minimum SDK;
- camera permission flow;
- lifecycle-owned viewfinder host;
- camera enumeration and Capability Registry;
- deterministic default-camera selection;
- Camera2 preview session controller;
- initial unit/static tests and Android CI;
- exact-revision Android build provenance and Concept-stage APK evidence;
- representative Android 16/API 36 emulator qualification using an emulated back camera;
- verified transition to `Session: previewing` with one detected virtual camera;
- runtime evidence collection covering UI hierarchy, screenshot, CameraService state, package/app-ops state, logcat, and provenance;
- Platform Contract 0.2 validation with all application-specific runtime Platform System integrations still blocked/unverified.

PR #4 candidate `cdc59289c607558090c298ee74ae2829268920c1` passed Android Foundation run `35020668795`, including preview-runtime emulator qualification, and Platform Contract run `35020668711`. After merge, authoritative commit `6dbd2c1a5c5e3fb523662f5a6e181c6dbf73644a` passed Android Foundation run `35021425774` and Platform Contract run `35021428000`. Documentation reconciliation PR #5 then produced authoritative `main` commit `1687d81a65c037b46a25f98a23e5a0d1453e58de`, whose Android Foundation and Platform Contract gates also passed.

The representative emulator result proves that the current authoritative Camera2 preview path can open/configure and run a repeating preview against the configured Android virtual camera. It does **not** qualify a physical phone, prove OEM/device support, establish production UI acceptance, or prove a merged still/video capture path.

#### PR #6 — first bounded JPEG Photo + MediaStore candidate

The active PR #6 source candidate adds the first bounded still-photo implementation while preserving product version `0.1.0`, Concept lifecycle, Platform Contract 0.2, the single Camera2 session authority, and the camera-only runtime permission boundary.

Candidate source includes:

- JPEG output-size discovery through the Capability Registry;
- one JPEG `ImageReader` added to the existing Camera2 session outputs;
- explicit engineering-stage **Capture photo** control and `CAPTURING` state;
- one-shot `TEMPLATE_STILL_CAPTURE` requests;
- deterministic UTC `GCAM_*.jpg` naming;
- MediaStore reservation in `DCIM/GoreeCloud Camera` using `IS_PENDING=1`;
- JPEG signature validation before publication;
- publish-after-write by clearing `IS_PENDING`;
- deletion of the pending MediaStore row on handled reserve/capture/write/finalization failure paths;
- lifecycle cleanup of pending capture state and `ImageReader` resources;
- no Internet, location, microphone, storage, all-files, or media-library read permission expansion;
- source-contract validation, filename unit coverage, and an Android 16/API 36 emulator capture gate intended to verify a non-zero published JPEG and JPEG signature.

This candidate implementation is **not yet authoritative `main` state** and must not be described as a supported Camera feature until the exact final PR head passes its required checks and the merged authoritative revision is independently revalidated. Physical-device support, production UI, process-death recovery, application-specific Platform System acceptance, Release Candidate, and Stable status remain unverified.

Still required to complete Phase 1 after the bounded still-photo candidate is accepted:

- basic Video/audio capture;
- local settings foundation;
- initial physical-device support/qualification record and camera-quirk evidence;
- stronger interrupted/process-death capture recovery beyond handled in-process cleanup;
- resolution of the public-repository open-source license blocker.

Camera remains **Concept**. Emulator runtime evidence materially strengthens implementation confidence but does not by itself establish a user-ready release or lifecycle promotion.

### Phase 2 — Capture reliability and device qualification
Status: **Planned**

- atomic/recoverable still and recording finalization beyond the bounded Phase 1 MediaStore commit path;
- recoverable/journaled recording design;
- storage reserve/remaining-time logic;
- battery-critical finalization;
- thermal degradation policy;
- camera-service/lifecycle recovery;
- device capability profiles and known-quirk overrides;
- qualification evidence tied to app/device/OS revisions.

### Phase 3 — High-quality automatic photography
Status: **Planned**

Exposure/white-balance optimization, dynamic-range enhancement, local multi-frame processing, low-light optimization, face-aware exposure, natural skin-tone handling, lens/distortion correction, and processing-quality profiles.

### Phase 4 — Advanced photography and filmmaking
Status: **Planned**

Night, Portrait, Motion Photos, burst/Best Shot, Pro Photo, RAW, Pro Video, Cinema, professional monitoring, stabilization/Horizon Lock, and qualified external microphone/storage workflows.

### Phase 5 — Multi-camera and creator workflows
Status: **Planned**

Dual Camera, separate-stream/combined recording, concurrent cameras, Director Mode, Multi-Capture, teleprompter, multi-clip creator tools, slow motion, timelapse, hyperlapse, panorama, macro, and product photography.

### Phase 6 — Creative platform and GoreeCloud Lenses
Status: **Planned**

Creative Mode, filters/styles, signed and sandboxed Lens packages, permission/resource controls, local effects, and future Lens Studio authoring direction.

### Phase 7 — Scanning and visual utilities
Status: **Planned**

Document scanning, multi-page capture, OCR/searchable documents, QR/barcode recognition, text/object/color/measurement assistance, and privacy-aware accessibility descriptions.

### Phase 8 — Private Camera, metadata, and provenance
Status: **Planned**

Private Capture, protected destinations, metadata policy, sharing-time privacy controls, provenance relationships, and future content-authenticity support.

### Phase 9 — GoreeCloud ecosystem integration
Status: **Planned**

Platform Contract 0.2 evaluation against exactly seven Integral Platform Systems and evidence-backed Gallery/Photos/Everkeep/Privacy Shield/Wardveil/Manager/Mesh/Identity relationships. GoreeCloud Sync remains separately governed. Glaze UI migration must use the current approved Stable contract when production UI implementation begins; no application-specific Glaze acceptance is claimed by the current engineering shell.

### Phase 10 — Remote and adaptive hardware experiences
Status: **Planned**

Remote Viewfinder, explicit pairing/session authority, foldable and large-screen layouts, outer-screen preview, rear-camera selfies, tabletop/flex posture, and secondary displays where supported.

### Phase 11 — Release qualification
Status: **Planned**

Real-device matrix, recovery/security/privacy/accessibility acceptance, current Glaze UI conformance, platform-system evidence, packaging/release evidence, documentation synchronization, and production-readiness gates before Stable.

## Canonical feature families

The synchronized Drive roadmap retains the detailed capability requirements for automatic photography, low light, portraits, Motion Photos, Best Shot, professional still/video, Cinema, stabilization, dual/multi-camera, creative capture, Lenses, filters, creator tools, scanning, visual utilities, Camera Intelligence, Private Capture, metadata, offline-first architecture, Gallery/Photos/Everkeep relationships, Glaze UI, Wardveil Security, Privacy Shield, Everkeep, capability profiles, reliability, storage/thermal/battery safeguards, provenance, accessibility, diagnostics, qualification, and release acceptance.
