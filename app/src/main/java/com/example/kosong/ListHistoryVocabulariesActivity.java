package com.example.kosong;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Layout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kosong.controller.dao.VocabularyDao;
import com.example.kosong.controller.database.AppDatabase;
import com.example.kosong.controller.entity.Vocabulary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListHistoryVocabulariesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history_vocabularies);

        listAllVocabularies();

    }
    public void listAllVocabularies(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AppDatabase db = AppDatabase.getInstance(ListHistoryVocabulariesActivity.this);
//                System.out.println("获取database");
//                VocabularyDao dao = db.getVocabularyDao();
//                System.out.println("获取dao");
//                List<Vocabulary> vocabularies = dao.getAll();
//                count[0] = dao.getAll().size();
//                System.out.println("添加成功");
//
//                db.close();
//            }
//        }).start();
        AppDatabase db = AppDatabase.getInstance(ListHistoryVocabulariesActivity.this);
        System.out.println("获取database");
        VocabularyDao dao = db.getVocabularyDao();
        System.out.println("获取dao");
        List<Vocabulary> vocabularies = dao.getAll();
        System.out.println(vocabularies);
        System.out.println("添加成功");

        db.close();

//
        LinearLayout main = (LinearLayout) findViewById(R.id.mainlayout);
        for (int i = vocabularies.size()-1; i >=0; i--) {
            Button bt= new Button(ListHistoryVocabulariesActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 10, 10, 10);//设置边距
            lp.height=300;
            bt.setBackground(getDrawable(R.drawable.card_shape));
            bt.setLayoutParams(lp);
            System.out.println("gen button");
            ArrayList<String> letters = vocabularies.get(i).getLetters();
            System.out.println("添加文字");
            String msg= "";
            for(int j = 0; j<letters.size();j++){
                msg += letters.get(j)+"  ";
            }



            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String s ="无日期";
            if(vocabularies.get(i).getDate() !=null) {
                s = simpleDateFormat.format(vocabularies.get(i).getDate());
            }
            bt.setText(s);
            main.addView(bt);

            System.out.println("添加view");
        }

//        for(int i = 0;i<vocabularyList.size();i++){
//            TextView textView= new TextView(ListHistoryVocabulariesActivity.this);
//            System.out.println("新建textview");
//            textView.setText(vocabularyList.get(i).getLetters().toString());
//            System.out.println("添加文字");
//            main.addView(textView);
//            System.out.println("添加view");
//        }

    }
}
