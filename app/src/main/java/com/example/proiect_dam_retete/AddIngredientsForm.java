package com.example.proiect_dam_retete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class AddIngredientsForm extends AppCompatActivity {
    private Spinner spinner;
    private TextInputEditText quantityEditText;
    private Button btn_add;
    private Button btn_submit;

    private Intent intent_submit;
    private Intent intent_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_ingredients_form);
        setBackPressAnimation(true, R.anim.slide_in_left, R.anim.slide_out_right);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        quantityEditText = findViewById(R.id.dinita_cosmina_tiet);

        spinner=findViewById(R.id.dinita_cosmina_spinner);
        EIngredients[] ingredients = EIngredients.values();
        String[] ingredientNames = new String[ingredients.length];

        for (int i = 0; i < ingredients.length; i++) {
            ingredientNames[i] = ingredients[i].name();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredientNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btn_submit=findViewById(R.id.dinita_cosmina_button_Submit);
        intent_submit=getIntent();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    EIngredients ingredientType = EIngredients.valueOf(spinner.getSelectedItem().toString());
                    float quantityText = Float.parseFloat(quantityEditText.getText().toString().trim());
                    Ingredient selectedIngredient = new Ingredient(quantityText, ingredientType);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(getString(R.string.ingredient), selectedIngredient);
                    intent_submit.putExtra(getString(R.string.activity_origin), getString(R.string.add_ingredients_from));
                    intent_submit.putExtra(getString(R.string.fetched_ingredient_tag), bundle);
                    setResult(RESULT_OK, intent_submit);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            }
        });
    }

    private boolean isValid() {
        if (quantityEditText == null || quantityEditText.getText() == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.system_error_form_not_properly_initialized), Toast.LENGTH_SHORT).show();
            return false;
        }

        String quantityText = quantityEditText.getText().toString().trim();
        if (quantityText.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.please_introduce_a_valid_quantity), Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Float.parseFloat(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(),getString(R.string.invalid_quantity), Toast.LENGTH_SHORT).show();
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