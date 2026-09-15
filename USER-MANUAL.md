# GoreeCloud Camera — User Manual

> Repository document version: **0.2.0**  
> Product internal version: **0.1.0**  
> Release lifecycle: **Concept**  
> Manual status: **Engineering preview foundation**

## Current availability

The repository now contains a buildable Android foundation and CI can produce a debug APK when validation succeeds. This is **not a supported release** and no device is yet qualified.

## Current engineering workflow

1. Build the exact repository revision with JDK 17, Gradle 9.6.0, Android SDK 37, and AGP 9.4.0.
2. Install the generated debug APK only on a test device or emulator appropriate for development work.
3. Launch **GoreeCloud Camera**.
4. Grant the Android camera permission when prompted.
5. The engineering shell attempts to select a rear camera and start a Camera2 live preview.
6. Leaving the activity closes the active preview session and camera device.

## Current limitations

There is no shutter control, still-image persistence, video/audio recording, MediaStore finalization, zoom/focus UI, Glaze UI implementation, Private Capture, Gallery/Photos handoff, or production signing. The current interface is intentionally an engineering shell.

The app currently requests only the camera permission. It does not request Internet, location, microphone, or media-library permissions.

## Troubleshooting

- If camera permission is denied, use the on-screen permission button and Android permission controls.
- If no camera is reported, the engineering shell displays that state rather than inventing support.
- A buildable APK is not evidence that preview behavior is qualified on the device.
- Camera or session errors are displayed as engineering status text; detailed privacy-safe diagnostics are future work.

## Future manual requirements

As capabilities become verified, document only supported devices/OS versions, capture, storage, metadata/privacy, Private Capture, professional/creative modes, qualified accessories/storage, accessibility, interrupted-recording recovery, downstream behavior, and known limitations.

A central GoreeCloud User Manuals copy has not yet been verified for Camera and remains a documentation follow-up item.
