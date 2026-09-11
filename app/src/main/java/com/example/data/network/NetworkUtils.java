package com.example.data.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Environment;
import com.example.data.model.OtaConstants;
import com.example.data.model.OtaRelease;
import com.google.android.gms.common.internal.ImagesContract;
import io.ktor.http.ContentDisposition;
import io.ktor.http.LinkHeader;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.OkHttpClient;
import org.json.JSONObject;

/* compiled from: NetworkUtils.kt */
@Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\bÇ\u0002\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0006\u0010\u000e\u001a\u00020\u000fJ&\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00130\u00120\u00112\b\b\u0002\u0010\u0014\u001a\u00020\u0015H\u0086@¢\u0006\u0004\b\u0016\u0010\u0017J\u001e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\u0006\u0010\u0019\u001a\u00020\u00152\b\b\u0002\u0010\u001a\u001a\u00020\u0015J$\u0010\u001b\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001a\u001a\u00020\u00152\b\b\u0002\u0010\u001e\u001a\u00020\u0015H\u0002J\u000e\u0010\u001f\u001a\u00020\u00152\u0006\u0010 \u001a\u00020\u000fR\u001b\u0010\u0004\u001a\u00020\u00058FX\u0086\u0084\u0002¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007¨\u0006!"}, d2 = {"Lcom/example/data/network/NetworkUtils;", "", "<init>", "()V", "okHttpClient", "Lokhttp3/OkHttpClient;", "getOkHttpClient", "()Lokhttp3/OkHttpClient;", "okHttpClient$delegate", "Lkotlin/Lazy;", "isNetworkAvailable", "", "context", "Landroid/content/Context;", "resolveTargetRomFile", "Ljava/io/File;", "fetchOtaManifest", "Lkotlin/Result;", "", "Lcom/example/data/model/OtaRelease;", "rawUrl", "", "fetchOtaManifest-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseOtaJson", "jsonString", "sourceUrl", "parseSingleReleaseObject", "obj", "Lorg/json/JSONObject;", "defaultChannel", "computeFileSha256", "file", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes5.dex */
public final class NetworkUtils {
    public static final NetworkUtils INSTANCE = new NetworkUtils();

