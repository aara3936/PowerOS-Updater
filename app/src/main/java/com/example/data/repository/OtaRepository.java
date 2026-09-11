package com.example.data.repository;

import com.example.data.local.OtaReleaseDao;
import com.example.data.local.UpdateHistoryDao;
import com.example.data.model.DownloadProgress;
import com.example.data.model.DownloadStatus;
import com.example.data.model.OtaConstants;
import com.example.data.model.OtaRelease;
import com.example.data.model.SystemDeviceInfo;
import com.example.data.model.UpdateHistoryItem;
import com.google.android.gms.common.internal.ImagesContract;
import java.util.List;
import java.util.concurrent.CancellationException;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;
import kotlinx.coroutines.flow.MutableStateFlow;
import kotlinx.coroutines.flow.StateFlow;
import kotlinx.coroutines.flow.StateFlowKt;

/* compiled from: OtaRepository.kt */
@Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\r\b\u0007\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0004\b\u0006\u0010\u0007J&\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0\u001c2\b\b\u0002\u0010\u001f\u001a\u00020\u0014H\u0086@¢\u0006\u0004\b \u0010!J\u001c\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0#2\b\b\u0002\u0010$\u001a\u00020\u0014J\"\u0010%\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001e0#2\b\b\u0002\u0010$\u001a\u00020\u00142\b\b\u0002\u0010&\u001a\u00020\u0014J\u0012\u0010'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\u001d0#J\u0012\u0010(\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0\u001d0#J\u0016\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u001eH\u0086@¢\u0006\u0002\u0010-J\u0016\u0010.\u001a\u00020+2\u0006\u0010/\u001a\u00020\u0014H\u0086@¢\u0006\u0002\u0010!J\u0016\u00100\u001a\u00020+2\u0006\u0010,\u001a\u00020\u001eH\u0086@¢\u0006\u0002\u0010-J\u000e\u00101\u001a\u00020+2\u0006\u0010\u001f\u001a\u00020\u0014J\u000e\u00102\u001a\u00020+2\u0006\u0010,\u001a\u00020\u001eJ\u0006\u00103\u001a\u00020+J\u000e\u00104\u001a\u00020+2\u0006\u0010,\u001a\u00020\u001eJ\u0006\u00105\u001a\u00020+J\u000e\u00106\u001a\u00020+2\u0006\u0010,\u001a\u00020\u001eJ\u0006\u00107\u001a\u00020+R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\n0\f¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00100\f¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000eR\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\f¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000eR\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u000e¢\u0006\u0002\n\u0000¨\u00068"}, d2 = {"Lcom/example/data/repository/OtaRepository;", "", "otaReleaseDao", "Lcom/example/data/local/OtaReleaseDao;", "updateHistoryDao", "Lcom/example/data/local/UpdateHistoryDao;", "<init>", "(Lcom/example/data/local/OtaReleaseDao;Lcom/example/data/local/UpdateHistoryDao;)V", "_deviceInfo", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/example/data/model/SystemDeviceInfo;", "deviceInfo", "Lkotlinx/coroutines/flow/StateFlow;", "getDeviceInfo", "()Lkotlinx/coroutines/flow/StateFlow;", "_downloadProgress", "Lcom/example/data/model/DownloadProgress;", "downloadProgress", "getDownloadProgress", "_serverUrl", "", "serverUrl", "getServerUrl", "downloadJob", "Lkotlinx/coroutines/Job;", "isDownloadPaused", "", "fetchFromGitHubRaw", "Lkotlin/Result;", "", "Lcom/example/data/model/OtaRelease;", ImagesContract.URL, "fetchFromGitHubRaw-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReleasesForDevice", "Lkotlinx/coroutines/flow/Flow;", "device", "getLatestRelease", "channel", "getAllReleases", "getUpdateHistory", "Lcom/example/data/model/UpdateHistoryItem;", "publishRelease", "", "release", "(Lcom/example/data/model/OtaRelease;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRelease", "id", "updateRelease", "setServerUrl", "startDownload", "pauseDownload", "resumeDownload", "cancelDownload", "installUpdate", "resetUpdateState", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes8.dex */
public final class OtaRepository {
    public static final int $stable = 8;
    private final MutableStateFlow<SystemDeviceInfo> _deviceInfo;
    private final MutableStateFlow<DownloadProgress> _downloadProgress;
    private final MutableStateFlow<String> _serverUrl;
    private final StateFlow<SystemDeviceInfo> deviceInfo;
    private Job downloadJob;
    private final StateFlow<DownloadProgress> downloadProgress;
    private boolean isDownloadPaused;
    private final OtaReleaseDao otaReleaseDao;
    private final StateFlow<String> serverUrl;
    private final UpdateHistoryDao updateHistoryDao;

