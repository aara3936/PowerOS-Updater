package com.example.data.repository;

import com.example.data.model.OtaRelease;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: OtaRepository.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001*\u00020\u0004H\n"}, d2 = {"<anonymous>", "Lkotlin/Result;", "", "Lcom/example/data/model/OtaRelease;", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.repository.OtaRepository$fetchFromGitHubRaw$2", f = "OtaRepository.kt", i = {1, 1, 2, 2}, l = {81, 85, 86}, m = "invokeSuspend", n = {"result", "releases", "result", "releases"}, s = {"L$0", "L$1", "L$0", "L$1"})
/* loaded from: classes8.dex */
public final class OtaRepository$fetchFromGitHubRaw$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Result<? extends List<? extends OtaRelease>>>, Object> {
    final /* synthetic */ String $url;
    Object L$0;
    Object L$1;
    int label;
    final /* synthetic */ OtaRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OtaRepository$fetchFromGitHubRaw$2(String str, OtaRepository otaRepository, Continuation<? super OtaRepository$fetchFromGitHubRaw$2> continuation) {
        super(2, continuation);
        this.$url = str;
        this.this$0 = otaRepository;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new OtaRepository$fetchFromGitHubRaw$2(this.$url, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Result<? extends List<? extends OtaRelease>>> continuation) {
        return invoke2(coroutineScope, (Continuation<? super Result<? extends List<OtaRelease>>>) continuation);
    }

    /* renamed from: invoke, reason: avoid collision after fix types in other method */
    public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Result<? extends List<OtaRelease>>> continuation) {
        return ((OtaRepository$fetchFromGitHubRaw$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:12:0x009a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x004c  */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r8) {
        /*
            r7 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r7.label
            switch(r1) {
                case 0: goto L31;
                case 1: goto L26;
                case 2: goto L1c;
                case 3: goto L11;
                default: goto L9;
            }
        L9:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "call to 'resume' before 'invoke' with coroutine"
            r0.<init>(r1)
            throw r0
        L11:
            java.lang.Object r0 = r7.L$1
            java.util.List r0 = (java.util.List) r0
            java.lang.Object r1 = r7.L$0
            kotlin.ResultKt.throwOnFailure(r8)
            goto L9d
        L1c:
            java.lang.Object r1 = r7.L$1
            java.util.List r1 = (java.util.List) r1
            java.lang.Object r2 = r7.L$0
            kotlin.ResultKt.throwOnFailure(r8)
            goto L80
        L26:
            kotlin.ResultKt.throwOnFailure(r8)
            r1 = r8
            kotlin.Result r1 = (kotlin.Result) r1
            java.lang.Object r1 = r1.getValue()
            goto L45
        L31:
            kotlin.ResultKt.throwOnFailure(r8)
            com.example.data.network.NetworkUtils r1 = com.example.data.network.NetworkUtils.INSTANCE
            java.lang.String r2 = r7.$url
            r3 = r7
            kotlin.coroutines.Continuation r3 = (kotlin.coroutines.Continuation) r3
            r4 = 1
            r7.label = r4
            java.lang.Object r1 = r1.m6987fetchOtaManifestgIAlus(r2, r3)
            if (r1 != r0) goto L45
            return r0
        L45:
            boolean r2 = kotlin.Result.m7140isSuccessimpl(r1)
            if (r2 == 0) goto L9e
            boolean r2 = kotlin.Result.m7139isFailureimpl(r1)
            if (r2 == 0) goto L54
            r2 = 0
            goto L55
        L54:
            r2 = r1
        L55:
            java.util.List r2 = (java.util.List) r2
            if (r2 != 0) goto L5d
            java.util.List r2 = kotlin.collections.CollectionsKt.emptyList()
        L5d:
            r3 = r2
            java.util.Collection r3 = (java.util.Collection) r3
            boolean r3 = r3.isEmpty()
            if (r3 != 0) goto L9e
            com.example.data.repository.OtaRepository r3 = r7.this$0
            com.example.data.local.OtaReleaseDao r3 = com.example.data.repository.OtaRepository.access$getOtaReleaseDao$p(r3)
            r4 = r7
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
            r7.L$0 = r1
            r7.L$1 = r2
            r5 = 2
            r7.label = r5
            java.lang.Object r3 = r3.clearAllReleases(r4)
            if (r3 != r0) goto L7d
            return r0
        L7d:
            r6 = r2
            r2 = r1
            r1 = r6
        L80:
            com.example.data.repository.OtaRepository r3 = r7.this$0
            com.example.data.local.OtaReleaseDao r3 = com.example.data.repository.OtaRepository.access$getOtaReleaseDao$p(r3)
            r4 = r7
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
            r7.L$0 = r2
            java.lang.Object r5 = kotlin.coroutines.jvm.internal.SpillingKt.nullOutSpilledVariable(r1)
            r7.L$1 = r5
            r5 = 3
            r7.label = r5
            java.lang.Object r3 = r3.insertReleases(r1, r4)
            if (r3 != r0) goto L9b
            return r0
        L9b:
            r0 = r1
            r1 = r2
        L9d:
        L9e:
            kotlin.Result r0 = kotlin.Result.m7132boximpl(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.repository.OtaRepository$fetchFromGitHubRaw$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
