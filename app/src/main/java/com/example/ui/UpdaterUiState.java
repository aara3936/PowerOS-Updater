package com.example.ui;

import androidx.core.app.FrameMetricsAggregator;
import com.example.data.model.OtaRelease;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: UpdaterViewModel.kt */
@Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Be\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u000f\u0010\u0010J\t\u0010\u001a\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001b\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0007HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0005HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0005HÆ\u0003J\t\u0010 \u001a\u00020\u0005HÆ\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\rHÆ\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0003HÆ\u0003Jg\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010$\u001a\u00020\u00052\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020'HÖ\u0001J\t\u0010(\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0013R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0013R\u0011\u0010\t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0013R\u0011\u0010\n\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0013R\u0011\u0010\u000b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0013R\u0013\u0010\f\u001a\u0004\u0018\u00010\r¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012¨\u0006)"}, d2 = {"Lcom/example/ui/UpdaterUiState;", "", "selectedChannel", "", "isCheckingForUpdate", "", "lastCheckTime", "", "isDeviceDetailOpen", "isHistoryOpen", "isLocalInstallOpen", "showRecoveryInstallDialog", "activeReleaseForInstall", "Lcom/example/data/model/OtaRelease;", "snackbarMessage", "<init>", "(Ljava/lang/String;ZJZZZZLcom/example/data/model/OtaRelease;Ljava/lang/String;)V", "getSelectedChannel", "()Ljava/lang/String;", "()Z", "getLastCheckTime", "()J", "getShowRecoveryInstallDialog", "getActiveReleaseForInstall", "()Lcom/example/data/model/OtaRelease;", "getSnackbarMessage", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "", "toString", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes8.dex */
public final /* data */ class UpdaterUiState {
    public static final int $stable = 0;
    private final OtaRelease activeReleaseForInstall;
    private final boolean isCheckingForUpdate;
    private final boolean isDeviceDetailOpen;
    private final boolean isHistoryOpen;
    private final boolean isLocalInstallOpen;
    private final long lastCheckTime;
    private final String selectedChannel;
    private final boolean showRecoveryInstallDialog;
    private final String snackbarMessage;

    public UpdaterUiState() {
        this(null, false, 0L, false, false, false, false, null, null, FrameMetricsAggregator.EVERY_DURATION, null);
    }

    public static /* synthetic */ UpdaterUiState copy$default(UpdaterUiState updaterUiState, String str, boolean z, long j, boolean z2, boolean z3, boolean z4, boolean z5, OtaRelease otaRelease, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = updaterUiState.selectedChannel;
        }
        if ((i & 2) != 0) {
            z = updaterUiState.isCheckingForUpdate;
        }
        if ((i & 4) != 0) {
            j = updaterUiState.lastCheckTime;
        }
        if ((i & 8) != 0) {
            z2 = updaterUiState.isDeviceDetailOpen;
        }
        if ((i & 16) != 0) {
            z3 = updaterUiState.isHistoryOpen;
        }
        if ((i & 32) != 0) {
            z4 = updaterUiState.isLocalInstallOpen;
        }
        if ((i & 64) != 0) {
            z5 = updaterUiState.showRecoveryInstallDialog;
        }
        if ((i & 128) != 0) {
            otaRelease = updaterUiState.activeReleaseForInstall;
        }
        if ((i & 256) != 0) {
            str2 = updaterUiState.snackbarMessage;
        }
        OtaRelease otaRelease2 = otaRelease;
        String str3 = str2;
        long j2 = j;
        return updaterUiState.copy(str, z, j2, z2, z3, z4, z5, otaRelease2, str3);
    }

    /* renamed from: component1, reason: from getter */
    public final String getSelectedChannel() {
        return this.selectedChannel;
    }

    /* renamed from: component2, reason: from getter */
    public final boolean getIsCheckingForUpdate() {
        return this.isCheckingForUpdate;
    }

    /* renamed from: component3, reason: from getter */
    public final long getLastCheckTime() {
        return this.lastCheckTime;
    }

    /* renamed from: component4, reason: from getter */
    public final boolean getIsDeviceDetailOpen() {
        return this.isDeviceDetailOpen;
    }

    /* renamed from: component5, reason: from getter */
    public final boolean getIsHistoryOpen() {
        return this.isHistoryOpen;
    }

    /* renamed from: component6, reason: from getter */
    public final boolean getIsLocalInstallOpen() {
        return this.isLocalInstallOpen;
    }

    /* renamed from: component7, reason: from getter */
    public final boolean getShowRecoveryInstallDialog() {
        return this.showRecoveryInstallDialog;
    }

    /* renamed from: component8, reason: from getter */
    public final OtaRelease getActiveReleaseForInstall() {
        return this.activeReleaseForInstall;
    }

    /* renamed from: component9, reason: from getter */
    public final String getSnackbarMessage() {
        return this.snackbarMessage;
    }

    public final UpdaterUiState copy(String selectedChannel, boolean isCheckingForUpdate, long lastCheckTime, boolean isDeviceDetailOpen, boolean isHistoryOpen, boolean isLocalInstallOpen, boolean showRecoveryInstallDialog, OtaRelease activeReleaseForInstall, String snackbarMessage) {
        Intrinsics.checkNotNullParameter(selectedChannel, "selectedChannel");
        return new UpdaterUiState(selectedChannel, isCheckingForUpdate, lastCheckTime, isDeviceDetailOpen, isHistoryOpen, isLocalInstallOpen, showRecoveryInstallDialog, activeReleaseForInstall, snackbarMessage);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof UpdaterUiState)) {
            return false;
        }
        UpdaterUiState updaterUiState = (UpdaterUiState) other;
        return Intrinsics.areEqual(this.selectedChannel, updaterUiState.selectedChannel) && this.isCheckingForUpdate == updaterUiState.isCheckingForUpdate && this.lastCheckTime == updaterUiState.lastCheckTime && this.isDeviceDetailOpen == updaterUiState.isDeviceDetailOpen && this.isHistoryOpen == updaterUiState.isHistoryOpen && this.isLocalInstallOpen == updaterUiState.isLocalInstallOpen && this.showRecoveryInstallDialog == updaterUiState.showRecoveryInstallDialog && Intrinsics.areEqual(this.activeReleaseForInstall, updaterUiState.activeReleaseForInstall) && Intrinsics.areEqual(this.snackbarMessage, updaterUiState.snackbarMessage);
    }

    public int hashCode() {
        return (((((((((((((((this.selectedChannel.hashCode() * 31) + Boolean.hashCode(this.isCheckingForUpdate)) * 31) + Long.hashCode(this.lastCheckTime)) * 31) + Boolean.hashCode(this.isDeviceDetailOpen)) * 31) + Boolean.hashCode(this.isHistoryOpen)) * 31) + Boolean.hashCode(this.isLocalInstallOpen)) * 31) + Boolean.hashCode(this.showRecoveryInstallDialog)) * 31) + (this.activeReleaseForInstall == null ? 0 : this.activeReleaseForInstall.hashCode())) * 31) + (this.snackbarMessage != null ? this.snackbarMessage.hashCode() : 0);
    }

    public String toString() {
        return "UpdaterUiState(selectedChannel=" + this.selectedChannel + ", isCheckingForUpdate=" + this.isCheckingForUpdate + ", lastCheckTime=" + this.lastCheckTime + ", isDeviceDetailOpen=" + this.isDeviceDetailOpen + ", isHistoryOpen=" + this.isHistoryOpen + ", isLocalInstallOpen=" + this.isLocalInstallOpen + ", showRecoveryInstallDialog=" + this.showRecoveryInstallDialog + ", activeReleaseForInstall=" + this.activeReleaseForInstall + ", snackbarMessage=" + this.snackbarMessage + ")";
    }

    public UpdaterUiState(String selectedChannel, boolean isCheckingForUpdate, long lastCheckTime, boolean isDeviceDetailOpen, boolean isHistoryOpen, boolean isLocalInstallOpen, boolean showRecoveryInstallDialog, OtaRelease activeReleaseForInstall, String snackbarMessage) {
        Intrinsics.checkNotNullParameter(selectedChannel, "selectedChannel");
        this.selectedChannel = selectedChannel;
        this.isCheckingForUpdate = isCheckingForUpdate;
        this.lastCheckTime = lastCheckTime;
        this.isDeviceDetailOpen = isDeviceDetailOpen;
        this.isHistoryOpen = isHistoryOpen;
        this.isLocalInstallOpen = isLocalInstallOpen;
        this.showRecoveryInstallDialog = showRecoveryInstallDialog;
        this.activeReleaseForInstall = activeReleaseForInstall;
        this.snackbarMessage = snackbarMessage;
    }

    public /* synthetic */ UpdaterUiState(String str, boolean z, long j, boolean z2, boolean z3, boolean z4, boolean z5, OtaRelease otaRelease, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? "Stable" : str, (i & 2) != 0 ? false : z, (i & 4) != 0 ? System.currentTimeMillis() : j, (i & 8) != 0 ? false : z2, (i & 16) != 0 ? false : z3, (i & 32) != 0 ? false : z4, (i & 64) != 0 ? false : z5, (i & 128) != 0 ? null : otaRelease, (i & 256) != 0 ? null : str2);
    }

    public final String getSelectedChannel() {
        return this.selectedChannel;
    }

    public final boolean isCheckingForUpdate() {
        return this.isCheckingForUpdate;
    }

    public final long getLastCheckTime() {
        return this.lastCheckTime;
    }

    public final boolean isDeviceDetailOpen() {
        return this.isDeviceDetailOpen;
    }

    public final boolean isHistoryOpen() {
        return this.isHistoryOpen;
    }

    public final boolean isLocalInstallOpen() {
        return this.isLocalInstallOpen;
    }

    public final boolean getShowRecoveryInstallDialog() {
        return this.showRecoveryInstallDialog;
    }

    public final OtaRelease getActiveReleaseForInstall() {
        return this.activeReleaseForInstall;
    }

    public final String getSnackbarMessage() {
        return this.snackbarMessage;
    }
}
