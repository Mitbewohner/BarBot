package com.example.tutorial;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.Collections;
import java.util.List;

public class RecipesSettings extends AppCompatActivity {

    private boolean loaded = false;
    private boolean inserted = false;
    public static final int DIVIDER_SIZE = 5;
    public static final String RECIPE = "com.example.BarBot.RECIPE";
    public static final String NEW_RECIPE = "com.example.BARBOT.NEW_RECIPE";
    private List<RecipeEntity> recipes;
    private static boolean hasIngredients = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listRecipes();
    }



    @Override
    public void onRestart() {
        super.onRestart();
        listRecipes();
    }
/*
    @Override
    public void onPause() {
        super.onPause();
        try {
            CoAndIn.save(this);
        } catch (IOException e) {
            //todo error message
        }
    }
    */

    protected void listRecipes() {
        LinearLayout ll = findViewById(R.id.linear_layout_recipes_settings);
        ll.removeAllViews();
        /*
        try {
            CoAndIn.load(this);
            Log.d("LOSGEHTS", "Sucessfully loaded!");
        } catch (IOException | ClassNotFoundException e) {
            //todo error message
            Log.e("LOSGEHTS", "Failed to list Recipes" + e.getMessage());
        }
        List<Recipe> recipes = CoAndIn.getRecipes();
        Log.d("LOSGEHTS", String.valueOf(recipes.size()));
        */
        synchronized (this) {
            loaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Do Something
                    synchronized (RecipesSettings.this) {
                        recipes = ListOfCocktails.recDao.getAll();
                        hasIngredients = !ListOfCocktails.inDao.getAll().isEmpty();
                        loaded = true;
                        RecipesSettings.this.notify();
                    }
                }
            }).start();

            while (!loaded) {
                try {

                    wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //hier solls weitergehn, nachdem der obige Thread abgeschlossen ist...
        Collections.sort(recipes);

        for (int i = 0; i < recipes.size(); i++) {
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            RecipeEntity rec = recipes.get(i);


            Button but = new Button(this);
            but.setText(rec.getName());

            but.setOnClickListener(new RecipeClickListener(rec));
            but.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 256); //Todo export constant

            but.setLayoutParams(params);
            but.setBackgroundColor(Color.TRANSPARENT);

            ll.addView(but);

            if (i < recipes.size() - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams par = new LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDisplayMetrics().density * DIVIDER_SIZE);
                par.setMargins(0, 50, 0, 50); //todo export constants
                divider.setLayoutParams(par);
                divider.setBackgroundColor(Color.GRAY);
                ll.addView(divider);
            }


        }

    }

    public void addRecipe(View v) {

        if (!hasIngredients) {
            //todo dialog message: no ingredients to create a recipe
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title"); //todo replace "title"
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setHint(R.string.recipe_name);
            input.setSingleLine();
            // todo set maxlength of text
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String name = input.getText().toString();
                    if (name.equals("")) {
                        //todo error
                        return;
                    }
                    //todo check, if recipe with name 'name' already exists. if so, print message
                    final RecipeEntity rec = new RecipeEntity();
                    final Intent intent = new Intent(RecipesSettings.this, RecipeSetting.class);
                    rec.setName(name);

                    synchronized (RecipesSettings.this) {
                        inserted = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                long id = ListOfCocktails.recDao.insert(rec);
                                RecipeEntity rec = ListOfCocktails.recDao.findById((int) id);
                                intent.putExtra(RECIPE, "" + rec.getUid());
                                Log.d("DATABASE", "inserted recipe " + rec.getName() + " with uid: " + rec.getUid());
                                synchronized (RecipesSettings.this) {
                                    inserted = true;
                                    RecipesSettings.this.notify();
                                }
                            }
                        }).start();

                        while (!inserted) {
                            try {
                                RecipesSettings.this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //Log.d("DATABASE", "erstellte id: " + rec.getUid());
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }

    }

    protected class RecipeClickListener implements View.OnClickListener {
        private final RecipeEntity rec;


        private RecipeClickListener(@NonNull RecipeEntity rec) {
            this.rec = rec;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), RecipeSetting.class);
            intent.putExtra(RECIPE, "" + rec.getUid());
            v.getContext().startActivity(intent);
        }
    }



}
