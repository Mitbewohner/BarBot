package com.example.tutorial;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class IngredientSetting extends AppCompatActivity {

    private IngredientEntity ingredient;
    private TextInputEditText name;
    private TextInputEditText coordinate;
    private TextInputEditText hold;
    private TextInputEditText wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        coordinate = findViewById(R.id.coordinate);
        hold = findViewById(R.id.hold);
        wait = findViewById(R.id.wait);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ingredient = ListOfCocktails.inDao.getById(Integer.parseInt(getIntent().getStringExtra(ListOfIngredients.INGREDIENT)));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(ingredient.getName());
                        coordinate.setText(String.valueOf(ingredient.getCoordinate()));
                        hold.setText(String.valueOf(ingredient.getHold()));
                        wait.setText(String.valueOf(ingredient.getWait()));
                    }
                });

            }
        }).start();

        //this.ingredient = (Ingredient) getIntent().getSerializableExtra(ListOfIngredients.INGREDIENT);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void saveChanges(View v) {
        //todo check if name is already taken (except itself)
        //todo check if inputs (coordinate, hold, wait) are correct
        try {
            ingredient.setName(name.getText().toString());

            ingredient.setCoordinate(Integer.parseInt(coordinate.getText().toString()));
            ingredient.setHold(Integer.parseInt(hold.getText().toString()));
            ingredient.setWait(Integer.parseInt(wait.getText().toString()));
            //todo save ingredients in storage
        /*
        try {
            CoAndIn.save(this);
            finish();
        } catch(IOException e) {
            //todo show error message
        }*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ListOfCocktails.inDao.update(ingredient);
                    finish();
                }
            }).start();
        } catch (NumberFormatException e) {
            //todo error
            finish();
        }
    }


}
