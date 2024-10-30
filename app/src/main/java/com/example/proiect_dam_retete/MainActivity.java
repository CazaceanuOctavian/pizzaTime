package com.example.proiect_dam_retete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    NavigationView mainNav;
    Toolbar mainToolbar;
    DrawerLayout mainDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //TODO --> move this to the navview
        Button navigateButton = findViewById(R.id.main_activity_launch_recipe_list_btn);

        this.mainNav = findViewById(R.id.cazaceanu_octavian_main_navview);
        this.mainToolbar = findViewById(R.id.cazaceanu_octavian_main_toolbar);
        this.mainDrawerLayout = findViewById(R.id.cazaceanu_octavian_main_drawer_layout);
        setSupportActionBar(mainToolbar);
        //setup the toolbar-navview combo
        ActionBarDrawerToggle toggleObject = new ActionBarDrawerToggle(
            this, mainDrawerLayout, mainToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        toggleObject.syncState();

        mainNav.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_recipes) {
                //TODO --> implement
                // Create an Intent to start SecondActivity
                Intent intent = new Intent(MainActivity.this, RecipeListActivity.class);
                startActivity(intent);
            }
            else if (item.getItemId() == R.id.nav_most_viewed) {
                //TODO --> implement
            }
            return true;
        });

        // Set click listener for the button
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}