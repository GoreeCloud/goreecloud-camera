# GoreeCloud Camera — Privacy Policy

> Repository document version: **0.3.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Runtime privacy status: **Camera-only permission boundary; Privacy Shield acceptance pending**

## Current implemented boundary

The manifest requests only `android.permission.CAMERA`. It does **not** request Internet, location, microphone, storage, all-files, or media-library read permissions.

The Camera2 preview path displays live frames locally. The initial still-photo path writes only a newly created image owned by GoreeCloud Camera into Android MediaStore. On Android 10+ this write does not require broad storage or media-library authority.

The app does not read the user's existing photo library as part of this milestone.

## Still-photo publication

A photo destination is first reserved as an `IS_PENDING` MediaStore image under `DCIM/GoreeCloud Camera`. Other apps should not see the item as a completed photo while it remains pending. After a successful JPEG write, Camera clears the pending state. Handled capture or write failures delete the reserved row rather than intentionally publishing an incomplete file.

This is a local media-persistence mechanism, not Privacy Shield acceptance and not a Private Capture implementation.

## Network, location, audio, accounts, and downstream services

This milestone adds no networking, location, microphone, account, synchronization, remote-control, Lens, or downstream-service authority. Core capture remains designed to work offline.

Gallery, Photos, Everkeep, Sync, or other services must not receive media merely because they are installed. Future handoff, backup, sharing, or synchronization requires its own effective authorization and documented contract.

## Metadata

The initial MediaStore path sets a display name, JPEG MIME type, relative path, and publication state. Location capture is not implemented. Future Original, Privacy Safe, and Custom metadata policies remain planned.

## Camera frames

Preview frames remain ephemeral by default. The JPEG output created by an explicit shutter operation is the only newly persisted camera payload in this milestone.

## Privacy Shield

No Camera-specific Privacy Shield runtime acceptance exists yet. Applicable future policy integration must be operation-bound, purpose-limited, and fail closed for dependent features.

## Changes

Revise this policy whenever implementation materially changes camera-frame handling, metadata, diagnostics, networking, accounts, extensions, remote access, backup, synchronization, sharing, or other privacy-relevant behavior.
