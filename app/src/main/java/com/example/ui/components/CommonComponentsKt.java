package com.example.ui.components;

import androidx.compose.animation.core.AnimateAsStateKt;
import androidx.compose.animation.core.AnimationSpecKt;
import androidx.compose.foundation.BackgroundKt;
import androidx.compose.foundation.BorderKt;
import androidx.compose.foundation.BorderStroke;
import androidx.compose.foundation.interaction.InteractionSourceKt;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.interaction.PressInteractionKt;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.BoxScope;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.RowKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.RowScopeInstance;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.shape.RoundedCornerShape;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material3.ButtonDefaults;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.SurfaceKt;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Applier;
import androidx.compose.runtime.ComposablesKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionLocalMap;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.State;
import androidx.compose.runtime.Updater;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.ComposedModifierKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.draw.ClipKt;
import androidx.compose.ui.draw.ShadowKt;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.GraphicsLayerModifierKt;
import androidx.compose.ui.graphics.GraphicsLayerScope;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.layout.MeasurePolicy;
import androidx.compose.ui.node.ComposeUiNode;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.compose.ui.unit.TextUnitKt;
import androidx.profileinstaller.ProfileVerifier;
import com.example.ui.theme.ColorKt;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CommonComponents.kt */
@Metadata(d1 = {"\u0000h\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0002\u001a#\u0010\u0000\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u0007¢\u0006\u0004\b\u0006\u0010\u0007\u001a5\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007¢\u0006\u0004\b\r\u0010\u000e\u001ac\u0010\u000f\u001a\u00020\u00012\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\b\u0002\u0010\u0016\u001a\u00020\u00172\u001c\u0010\u0018\u001a\u0018\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010\u0019¢\u0006\u0002\b\u001b¢\u0006\u0002\b\u001cH\u0007¢\u0006\u0004\b\u001d\u0010\u001e\u001aA\u0010\u001f\u001a\u00020\u00012\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\n0!2\u0006\u0010\"\u001a\u00020\n2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\u00192\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u0007¢\u0006\u0002\u0010$\u001aZ\u0010%\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010'2\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0015\b\u0002\u0010(\u001a\u000f\u0012\u0004\u0012\u00020\u0001\u0018\u00010'¢\u0006\u0002\b\u001b2\b\b\u0002\u0010)\u001a\u00020\fH\u0007¢\u0006\u0004\b*\u0010+¨\u0006,²\u0006\n\u0010-\u001a\u00020.X\u008a\u0084\u0002²\u0006\n\u0010/\u001a\u00020\fX\u008a\u0084\u0002²\u0006\n\u00100\u001a\u00020.X\u008a\u0084\u0002"}, d2 = {"PulsingStatusDot", "", "modifier", "Landroidx/compose/ui/Modifier;", "color", "Landroidx/compose/ui/graphics/Color;", "PulsingStatusDot-iJQMabo", "(Landroidx/compose/ui/Modifier;JLandroidx/compose/runtime/Composer;II)V", "StatusBadge", "text", "", "isPulsing", "", "StatusBadge-cf5BqRc", "(Ljava/lang/String;Landroidx/compose/ui/Modifier;JZLandroidx/compose/runtime/Composer;II)V", "LiquidGlassCard", "shape", "Landroidx/compose/ui/graphics/Shape;", "accentGradient", "Landroidx/compose/ui/graphics/Brush;", "borderStroke", "Landroidx/compose/foundation/BorderStroke;", "contentPadding", "Landroidx/compose/ui/unit/Dp;", "content", "Lkotlin/Function1;", "Landroidx/compose/foundation/layout/BoxScope;", "Landroidx/compose/runtime/Composable;", "Lkotlin/ExtensionFunctionType;", "LiquidGlassCard-WH-ejsw", "(Landroidx/compose/ui/Modifier;Landroidx/compose/ui/graphics/Shape;Landroidx/compose/ui/graphics/Brush;Landroidx/compose/foundation/BorderStroke;FLkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;II)V", "GlassChannelSegmentedBar", "channels", "", "selectedChannel", "onSelectChannel", "(Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "GlassButton", "onClick", "Lkotlin/Function0;", "leadingIcon", "enabled", "GlassButton-fWhpE4E", "(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;JLkotlin/jvm/functions/Function2;ZLandroidx/compose/runtime/Composer;II)V", "app", "alpha", "", "isPressed", "scale"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes8.dex */
public final class CommonComponentsKt {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GlassButton_fWhpE4E$lambda$21(String str, Function0 function0, Modifier modifier, long j, Function2 function2, boolean z, int i, int i2, Composer composer, int i3) {
        m6993GlassButtonfWhpE4E(str, function0, modifier, j, function2, z, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GlassChannelSegmentedBar$lambda$13(List list, String str, Function1 function1, Modifier modifier, int i, int i2, Composer composer, int i3) {
        GlassChannelSegmentedBar(list, str, function1, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit LiquidGlassCard_WH_ejsw$lambda$7(Modifier modifier, Shape shape, Brush brush, BorderStroke borderStroke, float f, Function3 function3, int i, int i2, Composer composer, int i3) {
        m6994LiquidGlassCardWHejsw(modifier, shape, brush, borderStroke, f, function3, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit PulsingStatusDot_iJQMabo$lambda$2(Modifier modifier, long j, int i, int i2, Composer composer, int i3) {
        m6995PulsingStatusDotiJQMabo(modifier, j, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit StatusBadge_cf5BqRc$lambda$4(String str, Modifier modifier, long j, boolean z, int i, int i2, Composer composer, int i3) {
        m6996StatusBadgecf5BqRc(str, modifier, j, z, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x025a  */
    /* renamed from: PulsingStatusDot-iJQMabo, reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void m6995PulsingStatusDotiJQMabo(androidx.compose.ui.Modifier r38, long r39, androidx.compose.runtime.Composer r41, final int r42, final int r43) {
        /*
            Method dump skipped, instructions count: 626
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CommonComponentsKt.m6995PulsingStatusDotiJQMabo(androidx.compose.ui.Modifier, long, androidx.compose.runtime.Composer, int, int):void");
    }

    private static final float PulsingStatusDot_iJQMabo$lambda$0(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* renamed from: StatusBadge-cf5BqRc, reason: not valid java name */
    public static final void m6996StatusBadgecf5BqRc(final String text, Modifier modifier, long color, boolean isPulsing, Composer $composer, final int $changed, final int i) {
        Modifier modifier2;
        long j;
        boolean z;
        Modifier.Companion modifier3;
        long color2;
        boolean isPulsing2;
        long m4157copywmQWz5c;
        long m4157copywmQWz5c2;
        long color3;
        Composer $composer2;
        final long color4;
        final boolean isPulsing3;
        final Modifier modifier4;
        Intrinsics.checkNotNullParameter(text, "text");
        Composer $composer3 = $composer.startRestartGroup(207052504);
        ComposerKt.sourceInformation($composer3, "C(StatusBadge)P(3,2,0:c#ui.graphics.Color)141@5142L705:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(text) ? 4 : 2;
        }
        int i2 = i & 2;
        if (i2 != 0) {
            $dirty |= 48;
            modifier2 = modifier;
        } else if (($changed & 48) == 0) {
            modifier2 = modifier;
            $dirty |= $composer3.changed(modifier2) ? 32 : 16;
        } else {
            modifier2 = modifier;
        }
        int i3 = i & 4;
        if (i3 != 0) {
            $dirty |= 384;
            j = color;
        } else if (($changed & 384) == 0) {
            j = color;
            $dirty |= $composer3.changed(j) ? 256 : 128;
        } else {
            j = color;
        }
        int i4 = i & 8;
        if (i4 != 0) {
            $dirty |= 3072;
            z = isPulsing;
        } else if (($changed & 3072) == 0) {
            z = isPulsing;
            $dirty |= $composer3.changed(z) ? 2048 : 1024;
        } else {
            z = isPulsing;
        }
        if (($dirty & 1171) == 1170 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
            modifier4 = modifier2;
            color4 = j;
            isPulsing3 = z;
        } else {
            if (i2 != 0) {
                modifier3 = Modifier.INSTANCE;
            } else {
                modifier3 = modifier2;
            }
            if (i3 == 0) {
                color2 = j;
            } else {
                color2 = ColorKt.getGlassPrimary();
            }
            if (i4 == 0) {
                isPulsing2 = z;
            } else {
                isPulsing2 = false;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(207052504, $dirty, -1, "com.example.ui.components.StatusBadge (CommonComponents.kt:140)");
            }
            Modifier clip = ClipKt.clip(modifier3, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(24)));
            long color5 = color2;
            m4157copywmQWz5c = Color.m4157copywmQWz5c(color5, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(color5) : 0.12f, (r12 & 2) != 0 ? Color.m4165getRedimpl(color5) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(color5) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(color5) : 0.0f);
            Modifier m225backgroundbw27NRU$default = BackgroundKt.m225backgroundbw27NRU$default(clip, m4157copywmQWz5c, null, 2, null);
            float m6622constructorimpl = Dp.m6622constructorimpl(1);
            m4157copywmQWz5c2 = Color.m4157copywmQWz5c(color5, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(color5) : 0.3f, (r12 & 2) != 0 ? Color.m4165getRedimpl(color5) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(color5) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(color5) : 0.0f);
            Modifier m671paddingVpY3zN4 = PaddingKt.m671paddingVpY3zN4(BorderKt.m236borderxT4_qwU(m225backgroundbw27NRU$default, m6622constructorimpl, m4157copywmQWz5c2, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(24))), Dp.m6622constructorimpl(12), Dp.m6622constructorimpl(6));
            Alignment.Vertical centerVertically = Alignment.INSTANCE.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart($composer3, 693286680, "CC(Row)P(2,1,3)98@4939L58,99@5002L130:Row.kt#2w3rfo");
            MeasurePolicy rowMeasurePolicy = RowKt.rowMeasurePolicy(Arrangement.INSTANCE.getStart(), centerVertically, $composer3, ((384 >> 3) & 14) | ((384 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer3, -1323940314, "CC(Layout)P(!1,2)78@3182L23,81@3333L411:Layout.kt#80mrfh");
            int currentCompositeKeyHash = ComposablesKt.getCurrentCompositeKeyHash($composer3, 0);
            CompositionLocalMap currentCompositionLocalMap = $composer3.getCurrentCompositionLocalMap();
            Modifier materializeModifier = ComposedModifierKt.materializeModifier($composer3, m671paddingVpY3zN4);
            Function0 constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i5 = ((((384 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, -692256719, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!($composer3.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer3.startReusableNode();
            if ($composer3.getInserting()) {
                $composer3.createNode(constructor);
            } else {
                $composer3.useNode();
            }
            Composer m3652constructorimpl = Updater.m3652constructorimpl($composer3);
            Updater.m3659setimpl(m3652constructorimpl, rowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m3659setimpl(m3652constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2 setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (m3652constructorimpl.getInserting() || !Intrinsics.areEqual(m3652constructorimpl.rememberedValue(), Integer.valueOf(currentCompositeKeyHash))) {
                m3652constructorimpl.updateRememberedValue(Integer.valueOf(currentCompositeKeyHash));
                m3652constructorimpl.apply(Integer.valueOf(currentCompositeKeyHash), setCompositeKeyHash);
            }
            Updater.m3659setimpl(m3652constructorimpl, materializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i6 = (i5 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer3, -407918630, "C100@5047L9:Row.kt#2w3rfo");
            RowScopeInstance rowScopeInstance = RowScopeInstance.INSTANCE;
            int i7 = ((384 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, 867637663, "C155@5677L10,153@5612L229:CommonComponents.kt#qonjpd");
            if (isPulsing2) {
                $composer3.startReplaceGroup(867644699);
                ComposerKt.sourceInformation($composer3, "150@5510L31,151@5554L39");
                m6995PulsingStatusDotiJQMabo(null, color5, $composer3, ($dirty >> 3) & 112, 1);
                color3 = color5;
                SpacerKt.Spacer(SizeKt.m720width3ABfNKs(Modifier.INSTANCE, Dp.m6622constructorimpl(6)), $composer3, 6);
            } else {
                color3 = color5;
                $composer3.startReplaceGroup(862188358);
            }
            $composer3.endReplaceGroup();
            Modifier modifier5 = modifier3;
            long color6 = color3;
            $composer2 = $composer3;
            TextKt.m2693Text4IGK_g(text, (Modifier) null, color6, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, TextStyle.m6101copyp1EtxEg$default(MaterialTheme.INSTANCE.getTypography($composer3, MaterialTheme.$stable).getLabelMedium(), 0L, 0L, FontWeight.INSTANCE.getBold(), null, null, null, null, TextUnitKt.getSp(0.5d), null, null, null, 0L, null, null, null, 0, 0, 0L, null, null, null, 0, 0, null, 16777083, null), $composer3, ($dirty & 14) | ($dirty & 896), 0, 65530);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer3.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            color4 = color6;
            isPulsing3 = isPulsing2;
            modifier4 = modifier5;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda8
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CommonComponentsKt.StatusBadge_cf5BqRc$lambda$4(text, modifier4, color4, isPulsing3, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r16v1 */
    /* JADX WARN: Type inference failed for: r16v4 */
    /* renamed from: LiquidGlassCard-WH-ejsw, reason: not valid java name */
    public static final void m6994LiquidGlassCardWHejsw(Modifier modifier, Shape shape, Brush accentGradient, BorderStroke borderStroke, float contentPadding, final Function3<? super BoxScope, ? super Composer, ? super Integer, Unit> content, Composer $composer, final int $changed, final int i) {
        Modifier modifier2;
        Shape shape2;
        Brush accentGradient2;
        BorderStroke borderStroke2;
        float f;
        int r16;
        Composer $composer2;
        Modifier modifier3;
        Shape shape3;
        final Brush accentGradient3;
        final float contentPadding2;
        boolean z;
        BorderStroke borderStroke3;
        final float contentPadding3;
        final Modifier modifier4;
        final Shape shape4;
        final Brush accentGradient4;
        final BorderStroke borderStroke4;
        int i2;
        Intrinsics.checkNotNullParameter(content, "content");
        Composer $composer3 = $composer.startRestartGroup(-1806956704);
        ComposerKt.sourceInformation($composer3, "C(LiquidGlassCard)P(4,5!2,3:c#ui.unit.Dp)199@7125L582,188@6796L911:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        int i3 = i & 1;
        if (i3 != 0) {
            $dirty |= 6;
            modifier2 = modifier;
        } else if (($changed & 6) == 0) {
            modifier2 = modifier;
            $dirty |= $composer3.changed(modifier2) ? 4 : 2;
        } else {
            modifier2 = modifier;
        }
        if (($changed & 48) == 0) {
            if ((i & 2) == 0) {
                shape2 = shape;
                if ($composer3.changed(shape2)) {
                    i2 = 32;
                    $dirty |= i2;
                }
            } else {
                shape2 = shape;
            }
            i2 = 16;
            $dirty |= i2;
        } else {
            shape2 = shape;
        }
        int i4 = i & 4;
        if (i4 != 0) {
            $dirty |= 384;
            accentGradient2 = accentGradient;
        } else if (($changed & 384) == 0) {
            accentGradient2 = accentGradient;
            $dirty |= $composer3.changed(accentGradient2) ? 256 : 128;
        } else {
            accentGradient2 = accentGradient;
        }
        int i5 = i & 8;
        if (i5 != 0) {
            $dirty |= 3072;
            borderStroke2 = borderStroke;
        } else if (($changed & 3072) == 0) {
            borderStroke2 = borderStroke;
            $dirty |= $composer3.changed(borderStroke2) ? 2048 : 1024;
        } else {
            borderStroke2 = borderStroke;
        }
        int i6 = i & 16;
        if (i6 != 0) {
            $dirty |= 24576;
            f = contentPadding;
        } else if (($changed & 24576) == 0) {
            f = contentPadding;
            $dirty |= $composer3.changed(f) ? 16384 : 8192;
        } else {
            f = contentPadding;
        }
        if ((196608 & $changed) == 0) {
            $dirty |= $composer3.changedInstance(content) ? 131072 : 65536;
        }
        if ((74899 & $dirty) == 74898 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            $composer2 = $composer3;
            modifier4 = modifier2;
            shape4 = shape2;
            accentGradient4 = accentGradient2;
            borderStroke4 = borderStroke2;
            contentPadding3 = f;
        } else {
            $composer3.startDefaults();
            if (($changed & 1) == 0 || $composer3.getDefaultsInvalid()) {
                Modifier.Companion modifier5 = i3 != 0 ? Modifier.INSTANCE : modifier2;
                if ((i & 2) != 0) {
                    r16 = 1;
                    $dirty &= -113;
                    shape2 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(28));
                } else {
                    r16 = 1;
                }
                if (i4 != 0) {
                    accentGradient2 = null;
                }
                if (i5 != 0) {
                    $composer2 = $composer3;
                    float m6622constructorimpl = Dp.m6622constructorimpl((float) 1.5d);
                    Brush.Companion companion = Brush.INSTANCE;
                    Color[] colorArr = new Color[3];
                    colorArr[0] = Color.m4149boximpl(ColorKt.getLiquidGlassStrokeTop());
                    colorArr[r16] = Color.m4149boximpl(androidx.compose.ui.graphics.ColorKt.Color(654311423));
                    colorArr[2] = Color.m4149boximpl(ColorKt.getLiquidGlassStrokeBottom());
                    borderStroke2 = new BorderStroke(m6622constructorimpl, Brush.Companion.m4116verticalGradient8A3gB4$default(companion, CollectionsKt.listOf((Object[]) colorArr), 0.0f, 0.0f, 0, 14, (Object) null), null);
                } else {
                    $composer2 = $composer3;
                }
                if (i6 != 0) {
                    contentPadding2 = Dp.m6622constructorimpl(20);
                    modifier3 = modifier5;
                    shape3 = shape2;
                    accentGradient3 = accentGradient2;
                    z = r16;
                    borderStroke3 = borderStroke2;
                } else {
                    modifier3 = modifier5;
                    shape3 = shape2;
                    accentGradient3 = accentGradient2;
                    contentPadding2 = f;
                    z = r16;
                    borderStroke3 = borderStroke2;
                }
            } else {
                $composer3.skipToGroupEnd();
                if ((i & 2) != 0) {
                    $dirty &= -113;
                }
                $composer2 = $composer3;
                modifier3 = modifier2;
                shape3 = shape2;
                accentGradient3 = accentGradient2;
                borderStroke3 = borderStroke2;
                contentPadding2 = f;
                z = true;
            }
            $composer2.endDefaults();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1806956704, $dirty, -1, "com.example.ui.components.LiquidGlassCard (CommonComponents.kt:187)");
            }
            Modifier modifier6 = modifier3;
            SurfaceKt.m2543SurfaceT9BRK9s(ShadowKt.m3823shadows4CzXII$default(modifier3, Dp.m6622constructorimpl(12), shape3, false, androidx.compose.ui.graphics.ColorKt.Color(1711276032), androidx.compose.ui.graphics.ColorKt.Color(859356664), 4, null), shape3, Color.INSTANCE.m4194getTransparent0d7_KjU(), 0L, 0.0f, 0.0f, borderStroke3, ComposableLambdaKt.rememberComposableLambda(902574309, z, new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CommonComponentsKt.LiquidGlassCard_WH_ejsw$lambda$6(Brush.this, contentPadding2, content, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer2, 54), $composer2, ($dirty & 112) | 12583296 | (($dirty << 9) & 3670016), 56);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            contentPadding3 = contentPadding2;
            modifier4 = modifier6;
            shape4 = shape3;
            accentGradient4 = accentGradient3;
            borderStroke4 = borderStroke3;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return CommonComponentsKt.LiquidGlassCard_WH_ejsw$lambda$7(Modifier.this, shape4, accentGradient4, borderStroke4, contentPadding3, content, $changed, i, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:28:0x019a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final kotlin.Unit LiquidGlassCard_WH_ejsw$lambda$6(androidx.compose.ui.graphics.Brush r26, float r27, kotlin.jvm.functions.Function3 r28, androidx.compose.runtime.Composer r29, int r30) {
        /*
            Method dump skipped, instructions count: 416
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CommonComponentsKt.LiquidGlassCard_WH_ejsw$lambda$6(androidx.compose.ui.graphics.Brush, float, kotlin.jvm.functions.Function3, androidx.compose.runtime.Composer, int):kotlin.Unit");
    }

    /* JADX WARN: Removed duplicated region for block: B:113:0x06aa  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x02d7  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x05d5  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0621  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0628  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x05da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void GlassChannelSegmentedBar(final java.util.List<java.lang.String> r121, final java.lang.String r122, final kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> r123, androidx.compose.ui.Modifier r124, androidx.compose.runtime.Composer r125, final int r126, final int r127) {
        /*
            Method dump skipped, instructions count: 1750
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.example.ui.components.CommonComponentsKt.GlassChannelSegmentedBar(java.util.List, java.lang.String, kotlin.jvm.functions.Function1, androidx.compose.ui.Modifier, androidx.compose.runtime.Composer, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GlassChannelSegmentedBar$lambda$12$lambda$11$lambda$9$lambda$8(Function1 $onSelectChannel, String $channel) {
        $onSelectChannel.invoke($channel);
        return Unit.INSTANCE;
    }

    /* renamed from: GlassButton-fWhpE4E, reason: not valid java name */
    public static final void m6993GlassButtonfWhpE4E(final String text, final Function0<Unit> onClick, Modifier modifier, long color, Function2<? super Composer, ? super Integer, Unit> function2, boolean enabled, Composer $composer, final int $changed, final int i) {
        Modifier modifier2;
        long j;
        Function2 leadingIcon;
        boolean z;
        Modifier.Companion modifier3;
        long color2;
        boolean enabled2;
        Object obj;
        Object obj2;
        long m4157copywmQWz5c;
        long m4157copywmQWz5c2;
        Composer $composer2;
        final Function2 leadingIcon2;
        final boolean enabled3;
        final Modifier modifier4;
        final long color3;
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(onClick, "onClick");
        Composer $composer3 = $composer.startRestartGroup(224460744);
        ComposerKt.sourceInformation($composer3, "C(GlassButton)P(5,4,3,0:c#ui.graphics.Color,2)327@11713L39,328@11792L25,330@11836L261,344@12267L77,365@12984L208,372@13277L478,339@12103L1652:CommonComponents.kt#qonjpd");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(text) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(onClick) ? 32 : 16;
        }
        int i2 = i & 4;
        if (i2 != 0) {
            $dirty |= 384;
            modifier2 = modifier;
        } else if (($changed & 384) == 0) {
            modifier2 = modifier;
            $dirty |= $composer3.changed(modifier2) ? 256 : 128;
        } else {
            modifier2 = modifier;
        }
        int i3 = i & 8;
        if (i3 != 0) {
            $dirty |= 3072;
            j = color;
        } else if (($changed & 3072) == 0) {
            j = color;
            $dirty |= $composer3.changed(j) ? 2048 : 1024;
        } else {
            j = color;
        }
        int i4 = i & 16;
        if (i4 != 0) {
            $dirty |= 24576;
            leadingIcon = function2;
        } else if (($changed & 24576) == 0) {
            leadingIcon = function2;
            $dirty |= $composer3.changedInstance(leadingIcon) ? 16384 : 8192;
        } else {
            leadingIcon = function2;
        }
        int i5 = i & 32;
        if (i5 != 0) {
            $dirty |= ProfileVerifier.CompilationStatus.RESULT_CODE_ERROR_CANT_WRITE_PROFILE_VERIFICATION_RESULT_CACHE_FILE;
            z = enabled;
        } else if (($changed & ProfileVerifier.CompilationStatus.RESULT_CODE_ERROR_CANT_WRITE_PROFILE_VERIFICATION_RESULT_CACHE_FILE) == 0) {
            z = enabled;
            $dirty |= $composer3.changed(z) ? 131072 : 65536;
        } else {
            z = enabled;
        }
        if (($dirty & 74899) == 74898 && $composer3.getSkipping()) {
            $composer3.skipToGroupEnd();
            modifier4 = modifier2;
            color3 = j;
            enabled3 = z;
            leadingIcon2 = leadingIcon;
            $composer2 = $composer3;
        } else {
            if (i2 != 0) {
                modifier3 = Modifier.INSTANCE;
            } else {
                modifier3 = modifier2;
            }
            if (i3 == 0) {
                color2 = j;
            } else {
                color2 = ColorKt.getGlassPrimary();
            }
            if (i4 != 0) {
                leadingIcon = null;
            }
            if (i5 == 0) {
                enabled2 = z;
            } else {
                enabled2 = true;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(224460744, $dirty, -1, "com.example.ui.components.GlassButton (CommonComponents.kt:326)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 958418255, "CC(remember):CommonComponents.kt#9igjgp");
            Object rememberedValue = $composer3.rememberedValue();
            if (rememberedValue == Composer.INSTANCE.getEmpty()) {
                obj = InteractionSourceKt.MutableInteractionSource();
                $composer3.updateRememberedValue(obj);
            } else {
                obj = rememberedValue;
            }
            MutableInteractionSource interactionSource = (MutableInteractionSource) obj;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            State isPressed$delegate = PressInteractionKt.collectIsPressedAsState(interactionSource, $composer3, 6);
            int $dirty2 = $dirty;
            final State scale$delegate = AnimateAsStateKt.animateFloatAsState(GlassButton_fWhpE4E$lambda$15(isPressed$delegate) ? 1064346583 : 1065353216, AnimationSpecKt.spring$default(0.75f, 200.0f, null, 4, null), 0.0f, "glass_button_scale", null, $composer3, 3120, 20);
            ComposerKt.sourceInformationMarkerStart($composer3, 958436021, "CC(remember):CommonComponents.kt#9igjgp");
            boolean changed = $composer3.changed(scale$delegate);
            Object rememberedValue2 = $composer3.rememberedValue();
            if (changed || rememberedValue2 == Composer.INSTANCE.getEmpty()) {
                obj2 = new Function1() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj3) {
                        return CommonComponentsKt.GlassButton_fWhpE4E$lambda$18$lambda$17(State.this, (GraphicsLayerScope) obj3);
                    }
                };
                $composer3.updateRememberedValue(obj2);
            } else {
                obj2 = rememberedValue2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            Modifier graphicsLayer = GraphicsLayerModifierKt.graphicsLayer(modifier3, (Function1) obj2);
            float m6622constructorimpl = GlassButton_fWhpE4E$lambda$15(isPressed$delegate) ? Dp.m6622constructorimpl(2) : Dp.m6622constructorimpl(8);
            RoundedCornerShape m953RoundedCornerShape0680j_4 = RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(28));
            m4157copywmQWz5c = Color.m4157copywmQWz5c(color2, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(color2) : 0.5f, (r12 & 2) != 0 ? Color.m4165getRedimpl(color2) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(color2) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(color2) : 0.0f);
            Modifier m3823shadows4CzXII$default = ShadowKt.m3823shadows4CzXII$default(graphicsLayer, m6622constructorimpl, m953RoundedCornerShape0680j_4, false, androidx.compose.ui.graphics.ColorKt.Color(1073741824), m4157copywmQWz5c, 4, null);
            float m6622constructorimpl2 = Dp.m6622constructorimpl((float) 1.5d);
            Brush.Companion companion = Brush.INSTANCE;
            m4157copywmQWz5c2 = Color.m4157copywmQWz5c(r29, (r12 & 1) != 0 ? Color.m4161getAlphaimpl(r29) : 0.65f, (r12 & 2) != 0 ? Color.m4165getRedimpl(r29) : 0.0f, (r12 & 4) != 0 ? Color.m4164getGreenimpl(r29) : 0.0f, (r12 & 8) != 0 ? Color.m4162getBlueimpl(Color.INSTANCE.m4196getWhite0d7_KjU()) : 0.0f);
            final Function2 leadingIcon3 = leadingIcon;
            long color4 = color2;
            Modifier modifier5 = modifier3;
            $composer2 = $composer3;
            boolean enabled4 = enabled2;
            ButtonKt.Button(onClick, BorderKt.m238borderziNgDLE(m3823shadows4CzXII$default, m6622constructorimpl2, Brush.Companion.m4116verticalGradient8A3gB4$default(companion, CollectionsKt.listOf((Object[]) new Color[]{Color.m4149boximpl(m4157copywmQWz5c2), Color.m4149boximpl(ColorKt.getLiquidGlassStrokeBottom())}), 0.0f, 0.0f, 0, 14, (Object) null), RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(28))), enabled4, RoundedCornerShapeKt.m953RoundedCornerShape0680j_4(Dp.m6622constructorimpl(28)), ButtonDefaults.INSTANCE.m1809buttonColorsro_MJ88(color4, Color.INSTANCE.m4196getWhite0d7_KjU(), androidx.compose.ui.graphics.ColorKt.Color(4281549141L), androidx.compose.ui.graphics.ColorKt.Color(4287931320L), $composer3, (($dirty2 >> 9) & 14) | 3504 | (ButtonDefaults.$stable << 12), 0), null, null, PaddingKt.m664PaddingValuesYgX7TsA(Dp.m6622constructorimpl(24), Dp.m6622constructorimpl(14)), interactionSource, ComposableLambdaKt.rememberComposableLambda(1180509112, true, new Function3() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj3, Object obj4, Object obj5) {
                    return CommonComponentsKt.GlassButton_fWhpE4E$lambda$20(Function2.this, text, (RowScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                }
            }, $composer3, 54), $composer2, (($dirty2 >> 3) & 14) | 918552576 | (($dirty2 >> 9) & 896), 96);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            leadingIcon2 = leadingIcon3;
            enabled3 = enabled4;
            modifier4 = modifier5;
            color3 = color4;
        }
        ScopeUpdateScope endRestartGroup = $composer2.endRestartGroup();
        if (endRestartGroup != null) {
            endRestartGroup.updateScope(new Function2() { // from class: com.example.ui.components.CommonComponentsKt$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return CommonComponentsKt.GlassButton_fWhpE4E$lambda$21(text, onClick, modifier4, color3, leadingIcon2, enabled3, $changed, i, (Composer) obj3, ((Integer) obj4).intValue());
                }
            });
        }
    }

    private static final boolean GlassButton_fWhpE4E$lambda$15(State<Boolean> state) {
        return ((Boolean) state.getValue()).booleanValue();
    }

    private static final float GlassButton_fWhpE4E$lambda$16(State<Float> state) {
        return ((Number) state.getValue()).floatValue();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GlassButton_fWhpE4E$lambda$18$lambda$17(State $scale$delegate, GraphicsLayerScope graphicsLayer) {
        Intrinsics.checkNotNullParameter(graphicsLayer, "$this$graphicsLayer");
        graphicsLayer.setScaleX(GlassButton_fWhpE4E$lambda$16($scale$delegate));
        graphicsLayer.setScaleY(GlassButton_fWhpE4E$lambda$16($scale$delegate));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final Unit GlassButton_fWhpE4E$lambda$20(Function2 $leadingIcon, String $text, RowScope Button, Composer $composer, int $changed) {
        Function0 function0;
        Intrinsics.checkNotNullParameter(Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C373@13287L462:CommonComponents.kt#qonjpd");
        if (($changed & 17) == 16 && $composer.getSkipping()) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1180509112, $changed, -1, "com.example.ui.components.GlassButton.<anonymous> (CommonComponents.kt:373)");
            }
            Alignment.Vertical centerVertically = Alignment.INSTANCE.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart($composer, 693286680, "CC(Row)P(2,1,3)98@4939L58,99@5002L130:Row.kt#2w3rfo");
            Modifier modifier = Modifier.INSTANCE;
            MeasurePolicy rowMeasurePolicy = RowKt.rowMeasurePolicy(Arrangement.INSTANCE.getStart(), centerVertically, $composer, ((384 >> 3) & 14) | ((384 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer, -1323940314, "CC(Layout)P(!1,2)78@3182L23,81@3333L411:Layout.kt#80mrfh");
            int currentCompositeKeyHash = ComposablesKt.getCurrentCompositeKeyHash($composer, 0);
            CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
            Modifier materializeModifier = ComposedModifierKt.materializeModifier($composer, modifier);
            Function0 constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i = ((((384 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -692256719, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp");
            if (!($composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer.startReusableNode();
            if ($composer.getInserting()) {
                function0 = constructor;
                $composer.createNode(function0);
            } else {
                function0 = constructor;
                $composer.useNode();
            }
            Composer m3652constructorimpl = Updater.m3652constructorimpl($composer);
            Updater.m3659setimpl(m3652constructorimpl, rowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m3659setimpl(m3652constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2 setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (m3652constructorimpl.getInserting() || !Intrinsics.areEqual(m3652constructorimpl.rememberedValue(), Integer.valueOf(currentCompositeKeyHash))) {
                m3652constructorimpl.updateRememberedValue(Integer.valueOf(currentCompositeKeyHash));
                m3652constructorimpl.apply(Integer.valueOf(currentCompositeKeyHash), setCompositeKeyHash);
            }
            Updater.m3659setimpl(m3652constructorimpl, materializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i2 = (i >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer, -407918630, "C100@5047L9:Row.kt#2w3rfo");
            RowScopeInstance rowScopeInstance = RowScopeInstance.INSTANCE;
            int i3 = ((384 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, 2081805059, "C382@13587L10,380@13514L225:CommonComponents.kt#qonjpd");
            if ($leadingIcon != null) {
                $composer.startReplaceGroup(2081821705);
                ComposerKt.sourceInformation($composer, "377@13418L13,378@13448L39");
                $leadingIcon.invoke($composer, 0);
                SpacerKt.Spacer(SizeKt.m720width3ABfNKs(Modifier.INSTANCE, Dp.m6622constructorimpl(8)), $composer, 6);
            } else {
                $composer.startReplaceGroup(2068524782);
            }
            $composer.endReplaceGroup();
            TextKt.m2693Text4IGK_g($text, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, TextStyle.m6101copyp1EtxEg$default(MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelLarge(), 0L, 0L, FontWeight.INSTANCE.getBold(), null, null, null, null, TextUnitKt.getSp(0.4d), null, null, null, 0L, null, null, null, 0, 0, 0L, null, null, null, 0, 0, null, 16777083, null), $composer, 0, 0, 65534);
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            $composer.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
