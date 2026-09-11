package com.example.data.model;

import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: OtaModels.kt */
@Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\"\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Bw\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u0012\b\b\u0002\u0010\t\u001a\u00020\u0007\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\f\u0012\b\b\u0002\u0010\u000f\u001a\u00020\f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0007¢\u0006\u0004\b\u0011\u0010\u0012J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0005HÆ\u0003J\t\u0010$\u001a\u00020\u0007HÆ\u0003J\t\u0010%\u001a\u00020\u0007HÆ\u0003J\t\u0010&\u001a\u00020\u0007HÆ\u0003J\t\u0010'\u001a\u00020\u0007HÆ\u0003J\t\u0010(\u001a\u00020\fHÆ\u0003J\t\u0010)\u001a\u00020\u0005HÆ\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\fHÆ\u0003J\t\u0010+\u001a\u00020\fHÆ\u0003J\t\u0010,\u001a\u00020\u0007HÆ\u0003Jy\u0010-\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u00072\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u00052\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\f2\b\b\u0002\u0010\u000f\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u0007HÆ\u0001J\u0013\u0010.\u001a\u00020/2\b\u00100\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00101\u001a\u000202HÖ\u0001J\t\u00103\u001a\u00020\fHÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\b\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\t\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018R\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0018R\u0011\u0010\u000b\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0011\u0010\r\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0016R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\f¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001dR\u0011\u0010\u000f\u001a\u00020\f¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u001dR\u0011\u0010\u0010\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018¨\u00064"}, d2 = {"Lcom/example/data/model/DownloadProgress;", "", NotificationCompat.CATEGORY_STATUS, "Lcom/example/data/model/DownloadStatus;", NotificationCompat.CATEGORY_PROGRESS, "", "downloadedBytes", "", "totalBytes", "speedBytesPerSec", "etaSeconds", "currentStep", "", "installProgress", "errorMessage", "destinationPath", "actualFileSize", "<init>", "(Lcom/example/data/model/DownloadStatus;FJJJJLjava/lang/String;FLjava/lang/String;Ljava/lang/String;J)V", "getStatus", "()Lcom/example/data/model/DownloadStatus;", "getProgress", "()F", "getDownloadedBytes", "()J", "getTotalBytes", "getSpeedBytesPerSec", "getEtaSeconds", "getCurrentStep", "()Ljava/lang/String;", "getInstallProgress", "getErrorMessage", "getDestinationPath", "getActualFileSize", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "copy", "equals", "", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes6.dex */
public final /* data */ class DownloadProgress {
    public static final int $stable = 0;
    private final long actualFileSize;
    private final String currentStep;
    private final String destinationPath;
    private final long downloadedBytes;
    private final String errorMessage;
    private final long etaSeconds;
    private final float installProgress;
    private final float progress;
    private final long speedBytesPerSec;
    private final DownloadStatus status;
    private final long totalBytes;

    public DownloadProgress() {
        this(null, 0.0f, 0L, 0L, 0L, 0L, null, 0.0f, null, null, 0L, 2047, null);
    }

    public static /* synthetic */ DownloadProgress copy$default(DownloadProgress downloadProgress, DownloadStatus downloadStatus, float f, long j, long j2, long j3, long j4, String str, float f2, String str2, String str3, long j5, int i, Object obj) {
        long j6;
        DownloadStatus downloadStatus2;
        DownloadStatus downloadStatus3 = (i & 1) != 0 ? downloadProgress.status : downloadStatus;
        float f3 = (i & 2) != 0 ? downloadProgress.progress : f;
        long j7 = (i & 4) != 0 ? downloadProgress.downloadedBytes : j;
        long j8 = (i & 8) != 0 ? downloadProgress.totalBytes : j2;
        long j9 = (i & 16) != 0 ? downloadProgress.speedBytesPerSec : j3;
        long j10 = (i & 32) != 0 ? downloadProgress.etaSeconds : j4;
        String str4 = (i & 64) != 0 ? downloadProgress.currentStep : str;
        float f4 = (i & 128) != 0 ? downloadProgress.installProgress : f2;
        String str5 = (i & 256) != 0 ? downloadProgress.errorMessage : str2;
        String str6 = (i & 512) != 0 ? downloadProgress.destinationPath : str3;
        if ((i & 1024) != 0) {
            downloadStatus2 = downloadStatus3;
            j6 = downloadProgress.actualFileSize;
        } else {
            j6 = j5;
            downloadStatus2 = downloadStatus3;
        }
        return downloadProgress.copy(downloadStatus2, f3, j7, j8, j9, j10, str4, f4, str5, str6, j6);
    }

    /* renamed from: component1, reason: from getter */
    public final DownloadStatus getStatus() {
        return this.status;
    }

    /* renamed from: component10, reason: from getter */
    public final String getDestinationPath() {
        return this.destinationPath;
    }

    /* renamed from: component11, reason: from getter */
    public final long getActualFileSize() {
        return this.actualFileSize;
    }

    /* renamed from: component2, reason: from getter */
    public final float getProgress() {
        return this.progress;
    }

    /* renamed from: component3, reason: from getter */
    public final long getDownloadedBytes() {
        return this.downloadedBytes;
    }

    /* renamed from: component4, reason: from getter */
    public final long getTotalBytes() {
        return this.totalBytes;
    }

    /* renamed from: component5, reason: from getter */
    public final long getSpeedBytesPerSec() {
        return this.speedBytesPerSec;
    }

    /* renamed from: component6, reason: from getter */
    public final long getEtaSeconds() {
        return this.etaSeconds;
    }

    /* renamed from: component7, reason: from getter */
    public final String getCurrentStep() {
        return this.currentStep;
    }

    /* renamed from: component8, reason: from getter */
    public final float getInstallProgress() {
        return this.installProgress;
    }

    /* renamed from: component9, reason: from getter */
    public final String getErrorMessage() {
        return this.errorMessage;
    }

    public final DownloadProgress copy(DownloadStatus status, float progress, long downloadedBytes, long totalBytes, long speedBytesPerSec, long etaSeconds, String currentStep, float installProgress, String errorMessage, String destinationPath, long actualFileSize) {
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(currentStep, "currentStep");
        Intrinsics.checkNotNullParameter(destinationPath, "destinationPath");
        return new DownloadProgress(status, progress, downloadedBytes, totalBytes, speedBytesPerSec, etaSeconds, currentStep, installProgress, errorMessage, destinationPath, actualFileSize);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DownloadProgress)) {
            return false;
        }
        DownloadProgress downloadProgress = (DownloadProgress) other;
        return this.status == downloadProgress.status && Float.compare(this.progress, downloadProgress.progress) == 0 && this.downloadedBytes == downloadProgress.downloadedBytes && this.totalBytes == downloadProgress.totalBytes && this.speedBytesPerSec == downloadProgress.speedBytesPerSec && this.etaSeconds == downloadProgress.etaSeconds && Intrinsics.areEqual(this.currentStep, downloadProgress.currentStep) && Float.compare(this.installProgress, downloadProgress.installProgress) == 0 && Intrinsics.areEqual(this.errorMessage, downloadProgress.errorMessage) && Intrinsics.areEqual(this.destinationPath, downloadProgress.destinationPath) && this.actualFileSize == downloadProgress.actualFileSize;
    }

    public int hashCode() {
        return (((((((((((((((((((this.status.hashCode() * 31) + Float.hashCode(this.progress)) * 31) + Long.hashCode(this.downloadedBytes)) * 31) + Long.hashCode(this.totalBytes)) * 31) + Long.hashCode(this.speedBytesPerSec)) * 31) + Long.hashCode(this.etaSeconds)) * 31) + this.currentStep.hashCode()) * 31) + Float.hashCode(this.installProgress)) * 31) + (this.errorMessage == null ? 0 : this.errorMessage.hashCode())) * 31) + this.destinationPath.hashCode()) * 31) + Long.hashCode(this.actualFileSize);
    }

    public String toString() {
        return "DownloadProgress(status=" + this.status + ", progress=" + this.progress + ", downloadedBytes=" + this.downloadedBytes + ", totalBytes=" + this.totalBytes + ", speedBytesPerSec=" + this.speedBytesPerSec + ", etaSeconds=" + this.etaSeconds + ", currentStep=" + this.currentStep + ", installProgress=" + this.installProgress + ", errorMessage=" + this.errorMessage + ", destinationPath=" + this.destinationPath + ", actualFileSize=" + this.actualFileSize + ")";
    }

    public DownloadProgress(DownloadStatus status, float progress, long downloadedBytes, long totalBytes, long speedBytesPerSec, long etaSeconds, String currentStep, float installProgress, String errorMessage, String destinationPath, long actualFileSize) {
        Intrinsics.checkNotNullParameter(status, "status");
        Intrinsics.checkNotNullParameter(currentStep, "currentStep");
        Intrinsics.checkNotNullParameter(destinationPath, "destinationPath");
        this.status = status;
        this.progress = progress;
        this.downloadedBytes = downloadedBytes;
        this.totalBytes = totalBytes;
        this.speedBytesPerSec = speedBytesPerSec;
        this.etaSeconds = etaSeconds;
        this.currentStep = currentStep;
        this.installProgress = installProgress;
        this.errorMessage = errorMessage;
        this.destinationPath = destinationPath;
        this.actualFileSize = actualFileSize;
    }

    public /* synthetic */ DownloadProgress(DownloadStatus downloadStatus, float f, long j, long j2, long j3, long j4, String str, float f2, String str2, String str3, long j5, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? DownloadStatus.IDLE : downloadStatus, (i & 2) != 0 ? 0.0f : f, (i & 4) != 0 ? 0L : j, (i & 8) != 0 ? 0L : j2, (i & 16) != 0 ? 0L : j3, (i & 32) != 0 ? 0L : j4, (i & 64) != 0 ? "" : str, (i & 128) == 0 ? f2 : 0.0f, (i & 256) != 0 ? null : str2, (i & 512) != 0 ? OtaConstants.DEFAULT_TARGET_FILE_PATH : str3, (i & 1024) != 0 ? 0L : j5);
    }

    public final DownloadStatus getStatus() {
        return this.status;
    }

    public final float getProgress() {
        return this.progress;
    }

    public final long getDownloadedBytes() {
        return this.downloadedBytes;
    }

    public final long getTotalBytes() {
        return this.totalBytes;
    }

    public final long getSpeedBytesPerSec() {
        return this.speedBytesPerSec;
    }

    public final long getEtaSeconds() {
        return this.etaSeconds;
    }

    public final String getCurrentStep() {
        return this.currentStep;
    }

    public final float getInstallProgress() {
        return this.installProgress;
    }

    public final String getErrorMessage() {
        return this.errorMessage;
    }

    public final String getDestinationPath() {
        return this.destinationPath;
    }

    public final long getActualFileSize() {
        return this.actualFileSize;
    }
}
