package com.example.kosong;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TodoActivity extends AppCompatActivity {
    SharedPreferences planSp;
    private String failureTime = "2022-12-23 23:00:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH){
                    ifsendScore(editText);
                }
                return false;
            }
        });
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifsendScore(editText);
            }
        });
        setTime();
    }


    public void ifsendScore(View view) {
        final EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setMessage("确认为"+message+"分吗？"); //设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                sendScore(editText);
                editText.setText("");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                editText.setText("");
            }
        });

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    //先弹出对话框再sendScore
    public void sendScore(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textValue2 = (TextView) findViewById(R.id.textValue2);
        TextView textScore = (TextView) findViewById(R.id.textScore);
        String message = editText.getText().toString();

        //初始化
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long nowTime = System.currentTimeMillis(); //获取当前时间
            Date overTime = simpleDateFormat.parse(failureTime); //利用SimpleDateFormat来把字符串日期转换为Date对象类型
            long a = overTime.getTime() - nowTime; // 计算总的时间差（毫秒级别）
            if (a > 0) { //如果大于零 说明时间有剩余
                // 总时间（a）
                // 总天数( day*(1000 * 60 * 60 * 24) )
                long time = 283 - a / (1000 * 60 * 60 * 24);
                planSp = getSharedPreferences("planSp",0);
                time = time-planSp.getInt("number",0);
                if(time<=0){
                    Toast.makeText(this,"不需要打分",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(message.isEmpty()){
                    Toast.makeText(this,"需要打分",Toast.LENGTH_SHORT).show();
                    return ;
                }
                SharedPreferences.Editor editor = planSp.edit();
                float score = planSp.getFloat("score",100);
                Integer ii = new Integer(message);
                float x = ii.intValue();
                if(x>100 || x <0){
                    Toast.makeText(this,"分数应介于0-100",Toast.LENGTH_SHORT).show();
                    return ;
                }
                score = score - (100-x)/100/283 ;
                editor.putInt("number",planSp.getInt("number",0)+1);
                editor.putFloat("score",score);
                editor.commit();
                textValue2.setText("实评次数："+planSp.getInt("number",0));
                textScore.setText("完成度："+planSp.getFloat("score",100)+"%");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //判断漏评次数
    @SuppressLint("SetTextI18n")
    private void setTime() {
        TextView textValue1 = (TextView) findViewById(R.id.textValue1);
        TextView textValue2 = (TextView) findViewById(R.id.textValue2);
        TextView textScore = (TextView) findViewById(R.id.textScore);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long nowTime = System.currentTimeMillis(); //获取当前时间
            Date overTime = simpleDateFormat.parse(failureTime); //利用SimpleDateFormat来把字符串日期转换为Date对象类型
            long a = overTime.getTime() - nowTime; // 计算总的时间差（毫秒级别）
            if (a > 0) { //如果大于零 说明时间有剩余
                // 总时间（a）
                // 总天数( day*(1000 * 60 * 60 * 24) )
                long time = 283 - a / (1000 * 60 * 60 * 24);
                planSp = getSharedPreferences("planSp",0);
                time = time-planSp.getInt("number",0);
                if(time>0){
                    textValue2.setText("漏评次数："+time);
                }
                textScore.setText("完成度："+planSp.getFloat("score",100)+"%");
            }else{
                textValue1.setText(" ");
                textValue2.setText("祝你成功");
                textScore.setText("everything will be good");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
