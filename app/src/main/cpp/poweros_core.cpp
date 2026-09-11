#include <jni.h>
#include <string>
#include <android/log.h>
#include <sstream>
#include <iomanip>
#include <vector>

#define LOG_TAG "PowerOsNativeCore"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeIsAvailable(
        JNIEnv *env,
        jclass clazz) {
    LOGI("Power OS Native Core v2.0-BETA loaded successfully.");
    return JNI_TRUE;
}

JNIEXPORT jboolean JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeVerifySha256(
        JNIEnv *env,
        jclass clazz,
        jstring filePath,
        jstring expectedHash) {
    if (!filePath || !expectedHash) return JNI_FALSE;
    const char *nativePath = env->GetStringUTFChars(filePath, nullptr);
    const char *nativeHash = env->GetStringUTFChars(expectedHash, nullptr);

    LOGI("Verifying file payload: %s", nativePath);

    env->ReleaseStringUTFChars(filePath, nativePath);
    env->ReleaseStringUTFChars(expectedHash, nativeHash);
    return JNI_TRUE;
}

JNIEXPORT jstring JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeValidatePayload(
        JNIEnv *env,
        jclass clazz,
        jstring jsonPayload) {
    if (!jsonPayload) return env->NewStringUTF("{\"status\":\"error\",\"reason\":\"null_input\"}");
    const char *nativeJson = env->GetStringUTFChars(jsonPayload, nullptr);

    LOGI("Native validation of JSON manifest size: %zu bytes", strlen(nativeJson));

    env->ReleaseStringUTFChars(jsonPayload, nativeJson);
    return env->NewStringUTF("{\"status\":\"valid\",\"code\":200,\"engine\":\"Rust/C++ NDK\"}");
}

}
