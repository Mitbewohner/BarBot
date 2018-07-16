package com.example.tutorial;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity
public class RecipeEntity implements Comparable<RecipeEntity>{

    @PrimaryKey(autoGenerate = true)
    private long uid;


    @ColumnInfo(name = "name")
    private String name;
    //todo add getter and setter


    //GETTER AND SETTER
    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return this.uid;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(RecipeEntity recipeEntity) {
        return (this.getName().compareToIgnoreCase(recipeEntity.getName()));
    }




}

