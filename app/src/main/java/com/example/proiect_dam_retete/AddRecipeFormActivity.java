package com.example.proiect_dam_retete;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeFormActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "RECIPE_KEY";
    private TextInputEditText add_recipe_name;
    private TextInputEditText add_recipe_description;
    private Button add_recipe_submit_btn;
    private Button add_recipe_add_ingredient_btn;
    private TextView ingredients_list_text_view;
    private static ArrayList<Ingredient> ingredients_list=new ArrayList<>();
    private ActivityResultLauncher<Intent> launcher;

    private Intent submitIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe_form);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        submitIntent = getIntent();

        initialization();
        launcher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                getAddIngredientCallback());
    }

    private ActivityResultCallback<ActivityResult> getAddIngredientCallback()
    {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK && result.getData()!=null) {
                    Bundle ingredientBundle = result.getData().getParcelableExtra("fetchedIngredientTag");
                    Ingredient fetchedIngredient = ingredientBundle.getParcelable("ingredient");

                    ingredients_list.add(fetchedIngredient);
                    StringBuilder ingredients=new StringBuilder();
                    for(Ingredient i : ingredients_list)
                    {
                        ingredients.append(i.getIngredient_name());
                        ingredients.append(" - ");
                        ingredients.append(i.getQuantity());
                        ingredients.append("\n");
                    }
                    ingredients_list_text_view.setText(ingredients.toString());
                }
            }
        };
    }

    private void initialization()
    {
        add_recipe_name=findViewById(R.id.dulica_georgiana_denisa_recipe_name);
        add_recipe_description=findViewById(R.id.dulica_georgiana_denisa_description);
        add_recipe_submit_btn=findViewById(R.id.dulica_georgiana_denisa_submit);
        add_recipe_add_ingredient_btn=findViewById(R.id.dulica_georgiana_denisa_add_ingredient);
        ingredients_list_text_view=findViewById(R.id.dulica_georgiana_denisa_ingredients_list);

        add_recipe_add_ingredient_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), AddIngredientsForm.class);
                launcher.launch(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        add_recipe_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    String recipe_name=add_recipe_name.getText().toString();
                    String recipe_description=add_recipe_description.getText().toString();
                    Recipe recipe=new Recipe(recipe_name, ingredients_list, recipe_description);

                    Bundle sendBundle = new Bundle();
                    sendBundle.putParcelable("recipe", recipe);
                    submitIntent.putExtra("fetchedRecipeTag", sendBundle);
                    submitIntent.putExtra("activityOrigin", "addRecipeFrom");
                    setResult(RESULT_OK,submitIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            }
        });
    }

    private boolean isValid()
    {
        if(add_recipe_name.getText()==null || add_recipe_name.getText().toString().trim().length()<4)
        {
            Toast.makeText(this, R.string.add_recipe_form_enter_a_valid_recipe_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(ingredients_list.isEmpty()){
            Toast.makeText(this, R.string.add_recipe_form_choose_at_least_one_ingredient,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
