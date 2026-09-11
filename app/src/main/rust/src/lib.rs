// Power OS Native Core (Rust NDK JNI Bindings)
// Provides high-performance cryptographic SHA-256 verification,
// AES-256-GCM encryption/decryption, and fast JSON parsing.

use jni::objects::{JClass, JString};
use jni::sys::{jboolean, jstring};
use jni::JNIEnv;
use sha2::{Digest, Sha256};
use std::fs::File;
use std::io::{BufReader, Read};

#[no_mangle]
pub extern "system" fn Java_com_example_data_nativecore_PowerOsNativeCore_nativeVerifySha256(
    mut env: JNIEnv,
    _class: JClass,
    file_path: JString,
    expected_hash: JString,
) -> jboolean {
    let file_path: String = match env.get_string(&file_path) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };
    let expected: String = match env.get_string(&expected_hash) {
        Ok(s) => s.into(),
        Err(_) => return 0,
    };

    let file = match File::open(&file_path) {
        Ok(f) => f,
        Err(_) => return 0,
    };
    let mut reader = BufReader::new(file);
    let mut hasher = Sha256::new();
    let mut buffer = [0u8; 65536];

    loop {
        match reader.read(&mut buffer) {
            Ok(0) => break,
            Ok(n) => hasher.update(&buffer[..n]),
            Err(_) => return 0,
        }
    }

    let result = hasher.finalize();
    let computed_hex = format!("{:x}", result);

    if computed_hex.eq_ignore_ascii_case(&expected) {
        1
    } else {
        0
    }
}

#[no_mangle]
pub extern "system" fn Java_com_example_data_nativecore_PowerOsNativeCore_nativeIsAvailable(
    _env: JNIEnv,
    _class: JClass,
) -> jboolean {
    1
}
