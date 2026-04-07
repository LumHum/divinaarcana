package com.divinaarcana.view;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

// Wisp — the dark oracle made of light and bad intentions
// speaks through DialogueBar now; this is just the beautiful, unsettling particle body
public class WispView extends StackPane {

    private final Canvas canvas;
    private final double w, h;
    private double cx, cy;

    private double[] px, py, pvx, pvy, pphase, psize;
    private final int particleCount = 55;
    private long lastNano = 0;
    private double tick = 0;
    private boolean spinning = false;

    public WispView() {
        this(120, 120);
    }

    public WispView(double w, double h) {
        this.w = w;
        this.h = h;
        this.cx = w / 2;
        this.cy = h / 2;
        canvas = new Canvas(w, h);
        setPrefSize(w, h);
        setMaxSize(w, h);
        getChildren().add(canvas);
        initParticles();
        startLoop();
    }

    private void initParticles() {
        Random rng = new Random();
        px = new double[particleCount];
        py = new double[particleCount];
        pvx = new double[particleCount];
        pvy = new double[particleCount];
        pphase = new double[particleCount];
        psize = new double[particleCount];
        double scale = w / 120.0; // scale orbit radius with view size
        for (int i = 0; i < particleCount; i++) {
            double angle = rng.nextDouble() * Math.PI * 2;
            double radius = (5 + rng.nextDouble() * 28) * scale;
            px[i] = cx + Math.cos(angle) * radius;
            py[i] = cy + Math.sin(angle) * radius;
            pvx[i] = (rng.nextDouble() - 0.5) * 18;
            pvy[i] = (rng.nextDouble() - 0.5) * 18;
            pphase[i] = rng.nextDouble() * Math.PI * 2;
            psize[i] = (1.5 + rng.nextDouble() * 3.5) * scale;
        }
    }

    private void startLoop() {
        AnimationTimer loop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastNano == 0) { lastNano = now; return; }
                double dt = Math.min((now - lastNano) / 1_000_000_000.0, 0.05);
                lastNano = now;
                tick += dt;
                updateParticles(dt);
                draw();
            }
        };
        loop.start();
    }

    private void updateParticles(double dt) {
        double scale = w / 120.0;
        for (int i = 0; i < particleCount; i++) {
            double dx = cx - px[i], dy = cy - py[i];
            double dist = Math.sqrt(dx * dx + dy * dy) + 0.001;
            double tx = -dy / dist, ty = dx / dist;
            double targetRadius = (14 + 18 * Math.sin(pphase[i] + tick * 0.6)) * scale;
            pvx[i] += (dx / dist * (dist - targetRadius) * 3 + tx * 22) * dt;
            pvy[i] += (dy / dist * (dist - targetRadius) * 3 + ty * 22) * dt;
            pvx[i] *= 0.92;
            pvy[i] *= 0.92;
            px[i] += pvx[i] * dt;
            py[i] += pvy[i] * dt;
        }
    }

    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        double pulse = 0.5 + 0.5 * Math.sin(tick * 2.0);
        double glowR = (20 + pulse * 9) * (w / 120.0);

        // layered glow at center
        for (int g = 5; g > 0; g--) {
            gc.setFill(Color.web("#5B2A9E", 0.05 * g * (0.7 + 0.3 * pulse)));
            gc.fillOval(cx - glowR * g / 3.5, cy - glowR * g / 3.5, glowR * g * 2 / 3.5, glowR * g * 2 / 3.5);
        }

        // particles
        for (int i = 0; i < particleCount; i++) {
            double life = 0.35 + 0.65 * (0.5 + 0.5 * Math.sin(tick * 1.9 + pphase[i]));
            double dist = Math.sqrt(Math.pow(px[i] - cx, 2) + Math.pow(py[i] - cy, 2));
            double fade = Math.max(0, 1.0 - dist / (52 * w / 120.0));
            double colorT = Math.min(dist / (28 * w / 120.0), 1.0);
            // inner gold → outer violet
            double r = lerp(0.85, 0.38, colorT);
            double gC = lerp(0.72, 0.22, colorT);
            double b = lerp(0.20, 0.88, colorT);
            gc.setFill(Color.color(r, gC, b, life * fade * 0.92));
            double sz = psize[i] * life * fade;
            gc.fillOval(px[i] - sz / 2, py[i] - sz / 2, sz, sz);
        }

        // bright inner core
        gc.setFill(Color.web("#FAE878", 0.65 + 0.35 * pulse));
        double coreR = 4.5 * (w / 120.0);
        gc.fillOval(cx - coreR, cy - coreR, coreR * 2, coreR * 2);
    }

    // speak via the persistent DialogueBar
    public void speak(String message, Runnable onDone) {
        spinOnce();
        DialogueBar.get().show(message, onDone);
    }

    public void speak(String message) {
        speak(message, null);
    }

    // 1.5 spins when the oracle has something to say
    public void spinOnce() {
        if (spinning) return;
        spinning = true;
        RotateTransition spin = new RotateTransition(Duration.millis(1100), this);
        spin.setByAngle(540);
        spin.setInterpolator(Interpolator.EASE_BOTH);
        spin.setOnFinished(e -> {
            setRotate(0);
            spinning = false;
        });
        spin.play();
    }

    // drift wisp to an absolute translated position
    public void floatTo(double x, double y) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(2000), this);
        tt.setToX(x);
        tt.setToY(y);
        tt.setInterpolator(Interpolator.EASE_BOTH);
        tt.play();
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * Math.max(0, Math.min(1, t));
    }
}
