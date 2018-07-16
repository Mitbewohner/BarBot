package com.example.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GeneralSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.settings));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void startIngredientSettings(View v) {
        startActivity(new Intent(this, ListOfIngredients.class));
    }

    public void startRecipeSettings(View v) {
        startActivity(new Intent(this, RecipesSettings.class));
    }

    public void startBluetoothSettings(View v) {
        startActivity(new Intent(this, BluetoothSettings.class));
    }



}