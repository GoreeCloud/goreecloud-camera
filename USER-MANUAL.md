# GoreeCloud Camera — User Manual

> Repository document version: **0.3.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Manual status: **Engineering capture foundation**

## Current availability

The repository contains a buildable native Android Camera2 foundation. Representative Android 16 / API 36 emulator evidence verifies the preview path, but no physical device is qualified and this is not a supported release.

## Current engineering workflow

1. Build the exact repository revision with JDK 17, Gradle 9.6.0, Android SDK 37, and AGP 9.4.0.
2. Install the generated debug APK on a development device or emulator.
3. Launch **GoreeCloud Camera** and grant camera permission.
4. Wait for the engineering status to show `Session: previewing`.
5. Use **Capture photo** to request a JPEG still.
6. On a successful candidate path, the status shows `Photo saved: <filename>` and the image is published under `DCIM/GoreeCloud Camera` through MediaStore.
7. Leaving the activity closes the active camera/session outputs.

## Permission behavior

The app requests only camera permission. It does not request Internet, location, microphone, storage, all-files, or media-library read permissions for this milestone.

## Current limitations

The interface remains an engineering shell. Physical-device capture support, video/audio, zoom/focus/exposure UI, production Glaze UI, Private Capture, rich metadata policy, downstream Gallery/Photos handoff, interrupted-capture recovery, and production signing remain unqualified or unimplemented.

A visible `Photo saved` result proves only that the current process reported publication. Supported-device claims require the separate qualification process.

## Troubleshooting

- If camera permission is denied, use the on-screen permission button and Android permission controls.
- If no camera/JPEG configuration is available, the app reports an error rather than inventing support.
- If capture fails, the engineering shell reports a photo failure and the implementation attempts to remove the pending MediaStore row.
- A CI or emulator success does not qualify a physical phone.

A central GoreeCloud User Manuals copy has not yet been verified for Camera and remains a documentation follow-up.
