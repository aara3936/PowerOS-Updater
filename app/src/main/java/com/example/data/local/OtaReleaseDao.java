package com.example.data.local;

import com.example.data.model.OtaConstants;
import com.example.data.model.OtaRelease;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

/* compiled from: OtaDaos.kt */
@Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u000b\bg\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H'J$\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\t\u001a\u00020\u0007H'J\u0018\u0010\n\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000b\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\u0011J\u001c\u0010\u0012\u001a\u00020\u000f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H§@¢\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\u0011J\u0016\u0010\u0016\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\u0011J\u0016\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\fJ\u000e\u0010\u0018\u001a\u00020\u000fH§@¢\u0006\u0002\u0010\u0019¨\u0006\u001aÀ\u0006\u0003"}, d2 = {"Lcom/example/data/local/OtaReleaseDao;", "", "getReleasesForDevice", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/data/model/OtaRelease;", "device", "", "getLatestRelease", "channel", "getReleaseById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllReleases", "insertRelease", "", "release", "(Lcom/example/data/model/OtaRelease;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertReleases", "releases", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateRelease", "deleteRelease", "deleteReleaseById", "clearAllReleases", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes7.dex */
public interface OtaReleaseDao {
    Object clearAllReleases(Continuation<? super Unit> continuation);

    Object deleteRelease(OtaRelease otaRelease, Continuation<? super Unit> continuation);

    Object deleteReleaseById(String str, Continuation<? super Unit> continuation);

    Flow<List<OtaRelease>> getAllReleases();

    Flow<OtaRelease> getLatestRelease(String device, String channel);

    Object getReleaseById(String str, Continuation<? super OtaRelease> continuation);

    Flow<List<OtaRelease>> getReleasesForDevice(String device);

    Object insertRelease(OtaRelease otaRelease, Continuation<? super Unit> continuation);

    Object insertReleases(List<OtaRelease> list, Continuation<? super Unit> continuation);

    Object updateRelease(OtaRelease otaRelease, Continuation<? super Unit> continuation);

    /* compiled from: OtaDaos.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes7.dex */
    public static final class DefaultImpls {
    }

    static /* synthetic */ Flow getReleasesForDevice$default(OtaReleaseDao otaReleaseDao, String str, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getReleasesForDevice");
        }
        if ((i & 1) != 0) {
            str = OtaConstants.DEVICE_MODEL_NAME;
        }
        return otaReleaseDao.getReleasesForDevice(str);
    }

    static /* synthetic */ Flow getLatestRelease$default(OtaReleaseDao otaReleaseDao, String str, String str2, int i, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: getLatestRelease");
        }
        if ((i & 1) != 0) {
            str = OtaConstants.DEVICE_MODEL_NAME;
        }
        if ((i & 2) != 0) {
            str2 = "Stable";
        }
        return otaReleaseDao.getLatestRelease(str, str2);
    }
}
