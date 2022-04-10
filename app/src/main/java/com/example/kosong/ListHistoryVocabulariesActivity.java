package com.example.kosong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Layout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kosong.controller.dao.VocabularyDao;
import com.example.kosong.controller.database.AppDatabase;
import com.example.kosong.controller.entity.Vocabulary;

import java.util.List;

public class ListHistoryVocabulariesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history_vocabularies);

        listAllVocabularies();

    }
    public void listAllVocabularies(){
        final int[] count = new int[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(ListHistoryVocabulariesActivity.this);
                System.out.println("获取database");
                VocabularyDao dao = db.getVocabularyDao();
                System.out.println("获取dao");
                List<Vocabulary> vocabularies = dao.getAll();
                count[0] = dao.getAll().size();
                System.out.println("添加成功");

                db.close();
            }
        }).start();
        ConstraintLayout main = (ConstraintLayout) findViewById(R.id.mainlayout);
            TextView textView= new TextView(ListHistoryVocabulariesActivity.this);
            System.out.println("新建textview");
            textView.setText(count[0]+"");
            System.out.println("添加文字");
            main.addView(textView);
            System.out.println("添加view");
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
