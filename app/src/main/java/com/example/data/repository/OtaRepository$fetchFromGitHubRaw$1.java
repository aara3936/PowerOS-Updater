package com.example.data.repository;

import com.google.android.gms.common.internal.ImagesContract;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: OtaRepository.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.repository.OtaRepository", f = "OtaRepository.kt", i = {0}, l = {80}, m = "fetchFromGitHubRaw-gIAlu-s", n = {ImagesContract.URL}, s = {"L$0"})
/* loaded from: classes8.dex */
public final class OtaRepository$fetchFromGitHubRaw$1 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ OtaRepository this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public OtaRepository$fetchFromGitHubRaw$1(OtaRepository otaRepository, Continuation<? super OtaRepository$fetchFromGitHubRaw$1> continuation) {
        super(continuation);
        this.this$0 = otaRepository;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        Object m6989fetchFromGitHubRawgIAlus = this.this$0.m6989fetchFromGitHubRawgIAlus(null, this);
        return m6989fetchFromGitHubRawgIAlus == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? m6989fetchFromGitHubRawgIAlus : Result.m7132boximpl(m6989fetchFromGitHubRawgIAlus);
    }
}
