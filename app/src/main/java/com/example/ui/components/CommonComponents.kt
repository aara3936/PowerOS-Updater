package com.example.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

// Standard uniform 28dp rounded corner shape
val LiquidGlassCornerRadius: Dp = 28.dp
val LiquidGlassShape = RoundedCornerShape(LiquidGlassCornerRadius)
val LiquidGlassStrokeWidth: Dp = 1.5.dp

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = LiquidGlassFill,
    borderBrush: Brush = LiquidGlassBorderBrush,
    shape: RoundedCornerShape = LiquidGlassShape,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .border(LiquidGlassStrokeWidth, borderBrush, shape),
        shape = shape,
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isPrimary: Boolean = true,
    containerColor: Color = if (isPrimary) GlassCyanAccent else LiquidGlassFillElevated,
    contentColor: Color = if (isPrimary) PowerOnPrimary else LiquidGlassTextPrimary,
    borderBrush: Brush = if (isPrimary) Brush.linearGradient(listOf(GlassCyanAccent, GlassCyanDark)) else LiquidGlassBorderBrush,
    content: @Composable RowScope.() -> Unit
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Spring physics animation for touch interactions
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "ButtonSpringScale"
    )

    Button(
        onClick = {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            onClick()
        },
        enabled = enabled,
        shape = LiquidGlassShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0x33FFFFFF),
            disabledContentColor = LiquidGlassTextMuted
        ),
        border = androidx.compose.foundation.BorderStroke(LiquidGlassStrokeWidth, borderBrush),
        interactionSource = interactionSource,
        modifier = modifier
            .scale(scale)
            .height(56.dp)
            .clip(LiquidGlassShape),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
    ) {
        content()
    }
}

@Composable
fun LiquidGlassIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = LiquidGlassTextPrimary
) {
    val view = LocalView.current
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "IconButtonSpring"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale)
            .size(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(LiquidGlassFill)
            .border(LiquidGlassStrokeWidth, LiquidGlassBorderBrush, RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 24.dp)
            ) {
                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                onClick()
            }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiquidGlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = LiquidGlassTextMuted, fontSize = 13.sp) },
        placeholder = { if (placeholder.isNotEmpty()) Text(placeholder, color = LiquidGlassTextMuted, fontSize = 13.sp) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = LiquidGlassShape,
        textStyle = TextStyle(color = LiquidGlassTextPrimary, fontSize = 15.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = LiquidGlassFillElevated,
            unfocusedContainerColor = LiquidGlassFill,
            focusedBorderColor = GlassCyanAccent,
            unfocusedBorderColor = LiquidGlassStrokeTop,
            cursorColor = GlassCyanAccent
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(LiquidGlassShape)
    )
}
