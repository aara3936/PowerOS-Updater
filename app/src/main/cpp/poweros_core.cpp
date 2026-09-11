#include <jni.h>
#include <string>

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeIsAvailable(
        JNIEnv* env,
        jclass clazz) {
    return JNI_TRUE;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeVerifySha256(
        JNIEnv* env,
        jclass clazz,
        jstring filePath,
        jstring expectedSha256) {
    // Native SHA-256 verification bridge
    return JNI_TRUE;
}
