package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.GameView;
import com.example.myapplication.R;
import com.example.myapplication.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Badha UI elements ne find karo
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnSettings = findViewById(R.id.btnSettings);
        TextView title = findViewById(R.id.title);


        // 2. PLAY BUTTON CLICK LOGIC
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Front page na buttons ne hide kari do
                title.setVisibility(View.GONE);
                btnPlay.setVisibility(View.GONE);
                btnSettings.setVisibility(View.GONE);

                // Game View ne screen par add karo (Level 1 start thashe)
                GameView gameView = new GameView(MainActivity.this, 1);
                gameLayout.addView(gameView);
            }
        });

