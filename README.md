# GoreeCloud Camera

> Repository document version: **0.4.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

GoreeCloud Camera is the first-party photography, video, scanning, and creative-capture application planned for GoreeCloud mobile devices and supported Android phones.

## Current state

The authoritative foundation is a native Android Camera2 implementation. Representative Android 16 / API 36 virtual-camera qualification has verified that the current preview path reaches `PREVIEWING`; this is emulator evidence only and does not qualify a physical device.

Authoritative `main` now includes the first bounded still-photo path: JPEG output through Camera2, a user-triggerable engineering shutter, and scoped-storage MediaStore publication using an `IS_PENDING` row with handled-failure cleanup. Final PR #6 candidate `a378a17d3e6cf28a16521428fa3d77c1910e2e53` and signed merged commit `d5272da9877b47bfca1551784f32b1031a084a8e` both passed the representative Android 16 / API 36 emulator capture gate, which verified one non-zero published JPEG and its JPEG signature. This remains emulator evidence only and does not qualify a physical device.

The release lifecycle remains **Concept**. Emulator success, source presence, and a buildable APK do not establish supported devices, production UI acceptance, or release readiness.

## Implemented foundation

- Canonical Android application ID: `com.goreecloud.camera`.
- Product version: `0.1.0`.
- Compile/target baseline: Android 17 / API 37; provisional minimum SDK: API 29.
- JDK 17, Android Gradle Plugin 9.4.0, and Gradle 9.6.0 in CI.
- Runtime manifest requests only `android.permission.CAMERA`.
- Camera2 device enumeration, capability profiles, deterministic default-camera selection, and lifecycle-owned session control.
- Representative Android 16 / API 36 emulated-back-camera preview qualification.
- Verified representative-emulator JPEG still capture using `TEMPLATE_STILL_CAPTURE` and `ImageReader`.
- Verified representative-emulator MediaStore commit path to `DCIM/GoreeCloud Camera` using `IS_PENDING`, publish-after-write, and pending-row deletion on handled failure.
- No Internet, location, microphone, storage, broad media-library, or all-files permission.
- Exact-revision lint, unit tests, APK build/provenance, Platform Contract validation, and emulator capture qualification.

## Still open

- physical-device preview and still-capture qualification;
- video/audio capture;
- zoom/focus/exposure and lens-switching controls;
- robust process-death/interrupted-capture recovery;
- production Glaze UI implementation and acceptance;
- Privacy Shield, Wardveil Security, Everkeep, Manager, Mesh, and Identity application acceptance where applicable;
- device-specific qualification profiles;
- Private Capture, richer metadata/provenance controls, and Gallery/Photos handoff;
- production signing/release and Stable qualification;
- recognized open-source license selection.

## Development

With matching local tooling installed:

```bash
gradle --no-daemon lintDebug testDebugUnitTest assembleDebug
```

The CI debug APK remains an engineering artifact only.

## Privacy boundary

The manifest remains camera-only. Android 10+ MediaStore lets Camera publish media that it creates without requesting storage or media-library permissions. The initial photo path writes only the newly reserved output owned by this app; it does not read the user's existing media library, use location, record audio, or use the network.

## GoreeCloud platform direction

- Platform Contract: **0.2**.
- Current required Glaze UI consumer target: **1.5.0**.
- Seven Integral Platform Systems are evaluated explicitly: GoreeCloud Manager, Privacy Shield, Wardveil Security, Everkeep, Glaze UI, GoreeCloud Mesh, and GoreeCloud Identity.
- GoreeCloud Sync remains separately governed and is not an eighth Integral Platform System.

## Repository documentation

- [SPECIFICATIONS.md](SPECIFICATIONS.md)
- [FEATURES.md](FEATURES.md)
- [FEATURE-ROADMAP.md](FEATURE-ROADMAP.md)
- [BENEFITS.md](BENEFITS.md)
- [COMPETITIVE-OBJECTIVES.md](COMPETITIVE-OBJECTIVES.md)
- [BRANDING.md](BRANDING.md)
- [USER-MANUAL.md](USER-MANUAL.md)
- [PRIVACY POLICY.md](PRIVACY%20POLICY.md)
- [SECURITY.md](SECURITY.md)
- [NOTES.md](NOTES.md)
- [goreecloud.platform.yaml](goreecloud.platform.yaml)

## License

A recognized open-source license has not yet been selected and committed. Repository visibility does not itself grant reuse rights.

## Status integrity

The current work does not prove Experimental lifecycle eligibility, physical-device support, Glaze UI conformance, Privacy Shield acceptance, Wardveil acceptance, production readiness, or Stable status.
