package com.example.proiect_dam_retete;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.core.splashscreen.SplashScreen;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<Ingredient> inputedIngredients = new ArrayList<Ingredient>();

    private ArrayList<Recipe> readRecipes = new ArrayList<Recipe>();
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
        final boolean[] isLoading = {true};
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() ->{
            InputStream inputStream = getResources().openRawResource(R.raw.input);
            readRecipes = new ArrayList<Recipe>(Recipe.readParceledTxtFile(inputStream));
            return isLoading[0];
        });
         new Handler().postDelayed(() -> {
             isLoading[0] =false;
        }, 1500);
        setContentView(R.layout.activity_main);
        // Initialize the launcher
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                getCallback()
        );

        initializeViews();
        Button navigateButton = findViewById(R.id.main_activity_launch_recipe_list_btn);
        Button addRecipeButton = findViewById(R.id.cazaceanu_octavian_add_recipe_button);

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
                reinitializeIngredientViews();
                Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.recipes_array),readRecipes);
                intent.putExtra(getString(R.string.fetched_recipe_bundle),bundle);
                launcher.launch(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
            else if (item.getItemId() == R.id.nav_most_viewed) {
                reinitializeIngredientViews();
                Intent intent = new Intent(MainActivity.this, MostViewedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.recipes_array),readRecipes);
                intent.putExtra(getString(R.string.fetched_recipe_bundle),bundle);
                launcher.launch(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
            return true;
        });

        // Setup navigate button listener
        navigateButton.setOnClickListener(v -> {
            reinitializeIngredientViews();
            Intent getIngredientIntent = new Intent(getApplicationContext(), AddIngredientsForm.class);
            launcher.launch(getIngredientIntent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        });

        addRecipeButton.setOnClickListener(v -> {
            reinitializeIngredientViews();
            Intent getIngredientIntent = new Intent(getApplicationContext(), AddRecipeFormActivity.class);
            launcher.launch(getIngredientIntent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });
    }
    void initializeViews(){
        // Initialize views
        this.noIngredientTextView = findViewById(R.id.cazaceanu_octavian_tv_no_ingredients);
        this.noIngredientImageView = findViewById(R.id.cazaceanu_octavian_no_food_icon);
        this.recipeButtonContainer = findViewById(R.id.cazaceanu_octavian_buttonContainer_recipe);
        this.buttonContainer = findViewById(R.id.cazaceanu_octavian_buttonContainer);
        this.mainNav = findViewById(R.id.cazaceanu_octavian_main_navview);
        this.mainToolbar = findViewById(R.id.cazaceanu_octavian_main_toolbar);
        this.mainDrawerLayout = findViewById(R.id.cazaceanu_octavian_main_drawer_layout);

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

                    //hide the current ingredients and replace them with the needed ingredients for the selected recipe
                    for(int i=0; i<buttonContainer.getChildCount(); i++) {
                        View child = buttonContainer.getChildAt(i);
                        child.setVisibility(View.GONE);
                    }

                    for(int i=0; i<fetchedRecipe.getIngredientList().size(); i++) {
                        addIngredientButton(fetchedRecipe.getIngredientList().get(i), false);
                    }

                    setRecipeButton(fetchedRecipe);
                    Log.i("mainActivityRecipe", fetchedRecipe.toString());

                } else if (String.valueOf(activityRouter).equals("addIngredientsFrom")) {
                    Bundle ingredientBundle = result.getData().getParcelableExtra("fetchedIngredientTag");
                    Ingredient fetchedIngredient = ingredientBundle.getParcelable("ingredient");

                    inputedIngredients.add(fetchedIngredient);
                    addIngredientButton(fetchedIngredient, true);
                    Log.i("mainActivityIngredient", fetchedIngredient.toString());

                } else if (String.valueOf(activityRouter).equals("addRecipeFrom")) {
                    Bundle recipeBundle = result.getData().getParcelableExtra("fetchedRecipeTag");
                    Recipe fetchedRecipe = recipeBundle.getParcelable("recipe");
                    readRecipes.add(fetchedRecipe);

                    setRecipeButton(fetchedRecipe);
                    Log.i("mainActivityRecipe", fetchedRecipe.toString());
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

    //butoanele pure sunt acele butoane care retin ingredientele utilizatorului
    //butoanele impure sunt acele butoane care apar in urma introducerii unei noi retete
    private void addIngredientButton(Ingredient fetchedIngredient, boolean isPure) {
        Button button = new Button(this);

        // Set button properties
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(100)
        );
        params.setMargins(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8)); // Updated margins
        button.setLayoutParams(params);

        //tag-urile pure nu isi iau delete
        if (isPure)
            button.setTag("pure");
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

    void reinitializeIngredientViews() {
        for(int i=0; i<this.buttonContainer.getChildCount(); i++) {
            View currentView = this.buttonContainer.getChildAt(i);
            if(currentView.getTag() != null && currentView.getTag().toString().equals("pure")) {
                currentView.setVisibility(View.VISIBLE);
            }
            else {
                currentView.setVisibility(View.GONE);
            }
        }

        if(this.recipeButtonContainer.getChildAt(0) != null) {
            this.recipeButtonContainer.getChildAt(0).setVisibility(View.GONE);
        }
    }

}