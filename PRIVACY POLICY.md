# GoreeCloud Camera — Privacy Policy

> Repository document version: **0.2.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Runtime privacy status: **Narrow source-level foundation; Privacy Shield acceptance pending**

## Current implemented boundary

The Android foundation requests only `android.permission.CAMERA`. The manifest does **not** request Internet, location, microphone, or media-library permissions.

The current Camera2 implementation displays preview frames to the application viewfinder. It does not implement still/video persistence, frame upload, networking, account behavior, location metadata, microphone capture, media-library reads, or downstream synchronization.

These source-level restrictions are useful privacy boundaries but do not constitute Privacy Shield runtime acceptance or real-device validation.

## Core privacy commitments

Core photography and video capture must not require an Internet connection, GoreeCloud account, GoreeCloud Photos server, commercial cloud service, or external AI service.

## Camera frames

Viewfinder and analysis frames should be ephemeral by default. They must not be retained, logged, uploaded, or exposed to extensions merely because preview or analysis is active. The current foundation does not contain a frame-persistence path.

## Metadata

Users should be able to control sensitive capture metadata, including location and selected device/software information. Planned export policies include **Original**, **Privacy Safe**, and **Custom**. Metadata capture is not yet implemented.

## Private Capture

Private Capture is planned to support disabling location metadata; suppressing automatic backup; restricting optional analysis; disabling external Lens packages; hiding ordinary recent-capture previews; stripping selected metadata; using protected storage; and preventing automatic sharing suggestions. A strong visible active-state indicator is required.

## Creative extensions

Lens packages must not silently upload camera frames. Any network authority must be explicit, purpose-limited, revocable, and separate from basic frame access.

## Remote control

Remote Viewfinder is planned as an explicit paired session. Remote authority must be session-scoped, visible, revocable, and limited to granted live-camera controls. Pairing must not grant access to unrelated prior media.

## Downstream services

Gallery, Photos, Everkeep, Sync, or other GoreeCloud services must not receive media or metadata merely because they are installed. Handoff, backup, synchronization, analysis, and sharing must follow effective operation policy and user configuration.

## Privacy Shield

Privacy Shield remains the planned authority for applicable Camera privacy contracts. No Camera-specific Privacy Shield runtime acceptance exists at this Concept-stage foundation.

## Changes

Revise this policy whenever implementation materially changes camera-frame handling, metadata, diagnostics, networking, account behavior, extensions, remote access, backup, synchronization, sharing, or other privacy-relevant behavior.
