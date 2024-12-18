package com.example.proiect_dam_retete;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Ingredient implements Parcelable {
    private float quantity;
    private EIngredients ingredient_name;

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public Ingredient(float quantity, EIngredients ingredient_name) {
        this.quantity = quantity;
        this.ingredient_name = ingredient_name;
    }
    protected Ingredient(Parcel in){
        quantity = in.readFloat();
        ingredient_name = EIngredients.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(ingredient_name.toString());
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public EIngredients getIngredient_name() {
        return ingredient_name;
    }

    public void setIngredient_name(EIngredients ingredient_name) {
        this.ingredient_name = ingredient_name;
    }

    public String displayIngredient() {
        return ingredient_name + " " + quantity + "g, ";
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", ingredient_name=" + ingredient_name +
                '}';
    }
    public String toParcelFormat(){
        return "Ingredient: " + quantity + "," + ingredient_name;
    }
    public void writeAsParcelToTxt(BufferedWriter writer) throws IOException {
        writer.append(this.toParcelFormat());
    }
    public static Ingredient createIngredient(String[] input){
        float quantity =Float.parseFloat(input[0]);
        String ingredient_name = input[1];
        return new Ingredient(quantity,EIngredients.valueOf(ingredient_name));
    }
    public static String sendIngredientsArrayToTextViewString(List<Ingredient> ingredients){
        StringBuilder recipeIngredients = new StringBuilder();
        recipeIngredients.append("Ingredients: " + "\n");
        for (Ingredient ingredient :ingredients) {
            recipeIngredients.append(" - " + ingredient.getIngredient_name().toString().replace("_", " ") + ": " + ingredient.getQuantity() +"\n");
        }
        return recipeIngredients.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Float.compare(quantity, that.quantity) == 0 && ingredient_name == that.ingredient_name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, ingredient_name);
    }
}

