package com.example.proiect_dam_retete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
        btn_add=findViewById(R.id.dinita_cosmina_button_Add);
        intent_submit=getIntent();


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    EIngredients ingredientType = EIngredients.valueOf(spinner.getSelectedItem().toString());
                    float quantityText = Float.parseFloat(quantityEditText.getText().toString().trim());
                    Ingredient selectedIngredient = new Ingredient(quantityText, ingredientType);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("ingredient", selectedIngredient);
                    intent_submit.putExtra("activityOrigin", "addIngredientsFrom");
                    intent_submit.putExtra("fetchedIngredientTag", bundle);
                    setResult(RESULT_OK, intent_submit);
                    finish();
                }
            }
        });
    }

    private boolean isValid() {
        if (quantityEditText == null || quantityEditText.getText() == null) {
            Toast.makeText(getApplicationContext(), "System error: form not properly initialized", Toast.LENGTH_SHORT).show();
            return false;
        }

        String quantityText = quantityEditText.getText().toString().trim();
        if (quantityText.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please introduce a valid quantity", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Float.parseFloat(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Invalid quantity", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}