package com.example.tutorial;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    long insert(RecipeEntity re);

    @Update
    void update(RecipeEntity... re);

    @Delete
    void delete(RecipeEntity... re);

    @Query("SELECT * FROM RecipeEntity WHERE uid=:uid LIMIT 1")
    RecipeEntity findById(long uid);

    @Query("SELECT * FROM RecipeEntity")
    List<RecipeEntity> getAll();

    @Query("DELETE FROM RecipeEntity")
    void nukeRecipes();
    /////////////////////////////////////////////////////////////



}
