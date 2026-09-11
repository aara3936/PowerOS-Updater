package com.example;

import androidx.compose.foundation.layout.RowScope;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.InfoKt;
import androidx.compose.material.icons.filled.RefreshKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import com.example.ui.theme.ColorKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MainActivity.kt */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes8.dex */
public final class ComposableSingletons$MainActivityKt {
    public static final ComposableSingletons$MainActivityKt INSTANCE = new ComposableSingletons$MainActivityKt();
    private static Function2<Composer, Integer, Unit> lambda$1057443264 = ComposableLambdaKt.composableLambdaInstance(1057443264, false, new Function2() { // from class: com.example.ComposableSingletons$MainActivityKt$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$MainActivityKt.lambda_1057443264$lambda$0((Composer) obj, ((Integer) obj2).intValue());
        }
    });
    private static Function2<Composer, Integer, Unit> lambda$625132585 = ComposableLambdaKt.composableLambdaInstance(625132585, false, new Function2() { // from class: com.example.ComposableSingletons$MainActivityKt$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$MainActivityKt.lambda_625132585$lambda$1((Composer) obj, ((Integer) obj2).intValue());
        }
    });

    /* renamed from: lambda$-529824694, reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f88lambda$529824694 = ComposableLambdaKt.composableLambdaInstance(-529824694, false, new Function3() { // from class: com.example.ComposableSingletons$MainActivityKt$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$MainActivityKt.lambda__529824694$lambda$2((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });
    private static Function2<Composer, Integer, Unit> lambda$743122270 = ComposableLambdaKt.composableLambdaInstance(743122270, false, new Function2() { // from class: com.example.ComposableSingletons$MainActivityKt$$ExternalSyntheticLambda3
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$MainActivityKt.lambda_743122270$lambda$5((Composer) obj, ((Integer) obj2).intValue());
        }
    });
    private static Function2<Composer, Integer, Unit> lambda$382662559 = ComposableLambdaKt.composableLambdaInstance(382662559, false, new Function2() { // from class: com.example.ComposableSingletons$MainActivityKt$$ExternalSyntheticLambda4
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$MainActivityKt.lambda_382662559$lambda$7((Composer) obj, ((Integer) obj2).intValue());
        }
    });

    /* renamed from: getLambda$-529824694$app, reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m6985getLambda$529824694$app() {
        return f88lambda$529824694;
    }

    public final Function2<Composer, Integer, Unit> getLambda$1057443264$app() {
        return lambda$1057443264;
    }

    public final Function2<Composer, Integer, Unit> getLambda$382662559$app() {
        return lambda$382662559;
    }

    public final Function2<Composer, Integer, Unit> getLambda$625132585$app() {
        return lambda$625132585;
    }

    public final Function2<Composer, Integer, Unit> getLambda$743122270$app() {
        return lambda$743122270;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda_1057443264$lambda$0(Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C163@8511L281:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1057443264, $changed, -1, "com.example.ComposableSingletons$MainActivityKt.lambda$1057443264.<anonymous> (MainActivity.kt:163)");
            }
            IconKt.m2150Iconww6aTOc(RefreshKt.getRefresh(Icons.INSTANCE.getDefault()), "Refresh", (Modifier) null, ColorKt.getNaturalLightTextSecondary(), $composer, 3120, 4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda_625132585$lambda$1(Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C174@9123L285:MainActivity.kt#to5c3");
        if (($changed & 3) == 2 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(625132585, $changed, -1, "com.example.ComposableSingletons$MainActivityKt.lambda$625132585.<anonymous> (MainActivity.kt:174)");
            }
            IconKt.m2150Iconww6aTOc(InfoKt.getInfo(Icons.INSTANCE.getDefault()), "About Power OS", (Modifier) null, ColorKt.getNaturalLightTextSecondary(), $composer, 3120, 4);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01e4  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x01f0  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x037b  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x01f6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit lambda_743122270$lambda$5(androidx.compose.runtime.Composer r90, int r91) {
        /*
            Method dump skipped, instructions count: 897
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ComposableSingletons$MainActivityKt.lambda_743122270$lambda$5(androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01e8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit lambda_382662559$lambda$7(androidx.compose.runtime.Composer r81, int r82) {
        /*
            Method dump skipped, instructions count: 494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ComposableSingletons$MainActivityKt.lambda_382662559$lambda$7(androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit lambda__529824694$lambda$2(RowScope Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C295@15903L40:MainActivity.kt#to5c3");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-529824694, $changed, -1, "com.example.ComposableSingletons$MainActivityKt.lambda$-529824694.<anonymous> (MainActivity.kt:295)");
            }
            TextKt.m2693Text4IGK_g("OK", (Modifier) null, 0L, 0L, (FontStyle) null, FontWeight.INSTANCE.getBold(), (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 196614, 0, 131038);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