    /* renamed from: okHttpClient$delegate, reason: from kotlin metadata */
    private static final Lazy okHttpClient = LazyKt.lazy(new Function0() { // from class: com.example.data.network.NetworkUtils$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            OkHttpClient build;
            build = new OkHttpClient.Builder().connectTimeout(20L, TimeUnit.SECONDS).readTimeout(60L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).followRedirects(true).followSslRedirects(true).build();
            return build;
        }
    });
    public static final int $stable = 8;

    private NetworkUtils() {
    }

    public final OkHttpClient getOkHttpClient() {
        return (OkHttpClient) okHttpClient.getValue();
    }

    public final boolean isNetworkAvailable(Context context) {
        NetworkCapabilities capabilities;
        Intrinsics.checkNotNullParameter(context, "context");
        Object systemService = context.getSystemService("connectivity");
        ConnectivityManager connectivityManager = systemService instanceof ConnectivityManager ? (ConnectivityManager) systemService : null;
        if (connectivityManager == null) {
            return true;
        }
        Network activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null || (capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)) == null) {
            return false;
        }
        return capabilities.hasCapability(12);
    }

    public final File resolveTargetRomFile() {
        File directPath = new File(OtaConstants.DEFAULT_TARGET_FILE_PATH);
        File parentDir = directPath.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        if (parentDir != null && parentDir.canWrite()) {
            return directPath;
        }
        File publicDownloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File otaDir = new File(publicDownloads, "OTA");
        if (!otaDir.exists()) {
            otaDir.mkdirs();
        }
        return new File(otaDir, OtaConstants.DEFAULT_TARGET_FILENAME);
    }

    /* renamed from: fetchOtaManifest-gIAlu-s$default, reason: not valid java name */
    public static /* synthetic */ Object m6986fetchOtaManifestgIAlus$default(NetworkUtils networkUtils, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = OtaConstants.DEFAULT_RAW_JSON_URL;
        }
        return networkUtils.m6987fetchOtaManifestgIAlus(str, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0024  */
    /* renamed from: fetchOtaManifest-gIAlu-s, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object m6987fetchOtaManifestgIAlus(java.lang.String r7, kotlin.coroutines.Continuation<? super kotlin.Result<? extends java.util.List<com.example.data.model.OtaRelease>>> r8) {
        /*
            r6 = this;
            boolean r0 = r8 instanceof com.example.data.network.NetworkUtils$fetchOtaManifest$1
            if (r0 == 0) goto L14
            r0 = r8
            com.example.data.network.NetworkUtils$fetchOtaManifest$1 r0 = (com.example.data.network.NetworkUtils$fetchOtaManifest$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r1 = r0.label
            int r1 = r1 - r2
            r0.label = r1
            goto L19
        L14:
            com.example.data.network.NetworkUtils$fetchOtaManifest$1 r0 = new com.example.data.network.NetworkUtils$fetchOtaManifest$1
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
            com.example.data.network.NetworkUtils$fetchOtaManifest$2 r4 = new com.example.data.network.NetworkUtils$fetchOtaManifest$2
            r5 = 0
            r4.<init>(r7, r5)
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
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.network.NetworkUtils.m6987fetchOtaManifestgIAlus(java.lang.String, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static /* synthetic */ List parseOtaJson$default(NetworkUtils networkUtils, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = OtaConstants.DEFAULT_RAW_JSON_URL;
        }
        return networkUtils.parseOtaJson(str, str2);
    }

    /* JADX WARN: Incorrect condition in loop: B:20:0x00c3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.util.List<com.example.data.model.OtaRelease> parseOtaJson(java.lang.String r18, java.lang.String r19) {
        /*
            Method dump skipped, instructions count: 422
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.data.network.NetworkUtils.parseOtaJson(java.lang.String, java.lang.String):java.util.List");
    }

    static /* synthetic */ OtaRelease parseSingleReleaseObject$default(NetworkUtils networkUtils, JSONObject jSONObject, String str, String str2, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = "Stable";
        }
        return networkUtils.parseSingleReleaseObject(jSONObject, str, str2);
    }

    private final OtaRelease parseSingleReleaseObject(JSONObject obj, String sourceUrl, String defaultChannel) {
        String str;
        String rawVer;
        int i;
        int i2;
        long packageSizeBytes;
        String downloadUrl;
        String checksumSha256;
        String changelog;
        try {
            if (obj.has("version_name")) {
                rawVer = obj.optString("version_name");
                str = "checksum_sha256";
            } else if (obj.has("version")) {
                rawVer = obj.optString("version");
                Intrinsics.checkNotNull(rawVer);
                str = "checksum_sha256";
                if (!StringsKt.startsWith(rawVer, "Power", true)) {
                    rawVer = "Power OS v" + rawVer;
                }
            } else {
                str = "checksum_sha256";
                rawVer = obj.has("name") ? obj.optString("name") : "Power OS System Update";
            }
            String versionName = rawVer;
            if (obj.has("versionCode")) {
                i = obj.optInt("versionCode");
            } else if (obj.has("version_code")) {
                i = obj.optInt("version_code");
            } else if (obj.has("version")) {
                String optString = obj.optString("version");
                Intrinsics.checkNotNullExpressionValue(optString, "optString(...)");
                CharSequence replace$default = StringsKt.replace$default(optString, ".", "", false, 4, (Object) null);
                Appendable sb = new StringBuilder();
                int length = replace$default.length();
                int i3 = 0;
                while (i3 < length) {
                    char charAt = replace$default.charAt(i3);
                    if (Character.isDigit(charAt)) {
                        i2 = i3;
                        sb.append(charAt);
                    } else {
                        i2 = i3;
                    }
                    i3 = i2 + 1;
                }
                String digits = ((StringBuilder) sb).toString();
                Integer intOrNull = StringsKt.toIntOrNull(digits);
                i = intOrNull != null ? intOrNull.intValue() : 1000;
            } else {
                i = 1000;
            }
            int versionCode = i;
            String buildNumber = obj.optString("build_number", obj.optString("build", "POS-" + versionCode + "-STABLE-OppoA6X"));
            String deviceModel = obj.optString("device_model", obj.optString("device", OtaConstants.DEVICE_MODEL_NAME));
            String releaseChannel = obj.optString("release_channel", obj.optString("channel", Intrinsics.areEqual(obj.optString("romtype"), "beta") ? "Beta" : defaultChannel));
            String releaseType = obj.optString("release_type", obj.optString(LinkHeader.Parameters.Type, "Full OTA Package"));
            if (obj.has("package_size_bytes")) {
                packageSizeBytes = obj.optLong("package_size_bytes");
            } else {
                packageSizeBytes = obj.has(ContentDisposition.Parameters.Size) ? obj.optLong(ContentDisposition.Parameters.Size) : obj.has("filesize") ? obj.optLong("filesize") : obj.has("packageSize") ? obj.optLong("packageSize") : 0L;
            }
            if (obj.has("zipUrl")) {
                downloadUrl = obj.optString("zipUrl");
            } else {
                downloadUrl = obj.has("download_url") ? obj.optString("download_url") : obj.has(ImagesContract.URL) ? obj.optString(ImagesContract.URL) : obj.has("downloadUrl") ? obj.optString("downloadUrl") : "";
            }
            Intrinsics.checkNotNull(downloadUrl);
            if (StringsKt.isBlank(downloadUrl)) {
                return null;
            }
            String str2 = str;
            if (obj.has(str2)) {
                checksumSha256 = obj.optString(str2);
            } else {
                checksumSha256 = obj.has("sha256") ? obj.optString("sha256") : obj.has("checksum") ? obj.optString("checksum") : obj.has("md5") ? obj.optString("md5") : "";
            }
            if (obj.has("changelog")) {
                changelog = obj.optString("changelog");
            } else {
                changelog = obj.has("notes") ? obj.optString("notes") : obj.has("description") ? obj.optString("description") : "System update for Oppo A6X with performance and stability improvements.";
            }
            String securityPatch = obj.optString("security_patch", obj.optString("securityPatch", "2026-08-05"));
            long releaseDate = obj.optLong("datetime", obj.optLong("release_date", System.currentTimeMillis()));
            String id = obj.optString("id", "oppoa6x_v" + versionCode);
            Intrinsics.checkNotNull(id);
            Intrinsics.checkNotNull(deviceModel);
            String str3 = !StringsKt.isBlank(deviceModel) ? deviceModel : OtaConstants.DEVICE_MODEL_NAME;
            Intrinsics.checkNotNull(versionName);
            Intrinsics.checkNotNull(buildNumber);
            Intrinsics.checkNotNull(releaseChannel);
            Intrinsics.checkNotNull(releaseType);
            Intrinsics.checkNotNull(checksumSha256);
            Intrinsics.checkNotNull(securityPatch);
            Intrinsics.checkNotNull(changelog);
            return new OtaRelease(id, str3, OtaConstants.DEVICE_CODENAME, versionName, versionCode, buildNumber, releaseChannel, releaseType, packageSizeBytes, downloadUrl, checksumSha256, "Android 15", securityPatch, changelog, releaseDate, obj.optBoolean("is_mandatory", obj.optBoolean("isMandatory", false)), obj.optInt("min_required_version", 0), "PUBLISHED", 100, sourceUrl, OtaConstants.DEFAULT_TARGET_FILE_PATH);
        } catch (Exception e) {
            return null;
        }
    }

    public final String computeFileSha256(File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                FileInputStream fileInputStream2 = fileInputStream;
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = fileInputStream2.read(bArr);
                    if (read <= 0) {
                        Unit unit = Unit.INSTANCE;
                        CloseableKt.closeFinally(fileInputStream, null);
                        byte[] digest2 = digest.digest();
                        Intrinsics.checkNotNullExpressionValue(digest2, "digest(...)");
                        return ArraysKt.joinToString$default(digest2, (CharSequence) "", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, new Function1() { // from class: com.example.data.network.NetworkUtils$$ExternalSyntheticLambda1
                            @Override // kotlin.jvm.functions.Function1
                            public final Object invoke(Object obj) {
                                return NetworkUtils.computeFileSha256$lambda$10(((Byte) obj).byteValue());
                            }
                        }, 30, (Object) null);
                    }
                    digest.update(bArr, 0, read);
                }
            } finally {
            }
        } catch (Exception e) {
            return "";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final CharSequence computeFileSha256$lambda$10(byte it) {
        String format = String.format("%02x", Arrays.copyOf(new Object[]{Byte.valueOf(it)}, 1));
        Intrinsics.checkNotNullExpressionValue(format, "format(...)");
        return format;
    }
}
