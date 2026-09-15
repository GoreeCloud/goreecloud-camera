# GoreeCloud Camera — Repository Notes

> Repository document version: **0.1.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**

## Current baseline

- Canonical repository: `GoreeCloud/goreecloud-camera`.
- Product version baseline: `0.1.0`.
- Lifecycle: Concept.
- Planned primary platform: native Android/Kotlin.
- Platform Contract: 0.2.
- Current Glaze UI consumer target: 1.4.1.
- Integral Platform Systems: exactly seven; GoreeCloud Sync is separate.
- No Camera runtime implementation is yet verified.

## Open engineering decisions

- minimum Android/API baseline;
- CameraX-first versus Camera2-centric orchestration;
- initial supported hardware/qualification tiers;
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

All seven Integral Platform Systems are blocked/unaccepted for Camera because there is no implementation evidence.

### User manual synchronization

A central Camera user-manual record has not yet been verified. Repository `USER-MANUAL.md` therefore remains explicitly pre-implementation and must later be synchronized through the approved central workflow.

## Next engineering milestone

After repository-governance review, begin a real native Android foundation with an actual buildable project and testable capture/session architecture. Do not create empty directories solely to appear implementation-complete.
