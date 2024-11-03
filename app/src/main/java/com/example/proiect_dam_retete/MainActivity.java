package com.example.proiect_dam_retete;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ContentInfoCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            if (item.getItemId() == R.id.cazaceanu_octavian_nav_recipes) {
                reinitializeIngredientViews();
                Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.recipes_array),readRecipes);
                bundle.putParcelableArrayList(getString(R.string.selected_ingredients), inputedIngredients);
                intent.putExtra(getString(R.string.fetched_recipe_bundle),bundle);
                launcher.launch(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
            else if (item.getItemId() == R.id.cazaceanu_octavian_nav_most_viewed) {
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
            recipeButtonContainer.setVisibility(View.GONE);
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

                    //increment with 1 the views of the selected Recipe
                    for (Recipe recipe : readRecipes) {
                        if (recipe.equals(fetchedRecipe)) {
                            recipe.setNrViews(recipe.getNrViews() + 1);
                            break;
                        }
                    }

                    for(int i=0; i<fetchedRecipe.getIngredientList().size(); i++) {
                        addIngredientButton(fetchedRecipe.getIngredientList().get(i), false, getButtonColor(fetchedRecipe.getIngredientList().get(i)));
                    }

                    setRecipeButton(fetchedRecipe);
                    Log.i("mainActivityRecipe", fetchedRecipe.toString());
                    recipeButtonContainer.setVisibility(View.VISIBLE);

                } else if (String.valueOf(activityRouter).equals("addIngredientsFrom")) {
                    Bundle ingredientBundle = result.getData().getParcelableExtra("fetchedIngredientTag");
                    Ingredient fetchedIngredient = ingredientBundle.getParcelable("ingredient");
                    if(inputedIngredients
                        .stream()
                        .map(Ingredient::getIngredient_name)
                        .collect(Collectors.toList())
                        .contains(fetchedIngredient.getIngredient_name())){
                        inputedIngredients.stream().forEach(item -> {
                            if (item.getIngredient_name().equals(fetchedIngredient.getIngredient_name())) {
                                item.setQuantity(item.getQuantity() + fetchedIngredient.getQuantity());
                                fetchedIngredient.setQuantity(item.getQuantity());
                            }
                        });
                        Button button;
                        for (int i = 0; i <buttonContainer.getChildCount() ; i++) {
                            if((button = (Button) buttonContainer.getChildAt(i)).getTag().toString().equals("pure" + fetchedIngredient.getIngredient_name())){
                                button.setText(fetchedIngredient.getIngredient_name().toString() +
                                        '\n' +
                                        Float.valueOf(fetchedIngredient.getQuantity()).toString()
                                );
                            }
                        }
                        //if no work, put redraw after this
                    }
                    else{
                        inputedIngredients.add(fetchedIngredient);
                        addIngredientButton(fetchedIngredient, true, Color.WHITE);
                        Log.i("mainActivityIngredient", fetchedIngredient.toString());

                    }

                } else if (String.valueOf(activityRouter).equals("addRecipeFrom")) {
                    Bundle recipeBundle = result.getData().getParcelableExtra("fetchedRecipeTag");
                    Recipe fetchedRecipe = recipeBundle.getParcelable("recipe");
                    fetchedRecipe.setNrViews(fetchedRecipe.getNrViews() + 1);
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
        button.setBackground(getResources().getDrawable(R.drawable.main_activity_button_styles, getTheme()));
        params.setMargins(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));

        // recipe.getName() + "\n" +  Ingredient.sendIngredientsArrayToTextViewString(recipe.getIngredientList() + "\n" + recipe.getDescription()
        button.setText(
         recipe.getName() + "\n" +
         Ingredient.sendIngredientsArrayToTextViewString(recipe.getIngredientList() )+ "\n" +
         recipe.getDescription()
        );

        button.setLayoutParams(params);
        recipeButtonContainer.addView(button);
    }

    //butoanele pure sunt acele butoane care retin ingredientele utilizatorului
    //butoanele impure sunt acele butoane care apar in urma introducerii unei noi retete
    private void addIngredientButton(Ingredient fetchedIngredient, boolean isPure, int color) {
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
            button.setTag("pure" + fetchedIngredient.getIngredient_name());

        Float quantity = new Float(0);
        for (Ingredient ingredient: inputedIngredients
             ) {
            if(ingredient.getIngredient_name().equals(fetchedIngredient.getIngredient_name())){
                quantity = ingredient.getQuantity();
            }
        }

        button.setText(fetchedIngredient.getIngredient_name().toString() + '\n' + quantity.toString());
        button.setPadding(dpToPx(16), dpToPx(12), dpToPx(16), dpToPx(12)); // Increased padding
        button.setTextColor(Color.BLACK);
        button.setElevation(dpToPx(2));

        float radius = dpToPx(20);
        float[] radii = new float[] {
            radius, radius, // Top left corner
            radius, radius, // Top right corner
            radius, radius, // Bottom right corner
            radius, radius  // Bottom left corner
        };
         // Create a new background color
        RoundRectShape roundRectShape = new RoundRectShape(radii, null, null);

        // Create shape drawable
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        // Add stroke if needed
        shapeDrawable.getPaint().setStrokeWidth(dpToPx(1));
        shapeDrawable.getPaint().setColor(color);

        // Create new ripple drawable
        RippleDrawable rippleDrawable = new RippleDrawable(
            ColorStateList.valueOf(color), // Ripple color
            shapeDrawable,  // Content
            null  // Mask
        );

        button.setBackground(rippleDrawable);


        // Add button to container
        buttonContainer.addView(button);
        //add onClick to delete
        button.setOnLongClickListener(view -> {
            button.setTag("imp");
            button.setVisibility(View.GONE);
            inputedIngredients.remove(fetchedIngredient);
            return true;
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    void reinitializeIngredientViews() {
        for(int i=0; i<this.buttonContainer.getChildCount(); i++) {
            View currentView = this.buttonContainer.getChildAt(i);
            if(currentView.getTag() != null && currentView.getTag().toString().startsWith("pure")) {
                currentView.setVisibility(View.VISIBLE);
            }
            else {
                currentView.setVisibility(View.GONE);
            }
        }

        if(this.recipeButtonContainer.getChildAt(0) != null) {
            this.recipeButtonContainer.getChildAt(recipeButtonContainer.getChildCount()-1).setVisibility(View.GONE);
        }
    }
    public int getButtonColor(Ingredient fetchedIngredient){
        int color = Color.WHITE;
        if(fetchedIngredient == null){
            return color;
        }
        for (Ingredient ingredient: inputedIngredients) {
           if( fetchedIngredient.getQuantity()
                   <= ingredient.getQuantity()
                   && fetchedIngredient.getIngredient_name()
                   .equals(ingredient.getIngredient_name())){
                 return Color.GREEN;
               }
           else{
                color = Color.RED;
           }
        }
        return color;
    }

}