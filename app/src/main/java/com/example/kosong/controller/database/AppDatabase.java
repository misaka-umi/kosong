package com.example.kosong.controller.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.kosong.controller.Converters;
import com.example.kosong.controller.dao.VocabularyDao;
import com.example.kosong.controller.entity.Vocabulary;

@androidx.room.Database(entities = {Vocabulary.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME="VocabularyDb.db";
    private static volatile AppDatabase instance;//创建单例
    public static synchronized AppDatabase getInstance(Context context){
        if (instance==null){
            instance=create(context);
        }
        return instance;
    }
    /**
     * 创建数据库*/
    private static AppDatabase create(Context context) {
        return Room.databaseBuilder(context,AppDatabase.class,DB_NAME)
                .allowMainThreadQueries()//允许在主线程运行
                .build();


//        AppDatabase db = Room.databaseBuilder(context,
//                AppDatabase.class, DB_NAME).build();
//        return db;
    }

    public  abstract VocabularyDao getVocabularyDao();//这个是必要的，创建DAO的抽象类
}