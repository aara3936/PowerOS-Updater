package com.example.data.local;

import androidx.core.app.NotificationCompat;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import com.example.data.model.OtaRelease;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KClass;
import kotlinx.coroutines.flow.Flow;

/* compiled from: OtaReleaseDao_Impl.kt */
@Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000b\b\u0007\u0018\u0000 #2\u00020\u0001:\u0001#B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000fJ\u001c\u0010\u0010\u001a\u00020\r2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u0012H\u0096@¢\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000fJ\u0016\u0010\u0015\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\u000fJ\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J \u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\b0\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u0019H\u0016J\u0018\u0010\u001c\u001a\u0004\u0018\u00010\b2\u0006\u0010\u001d\u001a\u00020\u0019H\u0096@¢\u0006\u0002\u0010\u001eJ\u0014\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00120\u0017H\u0016J\u0016\u0010 \u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u0019H\u0096@¢\u0006\u0002\u0010\u001eJ\u000e\u0010!\u001a\u00020\rH\u0096@¢\u0006\u0002\u0010\"R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\nX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lcom/example/data/local/OtaReleaseDao_Impl;", "Lcom/example/data/local/OtaReleaseDao;", "__db", "Landroidx/room/RoomDatabase;", "<init>", "(Landroidx/room/RoomDatabase;)V", "__insertAdapterOfOtaRelease", "Landroidx/room/EntityInsertAdapter;", "Lcom/example/data/model/OtaRelease;", "__deleteAdapterOfOtaRelease", "Landroidx/room/EntityDeleteOrUpdateAdapter;", "__updateAdapterOfOtaRelease", "insertRelease", "", "release", "(Lcom/example/data/model/OtaRelease;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertReleases", "releases", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRelease", "updateRelease", "getReleasesForDevice", "Lkotlinx/coroutines/flow/Flow;", "device", "", "getLatestRelease", "channel", "getReleaseById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllReleases", "deleteReleaseById", "clearAllReleases", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes7.dex */
public final class OtaReleaseDao_Impl implements OtaReleaseDao {
    private final RoomDatabase __db;
    private final EntityDeleteOrUpdateAdapter<OtaRelease> __deleteAdapterOfOtaRelease;
    private final EntityInsertAdapter<OtaRelease> __insertAdapterOfOtaRelease;
    private final EntityDeleteOrUpdateAdapter<OtaRelease> __updateAdapterOfOtaRelease;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public OtaReleaseDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfOtaRelease = new EntityInsertAdapter<OtaRelease>() { // from class: com.example.data.local.OtaReleaseDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `ota_releases` (`id`,`deviceModel`,`deviceCodename`,`versionName`,`versionCode`,`buildNumber`,`releaseChannel`,`releaseType`,`packageSizeBytes`,`downloadUrl`,`checksumSha256`,`androidVersion`,`securityPatch`,`changelog`,`releaseDate`,`isMandatory`,`minRequiredVersion`,`status`,`rolloutPercentage`,`sourceUrl`,`targetLocalPath`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, OtaRelease entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo6930bindText(1, entity.getId());
                statement.mo6930bindText(2, entity.getDeviceModel());
                statement.mo6930bindText(3, entity.getDeviceCodename());
                statement.mo6930bindText(4, entity.getVersionName());
                statement.mo6928bindLong(5, entity.getVersionCode());
                statement.mo6930bindText(6, entity.getBuildNumber());
                statement.mo6930bindText(7, entity.getReleaseChannel());
                statement.mo6930bindText(8, entity.getReleaseType());
                statement.mo6928bindLong(9, entity.getPackageSizeBytes());
                statement.mo6930bindText(10, entity.getDownloadUrl());
                statement.mo6930bindText(11, entity.getChecksumSha256());
                statement.mo6930bindText(12, entity.getAndroidVersion());
                statement.mo6930bindText(13, entity.getSecurityPatch());
                statement.mo6930bindText(14, entity.getChangelog());
                statement.mo6928bindLong(15, entity.getReleaseDate());
                statement.mo6928bindLong(16, entity.isMandatory() ? 1L : 0L);
                statement.mo6928bindLong(17, entity.getMinRequiredVersion());
                statement.mo6930bindText(18, entity.getStatus());
                statement.mo6928bindLong(19, entity.getRolloutPercentage());
                statement.mo6930bindText(20, entity.getSourceUrl());
                statement.mo6930bindText(21, entity.getTargetLocalPath());
            }
        };
        this.__deleteAdapterOfOtaRelease = new EntityDeleteOrUpdateAdapter<OtaRelease>() { // from class: com.example.data.local.OtaReleaseDao_Impl.2
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            protected String createQuery() {
                return "DELETE FROM `ota_releases` WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            public void bind(SQLiteStatement statement, OtaRelease entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo6930bindText(1, entity.getId());
            }
        };
        this.__updateAdapterOfOtaRelease = new EntityDeleteOrUpdateAdapter<OtaRelease>() { // from class: com.example.data.local.OtaReleaseDao_Impl.3
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            protected String createQuery() {
                return "UPDATE OR ABORT `ota_releases` SET `id` = ?,`deviceModel` = ?,`deviceCodename` = ?,`versionName` = ?,`versionCode` = ?,`buildNumber` = ?,`releaseChannel` = ?,`releaseType` = ?,`packageSizeBytes` = ?,`downloadUrl` = ?,`checksumSha256` = ?,`androidVersion` = ?,`securityPatch` = ?,`changelog` = ?,`releaseDate` = ?,`isMandatory` = ?,`minRequiredVersion` = ?,`status` = ?,`rolloutPercentage` = ?,`sourceUrl` = ?,`targetLocalPath` = ? WHERE `id` = ?";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityDeleteOrUpdateAdapter
            public void bind(SQLiteStatement statement, OtaRelease entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo6930bindText(1, entity.getId());
                statement.mo6930bindText(2, entity.getDeviceModel());
                statement.mo6930bindText(3, entity.getDeviceCodename());
                statement.mo6930bindText(4, entity.getVersionName());
                statement.mo6928bindLong(5, entity.getVersionCode());
                statement.mo6930bindText(6, entity.getBuildNumber());
                statement.mo6930bindText(7, entity.getReleaseChannel());
                statement.mo6930bindText(8, entity.getReleaseType());
                statement.mo6928bindLong(9, entity.getPackageSizeBytes());
                statement.mo6930bindText(10, entity.getDownloadUrl());
                statement.mo6930bindText(11, entity.getChecksumSha256());
                statement.mo6930bindText(12, entity.getAndroidVersion());
                statement.mo6930bindText(13, entity.getSecurityPatch());
                statement.mo6930bindText(14, entity.getChangelog());
                statement.mo6928bindLong(15, entity.getReleaseDate());
                statement.mo6928bindLong(16, entity.isMandatory() ? 1L : 0L);
                statement.mo6928bindLong(17, entity.getMinRequiredVersion());
                statement.mo6930bindText(18, entity.getStatus());
                statement.mo6928bindLong(19, entity.getRolloutPercentage());
                statement.mo6930bindText(20, entity.getSourceUrl());
                statement.mo6930bindText(21, entity.getTargetLocalPath());
                statement.mo6930bindText(22, entity.getId());
            }
        };
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Object insertRelease(final OtaRelease release, Continuation<? super Unit> continuation) {
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.insertRelease$lambda$0(OtaReleaseDao_Impl.this, release, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit insertRelease$lambda$0(OtaReleaseDao_Impl this$0, OtaRelease $release, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__insertAdapterOfOtaRelease.insert(_connection, (SQLiteConnection) $release);
        return Unit.INSTANCE;
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Object insertReleases(final List<OtaRelease> list, Continuation<? super Unit> continuation) {
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda7
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.insertReleases$lambda$1(OtaReleaseDao_Impl.this, list, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit insertReleases$lambda$1(OtaReleaseDao_Impl this$0, List $releases, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__insertAdapterOfOtaRelease.insert(_connection, $releases);
        return Unit.INSTANCE;
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Object deleteRelease(final OtaRelease release, Continuation<? super Unit> continuation) {
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.deleteRelease$lambda$2(OtaReleaseDao_Impl.this, release, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit deleteRelease$lambda$2(OtaReleaseDao_Impl this$0, OtaRelease $release, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__deleteAdapterOfOtaRelease.handle(_connection, $release);
        return Unit.INSTANCE;
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Object updateRelease(final OtaRelease release, Continuation<? super Unit> continuation) {
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda8
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.updateRelease$lambda$3(OtaReleaseDao_Impl.this, release, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit updateRelease$lambda$3(OtaReleaseDao_Impl this$0, OtaRelease $release, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__updateAdapterOfOtaRelease.handle(_connection, $release);
        return Unit.INSTANCE;
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Flow<List<OtaRelease>> getReleasesForDevice(final String device) {
        Intrinsics.checkNotNullParameter(device, "device");
        final String _sql = "SELECT * FROM ota_releases WHERE (deviceModel = ? OR deviceModel = 'Oppo A6X' OR deviceModel = '') ORDER BY versionCode DESC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"ota_releases"}, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.getReleasesForDevice$lambda$4(_sql, device, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final List getReleasesForDevice$lambda$4(String $_sql, String $device, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo6930bindText(1, $device);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfReleaseDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceModel");
            int _columnIndexOfDeviceCodename = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceCodename");
            int _columnIndexOfVersionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionName");
            int _columnIndexOfVersionCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionCode");
            int _columnIndexOfBuildNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "buildNumber");
            int _columnIndexOfReleaseChannel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseChannel");
            int _columnIndexOfReleaseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseType");
            int _columnIndexOfPackageSizeBytes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "packageSizeBytes");
            int _columnIndexOfDownloadUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "downloadUrl");
            int _columnIndexOfChecksumSha256 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "checksumSha256");
            int _columnIndexOfSourceUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "androidVersion");
            int _columnIndexOfSecurityPatch = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "securityPatch");
            int _columnIndexOfChangelog = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "changelog");
            int _columnIndexOfReleaseDate2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseDate");
            int _columnIndexOfIsMandatory = _columnIndexOfReleaseDate2;
            int _columnIndexOfIsMandatory2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isMandatory");
            int _columnIndexOfSecurityPatch2 = _columnIndexOfIsMandatory2;
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minRequiredVersion");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfStatus2 = _columnIndexOfStatus;
            int _columnIndexOfRolloutPercentage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rolloutPercentage");
            int _columnIndexOfRolloutPercentage2 = _columnIndexOfRolloutPercentage;
            int _columnIndexOfSourceUrl2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceUrl");
            int _columnIndexOfSourceUrl3 = _columnIndexOfSourceUrl2;
            int _columnIndexOfTargetLocalPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetLocalPath");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpDeviceModel = _stmt.getText(_columnIndexOfReleaseDate);
                String _tmpDeviceCodename = _stmt.getText(_columnIndexOfDeviceCodename);
                String _tmpVersionName = _stmt.getText(_columnIndexOfVersionName);
                int _columnIndexOfId2 = _columnIndexOfId;
                int _columnIndexOfDeviceModel = _columnIndexOfReleaseDate;
                int _tmpVersionCode = (int) _stmt.getLong(_columnIndexOfVersionCode);
                String _tmpBuildNumber = _stmt.getText(_columnIndexOfBuildNumber);
                String _tmpReleaseChannel = _stmt.getText(_columnIndexOfReleaseChannel);
                String _tmpReleaseType = _stmt.getText(_columnIndexOfReleaseType);
                long _tmpPackageSizeBytes = _stmt.getLong(_columnIndexOfPackageSizeBytes);
                String _tmpDownloadUrl = _stmt.getText(_columnIndexOfDownloadUrl);
                String _tmpChecksumSha256 = _stmt.getText(_columnIndexOfChecksumSha256);
                String _tmpAndroidVersion = _stmt.getText(_columnIndexOfSourceUrl);
                String _tmpSecurityPatch = _stmt.getText(_columnIndexOfSecurityPatch);
                String _tmpChangelog = _stmt.getText(_columnIndexOfChangelog);
                int _columnIndexOfReleaseDate3 = _columnIndexOfIsMandatory;
                long _tmpReleaseDate = _stmt.getLong(_columnIndexOfReleaseDate3);
                int _columnIndexOfAndroidVersion = _columnIndexOfSourceUrl;
                int _columnIndexOfChangelog2 = _columnIndexOfChangelog;
                int _columnIndexOfAndroidVersion2 = _columnIndexOfSecurityPatch2;
                int _columnIndexOfIsMandatory3 = _columnIndexOfSecurityPatch;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfAndroidVersion2);
                boolean _tmpIsMandatory = _tmp2 != 0;
                int _columnIndexOfIsMandatory4 = _tmp;
                int _tmpMinRequiredVersion = (int) _stmt.getLong(_columnIndexOfIsMandatory4);
                int _columnIndexOfMinRequiredVersion = _columnIndexOfStatus2;
                String _tmpStatus = _stmt.getText(_columnIndexOfMinRequiredVersion);
                _columnIndexOfStatus2 = _columnIndexOfMinRequiredVersion;
                int _columnIndexOfStatus3 = _columnIndexOfRolloutPercentage2;
                int _tmpRolloutPercentage = (int) _stmt.getLong(_columnIndexOfStatus3);
                _columnIndexOfRolloutPercentage2 = _columnIndexOfStatus3;
                int _columnIndexOfRolloutPercentage3 = _columnIndexOfSourceUrl3;
                String _tmpSourceUrl = _stmt.getText(_columnIndexOfRolloutPercentage3);
                String _tmpTargetLocalPath = _stmt.getText(_columnIndexOfTargetLocalPath);
                OtaRelease _item = new OtaRelease(_tmpId, _tmpDeviceModel, _tmpDeviceCodename, _tmpVersionName, _tmpVersionCode, _tmpBuildNumber, _tmpReleaseChannel, _tmpReleaseType, _tmpPackageSizeBytes, _tmpDownloadUrl, _tmpChecksumSha256, _tmpAndroidVersion, _tmpSecurityPatch, _tmpChangelog, _tmpReleaseDate, _tmpIsMandatory, _tmpMinRequiredVersion, _tmpStatus, _tmpRolloutPercentage, _tmpSourceUrl, _tmpTargetLocalPath);
                List _result2 = _result;
                _result2.add(_item);
                _columnIndexOfSourceUrl3 = _columnIndexOfRolloutPercentage3;
                _result = _result2;
                _columnIndexOfSecurityPatch = _columnIndexOfIsMandatory3;
                _tmp = _columnIndexOfIsMandatory4;
                _columnIndexOfId = _columnIndexOfId2;
                _columnIndexOfSourceUrl = _columnIndexOfAndroidVersion;
                _columnIndexOfChangelog = _columnIndexOfChangelog2;
                _columnIndexOfSecurityPatch2 = _columnIndexOfAndroidVersion2;
                _columnIndexOfIsMandatory = _columnIndexOfReleaseDate3;
                _columnIndexOfReleaseDate = _columnIndexOfDeviceModel;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Flow<OtaRelease> getLatestRelease(final String device, final String channel) {
        Intrinsics.checkNotNullParameter(device, "device");
        Intrinsics.checkNotNullParameter(channel, "channel");
        final String _sql = "SELECT * FROM ota_releases WHERE (deviceModel = ? OR deviceModel = 'Oppo A6X' OR deviceModel = '') AND (releaseChannel = ? OR (? = 'Stable' AND (releaseChannel = 'Official' OR releaseChannel = 'Stable' OR releaseChannel = 'stable' OR releaseChannel = 'official')) OR (? = 'Early Access' AND (releaseChannel = 'Early Access' OR releaseChannel = 'Beta' OR releaseChannel = 'beta' OR releaseChannel = 'early_access')) OR (? = 'Closed Beta' AND (releaseChannel = 'Closed Beta' OR releaseChannel = 'Alpha' OR releaseChannel = 'alpha' OR releaseChannel = 'nightly' OR releaseChannel = 'closed_beta'))) AND status != 'DEPRECATED' ORDER BY versionCode DESC LIMIT 1";
        return FlowUtil.createFlow(this.__db, false, new String[]{"ota_releases"}, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda9
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.getLatestRelease$lambda$5(_sql, device, channel, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final OtaRelease getLatestRelease$lambda$5(String $_sql, String $device, String $channel, SQLiteConnection _connection) {
        OtaRelease _result;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo6930bindText(1, $device);
            _stmt.mo6930bindText(2, $channel);
            _stmt.mo6930bindText(3, $channel);
            _stmt.mo6930bindText(4, $channel);
            _stmt.mo6930bindText(5, $channel);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfDeviceModel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceModel");
            int _columnIndexOfDeviceCodename = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceCodename");
            int _columnIndexOfVersionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionName");
            int _columnIndexOfVersionCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionCode");
            int _columnIndexOfBuildNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "buildNumber");
            int _columnIndexOfReleaseChannel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseChannel");
            int _columnIndexOfReleaseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseType");
            int _columnIndexOfPackageSizeBytes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "packageSizeBytes");
            int _columnIndexOfDownloadUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "downloadUrl");
            int _columnIndexOfChecksumSha256 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "checksumSha256");
            int _columnIndexOfAndroidVersion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "androidVersion");
            int _columnIndexOfSecurityPatch = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "securityPatch");
            int _columnIndexOfChangelog = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "changelog");
            int _columnIndexOfReleaseDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseDate");
            int _columnIndexOfIsMandatory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isMandatory");
            int _columnIndexOfMinRequiredVersion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minRequiredVersion");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfRolloutPercentage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rolloutPercentage");
            int _columnIndexOfSourceUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceUrl");
            int _columnIndexOfTargetLocalPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetLocalPath");
            if (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpDeviceModel = _stmt.getText(_columnIndexOfDeviceModel);
                String _tmpDeviceCodename = _stmt.getText(_columnIndexOfDeviceCodename);
                String _tmpVersionName = _stmt.getText(_columnIndexOfVersionName);
                int _tmpVersionCode = (int) _stmt.getLong(_columnIndexOfVersionCode);
                String _tmpBuildNumber = _stmt.getText(_columnIndexOfBuildNumber);
                String _tmpReleaseChannel = _stmt.getText(_columnIndexOfReleaseChannel);
                String _tmpReleaseType = _stmt.getText(_columnIndexOfReleaseType);
                long _tmpPackageSizeBytes = _stmt.getLong(_columnIndexOfPackageSizeBytes);
                String _tmpDownloadUrl = _stmt.getText(_columnIndexOfDownloadUrl);
                String _tmpChecksumSha256 = _stmt.getText(_columnIndexOfChecksumSha256);
                String _tmpAndroidVersion = _stmt.getText(_columnIndexOfAndroidVersion);
                String _tmpSecurityPatch = _stmt.getText(_columnIndexOfSecurityPatch);
                String _tmpChangelog = _stmt.getText(_columnIndexOfChangelog);
                long _tmpReleaseDate = _stmt.getLong(_columnIndexOfReleaseDate);
                int _tmp = (int) _stmt.getLong(_columnIndexOfIsMandatory);
                boolean _tmpIsMandatory = _tmp != 0;
                int _tmpMinRequiredVersion = (int) _stmt.getLong(_columnIndexOfMinRequiredVersion);
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                int _tmpRolloutPercentage = (int) _stmt.getLong(_columnIndexOfRolloutPercentage);
                String _tmpSourceUrl = _stmt.getText(_columnIndexOfSourceUrl);
                String _tmpTargetLocalPath = _stmt.getText(_columnIndexOfTargetLocalPath);
                _result = new OtaRelease(_tmpId, _tmpDeviceModel, _tmpDeviceCodename, _tmpVersionName, _tmpVersionCode, _tmpBuildNumber, _tmpReleaseChannel, _tmpReleaseType, _tmpPackageSizeBytes, _tmpDownloadUrl, _tmpChecksumSha256, _tmpAndroidVersion, _tmpSecurityPatch, _tmpChangelog, _tmpReleaseDate, _tmpIsMandatory, _tmpMinRequiredVersion, _tmpStatus, _tmpRolloutPercentage, _tmpSourceUrl, _tmpTargetLocalPath);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Object getReleaseById(final String id, Continuation<? super OtaRelease> continuation) {
        final String _sql = "SELECT * FROM ota_releases WHERE id = ? LIMIT 1";
        return DBUtil.performSuspending(this.__db, true, false, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda6
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.getReleaseById$lambda$6(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final OtaRelease getReleaseById$lambda$6(String $_sql, String $id, SQLiteConnection _connection) {
        OtaRelease _result;
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo6930bindText(1, $id);
            int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfDeviceModel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceModel");
            int _columnIndexOfDeviceCodename = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceCodename");
            int _columnIndexOfVersionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionName");
            int _columnIndexOfVersionCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionCode");
            int _columnIndexOfBuildNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "buildNumber");
            int _columnIndexOfReleaseChannel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseChannel");
            int _columnIndexOfReleaseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseType");
            int _columnIndexOfPackageSizeBytes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "packageSizeBytes");
            int _columnIndexOfDownloadUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "downloadUrl");
            int _columnIndexOfChecksumSha256 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "checksumSha256");
            int _columnIndexOfAndroidVersion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "androidVersion");
            int _columnIndexOfSecurityPatch = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "securityPatch");
            int _columnIndexOfChangelog = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "changelog");
            int _columnIndexOfReleaseDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseDate");
            int _columnIndexOfIsMandatory = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isMandatory");
            int _columnIndexOfMinRequiredVersion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minRequiredVersion");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfRolloutPercentage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rolloutPercentage");
            int _columnIndexOfSourceUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceUrl");
            int _columnIndexOfTargetLocalPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetLocalPath");
            if (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfId);
                String _tmpDeviceModel = _stmt.getText(_columnIndexOfDeviceModel);
                String _tmpDeviceCodename = _stmt.getText(_columnIndexOfDeviceCodename);
                String _tmpVersionName = _stmt.getText(_columnIndexOfVersionName);
                int _tmpVersionCode = (int) _stmt.getLong(_columnIndexOfVersionCode);
                String _tmpBuildNumber = _stmt.getText(_columnIndexOfBuildNumber);
                String _tmpReleaseChannel = _stmt.getText(_columnIndexOfReleaseChannel);
                String _tmpReleaseType = _stmt.getText(_columnIndexOfReleaseType);
                long _tmpPackageSizeBytes = _stmt.getLong(_columnIndexOfPackageSizeBytes);
                String _tmpDownloadUrl = _stmt.getText(_columnIndexOfDownloadUrl);
                String _tmpChecksumSha256 = _stmt.getText(_columnIndexOfChecksumSha256);
                String _tmpAndroidVersion = _stmt.getText(_columnIndexOfAndroidVersion);
                String _tmpSecurityPatch = _stmt.getText(_columnIndexOfSecurityPatch);
                String _tmpChangelog = _stmt.getText(_columnIndexOfChangelog);
                long _tmpReleaseDate = _stmt.getLong(_columnIndexOfReleaseDate);
                int _tmp = (int) _stmt.getLong(_columnIndexOfIsMandatory);
                boolean _tmpIsMandatory = _tmp != 0;
                int _tmpMinRequiredVersion = (int) _stmt.getLong(_columnIndexOfMinRequiredVersion);
                String _tmpStatus = _stmt.getText(_columnIndexOfStatus);
                int _tmpRolloutPercentage = (int) _stmt.getLong(_columnIndexOfRolloutPercentage);
                String _tmpSourceUrl = _stmt.getText(_columnIndexOfSourceUrl);
                String _tmpTargetLocalPath = _stmt.getText(_columnIndexOfTargetLocalPath);
                _result = new OtaRelease(_tmpId, _tmpDeviceModel, _tmpDeviceCodename, _tmpVersionName, _tmpVersionCode, _tmpBuildNumber, _tmpReleaseChannel, _tmpReleaseType, _tmpPackageSizeBytes, _tmpDownloadUrl, _tmpChecksumSha256, _tmpAndroidVersion, _tmpSecurityPatch, _tmpChangelog, _tmpReleaseDate, _tmpIsMandatory, _tmpMinRequiredVersion, _tmpStatus, _tmpRolloutPercentage, _tmpSourceUrl, _tmpTargetLocalPath);
            } else {
                _result = null;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Flow<List<OtaRelease>> getAllReleases() {
        final String _sql = "SELECT * FROM ota_releases ORDER BY releaseDate DESC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"ota_releases"}, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda5
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.getAllReleases$lambda$7(_sql, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final List getAllReleases$lambda$7(String $_sql, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            int _columnIndexOfSourceUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
            int _columnIndexOfDeviceModel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceModel");
            int _columnIndexOfDeviceCodename = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "deviceCodename");
            int _columnIndexOfVersionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionName");
            int _columnIndexOfVersionCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionCode");
            int _columnIndexOfBuildNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "buildNumber");
            int _columnIndexOfReleaseChannel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseChannel");
            int _columnIndexOfReleaseType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseType");
            int _columnIndexOfPackageSizeBytes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "packageSizeBytes");
            int _columnIndexOfDownloadUrl = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "downloadUrl");
            int _columnIndexOfChecksumSha256 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "checksumSha256");
            int _columnIndexOfAndroidVersion = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "androidVersion");
            int _columnIndexOfSecurityPatch = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "securityPatch");
            int _columnIndexOfChangelog = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "changelog");
            int _columnIndexOfReleaseDate = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseDate");
            int _columnIndexOfIsMandatory = _columnIndexOfReleaseDate;
            int _columnIndexOfIsMandatory2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "isMandatory");
            int _columnIndexOfReleaseDate2 = _columnIndexOfIsMandatory2;
            int _tmp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "minRequiredVersion");
            int _columnIndexOfStatus = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, NotificationCompat.CATEGORY_STATUS);
            int _columnIndexOfStatus2 = _columnIndexOfStatus;
            int _columnIndexOfRolloutPercentage = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "rolloutPercentage");
            int _columnIndexOfRolloutPercentage2 = _columnIndexOfRolloutPercentage;
            int _columnIndexOfSourceUrl2 = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "sourceUrl");
            int _columnIndexOfSourceUrl3 = _columnIndexOfSourceUrl2;
            int _columnIndexOfTargetLocalPath = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "targetLocalPath");
            List _result = new ArrayList();
            while (_stmt.step()) {
                String _tmpId = _stmt.getText(_columnIndexOfSourceUrl);
                String _tmpDeviceModel = _stmt.getText(_columnIndexOfDeviceModel);
                String _tmpDeviceCodename = _stmt.getText(_columnIndexOfDeviceCodename);
                String _tmpVersionName = _stmt.getText(_columnIndexOfVersionName);
                int _columnIndexOfDeviceModel2 = _columnIndexOfDeviceModel;
                int _columnIndexOfDeviceCodename2 = _columnIndexOfDeviceCodename;
                int _tmpVersionCode = (int) _stmt.getLong(_columnIndexOfVersionCode);
                String _tmpBuildNumber = _stmt.getText(_columnIndexOfBuildNumber);
                String _tmpReleaseChannel = _stmt.getText(_columnIndexOfReleaseChannel);
                String _tmpReleaseType = _stmt.getText(_columnIndexOfReleaseType);
                long _tmpPackageSizeBytes = _stmt.getLong(_columnIndexOfPackageSizeBytes);
                String _tmpDownloadUrl = _stmt.getText(_columnIndexOfDownloadUrl);
                String _tmpChecksumSha256 = _stmt.getText(_columnIndexOfChecksumSha256);
                String _tmpAndroidVersion = _stmt.getText(_columnIndexOfAndroidVersion);
                String _tmpSecurityPatch = _stmt.getText(_columnIndexOfSecurityPatch);
                String _tmpChangelog = _stmt.getText(_columnIndexOfChangelog);
                int _columnIndexOfReleaseDate3 = _columnIndexOfIsMandatory;
                long _tmpReleaseDate = _stmt.getLong(_columnIndexOfReleaseDate3);
                int _columnIndexOfId = _columnIndexOfSourceUrl;
                int _columnIndexOfId2 = _columnIndexOfReleaseDate2;
                int _tmp2 = (int) _stmt.getLong(_columnIndexOfId2);
                boolean _tmpIsMandatory = _tmp2 != 0;
                int _columnIndexOfIsMandatory3 = _tmp;
                int _tmpMinRequiredVersion = (int) _stmt.getLong(_columnIndexOfIsMandatory3);
                int _columnIndexOfMinRequiredVersion = _columnIndexOfStatus2;
                String _tmpStatus = _stmt.getText(_columnIndexOfMinRequiredVersion);
                _columnIndexOfStatus2 = _columnIndexOfMinRequiredVersion;
                int _columnIndexOfStatus3 = _columnIndexOfRolloutPercentage2;
                int _tmpRolloutPercentage = (int) _stmt.getLong(_columnIndexOfStatus3);
                _columnIndexOfRolloutPercentage2 = _columnIndexOfStatus3;
                int _columnIndexOfRolloutPercentage3 = _columnIndexOfSourceUrl3;
                String _tmpSourceUrl = _stmt.getText(_columnIndexOfRolloutPercentage3);
                String _tmpTargetLocalPath = _stmt.getText(_columnIndexOfTargetLocalPath);
                OtaRelease _item = new OtaRelease(_tmpId, _tmpDeviceModel, _tmpDeviceCodename, _tmpVersionName, _tmpVersionCode, _tmpBuildNumber, _tmpReleaseChannel, _tmpReleaseType, _tmpPackageSizeBytes, _tmpDownloadUrl, _tmpChecksumSha256, _tmpAndroidVersion, _tmpSecurityPatch, _tmpChangelog, _tmpReleaseDate, _tmpIsMandatory, _tmpMinRequiredVersion, _tmpStatus, _tmpRolloutPercentage, _tmpSourceUrl, _tmpTargetLocalPath);
                List _result2 = _result;
                _result2.add(_item);
                _columnIndexOfReleaseDate2 = _columnIndexOfId2;
                _columnIndexOfIsMandatory = _columnIndexOfReleaseDate3;
                _columnIndexOfSourceUrl3 = _columnIndexOfRolloutPercentage3;
                _result = _result2;
                _tmp = _columnIndexOfIsMandatory3;
                _columnIndexOfDeviceModel = _columnIndexOfDeviceModel2;
                _columnIndexOfDeviceCodename = _columnIndexOfDeviceCodename2;
                _columnIndexOfSourceUrl = _columnIndexOfId;
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Object deleteReleaseById(final String id, Continuation<? super Unit> continuation) {
        final String _sql = "DELETE FROM ota_releases WHERE id = ?";
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.deleteReleaseById$lambda$8(_sql, id, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit deleteReleaseById$lambda$8(String $_sql, String $id, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.mo6930bindText(1, $id);
            _stmt.step();
            _stmt.close();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            _stmt.close();
            throw th;
        }
    }

    @Override // com.example.data.local.OtaReleaseDao
    public Object clearAllReleases(Continuation<? super Unit> continuation) {
        final String _sql = "DELETE FROM ota_releases";
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.OtaReleaseDao_Impl$$ExternalSyntheticLambda4
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return OtaReleaseDao_Impl.clearAllReleases$lambda$9(_sql, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit clearAllReleases$lambda$9(String $_sql, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            _stmt.step();
            _stmt.close();
            return Unit.INSTANCE;
        } catch (Throwable th) {
            _stmt.close();
            throw th;
        }
    }

    /* compiled from: OtaReleaseDao_Impl.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¨\u0006\u0007"}, d2 = {"Lcom/example/data/local/OtaReleaseDao_Impl$Companion;", "", "<init>", "()V", "getRequiredConverters", "", "Lkotlin/reflect/KClass;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
    /* loaded from: classes7.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final List<KClass<?>> getRequiredConverters() {
            return CollectionsKt.emptyList();
        }
    }
}
