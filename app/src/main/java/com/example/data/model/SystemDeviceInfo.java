package com.example.data.model;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: OtaModels.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0007\n\u0002\b0\b\u0087\b\u0018\u00002\u00020\u0001B§\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0007\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0010\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003¢\u0006\u0004\b\u0016\u0010\u0017J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0003HÆ\u0003J\t\u0010.\u001a\u00020\u0007HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0003HÆ\u0003J\t\u00101\u001a\u00020\u0003HÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0007HÆ\u0003J\t\u00104\u001a\u00020\u000eHÆ\u0003J\t\u00105\u001a\u00020\u0010HÆ\u0003J\t\u00106\u001a\u00020\u0010HÆ\u0003J\t\u00107\u001a\u00020\u0003HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u0003HÆ\u0003J©\u0001\u0010;\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00072\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u00102\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u0003HÆ\u0001J\u0013\u0010<\u001a\u00020\u000e2\b\u0010=\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010>\u001a\u00020\u0007HÖ\u0001J\t\u0010?\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0019R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0019R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0019R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0019R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0019R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0019R\u0011\u0010\f\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001dR\u0011\u0010\r\u001a\u00020\u000e¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010#R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\u0011\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b&\u0010%R\u0011\u0010\u0012\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u0019R\u0011\u0010\u0013\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0019R\u0011\u0010\u0014\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0019R\u0011\u0010\u0015\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0019¨\u0006@"}, d2 = {"Lcom/example/data/model/SystemDeviceInfo;", "", "deviceName", "", "deviceCodename", "currentOsVersion", "currentVersionCode", "", "currentBuildNumber", "androidVersion", "securityPatch", "kernelVersion", "batteryLevel", "isCharging", "", "storageFreeGb", "", "storageTotalGb", "networkType", "cpuArch", "rawJsonSource", "targetSavePath", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IZFFLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getDeviceName", "()Ljava/lang/String;", "getDeviceCodename", "getCurrentOsVersion", "getCurrentVersionCode", "()I", "getCurrentBuildNumber", "getAndroidVersion", "getSecurityPatch", "getKernelVersion", "getBatteryLevel", "()Z", "getStorageFreeGb", "()F", "getStorageTotalGb", "getNetworkType", "getCpuArch", "getRawJsonSource", "getTargetSavePath", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "copy", "equals", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* data */ class SystemDeviceInfo {
    public static final int $stable = 0;
    private final String androidVersion;
    private final int batteryLevel;
    private final String cpuArch;
    private final String currentBuildNumber;
    private final String currentOsVersion;
    private final int currentVersionCode;
    private final String deviceCodename;
    private final String deviceName;
    private final boolean isCharging;
    private final String kernelVersion;
    private final String networkType;
    private final String rawJsonSource;
    private final String securityPatch;
    private final float storageFreeGb;
    private final float storageTotalGb;
    private final String targetSavePath;

    public SystemDeviceInfo() {
        this(null, null, null, 0, null, null, null, null, 0, false, 0.0f, 0.0f, null, null, null, null, 65535, null);
    }

    public static /* synthetic */ SystemDeviceInfo copy$default(SystemDeviceInfo systemDeviceInfo, String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, int i2, boolean z, float f, float f2, String str8, String str9, String str10, String str11, int i3, Object obj) {
        String str12 = (i3 & 1) != 0 ? systemDeviceInfo.deviceName : str;
        return systemDeviceInfo.copy(str12, (i3 & 2) != 0 ? systemDeviceInfo.deviceCodename : str2, (i3 & 4) != 0 ? systemDeviceInfo.currentOsVersion : str3, (i3 & 8) != 0 ? systemDeviceInfo.currentVersionCode : i, (i3 & 16) != 0 ? systemDeviceInfo.currentBuildNumber : str4, (i3 & 32) != 0 ? systemDeviceInfo.androidVersion : str5, (i3 & 64) != 0 ? systemDeviceInfo.securityPatch : str6, (i3 & 128) != 0 ? systemDeviceInfo.kernelVersion : str7, (i3 & 256) != 0 ? systemDeviceInfo.batteryLevel : i2, (i3 & 512) != 0 ? systemDeviceInfo.isCharging : z, (i3 & 1024) != 0 ? systemDeviceInfo.storageFreeGb : f, (i3 & 2048) != 0 ? systemDeviceInfo.storageTotalGb : f2, (i3 & 4096) != 0 ? systemDeviceInfo.networkType : str8, (i3 & 8192) != 0 ? systemDeviceInfo.cpuArch : str9, (i3 & 16384) != 0 ? systemDeviceInfo.rawJsonSource : str10, (i3 & 32768) != 0 ? systemDeviceInfo.targetSavePath : str11);
    }

    /* renamed from: component1, reason: from getter */
    public final String getDeviceName() {
        return this.deviceName;
    }

    /* renamed from: component10, reason: from getter */
    public final boolean getIsCharging() {
        return this.isCharging;
    }

    /* renamed from: component11, reason: from getter */
    public final float getStorageFreeGb() {
        return this.storageFreeGb;
    }

    /* renamed from: component12, reason: from getter */
    public final float getStorageTotalGb() {
        return this.storageTotalGb;
    }

    /* renamed from: component13, reason: from getter */
    public final String getNetworkType() {
        return this.networkType;
    }

    /* renamed from: component14, reason: from getter */
    public final String getCpuArch() {
        return this.cpuArch;
    }

    /* renamed from: component15, reason: from getter */
    public final String getRawJsonSource() {
        return this.rawJsonSource;
    }

    /* renamed from: component16, reason: from getter */
    public final String getTargetSavePath() {
        return this.targetSavePath;
    }

    /* renamed from: component2, reason: from getter */
    public final String getDeviceCodename() {
        return this.deviceCodename;
    }

    /* renamed from: component3, reason: from getter */
    public final String getCurrentOsVersion() {
        return this.currentOsVersion;
    }

    /* renamed from: component4, reason: from getter */
    public final int getCurrentVersionCode() {
        return this.currentVersionCode;
    }

    /* renamed from: component5, reason: from getter */
    public final String getCurrentBuildNumber() {
        return this.currentBuildNumber;
    }

    /* renamed from: component6, reason: from getter */
    public final String getAndroidVersion() {
        return this.androidVersion;
    }

    /* renamed from: component7, reason: from getter */
    public final String getSecurityPatch() {
        return this.securityPatch;
    }

    /* renamed from: component8, reason: from getter */
    public final String getKernelVersion() {
        return this.kernelVersion;
    }

    /* renamed from: component9, reason: from getter */
    public final int getBatteryLevel() {
        return this.batteryLevel;
    }

    public final SystemDeviceInfo copy(String deviceName, String deviceCodename, String currentOsVersion, int currentVersionCode, String currentBuildNumber, String androidVersion, String securityPatch, String kernelVersion, int batteryLevel, boolean isCharging, float storageFreeGb, float storageTotalGb, String networkType, String cpuArch, String rawJsonSource, String targetSavePath) {
        Intrinsics.checkNotNullParameter(deviceName, "deviceName");
        Intrinsics.checkNotNullParameter(deviceCodename, "deviceCodename");
        Intrinsics.checkNotNullParameter(currentOsVersion, "currentOsVersion");
        Intrinsics.checkNotNullParameter(currentBuildNumber, "currentBuildNumber");
        Intrinsics.checkNotNullParameter(androidVersion, "androidVersion");
        Intrinsics.checkNotNullParameter(securityPatch, "securityPatch");
        Intrinsics.checkNotNullParameter(kernelVersion, "kernelVersion");
        Intrinsics.checkNotNullParameter(networkType, "networkType");
        Intrinsics.checkNotNullParameter(cpuArch, "cpuArch");
        Intrinsics.checkNotNullParameter(rawJsonSource, "rawJsonSource");
        Intrinsics.checkNotNullParameter(targetSavePath, "targetSavePath");
        return new SystemDeviceInfo(deviceName, deviceCodename, currentOsVersion, currentVersionCode, currentBuildNumber, androidVersion, securityPatch, kernelVersion, batteryLevel, isCharging, storageFreeGb, storageTotalGb, networkType, cpuArch, rawJsonSource, targetSavePath);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SystemDeviceInfo)) {
            return false;
        }
        SystemDeviceInfo systemDeviceInfo = (SystemDeviceInfo) other;
        return Intrinsics.areEqual(this.deviceName, systemDeviceInfo.deviceName) && Intrinsics.areEqual(this.deviceCodename, systemDeviceInfo.deviceCodename) && Intrinsics.areEqual(this.currentOsVersion, systemDeviceInfo.currentOsVersion) && this.currentVersionCode == systemDeviceInfo.currentVersionCode && Intrinsics.areEqual(this.currentBuildNumber, systemDeviceInfo.currentBuildNumber) && Intrinsics.areEqual(this.androidVersion, systemDeviceInfo.androidVersion) && Intrinsics.areEqual(this.securityPatch, systemDeviceInfo.securityPatch) && Intrinsics.areEqual(this.kernelVersion, systemDeviceInfo.kernelVersion) && this.batteryLevel == systemDeviceInfo.batteryLevel && this.isCharging == systemDeviceInfo.isCharging && Float.compare(this.storageFreeGb, systemDeviceInfo.storageFreeGb) == 0 && Float.compare(this.storageTotalGb, systemDeviceInfo.storageTotalGb) == 0 && Intrinsics.areEqual(this.networkType, systemDeviceInfo.networkType) && Intrinsics.areEqual(this.cpuArch, systemDeviceInfo.cpuArch) && Intrinsics.areEqual(this.rawJsonSource, systemDeviceInfo.rawJsonSource) && Intrinsics.areEqual(this.targetSavePath, systemDeviceInfo.targetSavePath);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((this.deviceName.hashCode() * 31) + this.deviceCodename.hashCode()) * 31) + this.currentOsVersion.hashCode()) * 31) + Integer.hashCode(this.currentVersionCode)) * 31) + this.currentBuildNumber.hashCode()) * 31) + this.androidVersion.hashCode()) * 31) + this.securityPatch.hashCode()) * 31) + this.kernelVersion.hashCode()) * 31) + Integer.hashCode(this.batteryLevel)) * 31) + Boolean.hashCode(this.isCharging)) * 31) + Float.hashCode(this.storageFreeGb)) * 31) + Float.hashCode(this.storageTotalGb)) * 31) + this.networkType.hashCode()) * 31) + this.cpuArch.hashCode()) * 31) + this.rawJsonSource.hashCode()) * 31) + this.targetSavePath.hashCode();
    }

    public String toString() {
        return "SystemDeviceInfo(deviceName=" + this.deviceName + ", deviceCodename=" + this.deviceCodename + ", currentOsVersion=" + this.currentOsVersion + ", currentVersionCode=" + this.currentVersionCode + ", currentBuildNumber=" + this.currentBuildNumber + ", androidVersion=" + this.androidVersion + ", securityPatch=" + this.securityPatch + ", kernelVersion=" + this.kernelVersion + ", batteryLevel=" + this.batteryLevel + ", isCharging=" + this.isCharging + ", storageFreeGb=" + this.storageFreeGb + ", storageTotalGb=" + this.storageTotalGb + ", networkType=" + this.networkType + ", cpuArch=" + this.cpuArch + ", rawJsonSource=" + this.rawJsonSource + ", targetSavePath=" + this.targetSavePath + ")";
    }

    public SystemDeviceInfo(String deviceName, String deviceCodename, String currentOsVersion, int currentVersionCode, String currentBuildNumber, String androidVersion, String securityPatch, String kernelVersion, int batteryLevel, boolean isCharging, float storageFreeGb, float storageTotalGb, String networkType, String cpuArch, String rawJsonSource, String targetSavePath) {
        Intrinsics.checkNotNullParameter(deviceName, "deviceName");
        Intrinsics.checkNotNullParameter(deviceCodename, "deviceCodename");
        Intrinsics.checkNotNullParameter(currentOsVersion, "currentOsVersion");
        Intrinsics.checkNotNullParameter(currentBuildNumber, "currentBuildNumber");
        Intrinsics.checkNotNullParameter(androidVersion, "androidVersion");
        Intrinsics.checkNotNullParameter(securityPatch, "securityPatch");
        Intrinsics.checkNotNullParameter(kernelVersion, "kernelVersion");
        Intrinsics.checkNotNullParameter(networkType, "networkType");
        Intrinsics.checkNotNullParameter(cpuArch, "cpuArch");
        Intrinsics.checkNotNullParameter(rawJsonSource, "rawJsonSource");
        Intrinsics.checkNotNullParameter(targetSavePath, "targetSavePath");
        this.deviceName = deviceName;
        this.deviceCodename = deviceCodename;
        this.currentOsVersion = currentOsVersion;
        this.currentVersionCode = currentVersionCode;
        this.currentBuildNumber = currentBuildNumber;
        this.androidVersion = androidVersion;
        this.securityPatch = securityPatch;
        this.kernelVersion = kernelVersion;
        this.batteryLevel = batteryLevel;
        this.isCharging = isCharging;
        this.storageFreeGb = storageFreeGb;
        this.storageTotalGb = storageTotalGb;
        this.networkType = networkType;
        this.cpuArch = cpuArch;
        this.rawJsonSource = rawJsonSource;
        this.targetSavePath = targetSavePath;
    }

    public /* synthetic */ SystemDeviceInfo(String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, int i2, boolean z, float f, float f2, String str8, String str9, String str10, String str11, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? OtaConstants.DEVICE_MODEL_NAME : str, (i3 & 2) != 0 ? OtaConstants.DEVICE_CODENAME : str2, (i3 & 4) != 0 ? OtaConstants.CURRENT_BASE_VERSION_NAME : str3, (i3 & 8) != 0 ? 3000 : i, (i3 & 16) != 0 ? OtaConstants.CURRENT_BUILD_TAG : str4, (i3 & 32) != 0 ? "Android 15" : str5, (i3 & 64) != 0 ? "August 2026" : str6, (i3 & 128) != 0 ? "5.15.148-PowerOS-OppoA6X" : str7, (i3 & 256) != 0 ? 92 : i2, (i3 & 512) != 0 ? true : z, (i3 & 1024) != 0 ? 62.4f : f, (i3 & 2048) != 0 ? 128.0f : f2, (i3 & 4096) != 0 ? "Wi-Fi Connected" : str8, (i3 & 8192) != 0 ? "MediaTek Dimensity / ARM64" : str9, (i3 & 16384) != 0 ? OtaConstants.DEFAULT_RAW_JSON_URL : str10, (i3 & 32768) != 0 ? OtaConstants.DEFAULT_TARGET_FILE_PATH : str11);
    }

    public final String getDeviceName() {
        return this.deviceName;
    }

    public final String getDeviceCodename() {
        return this.deviceCodename;
    }

    public final String getCurrentOsVersion() {
        return this.currentOsVersion;
    }

    public final int getCurrentVersionCode() {
        return this.currentVersionCode;
    }

    public final String getCurrentBuildNumber() {
        return this.currentBuildNumber;
    }

    public final String getAndroidVersion() {
        return this.androidVersion;
    }

    public final String getSecurityPatch() {
        return this.securityPatch;
    }

    public final String getKernelVersion() {
        return this.kernelVersion;
    }

    public final int getBatteryLevel() {
        return this.batteryLevel;
    }

    public final boolean isCharging() {
        return this.isCharging;
    }

    public final float getStorageFreeGb() {
        return this.storageFreeGb;
    }

    public final float getStorageTotalGb() {
        return this.storageTotalGb;
    }

    public final String getNetworkType() {
        return this.networkType;
    }

    public final String getCpuArch() {
        return this.cpuArch;
    }

    public final String getRawJsonSource() {
        return this.rawJsonSource;
    }

    public final String getTargetSavePath() {
        return this.targetSavePath;
    }
}
