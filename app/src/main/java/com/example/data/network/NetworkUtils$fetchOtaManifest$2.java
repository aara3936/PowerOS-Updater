package com.example.data.network;

import com.example.data.model.OtaConstants;
import com.example.data.model.OtaRelease;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CoroutineScope;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NetworkUtils.kt */
@Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00030\u00020\u0001*\u00020\u0004H\n"}, d2 = {"<anonymous>", "Lkotlin/Result;", "", "Lcom/example/data/model/OtaRelease;", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.data.network.NetworkUtils$fetchOtaManifest$2", f = "NetworkUtils.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
/* loaded from: classes5.dex */
public final class NetworkUtils$fetchOtaManifest$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Result<? extends List<? extends OtaRelease>>>, Object> {
    final /* synthetic */ String $rawUrl;
    int label;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NetworkUtils$fetchOtaManifest$2(String str, Continuation<? super NetworkUtils$fetchOtaManifest$2> continuation) {
        super(2, continuation);
        this.$rawUrl = str;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new NetworkUtils$fetchOtaManifest$2(this.$rawUrl, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super Result<? extends List<? extends OtaRelease>>> continuation) {
        return invoke2(coroutineScope, (Continuation<? super Result<? extends List<OtaRelease>>>) continuation);
    }

    /* renamed from: invoke, reason: avoid collision after fix types in other method */
    public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super Result<? extends List<OtaRelease>>> continuation) {
        return ((NetworkUtils$fetchOtaManifest$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.Iterator] */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v13, types: [kotlin.Result, java.lang.Object] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        List urlsToTry;
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                if (!Intrinsics.areEqual(this.$rawUrl, OtaConstants.DEFAULT_FALLBACK_JSON_URL)) {
                    urlsToTry = CollectionsKt.listOf((Object[]) new String[]{this.$rawUrl, OtaConstants.DEFAULT_FALLBACK_JSON_URL});
                } else {
                    urlsToTry = CollectionsKt.listOf(this.$rawUrl);
                }
                Exception lastError = null;
                Iterator it = urlsToTry.iterator();
                while (it.hasNext()) {
                    String currentUrl = (String) it.next();
                    try {
                        Request request = new Request.Builder().url(currentUrl).addHeader("User-Agent", "PowerOS-OppoA6X-OTA/2.0").addHeader("Accept", "application/json, text/plain, */*").build();
                        Response response = NetworkUtils.INSTANCE.getOkHttpClient().newCall(request).execute();
                        if (!response.isSuccessful()) {
                            int code = response.code();
                            response.close();
                            lastError = new IllegalStateException("GitHub OTA server returned HTTP " + code + " for " + currentUrl);
                        } else {
                            ResponseBody body = response.body();
                            String bodyString = body != null ? body.string() : null;
                            if (bodyString == null) {
                                bodyString = "";
                            }
                            response.close();
                            if (StringsKt.isBlank(bodyString)) {
                                lastError = new IllegalStateException("Empty response from " + currentUrl);
                            } else {
                                List parsedReleases = NetworkUtils.INSTANCE.parseOtaJson(bodyString, currentUrl);
                                if (!parsedReleases.isEmpty()) {
                                    Result.Companion companion = Result.INSTANCE;
                                    it = Result.m7132boximpl(Result.m7133constructorimpl(parsedReleases));
                                    return it;
                                }
                                lastError = new IllegalStateException("No valid releases found in " + currentUrl);
                            }
                        }
                    } catch (Exception e) {
                        lastError = e;
                    }
                }
                Result.Companion companion2 = Result.INSTANCE;
                return Result.m7132boximpl(Result.m7133constructorimpl(ResultKt.createFailure(lastError == null ? new IllegalStateException("Unable to fetch manifest") : lastError)));
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
