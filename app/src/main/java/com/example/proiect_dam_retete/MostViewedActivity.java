package com.example.proiect_dam_retete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MostViewedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_most_viewed);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        ListView listView = findViewById(R.id.most_viewed_activity_list_view);
        ArrayList<Recipe> recipes = generateRandomRecipes(10);

        ArrayList<Recipe> sortedRecipesList = sortRecipesByViews(recipes);

        listView.setAdapter(new ArrayAdapter<Recipe>(this, 0, sortedRecipesList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Recipe recipe = getItem(position);

                // Inflate the item view if it’s not already created
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_item_layout, parent, false);
                }

                // Find TextViews and set recipe details
                TextView nameView = convertView.findViewById(R.id.recipe_activity_list_item_title);
                TextView descView = convertView.findViewById(R.id.recipe_activity_list_item_description);

                if (recipe != null) {
                    nameView.setText(recipe.getNume());
                    descView.setText(recipe.getDescriere() + "\nViews: " + recipe.getNrViews());
                }

                return convertView;
            }
        });
    }

    // Sort recipes in descending order of views
    private ArrayList<Recipe> sortRecipesByViews(ArrayList<Recipe> recipes) {
        ArrayList<Recipe> sortedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getNrViews() > 0) {
                sortedRecipes.add(recipe);
            }
        }

        Collections.sort(sortedRecipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                return Integer.compare(r2.getNrViews(), r1.getNrViews()); // Descending order
            }
        });

        return sortedRecipes;
    }

    private ArrayList<Recipe> generateRandomRecipes(int count) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        Random random = new Random();

        String[] sampleNames = {"Pasta", "Curry", "Salad", "Soup", "Stew"};
        String[] sampleDescriptions = {
                "Quick dinner recipe",
                "Healthy lunch option",
                "Easy to make meal"
        };

        for(int i = 0; i < count; i++) {
            // Create random ingredients
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ingredients.add(new Ingredient(random.nextFloat() * 5, EIngredients.TOMATO));

            // Create recipe with random name and description
            String name = sampleNames[random.nextInt(sampleNames.length)];
            String desc = sampleDescriptions[random.nextInt(sampleDescriptions.length)];
            int views = random.nextInt(100);
            Recipe recipe = new Recipe(name, ingredients, desc, views);

            recipes.add(recipe);
        }
        return recipes;
    }
}