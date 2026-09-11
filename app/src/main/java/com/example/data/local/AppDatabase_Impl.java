package com.example.data.local;

import androidx.core.app.NotificationCompat;
import androidx.room.InvalidationTracker;
import androidx.room.RoomMasterTable;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;

/* compiled from: AppDatabase_Impl.kt */
@Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\t\u001a\u00020\nH\u0014J\b\u0010\u000b\u001a\u00020\fH\u0014J\b\u0010\r\u001a\u00020\u000eH\u0016J\"\u0010\u000f\u001a\u001c\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u0011\u0012\u000e\u0012\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00110\u00120\u0010H\u0014J\u0016\u0010\u0013\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u00110\u0014H\u0016J*\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u00122\u001a\u0010\u0018\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u0011\u0012\u0004\u0012\u00020\u00150\u0010H\u0016J\b\u0010\u0019\u001a\u00020\u0006H\u0016J\b\u0010\u001a\u001a\u00020\bH\u0016R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001b"}, d2 = {"Lcom/example/data/local/AppDatabase_Impl;", "Lcom/example/data/local/AppDatabase;", "<init>", "()V", "_otaReleaseDao", "Lkotlin/Lazy;", "Lcom/example/data/local/OtaReleaseDao;", "_updateHistoryDao", "Lcom/example/data/local/UpdateHistoryDao;", "createOpenDelegate", "Landroidx/room/RoomOpenDelegate;", "createInvalidationTracker", "Landroidx/room/InvalidationTracker;", "clearAllTables", "", "getRequiredTypeConverterClasses", "", "Lkotlin/reflect/KClass;", "", "getRequiredAutoMigrationSpecClasses", "", "Landroidx/room/migration/AutoMigrationSpec;", "createAutoMigrations", "Landroidx/room/migration/Migration;", "autoMigrationSpecs", "otaReleaseDao", "updateHistoryDao", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes7.dex */
public final class AppDatabase_Impl extends AppDatabase {
    public static final int $stable = 8;
    private final Lazy<OtaReleaseDao> _otaReleaseDao = LazyKt.lazy(new Function0() { // from class: com.example.data.local.AppDatabase_Impl$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AppDatabase_Impl._otaReleaseDao$lambda$0(AppDatabase_Impl.this);
        }
    });
    private final Lazy<UpdateHistoryDao> _updateHistoryDao = LazyKt.lazy(new Function0() { // from class: com.example.data.local.AppDatabase_Impl$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return AppDatabase_Impl._updateHistoryDao$lambda$1(AppDatabase_Impl.this);
        }
    });

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final OtaReleaseDao_Impl _otaReleaseDao$lambda$0(AppDatabase_Impl this$0) {
        return new OtaReleaseDao_Impl(this$0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final UpdateHistoryDao_Impl _updateHistoryDao$lambda$1(AppDatabase_Impl this$0) {
        return new UpdateHistoryDao_Impl(this$0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.room.RoomDatabase
    public RoomOpenDelegate createOpenDelegate() {
        RoomOpenDelegate _openDelegate = new RoomOpenDelegate() { // from class: com.example.data.local.AppDatabase_Impl$createOpenDelegate$_openDelegate$1
            /* JADX INFO: Access modifiers changed from: package-private */
            {
                super(1, "8fdae635c9c6c4d0c63e92131250cdeb", "db146bbc52f12a43f86587d54f2d4459");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void createAllTables(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `ota_releases` (`id` TEXT NOT NULL, `deviceModel` TEXT NOT NULL, `deviceCodename` TEXT NOT NULL, `versionName` TEXT NOT NULL, `versionCode` INTEGER NOT NULL, `buildNumber` TEXT NOT NULL, `releaseChannel` TEXT NOT NULL, `releaseType` TEXT NOT NULL, `packageSizeBytes` INTEGER NOT NULL, `downloadUrl` TEXT NOT NULL, `checksumSha256` TEXT NOT NULL, `androidVersion` TEXT NOT NULL, `securityPatch` TEXT NOT NULL, `changelog` TEXT NOT NULL, `releaseDate` INTEGER NOT NULL, `isMandatory` INTEGER NOT NULL, `minRequiredVersion` INTEGER NOT NULL, `status` TEXT NOT NULL, `rolloutPercentage` INTEGER NOT NULL, `sourceUrl` TEXT NOT NULL, `targetLocalPath` TEXT NOT NULL, PRIMARY KEY(`id`))");
                SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `update_history` (`historyId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `versionName` TEXT NOT NULL, `versionCode` INTEGER NOT NULL, `buildNumber` TEXT NOT NULL, `installedTimestamp` INTEGER NOT NULL, `packageSizeBytes` INTEGER NOT NULL, `releaseChannel` TEXT NOT NULL, `installType` TEXT NOT NULL)");
                SQLite.execSQL(connection, RoomMasterTable.CREATE_QUERY);
                SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8fdae635c9c6c4d0c63e92131250cdeb')");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void dropAllTables(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `ota_releases`");
                SQLite.execSQL(connection, "DROP TABLE IF EXISTS `update_history`");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onCreate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onOpen(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                AppDatabase_Impl.this.internalInitInvalidationTracker(connection);
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onPreMigrate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                DBUtil.dropFtsSyncTriggers(connection);
            }

            @Override // androidx.room.RoomOpenDelegate
            public void onPostMigrate(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
            }

            @Override // androidx.room.RoomOpenDelegate
            public RoomOpenDelegate.ValidationResult onValidateSchema(SQLiteConnection connection) {
                Intrinsics.checkNotNullParameter(connection, "connection");
                Map _columnsOtaReleases = new LinkedHashMap();
                _columnsOtaReleases.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, 1));
                _columnsOtaReleases.put("deviceModel", new TableInfo.Column("deviceModel", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("deviceCodename", new TableInfo.Column("deviceCodename", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("versionName", new TableInfo.Column("versionName", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("versionCode", new TableInfo.Column("versionCode", "INTEGER", true, 0, null, 1));
                _columnsOtaReleases.put("buildNumber", new TableInfo.Column("buildNumber", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("releaseChannel", new TableInfo.Column("releaseChannel", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("releaseType", new TableInfo.Column("releaseType", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("packageSizeBytes", new TableInfo.Column("packageSizeBytes", "INTEGER", true, 0, null, 1));
                _columnsOtaReleases.put("downloadUrl", new TableInfo.Column("downloadUrl", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("checksumSha256", new TableInfo.Column("checksumSha256", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("androidVersion", new TableInfo.Column("androidVersion", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("securityPatch", new TableInfo.Column("securityPatch", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("changelog", new TableInfo.Column("changelog", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("releaseDate", new TableInfo.Column("releaseDate", "INTEGER", true, 0, null, 1));
                _columnsOtaReleases.put("isMandatory", new TableInfo.Column("isMandatory", "INTEGER", true, 0, null, 1));
                _columnsOtaReleases.put("minRequiredVersion", new TableInfo.Column("minRequiredVersion", "INTEGER", true, 0, null, 1));
                _columnsOtaReleases.put(NotificationCompat.CATEGORY_STATUS, new TableInfo.Column(NotificationCompat.CATEGORY_STATUS, "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("rolloutPercentage", new TableInfo.Column("rolloutPercentage", "INTEGER", true, 0, null, 1));
                _columnsOtaReleases.put("sourceUrl", new TableInfo.Column("sourceUrl", "TEXT", true, 0, null, 1));
                _columnsOtaReleases.put("targetLocalPath", new TableInfo.Column("targetLocalPath", "TEXT", true, 0, null, 1));
                Set _foreignKeysOtaReleases = new LinkedHashSet();
                Set _indicesOtaReleases = new LinkedHashSet();
                TableInfo _infoOtaReleases = new TableInfo("ota_releases", _columnsOtaReleases, _foreignKeysOtaReleases, _indicesOtaReleases);
                TableInfo _existingOtaReleases = TableInfo.INSTANCE.read(connection, "ota_releases");
                if (!_infoOtaReleases.equals(_existingOtaReleases)) {
                    return new RoomOpenDelegate.ValidationResult(false, "ota_releases(com.example.data.model.OtaRelease).\n Expected:\n" + _infoOtaReleases + "\n Found:\n" + _existingOtaReleases);
                }
                Map _columnsUpdateHistory = new LinkedHashMap();
                _columnsUpdateHistory.put("historyId", new TableInfo.Column("historyId", "INTEGER", true, 1, null, 1));
                _columnsUpdateHistory.put("versionName", new TableInfo.Column("versionName", "TEXT", true, 0, null, 1));
                _columnsUpdateHistory.put("versionCode", new TableInfo.Column("versionCode", "INTEGER", true, 0, null, 1));
                _columnsUpdateHistory.put("buildNumber", new TableInfo.Column("buildNumber", "TEXT", true, 0, null, 1));
                _columnsUpdateHistory.put("installedTimestamp", new TableInfo.Column("installedTimestamp", "INTEGER", true, 0, null, 1));
                _columnsUpdateHistory.put("packageSizeBytes", new TableInfo.Column("packageSizeBytes", "INTEGER", true, 0, null, 1));
                _columnsUpdateHistory.put("releaseChannel", new TableInfo.Column("releaseChannel", "TEXT", true, 0, null, 1));
                _columnsUpdateHistory.put("installType", new TableInfo.Column("installType", "TEXT", true, 0, null, 1));
                Set _foreignKeysUpdateHistory = new LinkedHashSet();
                Set _indicesUpdateHistory = new LinkedHashSet();
                TableInfo _infoUpdateHistory = new TableInfo("update_history", _columnsUpdateHistory, _foreignKeysUpdateHistory, _indicesUpdateHistory);
                TableInfo _existingUpdateHistory = TableInfo.INSTANCE.read(connection, "update_history");
                if (!_infoUpdateHistory.equals(_existingUpdateHistory)) {
                    return new RoomOpenDelegate.ValidationResult(false, "update_history(com.example.data.model.UpdateHistoryItem).\n Expected:\n" + _infoUpdateHistory + "\n Found:\n" + _existingUpdateHistory);
                }
                return new RoomOpenDelegate.ValidationResult(true, null);
            }
        };
        return _openDelegate;
    }

    @Override // androidx.room.RoomDatabase
    protected InvalidationTracker createInvalidationTracker() {
        Map _shadowTablesMap = new LinkedHashMap();
        Map _viewTables = new LinkedHashMap();
        return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "ota_releases", "update_history");
    }

    @Override // androidx.room.RoomDatabase
    public void clearAllTables() {
        super.performClear(false, "ota_releases", "update_history");
    }

    @Override // androidx.room.RoomDatabase
    protected Map<KClass<?>, List<KClass<?>>> getRequiredTypeConverterClasses() {
        Map _typeConvertersMap = new LinkedHashMap();
        _typeConvertersMap.put(Reflection.getOrCreateKotlinClass(OtaReleaseDao.class), OtaReleaseDao_Impl.INSTANCE.getRequiredConverters());
        _typeConvertersMap.put(Reflection.getOrCreateKotlinClass(UpdateHistoryDao.class), UpdateHistoryDao_Impl.INSTANCE.getRequiredConverters());
        return _typeConvertersMap;
    }

    @Override // androidx.room.RoomDatabase
    public Set<KClass<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecClasses() {
        Set _autoMigrationSpecsSet = new LinkedHashSet();
        return _autoMigrationSpecsSet;
    }

    @Override // androidx.room.RoomDatabase
    public List<Migration> createAutoMigrations(Map<KClass<? extends AutoMigrationSpec>, ? extends AutoMigrationSpec> autoMigrationSpecs) {
        Intrinsics.checkNotNullParameter(autoMigrationSpecs, "autoMigrationSpecs");
        List _autoMigrations = new ArrayList();
        return _autoMigrations;
    }

    @Override // com.example.data.local.AppDatabase
    public OtaReleaseDao otaReleaseDao() {
        return this._otaReleaseDao.getValue();
    }

    @Override // com.example.data.local.AppDatabase
    public UpdateHistoryDao updateHistoryDao() {
        return this._updateHistoryDao.getValue();
    }
}
