#include <jni.h>
#include <string>
#include <stdio.h>
#include <stdlib.h>

#include <android/log.h>
#define TAG "native-lib" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

extern "C"
JNIEXPORT jstring JNICALL Java_com_csizg_testjni_TestJniApi_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


typedef struct {
    int stuId;
    char stuName[50];
    int stuAge;
    char className[50];
} StuInfo;

extern "C"
JNIEXPORT jboolean JNICALL Java_com_csizg_testjni_TestJniApi_setStuList(
        JNIEnv *env, jobject obj, jobject jobj) {
    jboolean tRet = false;
    //获取ArrayList 对象
    jclass jcs_alist = env->GetObjectClass(jobj);
    LOGI("====1=====");
    //获取Arraylist的methodid
    jmethodID alist_get = env->GetMethodID(jcs_alist, "get",
                                           "(I)Ljava/lang/Object;");
    jmethodID alist_size = env->GetMethodID(jcs_alist, "size", "()I");
    LOGI("====2=====");
    jint len = env->CallIntMethod(jobj, alist_size);


    LOGI("======3====%d", len);
    for (int i = 0; i < len; i++) {
        //获取StuInfo对象
        jobject stu_obj = env->CallObjectMethod(jobj, alist_get, i);
        //获取StuInfo类
        jclass stu_cls = env->GetObjectClass(stu_obj);


        jmethodID methodId = env->GetMethodID(stu_cls, "getStuId", "()I");
        jmethodID nameId = env->GetMethodID(stu_cls, "getStuName",
                                            "()Ljava/lang/String;");
        jmethodID ageId = env->GetMethodID(stu_cls, "getStuAge", "()I");
        jmethodID classId = env->GetMethodID(stu_cls, "getClassName",
                                             "()Ljava/lang/String;");
        jint stuId = env->CallIntMethod(stu_obj, methodId);
        jstring nameStr = (jstring) env->CallObjectMethod(stu_obj, nameId);
        const char *stuName = env->GetStringUTFChars(nameStr, 0);
        jint stuAge = env->CallIntMethod(stu_obj, ageId);
        jstring classStr = (jstring) env->CallObjectMethod(stu_obj, classId);
        const char *className = env->GetStringUTFChars(classStr, 0);
        LOGI("第%d个学生信息：id=%d,name=%s,age=%d,className=%s", i + 1, stuId,
             stuName, stuAge, className);
    }
    tRet = true;
    return tRet;
}

extern "C"
JNIEXPORT jobject JNICALL Java_com_csizg_testjni_TestJniApi_getStuList(
        JNIEnv *env, jobject obj) {

    LOGI("====1=====");
    //获取ArrayList类引用
    jclass list_jcs = env->FindClass("java/util/ArrayList");
    if (list_jcs == NULL) {
        LOGI("ArrayList no  find!");
        return NULL;
    }
    LOGI("====2=====");
    //获取ArrayList构造函数id
    jmethodID list_init = env->GetMethodID(list_jcs, "<init>", "()V");
    //创建一个ArrayList对象
    jobject list_obj = env->NewObject(list_jcs, list_init, "");
    //获取ArrayList对象的add()的methodID
    jmethodID list_add = env->GetMethodID(list_jcs, "add",
                                          "(Ljava/lang/Object;)Z");
    //获取StuInfo类
    jclass stu_cls = env->FindClass("com/csizg/testjni/StuInfo");
    //获取StuInfo的构造函数
    jmethodID stu_init = env->GetMethodID(stu_cls, "<init>",
                                          "(ILjava/lang/String;ILjava/lang/String;)V");

    LOGI("====3=====");
    StuInfo stuInfo;
    stuInfo.stuId = 100001;
    stuInfo.stuAge = 20;
    const char *name = "牛八少爷";
    strcpy(stuInfo.stuName, name);
    const char *className = "高三六班";
    strcpy(stuInfo.className, className);


    for (int i = 0; i < 4; i++) {
        LOGI("第%d个学生信息：id=%d,name=%s,age=%d,className=%s", i + 1,
             (stuInfo.stuId + i), stuInfo.stuName, (stuInfo.stuAge + i),
             stuInfo.className);
        //通过StuInfo的构造函数创建StuInfo对象
        jobject stu_obj = env->NewObject(stu_cls, stu_init, (stuInfo.stuId + i),
                                         env->NewStringUTF(stuInfo.stuName), (stuInfo.stuAge + i * 5),
                                         env->NewStringUTF(stuInfo.className));


        env->CallBooleanMethod(list_obj, list_add, stu_obj);
    }
    return list_obj;
}

extern "C"
char*   Jstring2CStr(JNIEnv*   env,   jstring   jstr)
{
    char*   rtn   =   NULL;
    jclass   clsstring   =   env->FindClass("java/lang/String");//寻找 java里面String.class
    jstring   strencode   =   env->NewStringUTF("GB2312");//创建java字符串 "gb2312"
    jmethodID   mid   =   env->GetMethodID(clsstring,   "getBytes",   "(Ljava/lang/String;)[B");//寻找到java String getbytes();
    jbyteArray   barr=   (jbyteArray)env->CallObjectMethod(jstr,mid,strencode); // String .getByte("GB2312");
    jsize   alen   =   env->GetArrayLength(barr); //获取长度
    jbyte*   ba   =   env->GetByteArrayElements(barr,JNI_FALSE); //jbyteArray转为jbyte*

    if(alen   >   0)
    {
        rtn   =   (char*)malloc(alen+1);         //"\0"
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr,ba,0);  //释放掉
    return rtn;
}


extern "C"
JNIEXPORT jboolean JNICALL Java_com_csizg_testjni_TestJniApi_setString
        (JNIEnv *env, jobject obj, jstring jstr){
    LOGD("====2====%s", jstr);
    FILE * fp_w;
    const char *ptr = NULL;

    if (jstr)
        ptr = (env)->GetStringUTFChars(jstr, false);
//        ptr=Jstring2CStr(env, jstr);
    LOGD("====3====%s", ptr);
    //添加逻辑
    fp_w = fopen(ptr, "w");
    if (fp_w == NULL) {
        LOGD("====2====   openfile error");
    }

    fprintf(fp_w, "%s","teetest");
    fflush(fp_w);

    return true;
}



















