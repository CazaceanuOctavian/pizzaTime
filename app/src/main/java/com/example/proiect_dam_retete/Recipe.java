package com.example.proiect_dam_retete;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.function.IntFunction;

public class Recipe implements Parcelable {
    private String name;
    private ArrayList<Ingredient> ingredientList;
    private String description;
    private int nrViews;
    private int rating;

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    public Recipe(String name, ArrayList<Ingredient> ingredientList, String description, int nrViews, int rating) {
        this.name = name;
        this.ingredientList = ingredientList;
        this.description = description;
        this.nrViews = nrViews;
        this.rating = rating;
    }

    public Recipe(String name, ArrayList<Ingredient> ingredientList, String description, int nrViews) {
        this.name = name;
        this.ingredientList = ingredientList;
        this.description = description;
        this.nrViews = nrViews;
        this.rating = 0;
    }

    public Recipe(String name, ArrayList<Ingredient> ingredientList, String description) {
        this.name = name;
        this.ingredientList = ingredientList;
        this.description = description;
        this.nrViews = 0;
        this.rating = 0;
    }

    protected Recipe(Parcel in) {
        name = in.readString();
        ingredientList = new ArrayList<>();
        in.readTypedList(ingredientList, Ingredient.CREATOR);
        description = in.readString();
        nrViews = in.readInt();
        rating = in.readInt();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNrViews() {
        return nrViews;
    }

    public void setNrViews(int nrViews) {
        this.nrViews = nrViews;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(ingredientList);
        dest.writeString(description);
        dest.writeInt(nrViews);
        dest.writeInt(rating);
    }
    @Override
    public String toString() {
        return "Recipe{" +
                "nume='" + name + '\'' +
                ", ingredientList=" + ingredientList +
                ", descriere='" + description + '\'' +
                ", nrViews=" + nrViews +
                '}';
    }
}
