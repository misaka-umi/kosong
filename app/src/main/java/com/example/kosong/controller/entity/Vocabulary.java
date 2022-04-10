package com.example.kosong.controller.entity;


import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.ArrayList;

@androidx.room.Entity
public class Vocabulary {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name="date")
    private Date date;
    @ColumnInfo(name="letters")
    private ArrayList<String> letters;
    @ColumnInfo(name="group")
    private String group;


    public Vocabulary() {
    }




    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public ArrayList<String> getLetters() {
        return letters;
    }

    public void setLetters(ArrayList<String> letters) {
        this.letters = letters;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
