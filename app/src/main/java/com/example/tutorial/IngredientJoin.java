package com.example.tutorial;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
        tableName="recipe_ingredient_join",
        primaryKeys={"recipeId", "ingredientId"},
        foreignKeys={
                @ForeignKey(
                        entity=IngredientEntity.class,
                        parentColumns="uid",
                        childColumns="ingredientId",
                        onDelete=CASCADE),
                @ForeignKey(
                        entity=RecipeEntity.class,
                        parentColumns="uid",
                        childColumns="recipeId",
                        onDelete=CASCADE)},
        indices={
                @Index(value="ingredientId"),
                @Index(value="recipeId"),
                @Index(value="positionInRecipe")
        }
)
public class IngredientJoin implements Comparable<IngredientJoin>, Parcelable {
    @NonNull
    private final long recipeId;
    @NonNull
    private final long ingredientId;

    private int numberOfFills = 1;
    private int positionInRecipe = 0;


    public IngredientJoin(long recipeId, long ingredientId) {
        this.recipeId=recipeId;
        this.ingredientId=ingredientId;
    }

    protected IngredientJoin(Parcel in) {
        recipeId = in.readLong();
        ingredientId = in.readLong();
        numberOfFills = in.readInt();
        positionInRecipe = in.readInt();
    }


    @NonNull
    public long getRecipeId() {
        return recipeId;
    }

    @NonNull
    public long getIngredientId() {
        return ingredientId;
    }

    public int getNumberOfFills() {
        return numberOfFills;
    }

    public void setNumberOfFills(int numberOfFills) {
        this.numberOfFills = numberOfFills;
    }

    public void switchPositionInRecipeWith(IngredientJoin join) {
        if (join.getIngredientId() != ingredientId && join.getRecipeId() == recipeId && join.getPositionInRecipe() != positionInRecipe) {
            int pos = join.getPositionInRecipe();
            join.setPositionInRecipe(positionInRecipe);
            positionInRecipe = pos;
        }
    }

    @Override
    public int compareTo(@NonNull IngredientJoin o) {
        return Integer.compare(this.positionInRecipe, o.getPositionInRecipe());
    }

    public int getPositionInRecipe() {
        return positionInRecipe;
    }

    public void setPositionInRecipe(int positionInRecipe) {
        this.positionInRecipe = positionInRecipe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(recipeId);
        dest.writeLong(ingredientId);
        dest.writeInt(numberOfFills);
        dest.writeInt(positionInRecipe);
    }

    public static final Creator<IngredientJoin> CREATOR = new Creator<IngredientJoin>() {
        @Override
        public IngredientJoin createFromParcel(Parcel in) {
            return new IngredientJoin(in);
        }

        @Override
        public IngredientJoin[] newArray(int size) {
            return new IngredientJoin[size];
        }
    };

}
