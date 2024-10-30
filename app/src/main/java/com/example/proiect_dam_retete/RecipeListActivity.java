package com.example.proiect_dam_retete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RecipeListActivity extends AppCompatActivity {
    private static final List<Integer> currentSelectedRecipe = new ArrayList<>();



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

        ArrayAdapter<Recipe> adapter = new ArrayAdapter<>(
                this,
                R.layout.recipe_item_layout,
                recipes
        ) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
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


                descriptionView.setVisibility(
                        currentSelectedRecipe.contains(position) ? View.VISIBLE : View.GONE
                );

                if (recipe != null) {
                    titleView.setText(recipe.getName());

                    StringBuilder recipeIngredients = new StringBuilder();
                    recipeIngredients.append("Ingredients: ");
                    for (Ingredient ingredient : recipe.getIngredientList()) {
                        recipeIngredients.append(ingredient.getIngredient_name().toString());
                    }
                    ingredientsView.setText(recipeIngredients.toString());
                    descriptionView.setText(recipe.getDescription());
                }

                return view;
            }
        };


        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView descriptionView = view.findViewById(R.id.recipe_activity_list_item_description);
            Recipe recipe = adapter.getItem(position);
            Integer pos = position;
            if (currentSelectedRecipe.contains(pos)) {
                descriptionView.setVisibility(View.GONE);
                descriptionView.setText(R.string.empty_string);
                currentSelectedRecipe.remove(Integer.valueOf(position));

            } else {
                descriptionView.setVisibility(View.VISIBLE);
                descriptionView.setText(Objects.requireNonNull(recipe).getDescription());
                currentSelectedRecipe.add(pos);

            }
            adapter.notifyDataSetChanged();
        });
//        if(!currentSelectedRecipe.isEmpty()){
//            //set all items in the static array to clicked
//           currentSelectedRecipe.forEach(clickedItemPosition ->{
//                listView.performItemClick(
//                        listView.getChildAt(clickedItemPosition),
//                        clickedItemPosition,
//                        adapter.getItemId(clickedItemPosition));
//           });
//
//        }
        listView.setOnItemLongClickListener((parent,view, position, id) ->{
            Recipe recipe = adapter.getItem(position);
            sendRecipeToActivity(recipe,this, MainActivity.class);
            return true;
        });
    }

    private void sendRecipeToActivity(Recipe recipe, Activity sourceActivity, Class<? extends Activity> destinationActivity){
        //TODO: Need more time to look at reloading activity state to decide between start and a Contract
//        ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result ->{}
//        );
        Intent intent = new Intent(sourceActivity, destinationActivity);
        Bundle sendRecipeToMain = new Bundle();
        //TODO:CODO refactor static string
        sendRecipeToMain.putParcelable("selected_recipe", recipe);
        intent.putExtra("bundle",sendRecipeToMain);
        startActivity(intent);
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
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ingredients.add(new Ingredient(random.nextFloat() * 5,
                    EIngredients.values()[new Random().nextInt(EIngredients.values().length)]));

            String name = sampleNames[random.nextInt(sampleNames.length)];
            String desc = sampleDescriptions[random.nextInt(sampleDescriptions.length)];
            Recipe recipe = new Recipe(name, ingredients, desc, random.nextInt(100), random.nextInt(6));

            recipes.add(recipe);
        }
        return recipes;
    }
}