package com.example.proiect_dam_retete;

import java.util.ArrayList;

public class Recipe {
    private String nume;
    private ArrayList<Ingredient> ingredientList;
    private String descriere;
    private int nrViews;

    public Recipe(String nume, ArrayList<Ingredient> ingredientList, String descriere, int nrViews) {
        this.nume = nume;
        this.ingredientList = ingredientList;
        this.descriere = descriere;
        this.nrViews = nrViews;
    }

    public Recipe(String nume, ArrayList<Ingredient> ingredientList, String descriere) {
        this.nume = nume;
        this.ingredientList = ingredientList;
        this.descriere = descriere;
        this.nrViews = 0;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public int getNrViews() {
        return nrViews;
    }

    public void setNrViews(int nrViews) {
        this.nrViews = nrViews;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "nume='" + nume + '\'' +
                ", ingredientList=" + ingredientList +
                ", descriere='" + descriere + '\'' +
                ", nrViews=" + nrViews +
                '}';
    }

}