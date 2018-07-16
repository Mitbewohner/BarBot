package com.example.tutorial;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class RecipeSetting extends AppCompatActivity {

    public static final int VOLUME_PER_FILL = 25; //mililiters
    public static final int MAX_FILLS = 6; //so maximum volume of ingredient per recipe is VOLUME_PER_FILL * MAX_FILLS

    private boolean inserted = false;
    private boolean updated = false;
    private boolean loaded = false;
    private RecipeEntity rec = null;
    private List<IngredientEntity> allIngredients;
    private List<IngredientEntity> ingredients;
    private List<IngredientJoin> joins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listIngredients();
    }



    private void listIngredients() {
        synchronized (this) {
            loaded = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    rec = ListOfCocktails.recDao.findById(Integer.parseInt(getIntent().getStringExtra(RecipesSettings.RECIPE)));
                    ingredients = ListOfCocktails.recInDao.ingredientsForRecipe(rec.getUid());
                    allIngredients = ListOfCocktails.inDao.getAll();
                    joins = ListOfCocktails.recInDao.getAllIngredientJoinsForRecipe(rec.getUid());
                    synchronized (RecipeSetting.this) {
                        loaded = true;
                        RecipeSetting.this.notify();
                    }
                }
            }).start();
            while (!loaded) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        TextView tf = findViewById(R.id.nameOfRecipe);
        tf.setText(rec.getName());
        updateIngredients();


    }


    public void saveChanges(View v) {

        LinearLayout ll = findViewById(R.id.ll);

        for (int i = 0; i < ll.getChildCount(); i++) {

            //todo adding numberoffills
            ConstrLayout constr = ((ConstrLayout) ll.getChildAt(i));
            constr.getElement().setNumberOfFills(constr.getNumberOfFills());
        }

        rec.setName(((TextView) findViewById(R.id.nameOfRecipe)).getText().toString());
        synchronized (this) {
            updated = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ListOfCocktails.recDao.update(rec);
                    IngredientJoin[] j = new IngredientJoin[joins.size()];
                    ListOfCocktails.recInDao.insert(joins.toArray(j));
                    synchronized (RecipeSetting.this) {
                        updated = true;
                        RecipeSetting.this.notify();
                    }
                }
            }).start();

            while (!updated) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        finish();
    }

    public void addIngredient(View v) {
        final List<IngredientEntity> clone = new ArrayList<>(allIngredients);
        clone.removeAll(ingredients);
        if (!clone.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title"); //todo replace "title"
            final Spinner input = new Spinner(this);
            ArrayAdapter<IngredientEntity> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clone);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            input.setAdapter(aa);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //rec.addIngredient((Ingredient) input.getSelectedItem());
                    final IngredientJoin join = new IngredientJoin(rec.getUid(), ((IngredientEntity)input.getSelectedItem()).getUid());
                    join.setPositionInRecipe(allIngredients.size() - clone.size());
                    synchronized (RecipeSetting.this) {
                        inserted = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ListOfCocktails.recInDao.insert(join);

                                synchronized(RecipeSetting.this) {
                                    inserted = true;
                                    RecipeSetting.this.notify();
                                }
                            }
                        }).start();

                        while (!inserted) {
                            try {
                                RecipeSetting.this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                    dialog.cancel();
                    listIngredients();
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

    public void swapPosition(int firstId, int secondId) {
        if (firstId == secondId) {
            return;
        }
        IngredientJoin first = null;
        IngredientJoin second = null;

        for (IngredientJoin j : joins) {
            if (j.getPositionInRecipe() == firstId) {
                first = j;
            }
            if (j.getPositionInRecipe() == secondId) {
                second = j;
            }
        }
        if (first == null || second == null) {
            return;
        }

        int temp = first.getPositionInRecipe();
        first.setPositionInRecipe(second.getPositionInRecipe());
        second.setPositionInRecipe(temp);


        updateIngredients();



    }

    public void updateIngredients() {
        LinearLayout ll = findViewById(R.id.ll);
        ll.removeAllViews();

        Collections.sort(joins);



        for (int i = 0; i < joins.size(); i++) {

            ConstrLayout linLay = new ConstrLayout(this, joins.get(i));



            if (i == 0) {
                linLay.upwardsPossible(false);
            }
            if (i == joins.size() - 1) {
                linLay.downwardsPossible(false);
            }
            ll.addView(linLay);


        }

    }











    private class ConstrLayout extends ConstraintLayout {

        private IngredientJoin element;
        private IngredientEntity ingredient;
        private TextView tv = null;
        private ImageButton upwards = null;
        private ImageButton downwards = null;
        private TextThumbSeekBar sb = null;

        public ConstrLayout(@NonNull Context c, @NonNull IngredientJoin element) {
            super(c);
            this.element = element;
            this.setId(generateViewId());
            for (IngredientEntity i : ingredients) {
                if (i.getUid() == element.getIngredientId()) {
                    ingredient = i;
                    break;
                }
            }
            fillContainer(c);
        }

        private IngredientJoin getElement() {
            return this.element;
        }

        private int getNumberOfFills() {
            return sb.getProgress() + 1;
        }

        private void fillContainer(Context c) {







            upwards = new ImageButton(c);
            upwards.setId(generateViewId());
            upwards.setImageResource(R.drawable.ic_arrow_upward_black_32dp);

            downwards = new ImageButton(c);
            downwards.setId(generateViewId());
            downwards.setImageResource(R.drawable.ic_arrow_downward_black_32dp);

            upwards.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapPosition(element.getPositionInRecipe(), element.getPositionInRecipe() - 1);
                }
            });
            downwards.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    swapPosition(element.getPositionInRecipe(), element.getPositionInRecipe() + 1);
                }
            });

            tv = new TextView(c);
            tv.setId(generateViewId());
            tv.setText(ingredient.getName());
            tv.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            tv.setBackgroundColor(Color.GREEN);
            Log.d("DEBUG", ingredient.getName());

            sb = new TextThumbSeekBar(c);
            sb.setMax(MAX_FILLS - 1);
            sb.setId(generateViewId());
            sb.setProgress(element.getNumberOfFills() - 1);
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    element.setNumberOfFills(sb.getProgress() + 1);
                }
            });


            addView(upwards);
            addView(downwards);
            addView(tv);
            addView(sb);

            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            ConstraintSet set = new ConstraintSet();
            set.clone(this);


            set.constrainWidth(upwards.getId(), ConstraintSet.WRAP_CONTENT);
            set.constrainHeight(upwards.getId(), ConstraintSet.WRAP_CONTENT);

            set.constrainWidth(downwards.getId(), ConstraintSet.WRAP_CONTENT);
            set.constrainHeight(downwards.getId(), ConstraintSet.WRAP_CONTENT);

            set.constrainWidth(tv.getId(), ConstraintSet.MATCH_CONSTRAINT);
            set.constrainHeight(tv.getId(), ConstraintSet.MATCH_CONSTRAINT);

            set.constrainWidth(sb.getId(), ConstraintSet.MATCH_CONSTRAINT);
            set.constrainHeight(sb.getId(), ConstraintSet.WRAP_CONTENT);

            set.connect(upwards.getId(), ConstraintSet.START, this.getId(), ConstraintSet.START, 0); //todo export constant 16
            set.connect(upwards.getId(), ConstraintSet.TOP, this.getId(), ConstraintSet.TOP, 16);
            set.connect(downwards.getId(), ConstraintSet.START, upwards.getId(), ConstraintSet.END, 16);
            set.connect(downwards.getId(), ConstraintSet.TOP, upwards.getId(), ConstraintSet.TOP, 0);
            set.connect(sb.getId(), ConstraintSet.BOTTOM, downwards.getId(), ConstraintSet.BOTTOM, 0);
            set.connect(sb.getId(), ConstraintSet.START, downwards.getId(), ConstraintSet.END, 16);
            set.connect(sb.getId(), ConstraintSet.END, this.getId(), ConstraintSet.END, 16);
            set.connect(tv.getId(), ConstraintSet.TOP, downwards.getId(), ConstraintSet.TOP, 0);
            set.connect(tv.getId(), ConstraintSet.START, downwards.getId(), ConstraintSet.END, 16);
            set.connect(tv.getId(), ConstraintSet.END, this.getId(), ConstraintSet.END, 0);
            set.connect(tv.getId(), ConstraintSet.BOTTOM, sb.getId(), ConstraintSet.TOP, 0);
            set.applyTo(this);







        }

        public void upwardsPossible(boolean possible) {
            upwards.setVisibility(possible ? VISIBLE : INVISIBLE);
        }

        public void downwardsPossible(boolean possible) {
            downwards.setVisibility(possible ? VISIBLE : INVISIBLE);
        }






        public class TextThumbSeekBar extends android.support.v7.widget.AppCompatSeekBar {

            private int mThumbSize;
            private TextPaint mTextPaint;

            public TextThumbSeekBar(Context context) {
                this(context, null);
            }

            public TextThumbSeekBar(Context context, AttributeSet attrs) {
                this(context, attrs, android.R.attr.seekBarStyle);
            }

            public TextThumbSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);

                mThumbSize = getResources().getDimensionPixelSize(R.dimen.thumb_size);

                mTextPaint = new TextPaint();
                mTextPaint.setColor(Color.BLACK);
                mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.thumb_text_size));
                mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
                mTextPaint.setTextAlign(Paint.Align.CENTER);
            }

            @Override
            protected synchronized void onDraw(Canvas canvas) {
                super.onDraw(canvas);

                String progressText = String.valueOf((getProgress() + 1) * VOLUME_PER_FILL);
                Rect bounds = new Rect();
                mTextPaint.getTextBounds(progressText, 0, progressText.length(), bounds);

                int leftPadding = getPaddingLeft() - getThumbOffset();
                int rightPadding = getPaddingRight() - getThumbOffset();
                int width = getWidth() - leftPadding - rightPadding;
                float progressRatio = (float) getProgress() / getMax();
                float thumbOffset = mThumbSize * (.5f - progressRatio);
                float thumbX = progressRatio * width + leftPadding + thumbOffset;
                float thumbY = getHeight() / 2f + bounds.height() / 2f;
                canvas.drawText(progressText, thumbX, thumbY, mTextPaint);
            }
        }
    }



}
