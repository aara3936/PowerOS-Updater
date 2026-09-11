package com.example.data.local;

import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.coroutines.FlowUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteConnection;
import androidx.sqlite.SQLiteStatement;
import com.example.data.model.UpdateHistoryItem;
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

/* compiled from: UpdateHistoryDao_Impl.kt */
@Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\b\u0007\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\fJ\u0014\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u000f0\u000eH\u0016J\u000e\u0010\u0010\u001a\u00020\nH\u0096@¢\u0006\u0002\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, d2 = {"Lcom/example/data/local/UpdateHistoryDao_Impl;", "Lcom/example/data/local/UpdateHistoryDao;", "__db", "Landroidx/room/RoomDatabase;", "<init>", "(Landroidx/room/RoomDatabase;)V", "__insertAdapterOfUpdateHistoryItem", "Landroidx/room/EntityInsertAdapter;", "Lcom/example/data/model/UpdateHistoryItem;", "insertHistory", "", "item", "(Lcom/example/data/model/UpdateHistoryItem;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllHistory", "Lkotlinx/coroutines/flow/Flow;", "", "clearHistory", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes7.dex */
public final class UpdateHistoryDao_Impl implements UpdateHistoryDao {
    private final RoomDatabase __db;
    private final EntityInsertAdapter<UpdateHistoryItem> __insertAdapterOfUpdateHistoryItem;

    /* renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    public static final int $stable = 8;

    public UpdateHistoryDao_Impl(RoomDatabase __db) {
        Intrinsics.checkNotNullParameter(__db, "__db");
        this.__db = __db;
        this.__insertAdapterOfUpdateHistoryItem = new EntityInsertAdapter<UpdateHistoryItem>() { // from class: com.example.data.local.UpdateHistoryDao_Impl.1
            @Override // androidx.room.EntityInsertAdapter
            protected String createQuery() {
                return "INSERT OR REPLACE INTO `update_history` (`historyId`,`versionName`,`versionCode`,`buildNumber`,`installedTimestamp`,`packageSizeBytes`,`releaseChannel`,`installType`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // androidx.room.EntityInsertAdapter
            public void bind(SQLiteStatement statement, UpdateHistoryItem entity) {
                Intrinsics.checkNotNullParameter(statement, "statement");
                Intrinsics.checkNotNullParameter(entity, "entity");
                statement.mo6928bindLong(1, entity.getHistoryId());
                statement.mo6930bindText(2, entity.getVersionName());
                statement.mo6928bindLong(3, entity.getVersionCode());
                statement.mo6930bindText(4, entity.getBuildNumber());
                statement.mo6928bindLong(5, entity.getInstalledTimestamp());
                statement.mo6928bindLong(6, entity.getPackageSizeBytes());
                statement.mo6930bindText(7, entity.getReleaseChannel());
                statement.mo6930bindText(8, entity.getInstallType());
            }
        };
    }

    @Override // com.example.data.local.UpdateHistoryDao
    public Object insertHistory(final UpdateHistoryItem item, Continuation<? super Unit> continuation) {
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.UpdateHistoryDao_Impl$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return UpdateHistoryDao_Impl.insertHistory$lambda$0(UpdateHistoryDao_Impl.this, item, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit insertHistory$lambda$0(UpdateHistoryDao_Impl this$0, UpdateHistoryItem $item, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        this$0.__insertAdapterOfUpdateHistoryItem.insert(_connection, (SQLiteConnection) $item);
        return Unit.INSTANCE;
    }

    @Override // com.example.data.local.UpdateHistoryDao
    public Flow<List<UpdateHistoryItem>> getAllHistory() {
        final String _sql = "SELECT * FROM update_history ORDER BY installedTimestamp DESC";
        return FlowUtil.createFlow(this.__db, false, new String[]{"update_history"}, new Function1() { // from class: com.example.data.local.UpdateHistoryDao_Impl$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return UpdateHistoryDao_Impl.getAllHistory$lambda$1(_sql, (SQLiteConnection) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final List getAllHistory$lambda$1(String $_sql, SQLiteConnection _connection) {
        Intrinsics.checkNotNullParameter(_connection, "_connection");
        SQLiteStatement _stmt = _connection.prepare($_sql);
        try {
            int _columnIndexOfHistoryId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "historyId");
            int _columnIndexOfVersionName = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionName");
            int _columnIndexOfVersionCode = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "versionCode");
            int _columnIndexOfBuildNumber = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "buildNumber");
            int _columnIndexOfInstalledTimestamp = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "installedTimestamp");
            int _columnIndexOfPackageSizeBytes = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "packageSizeBytes");
            int _columnIndexOfReleaseChannel = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "releaseChannel");
            int _columnIndexOfInstallType = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "installType");
            List _result = new ArrayList();
            while (_stmt.step()) {
                long _tmpHistoryId = _stmt.getLong(_columnIndexOfHistoryId);
                String _tmpVersionName = _stmt.getText(_columnIndexOfVersionName);
                int _tmpVersionCode = (int) _stmt.getLong(_columnIndexOfVersionCode);
                String _tmpBuildNumber = _stmt.getText(_columnIndexOfBuildNumber);
                long _tmpInstalledTimestamp = _stmt.getLong(_columnIndexOfInstalledTimestamp);
                long _tmpPackageSizeBytes = _stmt.getLong(_columnIndexOfPackageSizeBytes);
                String _tmpReleaseChannel = _stmt.getText(_columnIndexOfReleaseChannel);
                String _tmpInstallType = _stmt.getText(_columnIndexOfInstallType);
                UpdateHistoryItem _item = new UpdateHistoryItem(_tmpHistoryId, _tmpVersionName, _tmpVersionCode, _tmpBuildNumber, _tmpInstalledTimestamp, _tmpPackageSizeBytes, _tmpReleaseChannel, _tmpInstallType);
                _result.add(_item);
            }
            return _result;
        } finally {
            _stmt.close();
        }
    }

    @Override // com.example.data.local.UpdateHistoryDao
    public Object clearHistory(Continuation<? super Unit> continuation) {
        final String _sql = "DELETE FROM update_history";
        Object performSuspending = DBUtil.performSuspending(this.__db, false, true, new Function1() { // from class: com.example.data.local.UpdateHistoryDao_Impl$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return UpdateHistoryDao_Impl.clearHistory$lambda$2(_sql, (SQLiteConnection) obj);
            }
        }, continuation);
        return performSuspending == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? performSuspending : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit clearHistory$lambda$2(String $_sql, SQLiteConnection _connection) {
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

    /* compiled from: UpdateHistoryDao_Impl.kt */
    @Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005¨\u0006\u0007"}, d2 = {"Lcom/example/data/local/UpdateHistoryDao_Impl$Companion;", "", "<init>", "()V", "getRequiredConverters", "", "Lkotlin/reflect/KClass;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
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