    public OtaRepository(OtaReleaseDao otaReleaseDao, UpdateHistoryDao updateHistoryDao) {
        Intrinsics.checkNotNullParameter(otaReleaseDao, "otaReleaseDao");
        Intrinsics.checkNotNullParameter(updateHistoryDao, "updateHistoryDao");
        this.otaReleaseDao = otaReleaseDao;
        this.updateHistoryDao = updateHistoryDao;
        this._deviceInfo = StateFlowKt.MutableStateFlow(new SystemDeviceInfo(OtaConstants.DEVICE_MODEL_NAME, OtaConstants.DEVICE_CODENAME, OtaConstants.CURRENT_BASE_VERSION_NAME, 3000, OtaConstants.CURRENT_BUILD_TAG, "Android 15", "August 2026", "5.15.148-PowerOS-OppoA6X", 92, true, 62.4f, 128.0f, "Wi-Fi Connected", "MediaTek Dimensity / ARM64", OtaConstants.DEFAULT_RAW_JSON_URL, OtaConstants.DEFAULT_TARGET_FILE_PATH));
        this.deviceInfo = FlowKt.asStateFlow(this._deviceInfo);
        this._downloadProgress = StateFlowKt.MutableStateFlow(new DownloadProgress(null, 0.0f, 0L, 0L, 0L, 0L, null, 0.0f, null, OtaConstants.DEFAULT_TARGET_FILE_PATH, 0L, 1535, null));
        this.downloadProgress = FlowKt.asStateFlow(this._downloadProgress);
        this._serverUrl = StateFlowKt.MutableStateFlow(OtaConstants.DEFAULT_RAW_JSON_URL);
        this.serverUrl = FlowKt.asStateFlow(this._serverUrl);
        BuildersKt__Builders_commonKt.launch$default(CoroutineScopeKt.CoroutineScope(Dispatchers.getIO()), null, null, new AnonymousClass1(null), 3, null);
    }

    public final StateFlow<SystemDeviceInfo> getDeviceInfo() {
        return this.deviceInfo;
    }

    public final StateFlow<DownloadProgress> getDownloadProgress() {
        return this.downloadProgress;
    }

    public final StateFlow<String> getServerUrl() {
        return this.serverUrl;
    }

