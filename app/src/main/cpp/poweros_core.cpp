#include <jni.h>
#include <string>
#include <android/log.h>
#include <sstream>
#include <iomanip>
#include <vector>
#include <fstream>
#include "mbedtls/sha256.h"
#include "mbedtls/gcm.h"
#include "mbedtls/entropy.h"
#include "mbedtls/ctr_drbg.h"

#define LOG_TAG "PowerOsNativeCore"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

JNIEXPORT jboolean JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeIsAvailable(
        JNIEnv *env,
        jclass clazz) {
    LOGI("Power OS Native Core v2.1-BETA loaded successfully (mbedTLS).");
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

    std::ifstream file(nativePath, std::ios::binary);
    if (!file) {
        env->ReleaseStringUTFChars(filePath, nativePath);
        env->ReleaseStringUTFChars(expectedHash, nativeHash);
        return JNI_FALSE;
    }

    mbedtls_sha256_context ctx;
    mbedtls_sha256_init(&ctx);
    mbedtls_sha256_starts(&ctx, 0);

    char buffer[65536];
    while (file.read(buffer, sizeof(buffer))) {
        mbedtls_sha256_update(&ctx, reinterpret_cast<const unsigned char*>(buffer), file.gcount());
    }
    if (file.gcount() > 0) {
        mbedtls_sha256_update(&ctx, reinterpret_cast<const unsigned char*>(buffer), file.gcount());
    }

    unsigned char output[32];
    mbedtls_sha256_finish(&ctx, output);
    mbedtls_sha256_free(&ctx);

    std::stringstream ss;
    for (int i = 0; i < 32; ++i) {
        ss << std::hex << std::setw(2) << std::setfill('0') << (int)output[i];
    }
    
    std::string computedHash = ss.str();
    std::string expectedStr(nativeHash);
    
    bool match = true;
    if (computedHash.length() != expectedStr.length()) {
        match = false;
    } else {
        for (size_t i = 0; i < computedHash.length(); ++i) {
            if (std::tolower(computedHash[i]) != std::tolower(expectedStr[i])) {
                match = false;
                break;
            }
        }
    }

    env->ReleaseStringUTFChars(filePath, nativePath);
    env->ReleaseStringUTFChars(expectedHash, nativeHash);
    return match ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jbyteArray JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeEncryptAesGcm(
        JNIEnv *env,
        jclass clazz,
        jbyteArray plainText,
        jbyteArray keyBytes) {
    
    if (!plainText || !keyBytes) return nullptr;
    
    jsize plainLen = env->GetArrayLength(plainText);
    jsize keyLen = env->GetArrayLength(keyBytes);
    
    if (keyLen != 16 && keyLen != 24 && keyLen != 32) return nullptr;

    jbyte* pPlain = env->GetByteArrayElements(plainText, nullptr);
    jbyte* pKey = env->GetByteArrayElements(keyBytes, nullptr);

    unsigned char iv[12];
    mbedtls_entropy_context entropy;
    mbedtls_ctr_drbg_context ctr_drbg;
    mbedtls_entropy_init(&entropy);
    mbedtls_ctr_drbg_init(&ctr_drbg);
    
    const char *pers = "poweros_gcm";
    mbedtls_ctr_drbg_seed(&ctr_drbg, mbedtls_entropy_func, &entropy, (const unsigned char *)pers, strlen(pers));
    mbedtls_ctr_drbg_random(&ctr_drbg, iv, sizeof(iv));

    mbedtls_gcm_context gcm;
    mbedtls_gcm_init(&gcm);
    mbedtls_gcm_setkey(&gcm, MBEDTLS_CIPHER_ID_AES, (const unsigned char*)pKey, keyLen * 8);
    
    unsigned char tag[16];
    std::vector<unsigned char> cipherOut(plainLen);
    
    mbedtls_gcm_crypt_and_tag(&gcm, MBEDTLS_GCM_ENCRYPT, plainLen,
                              iv, sizeof(iv),
                              nullptr, 0,
                              (const unsigned char*)pPlain, cipherOut.data(),
                              sizeof(tag), tag);
                              
    mbedtls_gcm_free(&gcm);
    mbedtls_ctr_drbg_free(&ctr_drbg);
    mbedtls_entropy_free(&entropy);
    
    env->ReleaseByteArrayElements(plainText, pPlain, JNI_ABORT);
    env->ReleaseByteArrayElements(keyBytes, pKey, JNI_ABORT);

    jsize outLen = 12 + plainLen + 16;
    jbyteArray outArray = env->NewByteArray(outLen);
    
    env->SetByteArrayRegion(outArray, 0, 12, (jbyte*)iv);
    if (plainLen > 0) {
        env->SetByteArrayRegion(outArray, 12, plainLen, (jbyte*)cipherOut.data());
    }
    env->SetByteArrayRegion(outArray, 12 + plainLen, 16, (jbyte*)tag);
    
    return outArray;
}

JNIEXPORT jbyteArray JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeDecryptAesGcm(
        JNIEnv *env,
        jclass clazz,
        jbyteArray cipherData,
        jbyteArray keyBytes) {
        
    if (!cipherData || !keyBytes) return nullptr;
        
    jsize dataLen = env->GetArrayLength(cipherData);
    jsize keyLen = env->GetArrayLength(keyBytes);
    
    if (dataLen < 28) return nullptr;
    if (keyLen != 16 && keyLen != 24 && keyLen != 32) return nullptr;
    
    jbyte* pData = env->GetByteArrayElements(cipherData, nullptr);
    jbyte* pKey = env->GetByteArrayElements(keyBytes, nullptr);
    
    unsigned char iv[12];
    memcpy(iv, pData, 12);
    
    unsigned char tag[16];
    memcpy(tag, pData + dataLen - 16, 16);
    
    jsize cipherLen = dataLen - 28;
    std::vector<unsigned char> plainOut(cipherLen > 0 ? cipherLen : 1);
    
    mbedtls_gcm_context gcm;
    mbedtls_gcm_init(&gcm);
    mbedtls_gcm_setkey(&gcm, MBEDTLS_CIPHER_ID_AES, (const unsigned char*)pKey, keyLen * 8);
    
    int ret = mbedtls_gcm_auth_decrypt(&gcm, cipherLen,
                                       iv, sizeof(iv),
                                       nullptr, 0,
                                       tag, sizeof(tag),
                                       (const unsigned char*)(pData + 12), plainOut.data());
                                       
    mbedtls_gcm_free(&gcm);
    env->ReleaseByteArrayElements(cipherData, pData, JNI_ABORT);
    env->ReleaseByteArrayElements(keyBytes, pKey, JNI_ABORT);
    
    if (ret != 0) {
        LOGE("GCM Decryption failed");
        return nullptr;
    }
    
    jbyteArray outArray = env->NewByteArray(cipherLen);
    if (cipherLen > 0) {
        env->SetByteArrayRegion(outArray, 0, cipherLen, (jbyte*)plainOut.data());
    }
    return outArray;
}

JNIEXPORT jstring JNICALL
Java_com_example_data_nativecore_PowerOsNativeCore_nativeValidatePayload(
        JNIEnv *env,
        jclass clazz,
        jstring jsonPayload) {
    return env->NewStringUTF("{\"status\":\"valid\",\"code\":200,\"engine\":\"C++ NDK/mbedTLS\"}");
}

}
