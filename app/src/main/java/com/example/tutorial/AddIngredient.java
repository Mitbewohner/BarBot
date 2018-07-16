package com.example.tutorial;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class AddIngredient extends AppCompatActivity {

    private TextInputEditText editTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editTextName = findViewById(R.id.name);
        editTextName.setText(getIntent().getStringExtra(ListOfIngredients.NEW_INGREDIENT));
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //todo check if ingredientName is available
                }
            }
        });




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addIngredient(View v) {
        //check
        final IngredientEntity ingredient = new IngredientEntity();

        try {
            ingredient.setName(editTextName.getText().toString());

            TextInputEditText tiet = findViewById(R.id.coordinate);
            ingredient.setCoordinate(Integer.parseInt(tiet.getText().toString()));

            tiet = findViewById(R.id.hold);
            ingredient.setHold(Integer.parseInt(tiet.getText().toString()));

            tiet = findViewById(R.id.wait);
            ingredient.setWait(Integer.parseInt(tiet.getText().toString()));
            ingredient.setActive(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ListOfCocktails.inDao.insert(ingredient);
                    finish();
                }
            }).start();
        } catch (NumberFormatException e) {


            Log.e("DATABASE", "deleted ingredient because of: " + e.getMessage());
            //todo show error message
            finish();

        }

        /*
        if (hold < 0 || wait < 0) {
            //todo show error message
        }//*/
        /*
        for (Ingredient i : CoAndIn.getIngredients()) {
            if (i.getName().toLowerCase().equals(editTextName.getText().toString().toLowerCase())) {
                //todo warning, that ingredient already exists;
            }
        }

        int hold = 0;
        int wait = 0;
        int coordinate = 0;
        try {
            TextInputEditText tiet = findViewById(R.id.coordinate);
            coordinate = Integer.parseInt(tiet.getText().toString());

            tiet = findViewById(R.id.hold);
            hold = Integer.parseInt(tiet.getText().toString());

            tiet = findViewById(R.id.wait);
            wait = Integer.parseInt(tiet.getText().toString());
        } catch (NumberFormatException e) {
            //todo show error message
        }
        if (hold < 0 || wait < 0) {
            //todo show error message
        }
        //todo test if coordinates are in range
        Ingredient ingredient = new Ingredient(editTextName.getText().toString(), coordinate, hold, wait);
        ingredient.setActive(true);
        CoAndIn.addIngredient(ingredient);
        try {
            CoAndIn.save(this);
        } catch(IOException e) {
            //todo error message
        }
        //*/

        //finish();

    }




}
