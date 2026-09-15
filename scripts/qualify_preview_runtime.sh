#!/usr/bin/env bash
# File internal version: 0.1.0
set -euo pipefail

readonly APP_ID="com.goreecloud.camera"
readonly ACTIVITY="${APP_ID}/.MainActivity"
readonly EVIDENCE_ROOT="runtime-preview-evidence"
preview_ready=0

rm -rf "$EVIDENCE_ROOT"
mkdir -p "$EVIDENCE_ROOT"

capture_evidence() {
  local status=$?
  set +e

  adb shell uiautomator dump /sdcard/goreecloud-camera-window.xml >/dev/null 2>&1 || true
  adb pull /sdcard/goreecloud-camera-window.xml "$EVIDENCE_ROOT/window.xml" >/dev/null 2>&1 || true
  adb exec-out screencap -p > "$EVIDENCE_ROOT/preview.png" 2>/dev/null || true
  adb shell dumpsys media.camera > "$EVIDENCE_ROOT/media-camera.txt" 2>/dev/null || true
  adb shell dumpsys package "$APP_ID" > "$EVIDENCE_ROOT/package.txt" 2>/dev/null || true
  adb shell cmd appops get "$APP_ID" > "$EVIDENCE_ROOT/appops.txt" 2>/dev/null || true
  adb logcat -d -v threadtime > "$EVIDENCE_ROOT/logcat.txt" 2>/dev/null || true

  local qualification_result="failed"
  if [ "$status" -eq 0 ] && [ "$preview_ready" -eq 1 ]; then
    qualification_result="passed"
  fi

  cat > "$EVIDENCE_ROOT/RUNTIME-PROVENANCE.txt" <<PROVENANCE
repository=GoreeCloud/goreecloud-camera
source_sha=${SOURCE_SHA:?SOURCE_SHA is required}
product_version=0.1.0
lifecycle=Concept
runtime=Android-16-API-36-emulator
camera_source=Android-emulated-back-camera
permission_scope=android.permission.CAMERA-only
expected_runtime_state=previewing
evidence_role=representative-emulator-preview-qualification
qualification_result=$qualification_result
exit_status=$status
physical_device_qualification=false
stable_release_authority=none
PROVENANCE

  trap - EXIT
  exit "$status"
}
trap capture_evidence EXIT

gradle --no-daemon --stacktrace assembleDebug
APK="$(find app/build/outputs/apk/debug -type f -name '*.apk' -print -quit)"
test -n "$APK"

adb install -r "$APK"
adb shell pm grant "$APP_ID" android.permission.CAMERA
adb shell am force-stop "$APP_ID"
adb shell input keyevent KEYCODE_WAKEUP || true
adb shell wm dismiss-keyguard || true
adb shell am start -W -n "$ACTIVITY"

for attempt in $(seq 1 45); do
  adb shell uiautomator dump /sdcard/goreecloud-camera-window.xml >/dev/null 2>&1 || true
  adb pull /sdcard/goreecloud-camera-window.xml "$EVIDENCE_ROOT/window.xml" >/dev/null 2>&1 || true
  if grep -q 'text="Session: previewing' "$EVIDENCE_ROOT/window.xml" 2>/dev/null; then
    preview_ready=1
    break
  fi
  sleep 1
done

if [ "$preview_ready" -ne 1 ]; then
  echo "Camera preview did not reach PREVIEWING state." >&2
  cat "$EVIDENCE_ROOT/window.xml" >&2 2>/dev/null || true
  exit 1
fi

grep -Eq 'text="Detected camera devices: [1-9][0-9]*"' "$EVIDENCE_ROOT/window.xml"
echo "Camera2 preview reached PREVIEWING on the Android 16 emulated camera runtime."
