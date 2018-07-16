package com.example.tutorial;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeIngredientDao {
    @Insert(onConflict = REPLACE)
    void insert(IngredientJoin... joins);

    @Update
    void update(IngredientJoin... joins);

    @Delete
    void delete(IngredientJoin... joins);

    @Query("DELETE FROM recipe_ingredient_join")
    void nukeJoins();


    @Query("SELECT IngredientEntity.* FROM IngredientEntity\n"+
            "INNER JOIN recipe_ingredient_join ON IngredientEntity.uid=recipe_ingredient_join.ingredientId\n"+
            "WHERE recipe_ingredient_join.recipeId=:recipeId")
    List<IngredientEntity> ingredientsForRecipe(long recipeId);

    @Query("SELECT RecipeEntity.* FROM RecipeEntity\n"+
            "INNER JOIN recipe_ingredient_join ON RecipeEntity.uid=recipe_ingredient_join.recipeId\n"+
            "WHERE recipe_ingredient_join.ingredientId=:ingredientId")
    List<RecipeEntity> recipesForIngredient(long ingredientId);

    @Query("SELECT recipe_ingredient_join.* FROM recipe_ingredient_join " +
            "INNER JOIN IngredientEntity ON recipe_ingredient_join.ingredientId=IngredientEntity.uid " +
            "WHERE recipe_ingredient_join.recipeId=:uid")
    List<IngredientJoin> getAllIngredientJoinsForRecipe(long uid);

    @Query("SELECT recipe_ingredient_join.* FROM recipe_ingredient_join")
    List<IngredientJoin> getAll();


}
