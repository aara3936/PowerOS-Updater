package com.example.data.model;

import kotlin.Metadata;

/* compiled from: OtaModels.kt */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0002\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003R\u000e\u0010\u0004\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0005X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lcom/example/data/model/OtaConstants;", "", "<init>", "()V", "DEFAULT_RAW_JSON_URL", "", "DEFAULT_FALLBACK_JSON_URL", "DEFAULT_TARGET_FILE_PATH", "DEFAULT_TARGET_DIRECTORY", "DEFAULT_TARGET_FILENAME", "DEVICE_MODEL_NAME", "DEVICE_CODENAME", "CURRENT_BASE_VERSION_NAME", "CURRENT_BASE_VERSION_CODE", "", "CURRENT_BUILD_TAG", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final class OtaConstants {
    public static final int $stable = 0;
    public static final int CURRENT_BASE_VERSION_CODE = 3000;
    public static final String CURRENT_BASE_VERSION_NAME = "Power OS v3.0.0";
    public static final String CURRENT_BUILD_TAG = "POS-3.0.0-STABLE-OppoA6X";
    public static final String DEFAULT_FALLBACK_JSON_URL = "https://raw.githubusercontent.com/aara3936/Oppo-A6X-OTA/main/updater.json";
    public static final String DEFAULT_RAW_JSON_URL = "https://raw.githubusercontent.com/aara3936/Oppo-A6X-OTA/main/metadata.json";
    public static final String DEFAULT_TARGET_DIRECTORY = "/sdcard/Download/OTA";
    public static final String DEFAULT_TARGET_FILENAME = "rom.zip";
    public static final String DEFAULT_TARGET_FILE_PATH = "/sdcard/Download/OTA/rom.zip";
    public static final String DEVICE_CODENAME = "oppo_a6x";
    public static final String DEVICE_MODEL_NAME = "Oppo A6X";
    public static final OtaConstants INSTANCE = new OtaConstants();

    private OtaConstants() {
    }
}
