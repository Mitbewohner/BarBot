package com.example.tutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static java.lang.Thread.sleep;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();


/*
        try {
            CoAndIn.load(this);

        } catch (IOException | ClassNotFoundException e) {
            CoAndIn.createNewLists();
            //todo show dialog-message, that recipes and ingredients could not be loaded
        }
        */
        Runnable run = new Runnable() {

            @Override
            public void run() {
                //TODO remove this line later, because ist drops all tables
                //BarbotDatabase.getInstance(SplashScreenActivity.this).clearAllTables();

                ListOfCocktails.recDao = BarbotDatabase.getInstance(SplashScreenActivity.this).recipeDao();
                ListOfCocktails.recInDao = BarbotDatabase.getInstance(SplashScreenActivity.this).recipeIngredientDao();
                ListOfCocktails.inDao = BarbotDatabase.getInstance(SplashScreenActivity.this).ingredientDao();

                //todo remove these lines, because they drop tables
                ListOfCocktails.recDao.nukeRecipes();
                ListOfCocktails.recInDao.nukeJoins();

                startActivity(new Intent(SplashScreenActivity.this, ListOfCocktails.class));
                SplashScreenActivity.this.finish();
            }
        };
        Thread th = new Thread(run);
        th.start();



/*
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                //getSupportActionBar().show();
                startActivity(new Intent(SplashScreenActivity.this, ListOfCocktails.class));
                //getSupportActionBar().show();
                finish();
            }
        }, 5000);*/




    }
}
