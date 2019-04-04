package com.csizg.testjni;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mRelativeLayout;
    private LayoutInflater layoutInflater;
    private View inflate;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (null != inflate) {
                mRelativeLayout.removeViewInLayout(inflate);
            }
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    inflate = layoutInflater.inflate(R.layout.main_item1, null, false);
                    mRelativeLayout.addView(inflate);
                    return true;
                case R.id.navigation_dashboard:
                    inflate = layoutInflater.inflate(R.layout.main_item2, null, false);
                    mRelativeLayout.addView(inflate);
                    return true;
                case R.id.navigation_notifications:
                    inflate = layoutInflater.inflate(R.layout.main_item3, null, false);
                    mRelativeLayout.addView(inflate);
                    getViewId();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutInflater = LayoutInflater.from(this);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.rl_root);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    TextView textView;
    private void getViewId(){
        Button button1 = inflate.findViewById(R.id.button1);
        Button button2 = inflate.findViewById(R.id.button2);
        Button button3 = inflate.findViewById(R.id.button3);
        textView = inflate.findViewById(R.id.textView);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }
    List<StuInfo> stuList;
    @Override
    public void onClick(View view) {
        TestJniApi jniApi = TestJniApi.getmTestJniApi();
        switch (view.getId()){
            case R.id.button1:
                textView.setText(jniApi.stringFromJNI());
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "xindun" + File.separator + "你好.txt";
                File file = new File(path);
                if (!file.isFile() || !file.exists()){
                    textView.setText("文件不存在");
                    return;
                }
                try {
                    path = new String(path.getBytes("gbk"),"GB2312");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                boolean setString = jniApi.setString(path);
                textView.setText(String.valueOf(setString));
                break;
            case R.id.button2:
                String text = "";
                stuList = jniApi.getStuList();
                for (int i = 0; i < stuList.size(); i++) {
                    StuInfo stuInfo = stuList.get(i);
                    text = text + stuInfo.toString() + "\n";
                }
                textView.setText(text);
                break;
            case R.id.button3:
                boolean stuList = jniApi.setStuList(this.stuList);
                textView.setText(stuList + "");
                break;
        }

    }
}
