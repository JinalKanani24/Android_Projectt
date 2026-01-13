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
    }

    // ================= GRID CREATION =================
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float r = getWidth() / 16f;
        grid.clear();

        for (int row = 0; row < (3 + level); row++) {
            for (int col = 0; col < 8; col++) {

                float x = col * (r * 2) + r;
                if (row % 2 != 0) x += r;

                float y = row * (r * 1.8f) + 180;

                grid.add(new Bubble(
                        x, y, r,
                        colors[new Random().nextInt(colors.length)]
                ));
            }
        }
        resetShooter();
    }

    // ================= RESET =================
    private void resetShooter() {
        bx = getWidth() / 2f;
        by = getHeight() - 280;
        isShooting = false;
        showAim = false;
        currentBallColor = colors[new Random().nextInt(colors.length)];
    }

    // ================= DRAW =================
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#121212"));

        // Draw grid
        for (Bubble b : grid) {
            if (!b.isVisible) continue;

            drawBubble(canvas, b.x, b.y, b.radius, b.color);

            if (isShooting &&
                    Math.hypot(bx - b.x, by - b.y) < ballRadius + b.radius) {

                Bubble newBubble = new Bubble(bx, by, ballRadius, currentBallColor);
                grid.add(newBubble);

                ArrayList<Bubble> connected = new ArrayList<>();
                findConnected(newBubble, connected);

                if (connected.size() >= 2) {
                    for (Bubble bb : connected) {
                        bb.isVisible = false;
                        score += 100;
                    }
                }
                resetShooter();
                break;
            }
        }

        // 🔹 WALL-BOUNCE AIM LINE
        if (showAim && !isShooting) {
            drawBounceAimLine(canvas);
        }

        // Draw shooter
        drawBubble(canvas, bx, by, ballRadius, currentBallColor);

        // HUD
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawText("SCORE: " + score, getWidth() - 350, 95, paint);
        canvas.drawText("AMMO: " + ammoLeft, 50, getHeight() - 80, paint);

        // Move bubble
        if (isShooting) {
            bx += vx;
            by += vy;

            if (bx < ballRadius || bx > getWidth() - ballRadius)
                vx = -vx;

            if (by < 150)
                resetShooter();
        }

        invalidate();
    }

    // ================= DRAW BUBBLE =================
    private void drawBubble(Canvas canvas, float x, float y, float r, int color) {
        paint.setColor(color);
        canvas.drawCircle(x, y, r, paint);

        paint.setColor(Color.argb(120, 255, 255, 255));
        canvas.drawCircle(x - r / 3, y - r / 3, r / 4, paint);
    }

    // ================= WALL BOUNCE AIM LINE =================
    private void drawBounceAimLine(Canvas canvas)
    // ================= TOUCH =================
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float dx = event.getX() - bx;
        float dy = event.getY() - by;
        float dist = (float) Math.hypot(dx, dy);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (!isShooting) {
                    aimX = dx / dist;
                    aimY = dy / dist;
                    showAim = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!isShooting && ammoLeft > 0) {
                    vx = aimX * 50;
                    vy = aimY * 50;
                    isShooting = true;
                    ammoLeft--;
                }
                showAim = false;
                break;
        }
        return true;
    }

    // ================= NEIGHBOR =================
    private boolean isNeighbor(Bubble a, Bubble b) {
        float d = (float) Math.hypot(a.x - b.x, a.y - b.y);
        return d <= a.radius * 2.2f;
    }



    // ================= CONNECTED DFS =================
    private void findConnected(Bubble start, ArrayList<Bubble> result) {
        result.add(start);

        for (Bubble b : grid) {
            if (b.isVisible &&
                    b.color == start.color &&
                    !result.contains(b) &&
                    isNeighbor(start, b)) {

                findConnected(b, result);
            }
        }
    }
}

