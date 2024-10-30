package com.example.proiect_dam_retete;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecipeListActivity extends AppCompatActivity {
    private List<Integer> currentSelectedRecipe = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.recipe_list_activity_list_view);
        ArrayList<Recipe> recipes = generateRandomRecipes(10);
        for ( Recipe recipe: recipes) {
            Log.d(getString(R.string.default_logging_string),recipe.toString());
        }

        ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(
                this,
                R.layout.recipe_item_layout,
                recipes
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    view = LayoutInflater.from(getContext())
                            .inflate(R.layout.recipe_item_layout, parent, false);
                }


                Recipe recipe = getItem(position);

                TextView titleView = view.findViewById(R.id.recipe_activity_list_item_title);
                TextView ingredientsView = view.findViewById(R.id.recipe_activity_list_item_ingredients);
                TextView descriptionView = view.findViewById(R.id.recipe_activity_list_item_description);
                descriptionView.setVisibility(View.GONE);
                if (recipe != null) {
                    titleView.setText(recipe.getNume());

                    StringBuilder recipeIngredients = new StringBuilder();
                    recipeIngredients.append("Ingredients: ");
                    for (Ingredient ingredient : recipe.getIngredientList()) {
                        recipeIngredients.append(ingredient.getIngredient_name().toString());
                    }
                    ingredientsView.setText(recipeIngredients.toString());
                }

                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView descriptionView = view.findViewById(R.id.recipe_activity_list_item_description);
            Recipe recipe = adapter.getItem(position);
            String currentFooter = descriptionView.getText().toString();
            Integer pos = position;
            if (currentSelectedRecipe.contains(pos)) {
                descriptionView.setVisibility(View.GONE);
                descriptionView.setText(R.string.empty_string);
                currentSelectedRecipe.remove(pos);

            } else {
                descriptionView.setVisibility(View.VISIBLE);
                descriptionView.setText(recipe.getDescriere());
                currentSelectedRecipe.add(pos);

            }
        });
    }
    //TODO: Make the generator a part of the Recipe class
    //TODO: Delete generateRandomRecipes after coupling with other activities
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
            Recipe recipe = new Recipe(name, ingredients, desc, random.nextInt(100));

            recipes.add(recipe);
        }
        return recipes;
    }
}
