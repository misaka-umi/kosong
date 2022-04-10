package com.example.kosong;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
        setTitle();//设置题目
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏为黑色
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

    public void changeVocabularies(View view) {
        Intent intent = new Intent(this, ListHistoryVocabulariesActivity.class);
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


     public void setTitle(){
        TextView title = (TextView) findViewById(R.id.title);
        SharedPreferences mainTitle = getSharedPreferences("mainTitle",MODE_PRIVATE);
        String message = mainTitle.getString("title","点击此处设置文本");
        title.setText(message);
     }


    //点击title textView弹出dialog并修改内容
    public void onClick(View v){
        showDialog(this,v);
    }
    public void showDialog(Context context, View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final EditText et = new EditText(context);
        et.setHint("💖写一句话吧"); //提示文为原有文字
        dialog.setView(et);//给对话框添加一个EditText输入文本框
        dialog.setTitle(" ");
        final SharedPreferences.Editor editor = getSharedPreferences("mainTitle", MODE_PRIVATE).edit();

        //给对话框添加一个确定按钮，同样的方法可以添加一个取消按钮
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String message =et.getText().toString();
                if(!message.isEmpty()){
                    editor.putString("title",message);
                    editor.apply();
                    setTitle();
                }
            }
        });
        //下面是弹出键盘的关键处
        AlertDialog tempDialog = dialog.create();
        tempDialog.setView(et, 0, 0, 0, 0);

        tempDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        tempDialog.show();
    }

}