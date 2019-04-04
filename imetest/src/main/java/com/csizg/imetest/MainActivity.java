package com.csizg.imetest;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * 1. 文件格式为UTF-8
 * 2. 文字以“/”分割，不要有空格
 */
public class MainActivity extends AppCompatActivity {

    String TAG = MainActivity.class.getSimpleName();
    Button startBt, fileBt, cancleBt;
    String path;
    EditText editText;
    boolean isCancle = false;
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        getData();
    }


    private void findViews() {
        editText = findViewById(R.id.editText);
        startBt = findViewById(R.id.start);
        fileBt = findViewById(R.id.file);
        cancleBt = findViewById(R.id.cancle);

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
                showGuide();
                break;

            case R.id.file:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
                break;

            case R.id.cancle:
                isCancle = true;
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
                            Toast.makeText(MainActivity.this, "选取文件成功", Toast.LENGTH_LONG);
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
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if (file != null) {
                    BufferedReader br = null;
                    try {
                        InputStreamReader read = new InputStreamReader(
                                new FileInputStream(file),"UTF-8");//考虑到编码格式
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

                }
                // “旋转”的拼音
                int[] keyCodeArray = new int[]{KeyEvent.KEYCODE_X, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_N, KeyEvent.KEYCODE_Z,
                        KeyEvent.KEYCODE_H, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_N, KeyEvent.KEYCODE_SPACE};

            }
        }).start();
    }

    private void typeIn(String str) {
        if (TextUtils.isEmpty(str)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "数据为空", Toast.LENGTH_LONG);
                }
            });
            return;
        }
        String[] split = str.split("/");
        String text = split[0];
        String number = split[1];
        int keycode = 0;
        for (int i = 0; i <= number.length(); i++) {
            if (i == number.length()){
                keycode = KeyEvent.KEYCODE_SPACE;
            } else {
                keycode = number.charAt(i) - 41;
            }
            if (keycode < 7)continue;
            try {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(keycode);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        writeFile();

    }


    private void writeFile() {
        String string = editText.getText().toString();
        if (TextUtils.isEmpty(string)) return;
        BufferedWriter bw = null;
        try {

            File file = new File(path + "结果.txt");
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
            bw.write(string);
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editText.setText("");
            }
        });
    }

}
