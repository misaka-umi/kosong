package com.example.kosong.controller.dao;

import androidx.room.Entity;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.kosong.controller.entity.Vocabulary;

import java.util.Date;
import java.util.List;

@Dao
public interface VocabularyDao {

    @Query("SELECT * FROM Vocabulary")
    List<Vocabulary> getAll();

//    @Query("SELECT * FROM Vocabulary WHERE id IN (:vocabularyIds)")
//    List<Vocabulary> loadAllByIds(int[] vocabularyIds);
//
//    @Query("SELECT * FROM Vocabulary WHERE date LIKE :date")
//    Vocabulary findByDate(Date date);

    @Insert
    void insertAll(Vocabulary... vocabularies);

    @Delete
    void delete(Vocabulary vocabulary);
}
