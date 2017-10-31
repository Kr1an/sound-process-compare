#include <jni.h>
#include <string>
#include <iomanip>
#include <ctime>
#include <iostream>
#include <sstream>

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_kulturraum_org_myapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "TEST 2";
    time_t _tm =time(NULL );
    struct tm * curtime = localtime ( &_tm );
    return env->NewStringUTF(asctime(curtime));
}
