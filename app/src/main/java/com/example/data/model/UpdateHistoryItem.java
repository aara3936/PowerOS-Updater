package com.example.data.model;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: OtaModels.kt */
@Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u001c\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0087\b\u0018\u00002\u00020\u0001BK\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0005\u0012\u0006\u0010\f\u001a\u00020\u0005¢\u0006\u0004\b\r\u0010\u000eJ\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0003HÆ\u0003J\t\u0010 \u001a\u00020\u0005HÆ\u0003J\t\u0010!\u001a\u00020\u0005HÆ\u0003JY\u0010\"\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u0005HÆ\u0001J\u0013\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020\u0007HÖ\u0001J\t\u0010'\u001a\u00020\u0005HÖ\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0011\u0010\u000b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012R\u0011\u0010\f\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012¨\u0006("}, d2 = {"Lcom/example/data/model/UpdateHistoryItem;", "", "historyId", "", "versionName", "", "versionCode", "", "buildNumber", "installedTimestamp", "packageSizeBytes", "releaseChannel", "installType", "<init>", "(JLjava/lang/String;ILjava/lang/String;JJLjava/lang/String;Ljava/lang/String;)V", "getHistoryId", "()J", "getVersionName", "()Ljava/lang/String;", "getVersionCode", "()I", "getBuildNumber", "getInstalledTimestamp", "getPackageSizeBytes", "getReleaseChannel", "getInstallType", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* data */ class UpdateHistoryItem {
    public static final int $stable = 0;
    private final String buildNumber;
    private final long historyId;
    private final String installType;
    private final long installedTimestamp;
    private final long packageSizeBytes;
    private final String releaseChannel;
    private final int versionCode;
    private final String versionName;

    public static /* synthetic */ UpdateHistoryItem copy$default(UpdateHistoryItem updateHistoryItem, long j, String str, int i, String str2, long j2, long j3, String str3, String str4, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            j = updateHistoryItem.historyId;
        }
        long j4 = j;
        if ((i2 & 2) != 0) {
            str = updateHistoryItem.versionName;
        }
        return updateHistoryItem.copy(j4, str, (i2 & 4) != 0 ? updateHistoryItem.versionCode : i, (i2 & 8) != 0 ? updateHistoryItem.buildNumber : str2, (i2 & 16) != 0 ? updateHistoryItem.installedTimestamp : j2, (i2 & 32) != 0 ? updateHistoryItem.packageSizeBytes : j3, (i2 & 64) != 0 ? updateHistoryItem.releaseChannel : str3, (i2 & 128) != 0 ? updateHistoryItem.installType : str4);
    }

    /* renamed from: component1, reason: from getter */
    public final long getHistoryId() {
        return this.historyId;
    }

    /* renamed from: component2, reason: from getter */
    public final String getVersionName() {
        return this.versionName;
    }

    /* renamed from: component3, reason: from getter */
    public final int getVersionCode() {
        return this.versionCode;
    }

    /* renamed from: component4, reason: from getter */
    public final String getBuildNumber() {
        return this.buildNumber;
    }

    /* renamed from: component5, reason: from getter */
    public final long getInstalledTimestamp() {
        return this.installedTimestamp;
    }

    /* renamed from: component6, reason: from getter */
    public final long getPackageSizeBytes() {
        return this.packageSizeBytes;
    }

    /* renamed from: component7, reason: from getter */
    public final String getReleaseChannel() {
        return this.releaseChannel;
    }

    /* renamed from: component8, reason: from getter */
    public final String getInstallType() {
        return this.installType;
    }

    public final UpdateHistoryItem copy(long historyId, String versionName, int versionCode, String buildNumber, long installedTimestamp, long packageSizeBytes, String releaseChannel, String installType) {
        Intrinsics.checkNotNullParameter(versionName, "versionName");
        Intrinsics.checkNotNullParameter(buildNumber, "buildNumber");
        Intrinsics.checkNotNullParameter(releaseChannel, "releaseChannel");
        Intrinsics.checkNotNullParameter(installType, "installType");
        return new UpdateHistoryItem(historyId, versionName, versionCode, buildNumber, installedTimestamp, packageSizeBytes, releaseChannel, installType);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UpdateHistoryItem)) {
            return false;
        }
        UpdateHistoryItem updateHistoryItem = (UpdateHistoryItem) other;
        return this.historyId == updateHistoryItem.historyId && Intrinsics.areEqual(this.versionName, updateHistoryItem.versionName) && this.versionCode == updateHistoryItem.versionCode && Intrinsics.areEqual(this.buildNumber, updateHistoryItem.buildNumber) && this.installedTimestamp == updateHistoryItem.installedTimestamp && this.packageSizeBytes == updateHistoryItem.packageSizeBytes && Intrinsics.areEqual(this.releaseChannel, updateHistoryItem.releaseChannel) && Intrinsics.areEqual(this.installType, updateHistoryItem.installType);
    }

    public int hashCode() {
        return (((((((((((((Long.hashCode(this.historyId) * 31) + this.versionName.hashCode()) * 31) + Integer.hashCode(this.versionCode)) * 31) + this.buildNumber.hashCode()) * 31) + Long.hashCode(this.installedTimestamp)) * 31) + Long.hashCode(this.packageSizeBytes)) * 31) + this.releaseChannel.hashCode()) * 31) + this.installType.hashCode();
    }

    public String toString() {
        return "UpdateHistoryItem(historyId=" + this.historyId + ", versionName=" + this.versionName + ", versionCode=" + this.versionCode + ", buildNumber=" + this.buildNumber + ", installedTimestamp=" + this.installedTimestamp + ", packageSizeBytes=" + this.packageSizeBytes + ", releaseChannel=" + this.releaseChannel + ", installType=" + this.installType + ")";
    }

    public UpdateHistoryItem(long historyId, String versionName, int versionCode, String buildNumber, long installedTimestamp, long packageSizeBytes, String releaseChannel, String installType) {
        Intrinsics.checkNotNullParameter(versionName, "versionName");
        Intrinsics.checkNotNullParameter(buildNumber, "buildNumber");
        Intrinsics.checkNotNullParameter(releaseChannel, "releaseChannel");
        Intrinsics.checkNotNullParameter(installType, "installType");
        this.historyId = historyId;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.buildNumber = buildNumber;
        this.installedTimestamp = installedTimestamp;
        this.packageSizeBytes = packageSizeBytes;
        this.releaseChannel = releaseChannel;
        this.installType = installType;
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ UpdateHistoryItem(long r15, java.lang.String r17, int r18, java.lang.String r19, long r20, long r22, java.lang.String r24, java.lang.String r25, int r26, kotlin.jvm.internal.DefaultConstructorMarker r27) {
        /*
            r14 = this;
            r0 = r26 & 1
            if (r0 == 0) goto L8
            r0 = 0
            r3 = r0
            goto L9
        L8:
            r3 = r15
        L9:
            r0 = r26 & 16
            if (r0 == 0) goto L13
            long r0 = java.lang.System.currentTimeMillis()
            r8 = r0
            goto L15
        L13:
            r8 = r20
        L15:
            r2 = r14
            r5 = r17
            r6 = r18
            r7 = r19
            r10 = r22
            r12 = r24
            r13 = r25
            r2.<init>(r3, r5, r6, r7, r8, r10, r12, r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.model.UpdateHistoryItem.<init>(long, java.lang.String, int, java.lang.String, long, long, java.lang.String, java.lang.String, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public final long getHistoryId() {
        return this.historyId;
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

    public final long getInstalledTimestamp() {
        return this.installedTimestamp;
    }

    public final long getPackageSizeBytes() {
        return this.packageSizeBytes;
    }

    public final String getReleaseChannel() {
        return this.releaseChannel;
    }

    public final String getInstallType() {
        return this.installType;
    }
}
