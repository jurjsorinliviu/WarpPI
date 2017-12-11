#include <jni.h>
#include <stdio.h>
#include "TestJNI.h"

JNIEXPORT void JNICALL Java_org_warp_picalculator_TestJNI_sayHello(JNIEnv *env, jobject thisObj)
{
   printf("Hello World!\n");
   return;
}
