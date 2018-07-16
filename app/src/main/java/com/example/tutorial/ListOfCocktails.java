package com.example.tutorial;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

public class ListOfCocktails extends AppCompatActivity {

    private LinearLayout ll;
    public static RecipeDao recDao;
    public static IngredientDao inDao;
    public static RecipeIngredientDao recInDao;

    private List<RecipeEntity> recs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_of_cocktails);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListOfCocktails.this, GeneralSettingsActivity.class));

            }
        });

        ll = findViewById(R.id.linearLayout);
        listCocktails();
    }

    public void listCocktails() {
        ll.removeAllViews();
        //List<Recipe> recipes = CoAndIn.getRecipes();
        new Thread(new Runnable() {

            @Override
            public void run() {
                //List<RecipeEntity> recs = recDao.getAll();
                recs = recDao.getAll();
                fillViews();
            }
        }).start();
        //Collections.sort(recipes);






/*
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);

            RelativeLayout rl = new RelativeLayout(this);
            rl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 256)); //TODO export 256 as Constant
            rl.setGravity(Gravity.CENTER_HORIZONTAL);

            TextView tv = new TextView(this);
            tv.setEnabled(false);
            tv.setFocusable(false);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20); //TODO export 20 as Constant
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tv.setLayoutParams(params);
            tv.setText(recipe.getName());
            tv.setGravity(Gravity.CENTER);
            rl.addView(tv);

            Button but = new Button(this);
            but.setText("pour");
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            but.setLayoutParams(params);
            rl.addView(but);

            ll.addView(rl);
        }*/
    }

    public void fillViews() {
        Collections.sort(recs);
        RelativeLayout rel = new RelativeLayout(ListOfCocktails.this);
        rel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 128)); //TODO export constant
        ll.addView(rel);
        for (int i = 0; i < recs.size(); i++) {
            RecipeEntity rec = recs.get(i);
            RelativeLayout rl = new RelativeLayout(ListOfCocktails.this);
            rl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 256)); //todo export constant 256
            rl.setGravity(Gravity.CENTER_HORIZONTAL);
            TextView tv = new TextView(ListOfCocktails.this);
            tv.setEnabled(false);
            tv.setFocusable(false);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20); //TODO export 20 as Constant
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tv.setLayoutParams(params);
            tv.setText(rec.getName());
            tv.setGravity(Gravity.CENTER);
            rl.addView(tv);

            Button but = new Button(ListOfCocktails.this);
            but.setText("pour");
            params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            but.setLayoutParams(params);
            rl.addView(but);

            ll.addView(rl);
        }
    }

}
