package com.csizg.imetest.autotouch;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csizg.imetest.LogUtil;
import com.csizg.imetest.MainActivity;
import com.csizg.imetest.R;

import java.io.IOException;

public class TouchActivity extends AppCompatActivity implements View.OnTouchListener{
    LinearLayout linearLayout;
    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        findViews();
        getData();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void findViews() {

        linearLayout = (LinearLayout) findViewById(R.id.ly);
        linearLayout.setOnTouchListener(this);

        textView = findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeIn();
            }
        });
        editText = findViewById(R.id.editText2);
    }

    private void getData() {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.i("xindun", "onTouch", motionEvent.getX() + " , " + motionEvent.getY());
                break;
        }
        return true;
    }
    float data = 20;
    private void typeIn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.i("xindun", "typeIn", data + " , ");
                try {
                    Runtime.getRuntime().exec(new String[]{"su", "-c", "input tap " + 620 + " " + 1210});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                data += 20;
            }
        }).start();
    }




    private void typeIn(String str) {
        if (TextUtils.isEmpty(str)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TouchActivity.this, "数据为空", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(TouchActivity.this, "测试出现异常，请重新开始", Toast.LENGTH_LONG).show();
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
        LogUtil.d("", "typeIn", "stringText = " + XYtouch.stringText);

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
                LogUtil.e("", "setEnpty", "editText.setText(\"\")");
                editText.setText("");
            }
        });
    }

    @NonNull
    private String getEditString(boolean isClock) {
        String string = editText.getText().toString();
        LogUtil.e("", "getEditString", "string == " + string);
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

}
