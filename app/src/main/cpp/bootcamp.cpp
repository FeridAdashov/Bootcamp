#include <jni.h>
#include "Calculator.h"

extern "C" {

JNIEXPORT jint JNICALL
Java_com_example_bootcamp_base_MainActivity_00024Companion_add(JNIEnv *env, jobject, jint a,
                                                               jint b) {
    return Calculator::add(a, b);
}

JNIEXPORT jint JNICALL
Java_com_example_bootcamp_base_MainActivity_00024Companion_subtract(JNIEnv *env, jobject, jint a,
                                                                    jint b) {
    return Calculator::subtract(a, b);
}

JNIEXPORT jint JNICALL
Java_com_example_bootcamp_base_MainActivity_00024Companion_multiply(JNIEnv *env, jobject, jint a,
                                                                    jint b) {
    return Calculator::multiply(a, b);
}

JNIEXPORT jfloat JNICALL
Java_com_example_bootcamp_base_MainActivity_00024Companion_divide(JNIEnv *env, jobject, jfloat a,
                                                                  jfloat b) {
    return Calculator::divide(a, b);
}


} // extern "C"
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_bootcamp_base_MainActivity_00024Companion_apiKey(JNIEnv *env, jobject thiz) {
    return env->NewStringUTF("Base url");
}