    /* compiled from: OtaRepository.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.example.data.repository.OtaRepository$1", f = "OtaRepository.kt", i = {}, l = {71}, m = "invokeSuspend", n = {}, s = {})
    /* renamed from: com.example.data.repository.OtaRepository$1, reason: invalid class name */
    /* loaded from: classes8.dex */
    static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new AnonymousClass1(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (OtaRepository.this.m6989fetchFromGitHubRawgIAlus(OtaConstants.DEFAULT_RAW_JSON_URL, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    ((Result) $result).getValue();
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* renamed from: fetchFromGitHubRaw-gIAlu-s$default, reason: not valid java name */
    public static /* synthetic */ Object m6988fetchFromGitHubRawgIAlus$default(OtaRepository otaRepository, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = otaRepository._serverUrl.getValue();
        }
        return otaRepository.m6989fetchFromGitHubRawgIAlus(str, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0024  */
    /* renamed from: fetchFromGitHubRaw-gIAlu-s, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object m6989fetchFromGitHubRawgIAlus(java.lang.String r7, kotlin.coroutines.Continuation<? super kotlin.Result<? extends java.util.List<com.example.data.model.OtaRelease>>> r8) {
        /*
            r6 = this;
            boolean r0 = r8 instanceof com.example.data.repository.OtaRepository$fetchFromGitHubRaw$1
            if (r0 == 0) goto L14
            r0 = r8
            com.example.data.repository.OtaRepository$fetchFromGitHubRaw$1 r0 = (com.example.data.repository.OtaRepository$fetchFromGitHubRaw$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r1 = r0.label
            int r1 = r1 - r2
            r0.label = r1
            goto L19
        L14:
            com.example.data.repository.OtaRepository$fetchFromGitHubRaw$1 r0 = new com.example.data.repository.OtaRepository$fetchFromGitHubRaw$1
            r0.<init>(r6, r8)
        L19:
            java.lang.Object r1 = r0.result
            java.lang.Object r2 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r3 = r0.label
            switch(r3) {
                case 0: goto L36;
                case 1: goto L2c;
                default: goto L24;
            }
        L24:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L2c:
            java.lang.Object r2 = r0.L$0
            r7 = r2
            java.lang.String r7 = (java.lang.String) r7
            kotlin.ResultKt.throwOnFailure(r1)
            r3 = r1
            goto L57
        L36:
            kotlin.ResultKt.throwOnFailure(r1)
            kotlinx.coroutines.CoroutineDispatcher r3 = kotlinx.coroutines.Dispatchers.getIO()
            kotlin.coroutines.CoroutineContext r3 = (kotlin.coroutines.CoroutineContext) r3
            com.example.data.repository.OtaRepository$fetchFromGitHubRaw$2 r4 = new com.example.data.repository.OtaRepository$fetchFromGitHubRaw$2
            r5 = 0
            r4.<init>(r7, r6, r5)
            kotlin.jvm.functions.Function2 r4 = (kotlin.jvm.functions.Function2) r4
            java.lang.Object r5 = kotlin.coroutines.jvm.internal.SpillingKt.nullOutSpilledVariable(r7)
            r0.L$0 = r5
            r5 = 1
            r0.label = r5
            java.lang.Object r3 = kotlinx.coroutines.BuildersKt.withContext(r3, r4, r0)
            if (r3 != r2) goto L57
            return r2
        L57:
            kotlin.Result r3 = (kotlin.Result) r3
            java.lang.Object r2 = r3.getValue()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.repository.OtaRepository.m6989fetchFromGitHubRawgIAlus(java.lang.String, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static /* synthetic */ Flow getReleasesForDevice$default(OtaRepository otaRepository, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = OtaConstants.DEVICE_MODEL_NAME;
        }
        return otaRepository.getReleasesForDevice(str);
    }

    public final Flow<List<OtaRelease>> getReleasesForDevice(String device) {
        Intrinsics.checkNotNullParameter(device, "device");
        return this.otaReleaseDao.getReleasesForDevice(device);
    }

    public static /* synthetic */ Flow getLatestRelease$default(OtaRepository otaRepository, String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            str = OtaConstants.DEVICE_MODEL_NAME;
        }
        if ((i & 2) != 0) {
            str2 = "Official";
        }
        return otaRepository.getLatestRelease(str, str2);
    }

    public final Flow<OtaRelease> getLatestRelease(String device, String channel) {
        Intrinsics.checkNotNullParameter(device, "device");
        Intrinsics.checkNotNullParameter(channel, "channel");
        return this.otaReleaseDao.getLatestRelease(device, channel);
    }

    public final Flow<List<OtaRelease>> getAllReleases() {
        return this.otaReleaseDao.getAllReleases();
    }

    public final Flow<List<UpdateHistoryItem>> getUpdateHistory() {
        return this.updateHistoryDao.getAllHistory();
    }

    public final Object publishRelease(OtaRelease release, Continuation<? super Unit> continuation) {
        Object withContext = BuildersKt.withContext(Dispatchers.getIO(), new OtaRepository$publishRelease$2(this, release, null), continuation);
        return withContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? withContext : Unit.INSTANCE;
    }

    public final Object deleteRelease(String id, Continuation<? super Unit> continuation) {
        Object withContext = BuildersKt.withContext(Dispatchers.getIO(), new OtaRepository$deleteRelease$2(this, id, null), continuation);
        return withContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? withContext : Unit.INSTANCE;
    }

    public final Object updateRelease(OtaRelease release, Continuation<? super Unit> continuation) {
        Object withContext = BuildersKt.withContext(Dispatchers.getIO(), new OtaRepository$updateRelease$2(this, release, null), continuation);
        return withContext == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? withContext : Unit.INSTANCE;
    }

    public final void setServerUrl(String url) {
        Intrinsics.checkNotNullParameter(url, "url");
        this._serverUrl.setValue(url);
    }

    public final void startDownload(OtaRelease release) {
        Job launch$default;
        Intrinsics.checkNotNullParameter(release, "release");
        Job job = this.downloadJob;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
        }
        this.isDownloadPaused = false;
        launch$default = BuildersKt__Builders_commonKt.launch$default(CoroutineScopeKt.CoroutineScope(Dispatchers.getIO()), null, null, new OtaRepository$startDownload$1(release, this, null), 3, null);
        this.downloadJob = launch$default;
    }

    public final void pauseDownload() {
        this.isDownloadPaused = true;
        Job job = this.downloadJob;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
        }
        this._downloadProgress.setValue(DownloadProgress.copy$default(this._downloadProgress.getValue(), DownloadStatus.PAUSED, 0.0f, 0L, 0L, 0L, 0L, "Download paused.", 0.0f, null, null, 0L, 1966, null));
    }

    public final void resumeDownload(OtaRelease release) {
        Intrinsics.checkNotNullParameter(release, "release");
        startDownload(release);
    }

    public final void cancelDownload() {
        this.isDownloadPaused = false;
        Job job = this.downloadJob;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
        }
        this._downloadProgress.setValue(new DownloadProgress(DownloadStatus.IDLE, 0.0f, 0L, 0L, 0L, 0L, "", 0.0f, null, OtaConstants.DEFAULT_TARGET_FILE_PATH, 0L, 1470, null));
    }

    public final void installUpdate(OtaRelease release) {
        Job launch$default;
        Intrinsics.checkNotNullParameter(release, "release");
        Job job = this.downloadJob;
        if (job != null) {
            Job.DefaultImpls.cancel$default(job, (CancellationException) null, 1, (Object) null);
        }
        launch$default = BuildersKt__Builders_commonKt.launch$default(CoroutineScopeKt.CoroutineScope(Dispatchers.getDefault()), null, null, new OtaRepository$installUpdate$1(this, release, null), 3, null);
        this.downloadJob = launch$default;
    }

    public final void resetUpdateState() {
        this._downloadProgress.setValue(new DownloadProgress(DownloadStatus.IDLE, 0.0f, 0L, 0L, 0L, 0L, null, 0.0f, null, OtaConstants.DEFAULT_TARGET_FILE_PATH, 0L, 1534, null));
    }
}
