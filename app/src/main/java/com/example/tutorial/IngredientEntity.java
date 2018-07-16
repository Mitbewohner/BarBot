package com.example.tutorial;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class IngredientEntity implements Comparable<IngredientEntity> {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "coordinate")
    private int coordinate;

    @ColumnInfo(name = "hold")
    private int hold;

    @ColumnInfo(name = "wait")
    private int wait;

    @ColumnInfo(name = "isActive")
    private boolean isActive;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(int coordinate) {
        this.coordinate = coordinate;
    }

    public int getHold() {
        return hold;
    }

    public void setHold(int hold) {
        this.hold = hold;
    }

    public int getWait() {
        return wait;
    }

    public void setWait(int wait) {
        this.wait = wait;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int compareTo(@NonNull IngredientEntity o) {
        return this.name.compareToIgnoreCase(o.getName());
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return (int)this.uid * 7;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != IngredientEntity.class) {
            return false;
        }
        IngredientEntity ingr = (IngredientEntity) o;
        if (ingr.getUid() == this.uid) {
            return true;
        }
        return false;
    }
}
