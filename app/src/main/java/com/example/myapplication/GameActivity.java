package com.example.myapplication.;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int levelNum = getIntent().getIntExtra("LEVEL", 1);
        setContentView(new com.example.bubbleshooter.GameView(this, levelNum));
    }
}