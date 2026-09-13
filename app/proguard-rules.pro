# Native Security Bridge & JNI
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.example.core.security.NativeSecurityBridge { *; }

# Jetpack Compose UI & State
-keep class androidx.compose.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable <methods>;
}

# Core Models & ViewModels
-keep class com.example.core.model.** { *; }
-keep class com.example.viewmodel.** { *; }

# Keep Activity & Views
-keep class com.example.MainActivity { *; }
-keep class com.example.updater.ui.** { *; }

# Coroutines & StateFlow
-keep class kotlinx.coroutines.** { *; }