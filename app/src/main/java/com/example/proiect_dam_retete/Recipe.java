package com.example.proiect_dam_retete;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                "name='" + name + '\'' +
                ", ingredientList=" + ingredientList +
                ", description='" + description + '\'' +
                ", nrViews=" + nrViews +
                ", rating=" + rating +
                '}';
    }
    public String toParcelFormat(){
        return "Recipe: " + name +
                "," + description+
                "," + nrViews+
                ","+ rating;
    }
    public void writeParcelToTxt(BufferedWriter writer) throws IOException {
        writer.append(this.toParcelFormat());
        for (Ingredient ingredient:this.ingredientList) {
            writer.newLine();
            writer.append(ingredient.toParcelFormat());
        }
        writer.newLine();
        writer.flush();
    }
    public static List<Recipe> readParceledTxtFile(InputStream rawResourceInputStream){
        ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        ArrayList<Ingredient> ingredients = null;
        int rating = 0;
        int nrViews = 0;
        String description= null;
        String name = null;
        try (BufferedReader bufferedReader =
                     new BufferedReader(
                             new InputStreamReader(rawResourceInputStream)
                     )
        ){
            String line;
            while ((line = bufferedReader.readLine())!= null){
                if (line.trim().isEmpty()) {
                    continue;
                }
                line = line.trim();
                String[] labelVarsSplit = line.split(":");
                String[] lineValues = labelVarsSplit[1].split(",");
                Arrays.stream(lineValues).forEach(val -> val.trim());

                if(labelVarsSplit[0].equals("Ingredient") ){
                    ingredients.add(Ingredient.createIngredient(lineValues));
                    
                } else if (labelVarsSplit[0].equals("Recipe") ) {
                    if(ingredients != null && name != null){
                        recipes.add(new Recipe(name,
                                         ingredients,
                                         description,
                                         nrViews,
                                         rating
                                         ));
                    }
                    ingredients = new ArrayList<>();
                    name = lineValues[0];
                    description = lineValues[1];
                    nrViews = Integer.parseInt(lineValues[2]);
                    rating = Integer.parseInt(lineValues[3]);
                }

            }
            if(ingredients != null && name != null){
                    recipes.add(new Recipe(name,
                                         ingredients,
                                         description,
                                         nrViews,
                                         rating
                                         ));
            }
        }catch (Exception e){
            return null;
        }
        return recipes;
    }
}
