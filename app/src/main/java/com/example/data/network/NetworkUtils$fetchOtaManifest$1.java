package com.example.data.network;

import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NetworkUtils.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.network.NetworkUtils", f = "NetworkUtils.kt", i = {0}, l = {79}, m = "fetchOtaManifest-gIAlu-s", n = {"rawUrl"}, s = {"L$0"})
/* loaded from: classes5.dex */
public final class NetworkUtils$fetchOtaManifest$1 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ NetworkUtils this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NetworkUtils$fetchOtaManifest$1(NetworkUtils networkUtils, Continuation<? super NetworkUtils$fetchOtaManifest$1> continuation) {
        super(continuation);
        this.this$0 = networkUtils;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        Object m6987fetchOtaManifestgIAlus = this.this$0.m6987fetchOtaManifestgIAlus(null, this);
        return m6987fetchOtaManifestgIAlus == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? m6987fetchOtaManifestgIAlus : Result.m7132boximpl(m6987fetchOtaManifestgIAlus);
    }
}
