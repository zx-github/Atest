package com.csizg.imetest.autotouch;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                    Runtime.getRuntime().exec(new String[]{"su", "-c", "input tap " + data + " " + 800});
                } catch (IOException e) {
                    e.printStackTrace();
                }
                data += 20;
            }
        }).start();
    }
}
