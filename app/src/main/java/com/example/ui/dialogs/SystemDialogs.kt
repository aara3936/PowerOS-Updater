package com.example.ui.dialogs

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.OtaConstants
import com.example.data.model.SystemDeviceInfo
import com.example.data.model.UpdateHistoryItem
import com.example.ui.components.*
import com.example.ui.security.DeveloperAuthManager
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DeveloperAuthDialog(
    onDismiss: () -> Unit,
    onAuthenticated: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var shakeOffset by remember { mutableStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current

    // Horizontal dynamic shake animation on incorrect password
    val shakeAnim = remember { Animatable(0f) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            val borderBrush = if (isError) LiquidGlassErrorBorderBrush else LiquidGlassBorderBrush

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = shakeAnim.value.dp)
                    .clip(LiquidGlassShape)
                    .border(LiquidGlassStrokeWidth, borderBrush, LiquidGlassShape),
                shape = LiquidGlassShape,
                color = if (isError) Color(0x33FF5252) else LiquidGlassFillElevated
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(if (isError) Color(0x40FF5252) else Color(0x3300E5FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isError) Icons.Default.Lock else Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = if (isError) LiquidGlassErrorGlow else GlassCyanAccent,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Developer Authentication",
                        style = Typography.titleLarge,
                        color = LiquidGlassTextPrimary
                    )

                    Text(
                        text = "Authorized engineering credentials required",
                        style = Typography.labelMedium,
                        color = LiquidGlassTextMuted,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    if (isError) {
                        Text(
                            text = errorMessage,
                            color = LiquidGlassErrorGlow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    LiquidGlassTextField(
                        value = username,
                        onValueChange = {
                            username = it
                            isError = false
                        },
                        label = "Username",
                        placeholder = "Enter engineering ID",
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = LiquidGlassTextMuted)
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    LiquidGlassTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            isError = false
                        },
                        label = "Password",
                        placeholder = "Enter master token",
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        leadingIcon = {
                            Icon(Icons.Default.Key, contentDescription = null, tint = LiquidGlassTextMuted)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LiquidGlassButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            isPrimary = false
                        ) {
                            Text("Cancel", style = Typography.labelLarge)
                        }

                        LiquidGlassButton(
                            onClick = {
                                val success = DeveloperAuthManager.authenticate(username, password)
                                if (success) {
                                    onAuthenticated()
                                } else {
                                    isError = true
                                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                                    val failed = DeveloperAuthManager.getFailedAttempts()
                                    errorMessage = "Invalid credentials. Attempt $failed of 3."

                                    // Trigger dynamic horizontal shake
                                    coroutineScope.launch {
                                        for (i in 0..2) {
                                            shakeAnim.animateTo(16f, tween(50, easing = LinearEasing))
                                            shakeAnim.animateTo(-16f, tween(50, easing = LinearEasing))
                                        }
                                        shakeAnim.animateTo(0f, tween(50, easing = LinearEasing))
                                    }

                                    if (DeveloperAuthManager.isLockedOut.value) {
                                        onDismiss()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            isPrimary = true
                        ) {
                            Text("Authenticate", style = Typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SecurityLockoutDialog(
    remainingSeconds: Int,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LiquidGlassShape)
                    .border(LiquidGlassStrokeWidth, LiquidGlassErrorBorderBrush, LiquidGlassShape),
                shape = LiquidGlassShape,
                color = Color(0x33100505)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0x40FF5252)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = LiquidGlassErrorGlow,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Security Lockout",
                        style = Typography.titleLarge,
                        color = LiquidGlassErrorGlow
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Developer Mode disabled for 5 minutes due to multiple invalid entries.",
                        style = Typography.bodyMedium,
                        color = LiquidGlassTextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val mins = remainingSeconds / 60
                    val secs = remainingSeconds % 60
                    val timeString = String.format("%02d:%02d", mins, secs)

                    Text(
                        text = "Cooldown Timer: $timeString",
                        style = Typography.headlineMedium,
                        color = LiquidGlassErrorGlow
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LiquidGlassButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = false
                    ) {
                        Text("Acknowledge", style = Typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsDialog(
    currentVersion: String,
    currentChannel: String,
    rawJsonUrl: String,
    targetSavePath: String,
    onChannelSelected: (String) -> Unit,
    onSaveUrlChanged: (String) -> Unit,
    onSavePathChanged: (String) -> Unit,
    onDeveloperTriggered: () -> Unit,
    onDismiss: () -> Unit
) {
    var urlText by remember { mutableStateOf(rawJsonUrl) }
    var pathText by remember { mutableStateOf(targetSavePath) }
    val view = LocalView.current
    val coroutineScope = rememberCoroutineScope()

    // 3% spring pulse scale down on tap
    var tapScale by remember { mutableStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = tapScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "TapScale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x99000000))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(LiquidGlassShape)
                    .border(LiquidGlassStrokeWidth, LiquidGlassBorderBrush, LiquidGlassShape),
                shape = LiquidGlassShape,
                color = LiquidGlassFillElevated
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Updater Settings", style = Typography.titleLarge, color = LiquidGlassTextPrimary)
                        LiquidGlassIconButton(
                            icon = Icons.Default.Close,
                            contentDescription = "Close",
                            onClick = onDismiss
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // APP VERSION ROW with exact 3-tap sequential trigger
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scale(animatedScale)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, LiquidGlassStrokeTop, RoundedCornerShape(16.dp))
                            .clickable {
                                // Micro-haptic tick
                                view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)

                                // 3% spring pulse scale down
                                coroutineScope.launch {
                                    tapScale = 0.97f
                                    delay(50)
                                    tapScale = 1.0f
                                }

                                val triggered = DeveloperAuthManager.registerVersionTap()
                                if (triggered) {
                                    onDeveloperTriggered()
                                }
                            },
                        color = LiquidGlassFill
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("App Version", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                                Text(currentVersion, style = Typography.titleMedium, color = GlassCyanAccent)
                            }
                            Icon(Icons.Default.Info, contentDescription = null, tint = LiquidGlassTextMuted)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Release Channel Selector
                    Text("Release Channel", style = Typography.labelMedium, color = LiquidGlassTextMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Stable", "Beta", "Developer").forEach { channel ->
                            val isSelected = currentChannel.equals(channel, ignoreCase = true)
                            Button(
                                onClick = { onChannelSelected(channel) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) GlassCyanAccent else LiquidGlassFill,
                                    contentColor = if (isSelected) PowerOnPrimary else LiquidGlassTextPrimary
                                )
                            ) {
                                Text(channel, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LiquidGlassTextField(
                        value = urlText,
                        onValueChange = {
                            urlText = it
                            onSaveUrlChanged(it)
                        },
                        label = "Raw Manifest JSON URL",
                        placeholder = "https://raw.githubusercontent.com/..."
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LiquidGlassTextField(
                        value = pathText,
                        onValueChange = {
                            pathText = it
                            onSavePathChanged(it)
                        },
                        label = "ROM Destination File Path",
                        placeholder = "/sdcard/Download/OTA/rom.zip"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LiquidGlassButton(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = true
                    ) {
                        Text("Save & Close", style = Typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun DeviceSpecsDialog(
    deviceInfo: SystemDeviceInfo,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = LiquidGlassFillElevated
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Device Specifications", style = Typography.titleLarge, color = LiquidGlassTextPrimary)
                LiquidGlassIconButton(icon = Icons.Default.Close, contentDescription = "Close", onClick = onDismiss)
            }

            Spacer(modifier = Modifier.height(16.dp))

            val specs = listOf(
                "Device Model" to "${deviceInfo.deviceName} (${deviceInfo.deviceCodename})",
                "Power OS" to deviceInfo.currentOsVersion,
                "Build Tag" to deviceInfo.currentBuildNumber,
                "Android Version" to deviceInfo.androidVersion,
                "Security Patch" to deviceInfo.securityPatch,
                "Kernel" to deviceInfo.kernelVersion,
                "CPU Architecture" to deviceInfo.cpuArch,
                "Available Storage" to "${deviceInfo.storageFreeGb} GB / ${deviceInfo.storageTotalGb} GB",
                "Battery Level" to "${deviceInfo.batteryLevel}%",
                "Network State" to deviceInfo.networkType
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(specs) { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(LiquidGlassFill)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label, style = Typography.bodyMedium, color = LiquidGlassTextMuted)
                        Text(value, style = Typography.bodyMedium, color = LiquidGlassTextPrimary, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LiquidGlassButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                isPrimary = false
            ) {
                Text("Dismiss", style = Typography.labelLarge)
            }
        }
    }
}

@Composable
fun UpdateHistoryDialog(
    history: List<UpdateHistoryItem>,
    onDismiss: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US) }

    Dialog(onDismissRequest = onDismiss) {
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = LiquidGlassFillElevated
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Installation History", style = Typography.titleLarge, color = LiquidGlassTextPrimary)
                LiquidGlassIconButton(icon = Icons.Default.Close, contentDescription = "Close", onClick = onDismiss)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No past OTA installations recorded.", style = Typography.bodyMedium, color = LiquidGlassTextMuted)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(LiquidGlassFill)
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.versionName, style = Typography.titleMedium, color = GlassCyanAccent)
                                Text(item.releaseChannel, style = Typography.labelMedium, color = GlassEmerald)
                            }
                            Text(
                                "Installed on: ${dateFormat.format(Date(item.installedTimestamp))}",
                                style = Typography.labelMedium,
                                color = LiquidGlassTextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LiquidGlassButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                isPrimary = false
            ) {
                Text("Close", style = Typography.labelLarge)
            }
        }
    }
}
