package com.example.proiect_dam_retete;

import androidx.annotation.NonNull;

public class Ingredient {
    private float quantity;
    private EIngredients ingredient_name;

    public Ingredient(float quantity, EIngredients ingredient_name) {
        this.quantity = quantity;
        this.ingredient_name = ingredient_name;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
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
}