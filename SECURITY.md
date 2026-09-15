# GoreeCloud Camera — Security

> Repository document version: **0.1.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Runtime security status: **Not yet implemented**

## Current status

There is no runnable Camera implementation in this repository at the Concept baseline. Security architecture described here is therefore a requirement set, not proof of implemented controls.

## Planned security boundaries

Protect active camera-session ownership, temporary preview/processing frames, RAW data, cached/in-progress video, Private Capture media/temp files, camera/microphone permissions, location metadata, Remote Viewfinder sessions, Lens packages/permissions, and applicable downstream credentials.

## Lens and extension security

Future Lens packages must be signed, sandboxed, permission-scoped, resource-limited, killable without destabilizing core capture where practical, denied unrestricted media-library access, and denied silent frame upload.

## Diagnostics

Ordinary diagnostics must not contain captured media, raw preview frames, microphone payloads, precise location, recognized faces, or OCR content.

## Cryptography

Camera must use approved mature platform or library cryptographic primitives. Custom cryptographic designs must not be introduced merely for product differentiation.

## Reporting security issues

Do not publish active secrets, private user data, or detailed exploitable vulnerability material in a public issue. Use an approved private GoreeCloud security-reporting channel. If GitHub private vulnerability reporting is enabled, it may be used. Public issues may be used only for non-sensitive hardening requests that do not expose exploit details or protected information.

## Release impact

Known defects involving media integrity, protected storage, permission bypass, unauthorized remote access, extension sandbox escape, or privacy/security boundary failure are release-blocking for the affected capability.
