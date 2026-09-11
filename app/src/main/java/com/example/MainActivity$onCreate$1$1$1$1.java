package com.example;

import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.State;
import com.example.ui.UpdaterUiState;
import com.example.ui.UpdaterViewModel;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SpillingKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.example.MainActivity$onCreate$1$1$1$1", f = "MainActivity.kt", i = {0, 0}, l = {93}, m = "invokeSuspend", n = {"msg\\1", "$i$a$-let-MainActivity$onCreate$1$1$1$1$1\\1\\92\\0"}, s = {"L$1", "I$0"})
/* loaded from: classes8.dex */
public final class MainActivity$onCreate$1$1$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ SnackbarHostState $snackbarHostState;
    final /* synthetic */ State<UpdaterUiState> $uiState$delegate;
    int I$0;
    Object L$0;
    Object L$1;
    int label;
    final /* synthetic */ MainActivity this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MainActivity$onCreate$1$1$1$1(State<UpdaterUiState> state, SnackbarHostState snackbarHostState, MainActivity mainActivity, Continuation<? super MainActivity$onCreate$1$1$1$1> continuation) {
        super(2, continuation);
        this.$uiState$delegate = state;
        this.$snackbarHostState = snackbarHostState;
        this.this$0 = mainActivity;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new MainActivity$onCreate$1$1$1$1(this.$uiState$delegate, this.$snackbarHostState, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((MainActivity$onCreate$1$1$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:2:0x0006. Please report as an issue. */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        UpdaterUiState onCreate$lambda$65$lambda$64$lambda$0;
        MainActivity mainActivity;
        UpdaterViewModel viewModel;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                onCreate$lambda$65$lambda$64$lambda$0 = MainActivity.onCreate$lambda$65$lambda$64$lambda$0(this.$uiState$delegate);
                String snackbarMessage = onCreate$lambda$65$lambda$64$lambda$0.getSnackbarMessage();
                if (snackbarMessage != null) {
                    SnackbarHostState snackbarHostState = this.$snackbarHostState;
                    MainActivity mainActivity2 = this.this$0;
                    this.L$0 = mainActivity2;
                    this.L$1 = SpillingKt.nullOutSpilledVariable(snackbarMessage);
                    this.I$0 = 0;
                    this.label = 1;
                    if (SnackbarHostState.showSnackbar$default(snackbarHostState, snackbarMessage, null, false, null, this, 14, null) != coroutine_suspended) {
                        mainActivity = mainActivity2;
                        viewModel = mainActivity.getViewModel();
                        viewModel.clearSnackbar();
                    } else {
                        return coroutine_suspended;
                    }
                }
                return Unit.INSTANCE;
            case 1:
                int i = this.I$0;
                mainActivity = (MainActivity) this.L$0;
                ResultKt.throwOnFailure($result);
                viewModel = mainActivity.getViewModel();
                viewModel.clearSnackbar();
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
