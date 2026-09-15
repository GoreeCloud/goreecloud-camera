#!/usr/bin/env python3
# File internal version: 0.2.0
from pathlib import Path
import re
import sys
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[1]
APP_GRADLE = ROOT / "app" / "build.gradle.kts"
MANIFEST = ROOT / "app" / "src" / "main" / "AndroidManifest.xml"
CONTROLLER = ROOT / "app" / "src" / "main" / "kotlin" / "com" / "goreecloud" / "camera" / "camera" / "CameraSessionController.kt"
MEDIA_COMMITTER = ROOT / "app" / "src" / "main" / "kotlin" / "com" / "goreecloud" / "camera" / "storage" / "PhotoMediaStoreCommitter.kt"

EXPECTED = {
    "applicationId": "com.goreecloud.camera",
    "namespace": "com.goreecloud.camera",
    "compileSdk": "37",
    "minSdk": "29",
    "targetSdk": "37",
    "versionName": "0.1.0",
}

REQUIRED_FILES = [
    ROOT / "settings.gradle.kts",
    ROOT / "build.gradle.kts",
    ROOT / "gradle.properties",
    APP_GRADLE,
    MANIFEST,
    ROOT / "app" / "src" / "main" / "kotlin" / "com" / "goreecloud" / "camera" / "MainActivity.kt",
    ROOT / "app" / "src" / "main" / "kotlin" / "com" / "goreecloud" / "camera" / "camera" / "CameraCapabilityRegistry.kt",
    CONTROLLER,
    ROOT / "app" / "src" / "main" / "kotlin" / "com" / "goreecloud" / "camera" / "storage" / "PhotoFileNamer.kt",
    MEDIA_COMMITTER,
]

FORBIDDEN_PERMISSIONS = {
    "android.permission.INTERNET",
    "android.permission.ACCESS_FINE_LOCATION",
    "android.permission.ACCESS_COARSE_LOCATION",
    "android.permission.ACCESS_MEDIA_LOCATION",
    "android.permission.RECORD_AUDIO",
    "android.permission.READ_EXTERNAL_STORAGE",
    "android.permission.WRITE_EXTERNAL_STORAGE",
    "android.permission.MANAGE_EXTERNAL_STORAGE",
    "android.permission.READ_MEDIA_IMAGES",
    "android.permission.READ_MEDIA_VIDEO",
}

ANDROID_NS = "{http://schemas.android.com/apk/res/android}"


def fail(message: str) -> None:
    print(f"camera-foundation: {message}", file=sys.stderr)
    raise SystemExit(1)


for path in REQUIRED_FILES:
    if not path.is_file():
        fail(f"missing required file: {path.relative_to(ROOT)}")

build_text = APP_GRADLE.read_text(encoding="utf-8")
for key, expected in EXPECTED.items():
    if key in {"compileSdk", "minSdk", "targetSdk"}:
        pattern = rf"\b{re.escape(key)}\s*=\s*{re.escape(expected)}\b"
    else:
        pattern = rf'\b{re.escape(key)}\s*=\s*"{re.escape(expected)}"'
    if not re.search(pattern, build_text):
        fail(f"{key} is not pinned to {expected}")

if 'id("com.android.application") version "9.4.0"' not in (ROOT / "build.gradle.kts").read_text(encoding="utf-8"):
    fail("Android Gradle plugin is not pinned to 9.4.0")

manifest_root = ET.parse(MANIFEST).getroot()
permissions = {
    element.attrib.get(ANDROID_NS + "name")
    for element in manifest_root.findall("uses-permission")
}
if permissions != {"android.permission.CAMERA"}:
    fail(f"runtime permissions must remain camera-only, found: {sorted(permissions)}")
for forbidden in sorted(FORBIDDEN_PERMISSIONS):
    if forbidden in permissions:
        fail(f"unexpected sensitive permission in capture milestone: {forbidden}")

controller_text = CONTROLLER.read_text(encoding="utf-8")
if "CameraDevice.TEMPLATE_STILL_CAPTURE" not in controller_text:
    fail("still capture request template is missing")
if "ImageReader.newInstance" not in controller_text or "ImageFormat.JPEG" not in controller_text:
    fail("JPEG ImageReader output is missing")

committer_text = MEDIA_COMMITTER.read_text(encoding="utf-8")
for required_fragment in (
    "MediaStore.Images.Media.IS_PENDING",
    "MediaStore.Images.Media.RELATIVE_PATH",
    "MediaStore.VOLUME_EXTERNAL_PRIMARY",
):
    if required_fragment not in committer_text:
        fail(f"MediaStore capture contract is missing: {required_fragment}")

platform_text = (ROOT / "goreecloud.platform.yaml").read_text(encoding="utf-8") if (ROOT / "goreecloud.platform.yaml").exists() else ""
if "lifecycle: concept" not in platform_text:
    fail("Platform Contract lifecycle must remain concept")
if 'version: "0.1.0"' not in platform_text:
    fail("Platform Contract product version must remain 0.1.0")

print("camera-foundation: preview and still-capture source contract checks passed")
