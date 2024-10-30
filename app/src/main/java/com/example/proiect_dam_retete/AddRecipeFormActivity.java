package com.example.proiect_dam_retete;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeFormActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "RECIPE_KEY";
    private TextInputEditText add_recipe_name;
    private TextInputEditText add_recipe_description;
    private Button add_recipe_submit_btn;
    private Button add_recipe_add_ingredient_btn;
    private TextView ingredients_list_text_view;
    private ArrayList<Ingredient> ingredients_list=new ArrayList<>();
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialization();
        launcher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),getAddIngredientCallback());

    }

    private ActivityResultCallback<ActivityResult> getAddIngredientCallback()
    {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK && result.getData()!=null) {
//                    Ingredient ingredient =(Ingredient)result.getData().getSerializableExtra(AddIngredientsForm.INGREDIENT_KEY);
//                    ingredients_list.add(ingredient);
                    StringBuilder ingredients=new StringBuilder();
                    for(Ingredient i : ingredients_list)
                    {
                        ingredients.append(i.getIngredient_name());
                        ingredients.append(" - ");
                        ingredients.append(i.getQuantity());
                        ingredients.append("/n");
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
                //TODO: Decomment lines after addIngredientActivity is made
//                Intent intent=new Intent(getApplicationContext(),AddIngredientsForm.class);
//                launcher.launch(intent);
            }
        });

        add_recipe_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid())
                {
                    String recipe_name=add_recipe_name.getText().toString();
                    String recipe_description=add_recipe_description.getText().toString();
                    Recipe recipe=new Recipe(recipe_name,ingredients_list,recipe_description);

                    Intent intent=new Intent();
                    intent.putExtra(RECIPE_KEY, recipe);
                    setResult(RESULT_OK,intent);
                    finish();
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
}
