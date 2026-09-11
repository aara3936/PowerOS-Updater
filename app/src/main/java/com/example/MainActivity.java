package com.example;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.activity.compose.ComponentActivityKt;
import androidx.compose.foundation.layout.BoxScope;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.WindowInsetsPadding_androidKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material3.ButtonColors;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.ScaffoldKt;
import androidx.compose.material3.SnackbarHostKt;
import androidx.compose.material3.SnackbarHostState;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.State;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.unit.Dp;
import androidx.lifecycle.ViewModelLazy;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.viewmodel.CreationExtras;
import com.example.data.model.DownloadProgress;
import com.example.data.model.OtaRelease;
import com.example.data.model.SystemDeviceInfo;
import com.example.data.model.UpdateHistoryItem;
import com.example.ui.UpdaterUiState;
import com.example.ui.UpdaterViewModel;
import com.example.ui.theme.ColorKt;
import com.example.ui.theme.ThemeKt;
import java.util.List;
import kotlin.Lazy;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

/* compiled from: MainActivity.kt */
@Metadata(d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0014R\u001b\u0010\u0004\u001a\u00020\u00058BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\b\u0010\t\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000e²\u0006\n\u0010\u000f\u001a\u00020\u0010X\u008a\u0084\u0002²\u0006\n\u0010\u0011\u001a\u00020\u0012X\u008a\u0084\u0002²\u0006\f\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u008a\u0084\u0002²\u0006\u0010\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u008a\u0084\u0002²\u0006\n\u0010\u0018\u001a\u00020\u0019X\u008a\u0084\u0002²\u0006\n\u0010\u001a\u001a\u00020\u001bX\u008a\u0084\u0002²\u0006\n\u0010\u001c\u001a\u00020\u0019X\u008a\u008e\u0002"}, d2 = {"Lcom/example/MainActivity;", "Landroidx/activity/ComponentActivity;", "<init>", "()V", "viewModel", "Lcom/example/ui/UpdaterViewModel;", "getViewModel", "()Lcom/example/ui/UpdaterViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app", "uiState", "Lcom/example/ui/UpdaterUiState;", "deviceInfo", "Lcom/example/data/model/SystemDeviceInfo;", "latestRelease", "Lcom/example/data/model/OtaRelease;", "updateHistory", "", "Lcom/example/data/model/UpdateHistoryItem;", "isNewUpdateAvailable", "", "downloadProgress", "Lcom/example/data/model/DownloadProgress;", "showAboutDialog"}, k = 1, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes8.dex */
public final class MainActivity extends ComponentActivity {
    public static final int $stable = 8;

    /* renamed from: viewModel$delegate, reason: from kotlin metadata */
    private final Lazy viewModel;

    public MainActivity() {
        final MainActivity mainActivity = this;
        final Function0 function0 = null;
        this.viewModel = new ViewModelLazy(Reflection.getOrCreateKotlinClass(UpdaterViewModel.class), new Function0<ViewModelStore>() { // from class: com.example.MainActivity$special$$inlined$viewModels$default$2
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            public final ViewModelStore invoke() {
                return ComponentActivity.this.getViewModelStore();
            }
        }, new Function0<ViewModelProvider.Factory>() { // from class: com.example.MainActivity$special$$inlined$viewModels$default$1
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            public final ViewModelProvider.Factory invoke() {
                return ComponentActivity.this.getDefaultViewModelProviderFactory();
            }
        }, new Function0<CreationExtras>() { // from class: com.example.MainActivity$special$$inlined$viewModels$default$3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            public final CreationExtras invoke() {
                CreationExtras creationExtras;
                Function0 function02 = Function0.this;
                return (function02 == null || (creationExtras = (CreationExtras) function02.invoke()) == null) ? mainActivity.getDefaultViewModelCreationExtras() : creationExtras;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final UpdaterViewModel getViewModel() {
        return (UpdaterViewModel) this.viewModel.getValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable$default(this, null, null, 3, null);
        ComponentActivityKt.setContent$default(this, null, ComposableLambdaKt.composableLambdaInstance(-601144069, true, new Function2() { // from class: com.example.MainActivity$$ExternalSyntheticLambda11
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return MainActivity.onCreate$lambda$65(MainActivity.this, (Composer) obj, ((Integer) obj2).intValue());
            }
        }), 1, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65(final MainActivity this$0, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C79@3516L12537,79@3497L12556:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-601144069, $changed, -1, "com.example.MainActivity.onCreate.<anonymous> (MainActivity.kt:79)");
            }
            ThemeKt.MyApplicationTheme(false, false, ComposableLambdaKt.rememberComposableLambda(434770631, true, new Function2() { // from class: com.example.MainActivity$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return MainActivity.onCreate$lambda$65$lambda$64(MainActivity.this, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), $composer, 384, 3);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x01d4  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0228  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x02ad  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0338  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x03d2  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x03c3  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0218  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01c4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit onCreate$lambda$65$lambda$64(final com.example.MainActivity r30, androidx.compose.runtime.Composer r31, int r32) {
        /*
            Method dump skipped, instructions count: 984
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivity.onCreate$lambda$65$lambda$64(com.example.MainActivity, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final UpdaterUiState onCreate$lambda$65$lambda$64$lambda$0(State<UpdaterUiState> state) {
        return (UpdaterUiState) state.getValue();
    }

    private static final SystemDeviceInfo onCreate$lambda$65$lambda$64$lambda$1(State<SystemDeviceInfo> state) {
        return (SystemDeviceInfo) state.getValue();
    }

    private static final OtaRelease onCreate$lambda$65$lambda$64$lambda$2(State<OtaRelease> state) {
        return (OtaRelease) state.getValue();
    }

    private static final List<UpdateHistoryItem> onCreate$lambda$65$lambda$64$lambda$3(State<? extends List<UpdateHistoryItem>> state) {
        return (List) state.getValue();
    }

    private static final boolean onCreate$lambda$65$lambda$64$lambda$4(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final DownloadProgress onCreate$lambda$65$lambda$64$lambda$5(State<DownloadProgress> state) {
        return (DownloadProgress) state.getValue();
    }

    private static final boolean onCreate$lambda$65$lambda$64$lambda$8(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void onCreate$lambda$65$lambda$64$lambda$9(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46(final MainActivity this$0, final MutableState $showAboutDialog$delegate, final SnackbarHostState $snackbarHostState, final State $deviceInfo$delegate, final State $latestRelease$delegate, final State $isNewUpdateAvailable$delegate, final State $downloadProgress$delegate, final State $uiState$delegate, BoxScope GlassBackground, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(GlassBackground, "$this$GlassBackground");
        ComposerKt.sourceInformation($composer, "C108@5047L4489,107@4977L35,183@9559L1785,101@4678L6666:MainActivity.kt#to5c3");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1692536480, $changed, -1, "com.example.MainActivity.onCreate.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:101)");
            }
            ScaffoldKt.m2408ScaffoldTvnljyQ(WindowInsetsPadding_androidKt.navigationBarsPadding(WindowInsetsPadding_androidKt.statusBarsPadding(SizeKt.fillMaxSize$default(Modifier.INSTANCE, 0.0f, 1, null))), ComposableLambdaKt.rememberComposableLambda(-125638372, true, new Function2() { // from class: com.example.MainActivity$$ExternalSyntheticLambda21
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return MainActivity.onCreate$lambda$65$lambda$64$lambda$46$lambda$20(MainActivity.this, $showAboutDialog$delegate, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), null, ComposableLambdaKt.rememberComposableLambda(1420445530, true, new Function2() { // from class: com.example.MainActivity$$ExternalSyntheticLambda22
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return MainActivity.onCreate$lambda$65$lambda$64$lambda$46$lambda$21(SnackbarHostState.this, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), null, 0, Color.INSTANCE.m4194getTransparent0d7_KjU(), 0L, null, ComposableLambdaKt.rememberComposableLambda(2020852401, true, new Function3() { // from class: com.example.MainActivity$$ExternalSyntheticLambda23
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj, Object obj2, Object obj3) {
                    return MainActivity.onCreate$lambda$65$lambda$64$lambda$46$lambda$45(MainActivity.this, $deviceInfo$delegate, $latestRelease$delegate, $isNewUpdateAvailable$delegate, $downloadProgress$delegate, $uiState$delegate, (PaddingValues) obj, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer, 54), $composer, 806882352, 436);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$21(SnackbarHostState $snackbarHostState, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C107@4979L31:MainActivity.kt#to5c3");
        if (($changed & 3) != 2 || !$composer.getSkipping()) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1420445530, $changed, -1, "com.example.MainActivity.onCreate.<anonymous>.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:107)");
            }
            SnackbarHostKt.SnackbarHost($snackbarHostState, null, null, $composer, 6, 6);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01dc  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x037e  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x038a  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x03c3  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x04f3  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x04ff  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0538  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x06e2  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x06ee  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0725  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x07a4  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0808  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x087b  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x081a  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x07b4  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x073b  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x06f4  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x054e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0505  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x03d9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0390  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$20(final com.example.MainActivity r136, final androidx.compose.runtime.MutableState r137, androidx.compose.runtime.Composer r138, int r139) {
        /*
            Method dump skipped, instructions count: 2177
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivity.onCreate$lambda$65$lambda$64$lambda$46$lambda$20(com.example.MainActivity, androidx.compose.runtime.MutableState, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$20$lambda$19$lambda$18$lambda$15$lambda$14(MainActivity this$0) {
        this$0.getViewModel().checkForUpdates(false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$20$lambda$19$lambda$18$lambda$17$lambda$16(MutableState $showAboutDialog$delegate) {
        onCreate$lambda$65$lambda$64$lambda$9($showAboutDialog$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:101:0x0295 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:103:0x0259 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x021d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01e1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x01d3  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0287  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x02ff  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x033b  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0377  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x03b3  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x03ed  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0438  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x03fa A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:91:0x03c1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0385 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0349 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:97:0x030d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x02d1 A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45(final com.example.MainActivity r50, androidx.compose.runtime.State r51, androidx.compose.runtime.State r52, androidx.compose.runtime.State r53, androidx.compose.runtime.State r54, androidx.compose.runtime.State r55, androidx.compose.foundation.layout.PaddingValues r56, androidx.compose.runtime.Composer r57, int r58) {
        /*
            Method dump skipped, instructions count: 1086
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.MainActivity.onCreate$lambda$65$lambda$64$lambda$46$lambda$45(com.example.MainActivity, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.runtime.State, androidx.compose.foundation.layout.PaddingValues, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$23$lambda$22(MainActivity this$0, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.getViewModel().selectChannel(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$25$lambda$24(MainActivity this$0) {
        UpdaterViewModel.checkForUpdates$default(this$0.getViewModel(), false, 1, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$27$lambda$26(MainActivity this$0, OtaRelease it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.getViewModel().startDownload(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$29$lambda$28(MainActivity this$0) {
        this$0.getViewModel().pauseDownload();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$31$lambda$30(MainActivity this$0, OtaRelease it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.getViewModel().resumeDownload(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$33$lambda$32(MainActivity this$0) {
        this$0.getViewModel().cancelDownload();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$35$lambda$34(MainActivity this$0, OtaRelease it) {
        Intrinsics.checkNotNullParameter(it, "it");
        this$0.getViewModel().openInstallDialog(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$37$lambda$36(MainActivity this$0) {
        this$0.getViewModel().setDeviceDetailOpen(true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$39$lambda$38(MainActivity this$0) {
        this$0.getViewModel().setHistoryOpen(true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$41$lambda$40(MainActivity this$0) {
        this$0.getViewModel().setLocalInstallOpen(true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$46$lambda$45$lambda$44$lambda$43$lambda$42(MainActivity this$0) {
        this$0.getViewModel().resetUpdateState();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$48$lambda$47(MainActivity this$0) {
        this$0.getViewModel().setDeviceDetailOpen(false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$50$lambda$49(MainActivity this$0) {
        this$0.getViewModel().setHistoryOpen(false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$52$lambda$51(MainActivity this$0) {
        this$0.getViewModel().setLocalInstallOpen(false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$54$lambda$53(MainActivity this$0) {
        this$0.getViewModel().showSnackbar("Local package queued at /sdcard/Download/OTA/rom.zip");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$58$lambda$57(MainActivity this$0) {
        this$0.getViewModel().executeSystemUpdate();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$56$lambda$55(MainActivity this$0) {
        this$0.getViewModel().closeInstallDialog();
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$60$lambda$59(MutableState $showAboutDialog$delegate) {
        onCreate$lambda$65$lambda$64$lambda$9($showAboutDialog$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$63(final MutableState $showAboutDialog$delegate, Composer $composer, int $changed) {
        Object obj;
        ComposerKt.sourceInformation($composer, "C292@15728L43,291@15643L27,290@15593L380:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-2110006182, $changed, -1, "com.example.MainActivity.onCreate.<anonymous>.<anonymous>.<anonymous> (MainActivity.kt:290)");
            }
            ButtonColors m1809buttonColorsro_MJ88 = ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(ColorKt.getGlassPrimary(), 0L, 0L, 0L, $composer, (ButtonDefaults.$stable << 12) | 6, 14);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(28));
            ComposerKt.sourceInformationMarkerStart($composer, -491374603, "CC(remember):MainActivity.kt#9igjgp");
            Object rememberedValue = $composer.rememberedValue();
            if (rememberedValue == Composer.INSTANCE.getEmpty()) {
                obj = new Function0() { // from class: com.example.MainActivity$$ExternalSyntheticLambda20
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return MainActivity.onCreate$lambda$65$lambda$64$lambda$63$lambda$62$lambda$61(MutableState.this);
                    }
                };
                $composer.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.Button((Function0) obj, null, false, m953RoundedCornerShape0680j_4, m1809buttonColorsro_MJ88, null, null, null, null, ComposableSingletons$MainActivityKt.INSTANCE.m6985getLambda$529824694$app(), $composer, 805306374, 486);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit onCreate$lambda$65$lambda$64$lambda$63$lambda$62$lambda$61(MutableState $showAboutDialog$delegate) {
        onCreate$lambda$65$lambda$64$lambda$9($showAboutDialog$delegate, false);
        return Unit.INSTANCE;
    }
}
