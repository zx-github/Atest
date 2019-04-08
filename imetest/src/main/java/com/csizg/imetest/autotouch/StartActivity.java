package com.csizg.imetest.autotouch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.csizg.imetest.LogUtil;
import com.csizg.imetest.MainActivity;
import com.csizg.imetest.R;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.button:
                intent = new Intent(StartActivity.this, TouchActivity.class);
            break;
            case R.id.button2:
                intent = new Intent(StartActivity.this, MainActivity2.class);
            break;
        }

        if (intent != null)
            startActivity(intent);


    }
}
