package com.example.kosong;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private TextView textView; //用于显示时间的view
    private String failureTime = "2022-12-23 23:00:00"; // 失效时间

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setTime();
            // 在handleMessage发送handle消息实现无限刷新
            handler.sendMessageDelayed(handler.obtainMessage(), 1000); // 延迟一秒发送，1秒刷新一次
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView1);
        handler.sendMessage(handler.obtainMessage());
    }

    /** Called when the user taps the Send button */
    public void changeVocabulary(View view) {
        Intent intent = new Intent(this, VocabularyActivity.class);
        startActivity(intent);
    }

    public void changePlan(View view) {
        Intent intent = new Intent(this, TodoActivity.class);
        startActivity(intent);
    }

    /* 测试用 */
    public void test(View view){
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }





    /* 实现日历倒计时 */
    @SuppressLint("SetTextI18n")
    private void setTime() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long nowTime = System.currentTimeMillis(); //获取当前时间
            Date overTime = simpleDateFormat.parse(failureTime); //利用SimpleDateFormat来把字符串日期转换为Date对象类型
            long a = overTime.getTime() - nowTime; // 计算总的时间差（毫秒级别）
            if (a > 0) { //如果大于零 说明时间有剩余
                // 总时间（a）
                // 总天数( day*(1000 * 60 * 60 * 24) )
                // 总小时数（ hour * (1000 * 60 * 60) ）
                // 总分钟数（ minute * (1000 * 60) ）
                long day = a / (1000 * 60 * 60 * 24);
                long hour = (a - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minute = (a - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
                long second = (a - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;
                textView.setText(day + " Days " + hour + " Hours " + minute + " Mins " + second + " s");
            }else {
                Toast.makeText(this, "小于等于零", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}