package com.csizg.imetest.autotouch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.csizg.imetest.FileUtils;
import com.csizg.imetest.LogUtil;
import com.csizg.imetest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 1. 文件格式为UTF-8
 * 2. 文字以“/”分割，不要有空格
 */
public class MainActivity2 extends AppCompatActivity {


    static EditText editText;
    String TAG = "xindun";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViews();
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        XYtouch.isCancle = true;
        Toast.makeText(MainActivity2.this, "请等10之后再开始", Toast.LENGTH_LONG).show();

    }

    private void findViews() {
        editText = findViewById(R.id.editText);

    }

    private void getData() {
        XYtouch.path = Environment.getExternalStorageDirectory().getPath() + File.separator + "xindun" + File.separator;
        File file = new File(XYtouch.path);
        try {
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                XYtouch.isCancle = false;
                if (TextUtils.isEmpty(XYtouch.currentIme) || XYtouch.list == null || XYtouch.list.size() <= 0) {
                    Toast.makeText(MainActivity2.this, "请选择输入法", Toast.LENGTH_LONG).show();
                    return;
                }
                showGuide();
                break;

            case R.id.file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                break;

            case R.id.cancle:

                XYtouch.isCancle = true;
                Toast.makeText(MainActivity2.this, "请等10之后再开始", Toast.LENGTH_LONG).show();
                break;
            case R.id.niudun:
                XYtouch.list = XYtouch.getNiuDunFloatList();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
                XYtouch.currentIme = XYtouch.NIUDUN;
                XYtouch.hangLength = 0;
                break;
            case R.id.sougou:
                XYtouch.list = XYtouch.getSouGouFloatList();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
                XYtouch.currentIme = XYtouch.SOUGOU;
                XYtouch.hangLength = 0;
                break;
            case R.id.baidu:
                XYtouch.list = XYtouch.getBaiDuFloatList();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
                XYtouch.currentIme = XYtouch.BAIDU;
                XYtouch.hangLength = 0;
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    String path = FileUtils.getPath(this, uri);
                    if (path != null) {
                        XYtouch.file = new File(path);
                        if (XYtouch.file.exists()) {
                            Toast.makeText(MainActivity2.this, "选取文件成功", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }


    private void showGuide() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (XYtouch.file != null) {
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(XYtouch.file));
                        String readline = "";
                        LogUtil.d(TAG, "typeIn", "hangLength = " + XYtouch.hangLength);
                        br.skip(XYtouch.hangLength);
                        while ((readline = br.readLine()) != null) {
                            if (XYtouch.isCancle) {
                                break;
                            }
                            XYtouch.hangLength += readline.length();
                            LogUtil.d(TAG, "typeIn", "readline = " + readline);
                            if (!TextUtils.isEmpty(readline) && readline.length() > 2) {
                                typeIn(readline);
                            }

                            try {
                                Thread.sleep(800);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
//                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
//                        try {
//                            br.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity2.this, "未选择文件", Toast.LENGTH_LONG).show();
                        }
                    });
                    typeIn("张旭/9426498");
                }
            }
        }).start();
    }

    private void typeIn(String str) {
        if (TextUtils.isEmpty(str)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity2.this, "数据为空", Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        if (!str.contains("/")) {
            return;
        }
        String[] split = str.split("/");
        String text = split[0];
        String number = split[1];
        int keycode = 0;
        StringBuffer stringBuffer = new StringBuffer();
        int time = 0;

        for (int j = 0; j < 3; j++) {
            boolean clock = false;
            for (int i = 0; i <= number.length(); i++) {
                try {
                    if (i == number.length()) {
                        float[] floats = XYtouch.list.get(8);
                        float x = floats[0];
                        int length = text.length();
                        if (length > 1) {
                            x = length / 2 * x + (length % 2 == 0 ? 0 : x / 2);
                            x = x + (x * 2) * j;
                        }
                        if (x > 500) {
                            x = 500;
                        }

                        if (clock) {
                            clock = clock(x, floats[1]);
                        } else {
                            floats = XYtouch.list.get(11);
                            clock = clock(floats[0], floats[1]);
                        }
                    } else {
                        keycode = number.charAt(i) - 50;
                        float[] floats = XYtouch.list.get(keycode);
                        clock = clock(floats[0], floats[1]);
                    }

                    Thread.sleep(200);
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity2.this, "测试出现异常，请重新开始", Toast.LENGTH_LONG).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String string = getEditString(clock);
            if (TextUtils.isEmpty(string)) {
                setEnpty();
                continue;
            }
            stringBuffer.append(string);

            if (text.equals(string)) {
                if (time == 0) {
                    time = j + 1;
                }
            }
            stringBuffer.append(",");

            setEnpty();
        }
        stringBuffer.append(String.valueOf(time));
        XYtouch.stringText = stringBuffer.toString();
        LogUtil.d(TAG, "typeIn", "stringText = " + XYtouch.stringText);
        writeFile();

    }

    private boolean clock(float x, float y){
//        try {
//            Runtime.getRuntime().exec(new String[]{"su", "-c", "input tap " + x + " " + y});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 利用ProcessBuilder执行shell命令
        String[] order = {"su", "-c", "input", "tap", "" + x, "" + y };
        try {
            new ProcessBuilder(order).start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    private void setEnpty() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.e(TAG, "setEnpty", "editText.setText(\"\")");
                editText.setText("");
            }
        });
    }

    @NonNull
    private String getEditString(boolean isClock) {
        String string = editText.getText().toString();
        LogUtil.e(TAG, "getEditString", "string == " + string);
        if (TextUtils.isEmpty(string)) {
            float[] floats = XYtouch.list.get(11);
            if (!isClock){
                clock(floats[0], floats[1]);
            } else {
                boolean clock = clock(100, 800);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (clock && !XYtouch.isCancle) {
                    getEditString(isClock);
                } else {
                    clock(floats[0], floats[1]);
                    try {
                        Thread.sleep(1100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    string = editText.getText().toString();
                }
            }
        }
        return string;
    }


    private void writeFile() {
        if (TextUtils.isEmpty(XYtouch.stringText)) return;
        BufferedWriter bw = null;
        try {
            if (TextUtils.isEmpty(XYtouch.currentIme)) {
                LogUtil.e(TAG, "writeFile", "当前输入法为空");
                return;
            }
            File file = new File(XYtouch.path + XYtouch.currentIme + ".txt");
            if (!file.isFile() || !file.exists()) {
                file.createNewFile();
            }
            /**
             * 为了提高写入的效率，使用了字符流的缓冲区。
             * 创建了一个字符写入流的缓冲区对象，并和指定要被缓冲的流对象相关联。
             */
            //第二个参数意义是说是否以append方式添加内容
            bw = new BufferedWriter(new FileWriter(file, true));

            //使用缓冲区中的方法将数据写入到缓冲区中。
            bw.write(XYtouch.stringText);
            bw.newLine();
            //使用缓冲区中的方法，将数据刷新到目的地文件中去。
            bw.flush();
            //关闭缓冲区,同时关闭了FileWriter流对象
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
