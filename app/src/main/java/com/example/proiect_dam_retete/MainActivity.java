package com.example.proiect_dam_retete;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<Recipe> inputedRecipes;

    private NavigationView mainNav;
    private Toolbar mainToolbar;
    private DrawerLayout mainDrawerLayout;
    private ActivityResultLauncher<Intent> launcher;
    private LinearLayout recipeButtonContainer;
    private LinearLayout buttonContainer;
    private TextView noIngredientTextView;
    private ImageView noIngredientImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize the launcher
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                getCallback()
        );

        // Initialize views
        this.noIngredientTextView = findViewById(R.id.cazaceanu_octavian_tv_no_ingredients);
        this.noIngredientImageView = findViewById(R.id.cazaceanu_octavian_no_food_icon);
        this.recipeButtonContainer = findViewById(R.id.cazaceanu_octavian_buttonContainer_recipe);
        this.buttonContainer = findViewById(R.id.cazaceanu_octavian_buttonContainer);
        this.mainNav = findViewById(R.id.cazaceanu_octavian_main_navview);
        this.mainToolbar = findViewById(R.id.cazaceanu_octavian_main_toolbar);
        this.mainDrawerLayout = findViewById(R.id.cazaceanu_octavian_main_drawer_layout);
        Button navigateButton = findViewById(R.id.main_activity_launch_recipe_list_btn);

        // Setup toolbar
        setSupportActionBar(mainToolbar);
        ActionBarDrawerToggle toggleObject = new ActionBarDrawerToggle(
                this, mainDrawerLayout, mainToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        toggleObject.syncState();

        // Setup navigation listener
        mainNav.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_recipes) {
                Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
                launcher.launch(intent);
            }
            else if (item.getItemId() == R.id.nav_most_viewed) {
                Intent intent = new Intent(MainActivity.this, MostViewedActivity.class);
                launcher.launch(intent);
            }
            return true;
        });

        // Setup navigate button listener
        navigateButton.setOnClickListener(v -> {
            Intent getIngredientIntent = new Intent(getApplicationContext(), AddIngredientsForm.class);
            launcher.launch(getIngredientIntent);
        });
    }

    private ActivityResultCallback<ActivityResult> getCallback() {
        return result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                //hide no food stuff
                noIngredientTextView.setVisibility(View.GONE);
                noIngredientImageView.setVisibility(View.GONE);

                String activityRouter = result.getData().getStringExtra("activityOrigin");
                if(String.valueOf(activityRouter).equals("RecipeListActivity")) {
                    Bundle fetchedRecipeBundle = result.getData().getParcelableExtra("fetchedRecipeBundle");
                    Recipe fetchedRecipe = fetchedRecipeBundle.getParcelable("fetchedRecipe");
                    setRecipeButton(fetchedRecipe);
                    Log.i("mainActivityRecipe", fetchedRecipe.toString());
                } else if (String.valueOf(activityRouter).equals("addIngredientsFrom")) {
                    Ingredient fetchedIngredient = result.getData().getParcelableExtra("fetchedIngredientTag");
                    addIngredientButton(fetchedIngredient);
                    Log.i("mainActivityIngredient", fetchedIngredient.toString());
                }
            }
        };
    }

    private void setRecipeButton(Recipe recipe) {
        Button button = new Button(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  // Width matches parent
                LinearLayout.LayoutParams.MATCH_PARENT   // Height matches parent
        );

        params.setMargins(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
        button.setText(recipe.toString());
        button.setLayoutParams(params);

        recipeButtonContainer.addView(button);
    }

    private void addIngredientButton(Ingredient fetchedIngredient) {
        Button button = new Button(this);

        // Set button properties
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(100)
        );
        params.setMargins(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8)); // Updated margins
        button.setLayoutParams(params);

        //nu ma judecati pentru acel \n va rog
        button.setText(fetchedIngredient.getIngredient_name().toString() + '\n' + String.valueOf(fetchedIngredient.getQuantity()));
        button.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12)); // Increased padding
        button.setBackground(getResources().getDrawable(R.drawable.main_activity_button_styles, getTheme()));
        button.setTextColor(Color.BLACK);
        button.setElevation(dpToPx(2));

        // Add button to container
        buttonContainer.addView(button);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}