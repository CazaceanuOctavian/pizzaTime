package com.example.proiect_dam_retete;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class RecipeListActivity extends AppCompatActivity {
    private static final List<Integer> currentSelectedRecipe = new ArrayList<>();
    private List<Ingredient> pickedIngredients = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setBackPressAnimation(true, R.anim.slide_in_left, R.anim.slide_out_right);

        setContentView(R.layout.activity_recipe_list);

        //changes by Oct here
        Intent intent_submit=getIntent();
        Bundle ingredientsBundle = intent_submit.getBundleExtra(getString(R.string.fetched_recipe_bundle));
        pickedIngredients = ingredientsBundle.getParcelableArrayList(getString(R.string.selected_ingredients));

        ListView listView = findViewById(R.id.codorean_andrei_recipe_list_activity_list_view);
        TextView tvStatus = findViewById(R.id.codorean_andrei_recipe_activity_list_status);
        ArrayList<Recipe> recipesLoaded = getIntent().getBundleExtra(
                getString(R.string.fetched_recipe_bundle))
                .getParcelableArrayList(getString(R.string.recipes_array));

         ArrayList<Recipe> recipes = new ArrayList<Recipe>();
         //you made me do this, it was such an easy DB QUERRRYYYYYYYY
        for (Recipe recipe: recipesLoaded
             ) {
            boolean ingredientsContained = false;
            for (Ingredient ingredientPicked:pickedIngredients
                 ) {
                if(recipe.getIngredientList()
                        .stream()
                        .map(Ingredient::getIngredient_name)
                        .collect(Collectors.toList())
                        .contains(ingredientPicked.getIngredient_name())
                       ){
                    ingredientsContained = true;
                }
//                for(Ingredient ingredient:recipe.getIngredientList() ){
//                    if(ingredient.getIngredient_name().equals(ingredientPicked.getIngredient_name())){
//                        isInThisRecipe = true;
//                    }
//                }
            }
            if(ingredientsContained){
                recipes.add(recipe);
            }
            ingredientsContained =false ;
        }
        if(recipes.size() == 0){
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tvStatus.getLayoutParams();
            params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            params.topMargin = dpToPx(0);
            tvStatus.setLayoutParams(params);
        }
        else {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) tvStatus.getLayoutParams();
            params.bottomToBottom = ConstraintLayout.LayoutParams.UNSET;
            params.topMargin = dpToPx(30);
            tvStatus.setLayoutParams(params);
            tvStatus.setText(R.string.recipes_found);
        }

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

                TextView titleView = view.findViewById(R.id.codorean_andrei_recipe_activity_list_item_title);
                TextView ingredientsView = view.findViewById(R.id.codorean_andrei_recipe_activity_list_item_ingredients);
                TextView descriptionView = view.findViewById(R.id.codorean_andrei_recipe_activity_list_item_description);
                ingredientsView.setVisibility(View.GONE);

                ingredientsView.setVisibility(
                        currentSelectedRecipe.contains(position) ? View.VISIBLE : View.GONE
                );

                if (recipe != null) {
                    titleView.setText(recipe.getName());
                    ingredientsView.setText(Ingredient.sendIngredientsArrayToTextViewString(recipe.getIngredientList()));
                    descriptionView.setText(recipe.getDescription());
                    // recipe.getName() + "\n" +  Ingredient.sendIngredientsArrayToTextViewString(recipe.getIngredientList() + "\n" + recipe.getDescription()
                }

                return view;
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView ingredientsView = view.findViewById(R.id.codorean_andrei_recipe_activity_list_item_ingredients);
            Recipe recipe = adapter.getItem(position);
            Integer pos = position;
            if (currentSelectedRecipe.contains(pos)) {
                ingredientsView.setVisibility(View.GONE);
                currentSelectedRecipe.remove(Integer.valueOf(position));

            } else {
                ingredientsView.setVisibility(View.VISIBLE);
                currentSelectedRecipe.add(pos);
            }
            adapter.notifyDataSetChanged();
        });
        listView.setOnItemLongClickListener((parent,view, position, id) ->{
            Recipe recipe = adapter.getItem(position);
            sendRecipeToActivity(recipe,this, MainActivity.class, intent_submit);
            return true;
        });
         ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        Bundle bundle = data.getBundleExtra("new_recipe");
                    }
                }
        );

        Button addNewRecipeBtn = findViewById(R.id.codorean_andrei_recipe_list_activity_add_recipe_btn);
        addNewRecipeBtn.setOnClickListener( v -> {

            Intent intent = new Intent(this.getBaseContext(), AddRecipeFormActivity.class);
            launcher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
    }

    private void setBackPressAnimation(boolean enabled, int newActivityEntryAnimation, int oldActivityExitAnimation) {
        OnBackPressedDispatcher backPressed = getOnBackPressedDispatcher();
        OnBackPressedCallback callback = new OnBackPressedCallback(enabled) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(newActivityEntryAnimation,oldActivityExitAnimation);
            }
        };
        backPressed.addCallback(this, callback);
    }

    @NonNull
    @Override
    public OnBackInvokedDispatcher getOnBackInvokedDispatcher() {
        return super.getOnBackInvokedDispatcher();
    }

    //CHANGES BY OCT HERE
    private void sendRecipeToActivity(Recipe recipe, Activity sourceActivity, Class<? extends Activity> destinationActivity, Intent intent){

        Bundle sendRecipeToMain = new Bundle();
        sendRecipeToMain.putParcelable(getString(R.string.fetched_recipe), recipe);



        intent.putExtra(getString(R.string.fetched_recipe_bundle),sendRecipeToMain);
        intent.putExtra(getString(R.string.activity_origin), getString(R.string.recipe_list_activity));
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                ingredients.add(new Ingredient(random.nextFloat() * 5,
                        EIngredients.values()[new Random().nextInt(EIngredients.values().length)]));
            }

            String name = sampleNames[random.nextInt(sampleNames.length)];
            String desc = sampleDescriptions[random.nextInt(sampleDescriptions.length)];
            Recipe recipe = new Recipe(name, ingredients, desc, random.nextInt(100), random.nextInt(6));

            recipes.add(recipe);
        }
        return recipes;
    }
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}

