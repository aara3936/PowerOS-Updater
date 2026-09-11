package com.example.data.model;

import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: OtaModels.kt */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b:\b\u0087\b\u0018\u00002\u00020\u0001BÑ\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0013\u001a\u00020\r\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\b\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0018\u001a\u00020\b\u0012\b\b\u0002\u0010\u0019\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0003¢\u0006\u0004\b\u001b\u0010\u001cJ\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\u0003HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\bHÆ\u0003J\t\u0010:\u001a\u00020\u0003HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\t\u0010<\u001a\u00020\u0003HÆ\u0003J\t\u0010=\u001a\u00020\rHÆ\u0003J\t\u0010>\u001a\u00020\u0003HÆ\u0003J\t\u0010?\u001a\u00020\u0003HÆ\u0003J\t\u0010@\u001a\u00020\u0003HÆ\u0003J\t\u0010A\u001a\u00020\u0003HÆ\u0003J\t\u0010B\u001a\u00020\u0003HÆ\u0003J\t\u0010C\u001a\u00020\rHÆ\u0003J\t\u0010D\u001a\u00020\u0015HÆ\u0003J\t\u0010E\u001a\u00020\bHÆ\u0003J\t\u0010F\u001a\u00020\u0003HÆ\u0003J\t\u0010G\u001a\u00020\bHÆ\u0003J\t\u0010H\u001a\u00020\u0003HÆ\u0003J\t\u0010I\u001a\u00020\u0003HÆ\u0003JÛ\u0001\u0010J\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\r2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0016\u001a\u00020\b2\b\b\u0002\u0010\u0017\u001a\u00020\u00032\b\b\u0002\u0010\u0018\u001a\u00020\b2\b\b\u0002\u0010\u0019\u001a\u00020\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u0003HÆ\u0001J\u0013\u0010K\u001a\u00020\u00152\b\u0010L\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010M\u001a\u00020\bHÖ\u0001J\t\u0010N\u001a\u00020\u0003HÖ\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001eR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001eR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001eR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b$\u0010\u001eR\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001eR\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001eR\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001eR\u0011\u0010\u000f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b*\u0010\u001eR\u0011\u0010\u0010\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001eR\u0011\u0010\u0011\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\u001eR\u0011\u0010\u0012\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\u001eR\u0011\u0010\u0013\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b.\u0010(R\u0011\u0010\u0014\u001a\u00020\u0015¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010/R\u0011\u0010\u0016\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b0\u0010#R\u0011\u0010\u0017\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b1\u0010\u001eR\u0011\u0010\u0018\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b2\u0010#R\u0011\u0010\u0019\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001eR\u0011\u0010\u001a\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b4\u0010\u001e¨\u0006O"}, d2 = {"Lcom/example/data/model/OtaRelease;", "", "id", "", "deviceModel", "deviceCodename", "versionName", "versionCode", "", "buildNumber", "releaseChannel", "releaseType", "packageSizeBytes", "", "downloadUrl", "checksumSha256", "androidVersion", "securityPatch", "changelog", "releaseDate", "isMandatory", "", "minRequiredVersion", NotificationCompat.CATEGORY_STATUS, "rolloutPercentage", "sourceUrl", "targetLocalPath", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JZILjava/lang/String;ILjava/lang/String;Ljava/lang/String;)V", "getId", "()Ljava/lang/String;", "getDeviceModel", "getDeviceCodename", "getVersionName", "getVersionCode", "()I", "getBuildNumber", "getReleaseChannel", "getReleaseType", "getPackageSizeBytes", "()J", "getDownloadUrl", "getChecksumSha256", "getAndroidVersion", "getSecurityPatch", "getChangelog", "getReleaseDate", "()Z", "getMinRequiredVersion", "getStatus", "getRolloutPercentage", "getSourceUrl", "getTargetLocalPath", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component20", "component21", "copy", "equals", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* data */ class OtaRelease {
    public static final int $stable = 0;
    private final String androidVersion;
    private final String buildNumber;
    private final String changelog;
    private final String checksumSha256;
    private final String deviceCodename;
    private final String deviceModel;
    private final String downloadUrl;
    private final String id;
    private final boolean isMandatory;
    private final int minRequiredVersion;
    private final long packageSizeBytes;
    private final String releaseChannel;
    private final long releaseDate;
    private final String releaseType;
    private final int rolloutPercentage;
    private final String securityPatch;
    private final String sourceUrl;
    private final String status;
    private final String targetLocalPath;
    private final int versionCode;
    private final String versionName;

    public static /* synthetic */ OtaRelease copy$default(OtaRelease otaRelease, String str, String str2, String str3, String str4, int i, String str5, String str6, String str7, long j, String str8, String str9, String str10, String str11, String str12, long j2, boolean z, int i2, String str13, int i3, String str14, String str15, int i4, Object obj) {
        String str16;
        String str17;
        String str18 = (i4 & 1) != 0 ? otaRelease.id : str;
        String str19 = (i4 & 2) != 0 ? otaRelease.deviceModel : str2;
        String str20 = (i4 & 4) != 0 ? otaRelease.deviceCodename : str3;
        String str21 = (i4 & 8) != 0 ? otaRelease.versionName : str4;
        int i5 = (i4 & 16) != 0 ? otaRelease.versionCode : i;
        String str22 = (i4 & 32) != 0 ? otaRelease.buildNumber : str5;
        String str23 = (i4 & 64) != 0 ? otaRelease.releaseChannel : str6;
        String str24 = (i4 & 128) != 0 ? otaRelease.releaseType : str7;
        long j3 = (i4 & 256) != 0 ? otaRelease.packageSizeBytes : j;
        String str25 = (i4 & 512) != 0 ? otaRelease.downloadUrl : str8;
        String str26 = (i4 & 1024) != 0 ? otaRelease.checksumSha256 : str9;
        String str27 = (i4 & 2048) != 0 ? otaRelease.androidVersion : str10;
        String str28 = (i4 & 4096) != 0 ? otaRelease.securityPatch : str11;
        String str29 = str18;
        String str30 = (i4 & 8192) != 0 ? otaRelease.changelog : str12;
        long j4 = (i4 & 16384) != 0 ? otaRelease.releaseDate : j2;
        boolean z2 = (i4 & 32768) != 0 ? otaRelease.isMandatory : z;
        int i6 = (i4 & 65536) != 0 ? otaRelease.minRequiredVersion : i2;
        boolean z3 = z2;
        String str31 = (i4 & 131072) != 0 ? otaRelease.status : str13;
        int i7 = (i4 & 262144) != 0 ? otaRelease.rolloutPercentage : i3;
        String str32 = (i4 & 524288) != 0 ? otaRelease.sourceUrl : str14;
        if ((i4 & 1048576) != 0) {
            str17 = str32;
            str16 = otaRelease.targetLocalPath;
        } else {
            str16 = str15;
            str17 = str32;
        }
        return otaRelease.copy(str29, str19, str20, str21, i5, str22, str23, str24, j3, str25, str26, str27, str28, str30, j4, z3, i6, str31, i7, str17, str16);
    }

    /* renamed from: component1, reason: from getter */
    public final String getId() {
        return this.id;
    }

    /* renamed from: component10, reason: from getter */
    public final String getDownloadUrl() {
        return this.downloadUrl;
    }

    /* renamed from: component11, reason: from getter */
    public final String getChecksumSha256() {
        return this.checksumSha256;
    }

    /* renamed from: component12, reason: from getter */
    public final String getAndroidVersion() {
        return this.androidVersion;
    }

    /* renamed from: component13, reason: from getter */
    public final String getSecurityPatch() {
        return this.securityPatch;
    }

    /* renamed from: component14, reason: from getter */
    public final String getChangelog() {
        return this.changelog;
    }

    /* renamed from: component15, reason: from getter */
    public final long getReleaseDate() {
        return this.releaseDate;
    }

    /* renamed from: component16, reason: from getter */
    public final boolean getIsMandatory() {
        return this.isMandatory;
    }

    /* renamed from: component17, reason: from getter */
    public final int getMinRequiredVersion() {
        return this.minRequiredVersion;
    }

    /* renamed from: component18, reason: from getter */
    public final String getStatus() {
        return this.status;
    }

    /* renamed from: component19, reason: from getter */
    public final int getRolloutPercentage() {
        return this.rolloutPercentage;
    }

    /* renamed from: component2, reason: from getter */
    public final String getDeviceModel() {
        return this.deviceModel;
    }

    /* renamed from: component20, reason: from getter */
    public final String getSourceUrl() {
        return this.sourceUrl;
    }

    /* renamed from: component21, reason: from getter */
    public final String getTargetLocalPath() {
        return this.targetLocalPath;
    }

    /* renamed from: component3, reason: from getter */
    public final String getDeviceCodename() {
        return this.deviceCodename;
    }

    /* renamed from: component4, reason: from getter */
    public final String getVersionName() {
        return this.versionName;
    }

    /* renamed from: component5, reason: from getter */
    public final int getVersionCode() {
        return this.versionCode;
    }

    /* renamed from: component6, reason: from getter */
    public final String getBuildNumber() {
        return this.buildNumber;
    }

    /* renamed from: component7, reason: from getter */
    public final String getReleaseChannel() {
        return this.releaseChannel;
    }

    /* renamed from: component8, reason: from getter */
    public final String getReleaseType() {
        return this.releaseType;
    }

    /* renamed from: component9, reason: from getter */
    public final long getPackageSizeBytes() {
        return this.packageSizeBytes;
    }

    public final OtaRelease copy(String id, String deviceModel, String deviceCodename, String versionName, int versionCode, String buildNumber, String releaseChannel, String releaseType, long packageSizeBytes, String downloadUrl, String checksumSha256, String androidVersion, String securityPatch, String changelog, long releaseDate, boolean isMandatory, int minRequiredVersion, String status, int rolloutPercentage, String sourceUrl, String targetLocalPath) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(deviceModel, "deviceModel");
        Intrinsics.checkNotNullParameter(deviceCodename, "deviceCodename");
        Intrinsics.checkNotNullParameter(versionName, "versionName");
        Intrinsics.checkNotNullParameter(buildNumber, "buildNumber");
        Intrinsics.checkNotNullParameter(releaseChannel, "releaseChannel");
        Intrinsics.checkNotNullParameter(releaseType, "releaseType");
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(checksumSha256, "checksumSha256");
        Intrinsics.checkNotNullParameter(androidVersion, "androidVersion");
        Intrinsics.checkNotNullParameter(securityPatch, "securityPatch");
        Intrinsics.checkNotNullParameter(changelog, "changelog");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter(targetLocalPath, "targetLocalPath");
        return new OtaRelease(id, deviceModel, deviceCodename, versionName, versionCode, buildNumber, releaseChannel, releaseType, packageSizeBytes, downloadUrl, checksumSha256, androidVersion, securityPatch, changelog, releaseDate, isMandatory, minRequiredVersion, status, rolloutPercentage, sourceUrl, targetLocalPath);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OtaRelease)) {
            return false;
        }
        OtaRelease otaRelease = (OtaRelease) other;
        return Intrinsics.areEqual(this.id, otaRelease.id) && Intrinsics.areEqual(this.deviceModel, otaRelease.deviceModel) && Intrinsics.areEqual(this.deviceCodename, otaRelease.deviceCodename) && Intrinsics.areEqual(this.versionName, otaRelease.versionName) && this.versionCode == otaRelease.versionCode && Intrinsics.areEqual(this.buildNumber, otaRelease.buildNumber) && Intrinsics.areEqual(this.releaseChannel, otaRelease.releaseChannel) && Intrinsics.areEqual(this.releaseType, otaRelease.releaseType) && this.packageSizeBytes == otaRelease.packageSizeBytes && Intrinsics.areEqual(this.downloadUrl, otaRelease.downloadUrl) && Intrinsics.areEqual(this.checksumSha256, otaRelease.checksumSha256) && Intrinsics.areEqual(this.androidVersion, otaRelease.androidVersion) && Intrinsics.areEqual(this.securityPatch, otaRelease.securityPatch) && Intrinsics.areEqual(this.changelog, otaRelease.changelog) && this.releaseDate == otaRelease.releaseDate && this.isMandatory == otaRelease.isMandatory && this.minRequiredVersion == otaRelease.minRequiredVersion && Intrinsics.areEqual(this.status, otaRelease.status) && this.rolloutPercentage == otaRelease.rolloutPercentage && Intrinsics.areEqual(this.sourceUrl, otaRelease.sourceUrl) && Intrinsics.areEqual(this.targetLocalPath, otaRelease.targetLocalPath);
    }

    public int hashCode() {
        return (((((((((((((((((((((((((((((((((((((((this.id.hashCode() * 31) + this.deviceModel.hashCode()) * 31) + this.deviceCodename.hashCode()) * 31) + this.versionName.hashCode()) * 31) + Integer.hashCode(this.versionCode)) * 31) + this.buildNumber.hashCode()) * 31) + this.releaseChannel.hashCode()) * 31) + this.releaseType.hashCode()) * 31) + Long.hashCode(this.packageSizeBytes)) * 31) + this.downloadUrl.hashCode()) * 31) + this.checksumSha256.hashCode()) * 31) + this.androidVersion.hashCode()) * 31) + this.securityPatch.hashCode()) * 31) + this.changelog.hashCode()) * 31) + Long.hashCode(this.releaseDate)) * 31) + Boolean.hashCode(this.isMandatory)) * 31) + Integer.hashCode(this.minRequiredVersion)) * 31) + this.status.hashCode()) * 31) + Integer.hashCode(this.rolloutPercentage)) * 31) + this.sourceUrl.hashCode()) * 31) + this.targetLocalPath.hashCode();
    }

    public String toString() {
        return "OtaRelease(id=" + this.id + ", deviceModel=" + this.deviceModel + ", deviceCodename=" + this.deviceCodename + ", versionName=" + this.versionName + ", versionCode=" + this.versionCode + ", buildNumber=" + this.buildNumber + ", releaseChannel=" + this.releaseChannel + ", releaseType=" + this.releaseType + ", packageSizeBytes=" + this.packageSizeBytes + ", downloadUrl=" + this.downloadUrl + ", checksumSha256=" + this.checksumSha256 + ", androidVersion=" + this.androidVersion + ", securityPatch=" + this.securityPatch + ", changelog=" + this.changelog + ", releaseDate=" + this.releaseDate + ", isMandatory=" + this.isMandatory + ", minRequiredVersion=" + this.minRequiredVersion + ", status=" + this.status + ", rolloutPercentage=" + this.rolloutPercentage + ", sourceUrl=" + this.sourceUrl + ", targetLocalPath=" + this.targetLocalPath + ")";
    }

    public OtaRelease(String id, String deviceModel, String deviceCodename, String versionName, int versionCode, String buildNumber, String releaseChannel, String releaseType, long packageSizeBytes, String downloadUrl, String checksumSha256, String androidVersion, String securityPatch, String changelog, long releaseDate, boolean isMandatory, int minRequiredVersion, String status, int rolloutPercentage, String sourceUrl, String targetLocalPath) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(deviceModel, "deviceModel");
        Intrinsics.checkNotNullParameter(deviceCodename, "deviceCodename");
        Intrinsics.checkNotNullParameter(versionName, "versionName");
        Intrinsics.checkNotNullParameter(buildNumber, "buildNumber");
        Intrinsics.checkNotNullParameter(releaseChannel, "releaseChannel");
        Intrinsics.checkNotNullParameter(releaseType, "releaseType");
        Intrinsics.checkNotNullParameter(downloadUrl, "downloadUrl");
        Intrinsics.checkNotNullParameter(checksumSha256, "checksumSha256");
        Intrinsics.checkNotNullParameter(androidVersion, "androidVersion");
        Intrinsics.checkNotNullParameter(securityPatch, "securityPatch");
        Intrinsics.checkNotNullParameter(changelog, "changelog");
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter(targetLocalPath, "targetLocalPath");
        this.id = id;
        this.deviceModel = deviceModel;
        this.deviceCodename = deviceCodename;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.buildNumber = buildNumber;
        this.releaseChannel = releaseChannel;
        this.releaseType = releaseType;
        this.packageSizeBytes = packageSizeBytes;
        this.downloadUrl = downloadUrl;
        this.checksumSha256 = checksumSha256;
        this.androidVersion = androidVersion;
        this.securityPatch = securityPatch;
        this.changelog = changelog;
        this.releaseDate = releaseDate;
        this.isMandatory = isMandatory;
        this.minRequiredVersion = minRequiredVersion;
        this.status = status;
        this.rolloutPercentage = rolloutPercentage;
        this.sourceUrl = sourceUrl;
        this.targetLocalPath = targetLocalPath;
    }

    public /* synthetic */ OtaRelease(String str, String str2, String str3, String str4, int i, String str5, String str6, String str7, long j, String str8, String str9, String str10, String str11, String str12, long j2, boolean z, int i2, String str13, int i3, String str14, String str15, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i4 & 2) != 0 ? OtaConstants.DEVICE_MODEL_NAME : str2, (i4 & 4) != 0 ? OtaConstants.DEVICE_CODENAME : str3, str4, i, (i4 & 32) != 0 ? "" : str5, (i4 & 64) != 0 ? "Official" : str6, (i4 & 128) != 0 ? "Full OTA Package" : str7, (i4 & 256) != 0 ? 0L : j, str8, (i4 & 1024) != 0 ? "" : str9, (i4 & 2048) != 0 ? "Android 15" : str10, (i4 & 4096) != 0 ? "" : str11, (i4 & 8192) != 0 ? "" : str12, (i4 & 16384) != 0 ? System.currentTimeMillis() : j2, (32768 & i4) != 0 ? false : z, (65536 & i4) != 0 ? 0 : i2, (131072 & i4) != 0 ? "PUBLISHED" : str13, (262144 & i4) != 0 ? 100 : i3, (524288 & i4) != 0 ? OtaConstants.DEFAULT_RAW_JSON_URL : str14, (i4 & 1048576) != 0 ? OtaConstants.DEFAULT_TARGET_FILE_PATH : str15);
    }

    public final String getId() {
        return this.id;
    }

    public final String getDeviceModel() {
        return this.deviceModel;
    }

    public final String getDeviceCodename() {
        return this.deviceCodename;
    }

    public final String getVersionName() {
        return this.versionName;
    }

    public final int getVersionCode() {
        return this.versionCode;
    }

    public final String getBuildNumber() {
        return this.buildNumber;
    }

    public final String getReleaseChannel() {
        return this.releaseChannel;
    }

    public final String getReleaseType() {
        return this.releaseType;
    }

    public final long getPackageSizeBytes() {
        return this.packageSizeBytes;
    }

    public final String getDownloadUrl() {
        return this.downloadUrl;
    }

    public final String getChecksumSha256() {
        return this.checksumSha256;
    }

    public final String getAndroidVersion() {
        return this.androidVersion;
    }

    public final String getSecurityPatch() {
        return this.securityPatch;
    }

    public final String getChangelog() {
        return this.changelog;
    }

    public final long getReleaseDate() {
        return this.releaseDate;
    }

    public final boolean isMandatory() {
        return this.isMandatory;
    }

    public final int getMinRequiredVersion() {
        return this.minRequiredVersion;
    }

    public final String getStatus() {
        return this.status;
    }

    public final int getRolloutPercentage() {
        return this.rolloutPercentage;
    }

    public final String getSourceUrl() {
        return this.sourceUrl;
    }

    public final String getTargetLocalPath() {
        return this.targetLocalPath;
    }
}
