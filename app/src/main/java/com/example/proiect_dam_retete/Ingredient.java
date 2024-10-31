package com.example.proiect_dam_retete;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Ingredient implements Serializable {
    private float quantity;
    private EIngredients ingredient_name;

    public Ingredient(float quantity, EIngredients ingredient_name) {
        this.quantity = quantity;
        this.ingredient_name = ingredient_name;
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
}