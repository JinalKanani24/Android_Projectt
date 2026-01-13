package com.example.bubbleshooter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {

    // ================= VARIABLES =================
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float bx, by;
    private float ballRadius = 45f;
    private float vx, vy;
    private boolean isShooting = false;

    // Aim variables
    private float aimX, aimY;
    private boolean showAim = false;

    private int currentBallColor;
    private int ammoLeft = 20;
    private int score = 0;
    private int level;

    private int[] colors = {
            Color.RED, Color.CYAN, Color.YELLOW,
            Color.GREEN, Color.MAGENTA
    };

    private ArrayList<Bubble> grid = new ArrayList<>();

    // ================= CONSTRUCTOR =================
    public GameView(Context context, int level) {
        super(context);
        this.level = level;
        currentBallColor = colors[new Random().nextInt(colors.length)];
