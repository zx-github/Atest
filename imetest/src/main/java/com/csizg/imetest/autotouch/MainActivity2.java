package com.csizg.imetest.autotouch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.csizg.imetest.FileUtils;
import com.csizg.imetest.LogUtil;
import com.csizg.imetest.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 1. 文件格式为UTF-8
 * 2. 文字以“/”分割，不要有空格
 */
public class MainActivity2 extends AppCompatActivity {


    static final String NIUDUN = "牛盾";
    static final String SOUGOU = "搜狗";
    static final String BAIDU = "百度";
    String TAG = "xindun";
    String path;
    EditText editText;
    RelativeLayout relativeLayout;
    boolean isCancle = false;
    File file;
    List<float[]> list;
    String stringText;
    String currentIme;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        findViews();
        getData();
    }


    private void findViews() {
        relativeLayout = findViewById(R.id.rl);
        editText = findViewById(R.id.editText);

    }

    private void getData() {
        path = Environment.getExternalStorageDirectory().getPath() + File.separator + "xindun" + File.separator;
        File file = new File(path);
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
                isCancle = false;
                if (TextUtils.isEmpty(currentIme) || list == null || list.size() <= 0) {
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

                isCancle = true;
                Toast.makeText(MainActivity2.this, "请等10之后再开始", Toast.LENGTH_LONG).show();
                break;
            case R.id.niudun:
                list = XYtouch.getNiuDunFloatList();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
                currentIme = NIUDUN;
                break;
            case R.id.sougou:
                list = XYtouch.getSouGouFloatList();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
                currentIme = SOUGOU;
                break;
            case R.id.baidu:
                list = XYtouch.getBaiDuFloatList();
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker();
                currentIme = BAIDU;
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
                        file = new File(path);
                        if (file.exists()) {
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
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if (file != null) {
                    BufferedReader br = null;
                    try {
                        InputStreamReader read = new InputStreamReader(
                                new FileInputStream(file), "UTF-8");//考虑到编码格式
                        br = new BufferedReader(read);
                        String readline = "";
                        while ((readline = br.readLine()) != null) {
                            if (isCancle) {
                                break;
                            }
                            LogUtil.d(TAG, "typeIn", "readline = " + readline);
                            typeIn(readline);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                        }
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        String[] split = str.split("/");
        String text = split[0];
        String number = split[1];
        int keycode = 0;
        StringBuffer stringBuffer = new StringBuffer();
        int time = 0;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i <= number.length(); i++) {
                try {
                    if (i == number.length()) {
                        float[] floats = list.get(8);
                        float x = floats[0];
                        int length = text.length();
                        if (length < 2) {
                            x = x / 2;
                            x = x + (x * 2) * j;
                        } else if (length == 2) {
                            x = x + (x * 2) * j;
                        } else if (length == 3) {
                            x = x / 2 + x;
                            x = x + (x * 2) * j;
                        } else if (length == 4) {
                            x = 2 * x;
                            x = x + (x * 2) * j;
                        }
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "input tap " + x + " " + floats[1]});
                    } else {
                        keycode = number.charAt(i) - 50;
                        float[] floats = list.get(keycode);
                        Runtime.getRuntime().exec(new String[]{"su", "-c", "input tap " + floats[0] + " " + floats[1]});

                    }

                    Thread.sleep(500);
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String string = editText.getText().toString();
            if (TextUtils.isEmpty(string)) {
                continue;
            }
            stringBuffer.append(string);

            if (text.equals(string)) {
                time = j + 1;
            }
            stringBuffer.append(", ");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editText.setText("");
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stringBuffer.append(String.valueOf(time));
        stringText = stringBuffer.toString();
        LogUtil.d(TAG, "typeIn", "stringText = " + stringText);
        writeFile();

    }


    private void writeFile() {
        if (TextUtils.isEmpty(stringText)) return;
        BufferedWriter bw = null;
        try {
            if (TextUtils.isEmpty(currentIme)) {
                LogUtil.e(TAG, "writeFile", "当前输入法为空");
                return;
            }
            File file = new File(path + currentIme + ".txt");
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
            bw.write(stringText);
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
