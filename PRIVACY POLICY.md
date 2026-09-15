# GoreeCloud Camera — Privacy Policy

> Repository document version: **0.1.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Runtime privacy status: **Not yet implemented**

## Current repository state

This repository does not currently contain a runnable Camera application. Therefore no GoreeCloud Camera runtime collection, telemetry, camera-frame processing, metadata storage, upload behavior, or account behavior is currently implemented by this repository.

The requirements below define the planned privacy baseline and must not be presented as completed controls until implementation and acceptance evidence exists.

## Planned privacy commitments

Core photography and video capture must not require an Internet connection, GoreeCloud account, GoreeCloud Photos server, commercial cloud service, or external AI service.

## Camera frames

Viewfinder and analysis frames should be ephemeral by default. They must not be retained, logged, uploaded, or exposed to extensions merely because preview or analysis is active.

## Metadata

Users should be able to control sensitive capture metadata, including location and selected device/software information. Planned export policies include **Original**, **Privacy Safe**, and **Custom**.

## Private Capture

Private Capture is planned to support disabling location metadata; suppressing automatic backup; restricting optional analysis; disabling external Lens packages; hiding ordinary recent-capture previews; stripping selected metadata; using protected storage; and preventing automatic sharing suggestions. A strong visible active-state indicator is required.

## Creative extensions

Lens packages must not silently upload camera frames. Any network authority must be explicit, purpose-limited, revocable, and separate from basic frame access.

## Remote control

Remote Viewfinder is planned as an explicit paired session. Remote authority must be session-scoped, visible, revocable, and limited to granted live-camera controls. Pairing must not grant access to unrelated prior media.

## Downstream services

Gallery, Photos, Everkeep, Sync, or other GoreeCloud services must not receive media or metadata merely because they are installed. Handoff, backup, synchronization, analysis, and sharing must follow effective operation policy and user configuration.

## Privacy Shield

Privacy Shield is the planned authority for applicable Camera privacy contracts. No Camera-specific Privacy Shield runtime acceptance exists at the Concept baseline.

## Changes

Revise this policy whenever implementation materially changes camera-frame handling, metadata, diagnostics, networking, account behavior, extensions, remote access, backup, synchronization, sharing, or other privacy-relevant behavior.
