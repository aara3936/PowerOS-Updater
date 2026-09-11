package com.example.data.repository;

import com.example.data.model.OtaRelease;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: OtaRepository.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.repository.OtaRepository$startDownload$1", f = "OtaRepository.kt", i = {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1}, l = {235, 275}, m = "invokeSuspend", n = {"targetFile", "downloaded", "out", "totalBytes", "realDownloadSucceeded", "chunkSize", "updateInterval", "targetFile", "downloaded", "totalBytes", "realDownloadSucceeded"}, s = {"L$0", "L$1", "L$2", "J$0", "I$0", "J$1", "J$2", "L$0", "L$1", "J$0", "I$0"})
/* loaded from: classes8.dex */
public final class OtaRepository$startDownload$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ OtaRelease $release;
    int I$0;
    long J$0;
    long J$1;
    long J$2;
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    final /* synthetic */ OtaRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OtaRepository$startDownload$1(OtaRelease otaRelease, OtaRepository otaRepository, Continuation<? super OtaRepository$startDownload$1> continuation) {
        super(2, continuation);
        this.$release = otaRelease;
        this.this$0 = otaRepository;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new OtaRepository$startDownload$1(this.$release, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((OtaRepository$startDownload$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:136:0x02ee, code lost:
    
        if (r47.length() > 0) goto L84;
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000f. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:106:0x0326  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0394  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0367  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0444  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x038a -> B:11:0x038c). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r61) {
        /*
            Method dump skipped, instructions count: 1334
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.repository.OtaRepository$startDownload$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
