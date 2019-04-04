package com.csizg.testjni;

import java.util.List;

/**
 * Created by Leo on 2019/3/14.
 *
 * @description：
 */

public class TestJniApi {

    private static TestJniApi mTestJniApi;

    static {
        System.loadLibrary("native-lib");
    }

    public static TestJniApi getmTestJniApi(){
        if (mTestJniApi == null){
            mTestJniApi = new TestJniApi();
        }
        return mTestJniApi;
    }

    public native String stringFromJNI();

    /**
     * 传递List到c
     *
     * @param stuList
     */
    public native boolean setStuList(List<StuInfo> stuList);

    /**
     * 从C获取list
     *
     * @return
     */
    public native List<StuInfo> getStuList();

    public native boolean setString(String str);
}
