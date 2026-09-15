# GoreeCloud Camera — Feature Roadmap

> Repository document version: **0.2.1**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Drive synchronized representation: **GoreeCloud/Feature Roadmap/GoreeCloud Camera/FEATURE-ROADMAP.md v1.2**

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

Verified on authoritative `main` at squash commit `8de1eac6693f09ef52b0f12886108727352404e1`:

- real Android/Gradle project;
- canonical app identity and version metadata;
- Android 17/API 37 compile and target baseline;
- provisional API 29 minimum SDK;
- camera permission flow;
- lifecycle-owned viewfinder host;
- camera enumeration and Capability Registry;
- deterministic default-camera selection;
- Camera2 preview session controller;
- initial unit/static tests and Android CI.

The exact merged `main` commit passed push-triggered Android Foundation run `35017167769` and Platform Contract run `35017168819`.

Still required to complete Phase 1:

- verified runtime preview on representative device/emulator;
- basic still Photo capture;
- basic Video/audio capture;
- MediaStore finalization;
- local settings foundation;
- initial device support/qualification record.

### Phase 2 — Capture reliability and device qualification
Status: **Planned**

- atomic still finalization;
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

Current Platform Contract 0.2 evaluation against exactly seven Integral Platform Systems, current Glaze UI consumer target 1.4.1, and evidence-backed Gallery/Photos/Everkeep/Privacy Shield/Wardveil/Manager/Mesh/Identity relationships. GoreeCloud Sync remains separately governed.

### Phase 10 — Remote and adaptive hardware experiences
Status: **Planned**

Remote Viewfinder, explicit pairing/session authority, foldable and large-screen layouts, outer-screen preview, rear-camera selfies, tabletop/flex posture, and secondary displays where supported.

### Phase 11 — Release qualification
Status: **Planned**

Real-device matrix, recovery/security/privacy/accessibility acceptance, current Glaze UI conformance, platform-system evidence, packaging/release evidence, documentation synchronization, and production-readiness gates before Stable.

## Canonical feature families

The synchronized Drive roadmap retains the detailed capability requirements for automatic photography, low light, portraits, Motion Photos, Best Shot, professional still/video, Cinema, stabilization, dual/multi-camera, creative capture, Lenses, filters, creator tools, scanning, visual utilities, Camera Intelligence, Private Capture, metadata, offline-first architecture, Gallery/Photos/Keepsake relationships, Glaze UI, Wardveil Security, Privacy Shield, Everkeep, capability profiles, reliability, storage/thermal/battery safeguards, provenance, accessibility, diagnostics, qualification, and release acceptance.
