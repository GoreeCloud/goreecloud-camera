# GoreeCloud Camera — Security

> Repository document version: **0.3.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Runtime security status: **Initial session and media-finalization boundaries implemented; Wardveil acceptance pending**

## Current status

`CameraSessionController` centralizes Camera2 device/session ownership and now also owns the bounded still-capture lifecycle. Activity pause, preview-surface destruction, stop, and shutdown close active camera/session/ImageReader resources.

The still-photo path uses a pending MediaStore row. Successful JPEG writes are published only after the write completes; handled capture/write failures discard the pending row.

These controls are source/runtime engineering boundaries, not Wardveil Security acceptance or production recovery qualification.

## Current permission boundary

The manifest requests only camera permission. It does not request Internet, location, microphone, storage, all-files, or media-library permissions. No remote-control, Lens, account, synchronization, or protected-storage path exists.

## Media integrity boundary

The initial committer checks that the received payload begins with a JPEG signature before publishing it. An invalid payload or failed MediaStore publication is treated as a capture failure and the pending row is discarded where the process remains alive.

Process-death recovery, storage exhaustion handling, transactional journaling, and protected/private destinations remain future reliability/security work.

## Planned security boundaries

Protect active camera-session ownership, temporary frames, RAW data, cached/in-progress video, Private Capture media/temp files, camera/microphone permissions, location metadata, Remote Viewfinder sessions, Lens packages/permissions, and applicable downstream credentials.

## Diagnostics

Ordinary diagnostics must not contain captured media or raw preview frames by default. CI qualification artifacts may contain the synthetic emulator capture used solely as non-user test evidence; this does not authorize production diagnostics to collect user media.

## Reporting security issues

Do not publish active secrets, private user data, or detailed exploitable vulnerability material in a public issue. Use an approved private GoreeCloud security-reporting channel.

## Release impact

Known defects involving media integrity, protected storage, permission bypass, unauthorized remote access, extension sandbox escape, or privacy/security boundary failure are release-blocking for the affected capability.
