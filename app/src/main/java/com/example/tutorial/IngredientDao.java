package com.example.tutorial;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {



    @Insert
    void insert(IngredientEntity iee);

    @Update
    void update(IngredientEntity... iee);

    @Delete
    void delete(IngredientEntity... iee);

    //not sure if I need this one
    @Query("SELECT * FROM IngredientEntity")
    List<IngredientEntity> getAll();

    @Query("SELECT * FROM IngredientEntity WHERE uid=:uid")
    IngredientEntity getById(int uid);
/*
    @Query("Select * FROM IngredientEntity WHERE recipeId=:recipeId")
    List<IngredientEntity> findIngredientsForRecipe(final int recipeId);
    */



}
