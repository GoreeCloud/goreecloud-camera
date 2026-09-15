# GoreeCloud Camera

> Repository document version: **0.1.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

GoreeCloud Camera is the planned first-party photography, video, scanning, and creative-capture application for GoreeCloud mobile devices and supported Android phones.

## Current state

This repository is in **Concept** state. The repository has been initialized and the product architecture, feature roadmap, privacy model, security boundaries, and platform-integration requirements are being defined. **No runnable GoreeCloud Camera application, Android build, tested camera pipeline, packaged release, or production-ready capability is currently present.**

Repository creation and documentation do not constitute implementation evidence.

## Product direction

GoreeCloud Camera is intended to combine three coherent experiences:

- **Professional Camera** — high-quality photography and video, manual controls, RAW-capable workflows, monitoring, stabilization, and filmmaking-oriented tools.
- **Creative Camera** — filters, GoreeCloud Lenses, multi-camera creator workflows, expressive overlays, short-form production tools, and local effects.
- **Private Camera** — offline-first capture, local processing, explicit metadata control, protected capture destinations, and strict limits on secondary processing or sharing.

Core capture is intended to work without an account, GoreeCloud server, external AI provider, commercial cloud service, or Internet connection.

## Planned implementation direction

- Native Android application.
- Kotlin-based implementation.
- Platform camera APIs with lower-level Camera2 access where advanced control requires it.
- Capability-driven UI that exposes only device-supported functions.
- Current GoreeCloud Platform Contract: **0.2**.
- Current required Glaze UI consumer target: **1.4.1**.
- Seven Integral Platform Systems are evaluated explicitly: GoreeCloud Manager, Privacy Shield, Wardveil Security, Everkeep, Glaze UI, GoreeCloud Mesh, and GoreeCloud Identity.
- GoreeCloud Sync is separately governed and is **not** an eighth Integral Platform System.

These are requirements and targets, not implementation claims.

## Repository documentation

- [SPECIFICATIONS.md](SPECIFICATIONS.md) — repository-coupled product and implementation specification.
- [FEATURES.md](FEATURES.md) — verified current functionality and implementation state.
- [FEATURE-ROADMAP.md](FEATURE-ROADMAP.md) — planned capability and delivery roadmap.
- [BENEFITS.md](BENEFITS.md) — planned product benefits.
- [COMPETITIVE-OBJECTIVES.md](COMPETITIVE-OBJECTIVES.md) — differentiation and improvement objectives.
- [BRANDING.md](BRANDING.md) — product identity and presentation requirements.
- [USER-MANUAL.md](USER-MANUAL.md) — current user-facing status and future manual scope.
- [PRIVACY POLICY.md](PRIVACY%20POLICY.md) — current privacy commitments and planned runtime requirements.
- [SECURITY.md](SECURITY.md) — security boundaries and reporting guidance.
- [NOTES.md](NOTES.md) — unresolved implementation decisions and current blockers.
- [goreecloud.platform.yaml](goreecloud.platform.yaml) — machine-readable Platform Contract declaration.

## Development

There is not yet an Android source tree, Gradle build, test suite, packaging configuration, or installable artifact. Build and run instructions will be added only when a real implementation exists.

Implementation work should begin with the architecture and delivery phases defined in `SPECIFICATIONS.md` and `FEATURE-ROADMAP.md`. Empty source scaffolding should not be added merely to make the repository appear more mature.

## License

A recognized open-source license has **not yet been selected and committed** for this repository. Because the repository is public and GoreeCloud requires transparent open-source licensing, license selection is an active governance blocker. Do not infer permission terms from repository visibility alone.

## Status integrity

Nothing in this repository should be interpreted as evidence that GoreeCloud Camera is Experimental, Development, Release Candidate, Stable, production-ready, device-qualified, privacy-certified, security-certified, or Glaze UI-conformant. Those states require separate implementation and acceptance evidence.
