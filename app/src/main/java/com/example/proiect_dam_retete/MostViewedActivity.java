package com.example.proiect_dam_retete;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MostViewedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_most_viewed);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        setBackPressAnimation(true, R.anim.slide_in_right, R.anim.slide_out_left);
        Switch sortSwitch = findViewById(R.id.cobzarencu_robert_most_viewed_activity_sort_switch);
        ListView listView = findViewById(R.id.cobzarencu_robert_most_viewed_activity_list_view);

        ArrayList<Recipe> recipes = getIntent().getBundleExtra(
                getString(R.string.fetched_recipe_bundle))
                .getParcelableArrayList(getString(R.string.recipes_array));

        ArrayList<Recipe> sortedRecipesList = sortRecipesByViews(recipes);

        ArrayAdapter<Recipe> adapter = new ArrayAdapter<Recipe>(
                this,
                 R.layout.most_viewed_recipes_items,
                 sortedRecipesList

        ){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.most_viewed_recipes_items, parent, false);
                }

                Recipe recipe = getItem(position);

                TextView nameView = view.findViewById(R.id.cobzarencu_robert_most_viewed_activity_list_item_title);
                TextView descView = view.findViewById(R.id.cobzarencu_robert_most_viewed_activity_list_item_description);
                TextView nrView = view.findViewById(R.id.cobzarencu_robert_most_viewed_activity_list_item_ingredients);
                TextView ratingView = view.findViewById(R.id.cobzarencu_robert_most_viewed_activity_list_item_rating);

                if (recipe != null) {
                    nameView.setText(recipe.getName());
                    descView.setText(recipe.getDescription());
                    ratingView.setText(getString(R.string.recipe_size) + recipe.getIngredientList().size());
                    nrView.setText(getString(R.string.views_comma) + recipe.getNrViews());
                }

                return view;
            }
        };

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        sortSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                adapter.clear();
                adapter.addAll(sortRecipesByRating(recipes));
            } else {
                adapter.clear();
                adapter.addAll(sortRecipesByViews(recipes));
            }
            adapter.notifyDataSetChanged();
        });
    }

    // Sort recipes in descending order of views
    private ArrayList<Recipe> sortRecipesByViews(ArrayList<Recipe> recipes) {
        ArrayList<Recipe> sortedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getNrViews() >= 0) {
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

    private ArrayList<Recipe> sortRecipesByRating(ArrayList<Recipe> recipes) {
        ArrayList<Recipe> sortedRecipes = new ArrayList<>();

        for (Recipe recipe : recipes) {
            if (recipe.getRating() >= 0) {
                sortedRecipes.add(recipe);
            }
        }

        Collections.sort(sortedRecipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                return Integer.compare(r2.getIngredientList().size(), r1.getIngredientList().size()); // Descending order
            }
        });

        return sortedRecipes;
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
}