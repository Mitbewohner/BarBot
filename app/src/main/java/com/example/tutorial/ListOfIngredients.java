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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Switch;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.view.View.TEXT_ALIGNMENT_VIEW_START;
import static java.lang.Thread.sleep;


public class ListOfIngredients extends AppCompatActivity {


    private LinearLayout ll;
    private List<IngredientEntity> ingredients;
    private Set<IngredientEntity> changedIngredients;

    public static final int DIVIDER_SIZE = 5;
    public static final String NEW_INGREDIENT = "com.example.BarBot.NEW_INGREDIENT";
    public static final String INGREDIENT = "com.example.BarBot.INGREDIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changedIngredients = new HashSet<>();
        listIngredients();
        //create List of Ingredients



    }

    @Override
    public void onRestart() {
        super.onRestart();
        listIngredients();
    }
/*
    @Override
    public void onPause() {
        super.onPause();
        /*
        try {
            CoAndIn.save(this);
        } catch (IOException e) {
            //todo error message
        }//*//*
    }*/

    protected void listIngredients() {
        ll = findViewById(R.id.linear_layout_ingredients_settings);
        ll.removeAllViews();


        new Thread(new Runnable() {

            @Override
            public void run() {
                ingredients = ListOfCocktails.inDao.getAll();
                Log.d("DATABASE", "" + ingredients.size());
                Collections.sort(ingredients);
                if (!ingredients.isEmpty()) {
                    Log.d("DATABASE", "" + ingredients.get(0).getName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fillViews();
                    }
                });
                //ListOfIngredients.this.fillViews();

            }
        }).start();
    }


    private void fillViews() {
/*
        try {
            CoAndIn.load(this);
        } catch (ClassNotFoundException | IOException e) {
            //todo error message
        }
        List<Ingredient> ingredients = CoAndIn.getIngredients();

        Collections.sort(ingredients);
*/
        for (int i = 0; i < ingredients.size(); i++) {
            RelativeLayout rl = new RelativeLayout(this);
            LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            rl.setLayoutParams(lllp);
            IngredientEntity ingr = ingredients.get(i);

            Switch sw = new Switch(this);
            Button but = new Button(this);
            but.setText(ingr.getName());
            sw.setId(View.generateViewId());


            but.setOnClickListener(new IngredientClickListener(ingr));
            but.setTextAlignment(TEXT_ALIGNMENT_VIEW_START);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 128); //Todo export constant
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.addRule(RelativeLayout.LEFT_OF, sw.getId());

            but.setLayoutParams(params);
            but.setBackgroundColor(Color.TRANSPARENT);


            params = new RelativeLayout.LayoutParams(128, 128); //todo export constants
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            sw.setLayoutParams(params);
            //sw.setActivated(ingr.isActive());
            sw.setChecked(ingr.isActive());
            sw.setOnCheckedChangeListener(new SwitchChangeListener(ingr));


            rl.addView(but);
            rl.addView(sw);
            ll.addView(rl);

            if (i < ingredients.size() - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams par = new LayoutParams(LayoutParams.MATCH_PARENT, (int) getResources().getDisplayMetrics().density * DIVIDER_SIZE);
                par.setMargins(0, 50, 0, 50); //todo export constants
                divider.setLayoutParams(par);
                divider.setBackgroundColor(Color.GRAY);
                ll.addView(divider);
            }


        }
    }



    public void addIngredient(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title"); //todo replace "title"
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint(R.string.ingredient_name);
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
                //todo check, if ingredient with name 'name' already exists. if so, print message
                startActivity(new Intent(ListOfIngredients.this, AddIngredient.class).putExtra(NEW_INGREDIENT, name));
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

    private class IngredientClickListener implements View.OnClickListener {



        private final IngredientEntity ingredient;

        private IngredientClickListener(@NonNull IngredientEntity ingredient) {
            this.ingredient = ingredient;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), IngredientSetting.class);
            intent.putExtra(INGREDIENT, "" + ingredient.getUid());
            //Log.d("HASHCODE", ingredient.getName() + " " + ingredient.hashCode());
            v.getContext().startActivity(intent);
        }
    }



    private class SwitchChangeListener implements CompoundButton.OnCheckedChangeListener {

        private final IngredientEntity ingr;

        private SwitchChangeListener(@NonNull IngredientEntity ingr) {
            this.ingr = ingr;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ingr.setActive(isChecked);
            if (changedIngredients.contains(ingr)) {
                changedIngredients.remove(ingr);
            } else {
                changedIngredients.add(ingr);
            }
        }
    }

    @Override
    public void onPause() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                IngredientEntity[] array = new IngredientEntity[changedIngredients.size()];
                ListOfCocktails.inDao.update(changedIngredients.toArray(array));
                synchronized (ListOfIngredients.this) {
                    ListOfIngredients.this.notify();
                }

            }
        }).start();
        synchronized(this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onPause();

    }



}
