package com.csizg.imetest;

import android.util.Log;

/**
 * 日志管理工具
 * Created by CS-1113 on 2017/8/16.
 */

public class LogUtil {

    private static boolean DEBUG = BuildConfig.DEBUG;
    public static boolean isLog = DEBUG;

    private static boolean LOGV = DEBUG;
    private static boolean LOGD = DEBUG;
    private static boolean LOGI = DEBUG;
    private static boolean LOGW = DEBUG;
    private static boolean LOGE = DEBUG;

    private static final String BASE_TAG = LogUtil.class.getSimpleName();

    public static void v(String TAG, String method, String msg) {
        if (LOGV) {
            Log.v(BASE_TAG, TAG + "." + method + "()-->" + msg);
        }
    }

    public static void d(String TAG, String method, String msg) {
        if (LOGD) {
            Log.d(BASE_TAG, TAG + "." + method + "()-->" + msg);
        }
    }

    //超长日志打印使用
    public static void d(String tag, String msg) {
        if (LOGD){
            d(tag,"", msg);
            return;
        }
        if (!LOGD) {
            return;
        }
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {
            Log.d(BASE_TAG,tag+ msg);
        }else {
            while (msg.length() > segmentSize ) {
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.d(BASE_TAG,tag+ logContent);
            }
            Log.d(BASE_TAG,tag+ msg);// 打印剩余日志
        }
    }

    public static void i(String TAG, String method, String msg) {
        if (LOGI) {
            Log.i(BASE_TAG, TAG + "." + method + "()-->" + msg);
        }
    }

    public static void w(String TAG, String method, String msg) {
        if (LOGW) {
            Log.w(BASE_TAG, TAG + "." + method + "()-->" + msg);
        }
    }

    public static void e(String TAG, String method, String msg) {
        if (LOGE) {
            Log.e(BASE_TAG, TAG + "." + method + "()-->" + msg);
        }
    }

//    public static void w2f(String TAG, String method, String msg) {
//        if (LOGD) {
//            Log.d(BASE_TAG, TAG + "." + method + "()-->" + msg);
//            writeFile(msg);
//        }
//    }

//    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);//日期格式;

//    private static void writeFile(String msg) {
//        String logString = CustomConstant.BASE_PATH + "LOG" + File.separator + "log.txt";
//        FileOutputStream fos = null;//FileOutputStream会自动调用底层的close()方法，不用关闭
//        BufferedWriter bw = null;
//        msg = dateFormat.format(new Date()) + "--> " + msg + "\n";
//        try {
//            File logfile = new File(logString);
//            if (!logfile.exists()) {
//                logfile.getParentFile().mkdirs();
//                logfile.createNewFile();
//            }
//            fos = new FileOutputStream(logString, true);//这里的第二个参数代表追加还是覆盖，true为追加，flase为覆盖
//            bw = new BufferedWriter(new OutputStreamWriter(fos));
//            bw.write(msg);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (bw != null) {
//                    bw.close();//关闭缓冲流
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
