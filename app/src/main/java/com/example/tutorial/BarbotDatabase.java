package com.example.tutorial;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;



@Database(entities = {RecipeEntity.class, IngredientJoin.class, IngredientEntity.class}, version = 1)
public abstract class BarbotDatabase extends RoomDatabase {

    private static final String DB_NAME = "BarbotDB.db";
    private static volatile BarbotDatabase instance;

    public static synchronized BarbotDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static BarbotDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                BarbotDatabase.class,
                DB_NAME).build();
    }



    public abstract RecipeDao recipeDao();
    public abstract IngredientDao  ingredientDao();
    public abstract RecipeIngredientDao recipeIngredientDao();

}
