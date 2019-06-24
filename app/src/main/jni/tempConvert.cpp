#include "convJni.h"

JNIEXPORT jint JNICALL Java_com_example_projekat1_FahrConverter_CelsiusToFahrenheit(JNIEnv *env, jobject obj, jint temp){
    return temp*9/5 + 32;
}

JNIEXPORT jint JNICALL Java_com_example_projekat1_FahrConverter_FahrenheitToCelsius(JNIEnv *env, jobject obj, jint temp){
    return (temp-32)*5/9;
}