package com.example.kosong;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class VocabularyActivity extends AppCompatActivity {

    ArrayList<String> letters = new ArrayList<>();
    SharedPreferences lettersSP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        //获取之前保存的数据
        SharedPreferences DataList = getSharedPreferences("lettersSP", MODE_PRIVATE);
        int environNums = DataList.getInt("amount", 0);
        for (int i = 0; i < environNums; i++)
        {
            String environItem = DataList.getString("item_"+i, null);
            letters.add(environItem);
        }
        if(environNums>0){
            recoverVocabulary();
        }


        final EditText editText = (EditText) findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_SEARCH){
                    sendMessage(editText);
                    editText.setText("");
                }
                return false;
            }
        });
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(editText);
                editText.setText("");
            }
        });





    }
    @SuppressLint("SetTextI18n")
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView1);
        String message = editText.getText().toString();
        if(message.isEmpty()){
            recoverVocabulary();
            return ;
        }
        //若输入clear，则清空sharedpreference
        if(message.equals("clear")){
            SharedPreferences.Editor editor = getSharedPreferences("lettersSP", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            letters.clear();
            Toast.makeText(this,"已删除数组",Toast.LENGTH_SHORT).show();
            recoverVocabulary();
            return ;
        }else{
            letters.add(message);
        }
        ArrayList<Integer> numbers = new ArrayList<>();
        if(letters.size()>0){
            for(int i= 0;i<letters.size();i++){
                int rightNumber = 0;
                while(rightNumber==0){
                    Random r = new Random();
                    int number = r.nextInt(letters.size());
                    rightNumber = seekNumber(numbers,number);
                }
            }
            String vocabulary = "";
            int amount = 0;
            for(int i = 0; i< letters.size();i++){
                vocabulary = vocabulary + letters.get(numbers.get(i))+"  ";
                amount+=1;
            }


            //将letters存入lettersSP；
            SharedPreferences.Editor editor = getSharedPreferences("lettersSP", MODE_PRIVATE).edit();
            editor.putInt("amount", letters.size());
            for (int i = 0; i < letters.size(); i++)
            {
                editor.putString("item_"+i, letters.get(i));
            }
            editor.commit();


            textView.setText(vocabulary);
            textView2.setText("" + amount);

        }
    }
    public static int seekNumber(ArrayList<Integer> numbers,int number){
        if(numbers.size()==0){
            numbers.add(number);
            return 1;
        }else{
            for(int j=0;j<numbers.size();j++){
                if(number==numbers.get(j)){
                    return 0;
                }
            }
            numbers.add(number);
            return 1;
        }
    }

    //输入空单词恢复原顺序
    @SuppressLint("SetTextI18n")
    public void recoverVocabulary() {
        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView1);
        String vocabulary = "";
        int amount = 0;
        if(letters.size()>0) {
            for (int i = 0; i < letters.size(); i++) {
                vocabulary = vocabulary + letters.get(i) + "  ";
                amount += 1;
            }
            textView.setText(vocabulary);
            textView2.setText("" + amount);
            //Toast.makeText(this,"需要输入单词/已还原顺序",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(letters.size()==0){
            textView.setText("");
            textView2.setText("");
            return ;
        }
    }


}