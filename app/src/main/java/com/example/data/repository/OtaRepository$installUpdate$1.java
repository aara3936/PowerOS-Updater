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
@DebugMetadata(c = "com.example.data.repository.OtaRepository$installUpdate$1", f = "OtaRepository.kt", i = {}, l = {325, 331, 337, 343, 349, 361}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes8.dex */
public final class OtaRepository$installUpdate$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ OtaRelease $release;
    int label;
    final /* synthetic */ OtaRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OtaRepository$installUpdate$1(OtaRepository otaRepository, OtaRelease otaRelease, Continuation<? super OtaRepository$installUpdate$1> continuation) {
        super(2, continuation);
        this.this$0 = otaRepository;
        this.$release = otaRelease;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new OtaRepository$installUpdate$1(this.this$0, this.$release, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((OtaRepository$installUpdate$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x000c. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0261  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x02ad A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0266  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0183 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0141 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00fe A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00bd A[RETURN] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r29) {
        /*
            Method dump skipped, instructions count: 782
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.repository.OtaRepository$installUpdate$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
