# GoreeCloud Camera

> Repository document version: **0.2.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

GoreeCloud Camera is the first-party photography, video, scanning, and creative-capture application planned for GoreeCloud mobile devices and supported Android phones.

## Current state

This repository now contains a **real native Android implementation foundation** rather than documentation-only scaffolding. The source includes an Android/Gradle project, application identity, Camera2 permission flow, a live-preview session controller, hardware capability discovery, deterministic default-camera selection, unit tests, source-contract checks, and Android CI that assembles a debug APK from the exact candidate revision.

The release lifecycle remains **Concept** because no supported real device or Android emulator has yet verified that the preview opens and behaves correctly at runtime. A successful build proves source/build viability, not device qualification or production readiness.

## Implemented foundation

- Canonical Android application ID: `com.goreecloud.camera`.
- Product version: `0.1.0`.
- Compile/target baseline: Android 17 / API 37.
- Provisional minimum SDK: API 29, pending explicit support-policy approval and device qualification.
- JDK target: 17.
- Android Gradle Plugin: 9.4.0.
- Gradle validation baseline: 9.6.0.
- Native Kotlin source using AGP built-in Kotlin support.
- Camera permission only for the current runtime milestone.
- Camera2 device enumeration and capability registry.
- Deterministic preference for a rear camera when available.
- Lifecycle-owned preview session with explicit open/close cleanup.
- No network, location, microphone, or media-library permission in the foundation manifest.
- Unit tests for camera selection and a static source-contract validator.
- Exact-revision CI that lints, tests, assembles a debug APK, generates provenance, verifies its SHA-256 digest, and uploads it as evidence.

## Not yet implemented or accepted

- still-photo capture and file finalization;
- video/audio capture;
- MediaStore persistence;
- atomic/recoverable media staging;
- Glaze UI integration or acceptance;
- Privacy Shield runtime integration;
- Wardveil Security runtime integration;
- Everkeep integration;
- GoreeCloud Manager, Mesh, or Identity integration;
- device-specific qualification profiles;
- real-device preview acceptance;
- Private Capture;
- Gallery/Photos handoff;
- production signing, release packaging, or Stable qualification.

## Product direction

GoreeCloud Camera is intended to combine three coherent experiences:

- **Professional Camera** — high-quality photography and video, manual controls, RAW-capable workflows, monitoring, stabilization, and filmmaking-oriented tools.
- **Creative Camera** — filters, GoreeCloud Lenses, multi-camera creator workflows, expressive overlays, short-form production tools, and local effects.
- **Private Camera** — offline-first capture, local processing, explicit metadata control, protected capture destinations, and strict limits on secondary processing or sharing.

Core capture is intended to remain usable without an account, GoreeCloud server, external AI provider, commercial cloud service, or Internet connection.

## Development

Current CI uses JDK 17, Gradle 9.6.0, Android SDK 37, and AGP 9.4.0.

With matching local tooling installed:

```bash
gradle --no-daemon lintDebug testDebugUnitTest assembleDebug
```

The debug APK is generated under `app/build/outputs/apk/debug/`. It is an engineering artifact only and does not establish device support or a release.

## Privacy and security boundary of the foundation

The initial manifest requests only `android.permission.CAMERA`. It does not request Internet, location, microphone, or media-library permissions. Preview frames are displayed through the Android camera pipeline and are not written to storage by this milestone. This is a narrow implementation boundary, not Privacy Shield or Wardveil acceptance.

## GoreeCloud platform direction

- Current GoreeCloud Platform Contract: **0.2**.
- Current required Glaze UI consumer target: **1.4.1**.
- Seven Integral Platform Systems are evaluated explicitly: GoreeCloud Manager, Privacy Shield, Wardveil Security, Everkeep, Glaze UI, GoreeCloud Mesh, and GoreeCloud Identity.
- GoreeCloud Sync is separately governed and is **not** an eighth Integral Platform System.

## Repository documentation

- [SPECIFICATIONS.md](SPECIFICATIONS.md) — repository-coupled product and implementation specification.
- [FEATURES.md](FEATURES.md) — verified source/build functionality and implementation state.
- [FEATURE-ROADMAP.md](FEATURE-ROADMAP.md) — planned capability and delivery roadmap.
- [BENEFITS.md](BENEFITS.md) — planned product benefits.
- [COMPETITIVE-OBJECTIVES.md](COMPETITIVE-OBJECTIVES.md) — differentiation and improvement objectives.
- [BRANDING.md](BRANDING.md) — product identity and presentation requirements.
- [USER-MANUAL.md](USER-MANUAL.md) — current engineering-use instructions and limitations.
- [PRIVACY POLICY.md](PRIVACY%20POLICY.md) — current privacy behavior and future requirements.
- [SECURITY.md](SECURITY.md) — current security boundary and future requirements.
- [NOTES.md](NOTES.md) — unresolved engineering decisions and blockers.
- [goreecloud.platform.yaml](goreecloud.platform.yaml) — machine-readable Platform Contract declaration.

## License

A recognized open-source license has **not yet been selected and committed** for this repository. Because the repository is public and GoreeCloud requires transparent open-source licensing, license selection remains an active governance blocker. Do not infer permission terms from repository visibility alone.

## Status integrity

The native foundation does **not** prove Experimental lifecycle eligibility, device qualification, Glaze UI conformance, Privacy Shield acceptance, Wardveil acceptance, production readiness, or Stable status. Lifecycle promotion requires the evidence defined by the governing GoreeCloud release-lifecycle records.
