package com.example.kosong;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
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
        setGoal(); //设置目标
        setVisibility();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏为黑色

        //监听+按钮
        Button listPlus = (Button) findViewById(R.id.listPlus);
        listPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences goalsNumber = getSharedPreferences("goalsNumber", MODE_PRIVATE);
                SharedPreferences.Editor editor  = goalsNumber.edit();
                int nowNumber = goalsNumber.getInt("number",1);
                if(nowNumber<12){
                    editor.putInt("number",nowNumber+1);
                    editor.apply();
                    setVisibility();
                }else{
                    Toast.makeText(TodoActivity.this,"一天的目标太多啦，休息一下吧",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button listMinus = (Button) findViewById(R.id.listMinus);
        listMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences goalsNumber = getSharedPreferences("goalsNumber", MODE_PRIVATE);
                SharedPreferences.Editor editor  = goalsNumber.edit();
                int nowNumber = goalsNumber.getInt("number",1);
                if(nowNumber>0){
                    editor.putInt("number",goalsNumber.getInt("number",1)-1);
                    editor.apply();
                    setVisibility();
                }
            }
        });

    }

    public void setVisibility(){
        SharedPreferences goalsNumber = getSharedPreferences("goalsNumber", MODE_PRIVATE);
        int number = goalsNumber.getInt("number",1);
        String line = "line";
        for(int i = 1;i<=number;i++){
            try {
                Field idField = R.id.class.getDeclaredField(line+i);
                int id = idField.getInt(R.id.class);
                LinearLayout layout = findViewById(id);
                layout.setVisibility(View.VISIBLE);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        for(int i = number+1;i<=12;i++){
            try {
                Field idField = R.id.class.getDeclaredField(line+i);
                int id = idField.getInt(R.id.class);
                LinearLayout layout = findViewById(id);
                layout.setVisibility(View.GONE);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }


    public void setGoal(){
        String goal = "goal";
        for(int i = 1;i<=12;i++){
            try {
                SharedPreferences goalSp = getSharedPreferences("goalSp", MODE_PRIVATE);
                Field idField = R.id.class.getDeclaredField(goal+i);
                int id = idField.getInt(R.id.class);
                TextView textView = findViewById(id);
                textView.setText(goalSp.getString(id+"","点击此处设置目标"));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //获取输入的各目标分数
    public int getScore(){
        int score = 0;
        String edit_score = "edit_score";
        for(int i = 1;i<=12;i++){
            try {
                Field idField = R.id.class.getDeclaredField(edit_score+i);
                int id = idField.getInt(R.id.class);
                EditText editText= findViewById(id);
                if(!editText.getText().toString().isEmpty()){
                    score +=Integer.parseInt(editText.getText().toString());
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return score;
    }

    //弹出对话框，进行确认
    public void ifsendScore(View view) {
        final EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        if(!message.isEmpty()){
            int score = getScore();
            AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
            builder.setMessage("目标设置计算为"+score+"分，确认为"+message+"分吗？"); //设置内容
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); //关闭dialog
                    sendScore(editText);
                    editText.setText("");
                    String edit_score = "edit_score";
                    //分数提交后将分数清0
                    for(int i = 1;i<=12;i++){
                        try {
                            Field idField = R.id.class.getDeclaredField(edit_score+i);
                            int id = idField.getInt(R.id.class);
                            EditText editText= findViewById(id);
                            if(!editText.getText().toString().isEmpty()){
                                editText.setText("");
                            }
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
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
            return;
        }else{
            Toast.makeText(this,"需要打分",Toast.LENGTH_SHORT).show();
            return;
        }
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
                if(time>0 && time<5){
                    textValue2.setText("漏评次数："+time);
                }
                if(time>=5){
                    //如果超过五次漏评则弹出弹窗修改总分
                    showScoreDialog(this,(int)time-1);
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


    public void showScoreDialog(Context context,final int time){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final EditText et = new EditText(context);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setHint("漏评"+time+"次，请输入平均分 0~100");
        dialog.setView(et);//给对话框添加一个EditText输入文本框
        dialog.setTitle(" ");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String message =et.getText().toString();
                SharedPreferences planSp = getSharedPreferences("planSp",MODE_PRIVATE);
                SharedPreferences.Editor editor = planSp.edit();
                if(!message.isEmpty()){
                    int x = Integer.parseInt(message);
                    if(x>100 || x <0){
                        return ;
                    }
                    float score = planSp.getFloat("score",100);
                    score = score - ((100-x)/100/283)*time ;
                    editor.putInt("number",planSp.getInt("number",0)+time);
                    editor.putFloat("score",score);
                    editor.commit();
                    setTime();
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


    //点击textView弹出dialog并修改内容

    public void onClick(View v){
            showDialog(this,v);

    }

    public void showDialog(Context context,View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        final EditText et = new EditText(context);
        final TextView goal = (TextView) findViewById(view.getId());
        et.setHint(""+goal.getText()); //提示文为原有文字
        if(goal.getText().equals("点击此处设置目标")){
            et.setText("");
        }else{
            et.setText(""+goal.getText()); //填充为原有文字
        }
        dialog.setView(et);//给对话框添加一个EditText输入文本框
        dialog.setTitle(" ");
        final SharedPreferences.Editor editor = getSharedPreferences("goalSp", MODE_PRIVATE).edit();

        //给对话框添加一个确定按钮，同样的方法可以添加一个取消按钮
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String message =et.getText().toString();
                if(!message.isEmpty()){
                    editor.putString(goal.getId()+"",message);
                    editor.apply();
                    setGoal();
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
