package com.example.data.local;

import com.example.data.model.UpdateHistoryItem;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

/* compiled from: OtaDaos.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H'J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H§@¢\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u0007H§@¢\u0006\u0002\u0010\u000b¨\u0006\fÀ\u0006\u0003"}, d2 = {"Lcom/example/data/local/UpdateHistoryDao;", "", "getAllHistory", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/example/data/model/UpdateHistoryItem;", "insertHistory", "", "item", "(Lcom/example/data/model/UpdateHistoryItem;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "clearHistory", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes7.dex */
public interface UpdateHistoryDao {
    Object clearHistory(Continuation<? super Unit> continuation);

    Flow<List<UpdateHistoryItem>> getAllHistory();

    Object insertHistory(UpdateHistoryItem updateHistoryItem, Continuation<? super Unit> continuation);
}
