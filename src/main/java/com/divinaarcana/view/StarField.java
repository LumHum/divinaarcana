package com.divinaarcana.view;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

// the eternal backdrop — stars that breathe and drift with the mouse
public class StarField extends Canvas {

    private record Star(double x, double y, double size, double phase, double speed, double depth) {}

    private final Star[] stars;
    private final Star[] floaties;
    private long lastTime = 0;
    private double tick = 0;
    private volatile double parallaxX = 0, parallaxY = 0; // -1..1

    public StarField(double w, double h) {
        super(w, h);
        Random rng = new Random(42);

        stars = new Star[260];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(
                rng.nextDouble() * w, rng.nextDouble() * h,
                0.6 + rng.nextDouble() * 2.2,
                rng.nextDouble() * Math.PI * 2,
                0.3 + rng.nextDouble() * 1.6,
                0.3 + rng.nextDouble() * 0.7 // depth: near=1, far=0 (parallax multiplier)
            );
        }

        floaties = new Star[30];
        for (int i = 0; i < floaties.length; i++) {
            floaties[i] = new Star(
                rng.nextDouble() * w, rng.nextDouble() * h,
                1.8 + rng.nextDouble() * 3.5,
                rng.nextDouble() * Math.PI * 2,
                0.04 + rng.nextDouble() * 0.14,
                0.8 + rng.nextDouble() * 0.2 // floaties move most with mouse
            );
        }

        AnimationTimer loop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastTime == 0) { lastTime = now; return; }
                double dt = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;
                tick += dt;
                draw(tick);
            }
        };
        loop.start();
    }

    // call this from scene mouse move — dx/dy are normalized -1..1
    public void setParallaxOffset(double dx, double dy) {
        // smooth lerp toward target
        this.parallaxX = this.parallaxX * 0.85 + dx * 0.15;
        this.parallaxY = this.parallaxY * 0.85 + dy * 0.15;
    }

    private void draw(double t) {
        GraphicsContext gc = getGraphicsContext2D();
        double w = getWidth(), h = getHeight();

        gc.setFill(Color.web("#070A18"));
        gc.fillRect(0, 0, w, h);

        // subtle vignette
        for (int ring = 0; ring < 6; ring++) {
            double frac = ring / 6.0;
            gc.setFill(Color.web("#000000", frac * 0.28));
            double inset = frac * Math.min(w, h) * 0.5;
            gc.fillOval(inset, inset, w - inset * 2, h - inset * 2);
        }

        // twinkling stars with parallax layers
        for (Star s : stars) {
            double twinkle = 0.35 + 0.65 * (0.5 + 0.5 * Math.sin(t * s.speed + s.phase));
            // far stars move less with parallax (depth near 0 = barely moves)
            double px = s.x + parallaxX * 22 * s.depth;
            double py = s.y + parallaxY * 16 * s.depth;
            // wrap around edges
            px = ((px % w) + w) % w;
            py = ((py % h) + h) % h;
            gc.setFill(Color.web("#F5E6C8", twinkle * 0.92));
            gc.fillOval(px - s.size / 2, py - s.size / 2, s.size, s.size);
        }

        // gold drifting motes — move most with parallax
        for (Star f : floaties) {
            double drift = (t * f.speed * 28) % h;
            double yPos = (f.y - drift + h) % h;
            double glow = 0.28 + 0.72 * (0.5 + 0.5 * Math.sin(t * 0.75 + f.phase));
            double pxF = f.x + parallaxX * 38 * f.depth;
            pxF = ((pxF % w) + w) % w;
            gc.setFill(Color.web("#D4AF37", glow * 0.45));
            gc.fillOval(pxF - f.size / 2, yPos - f.size / 2, f.size, f.size);
            // soft trail
            gc.setFill(Color.web("#D4AF37", glow * 0.12));
            gc.fillOval(pxF - f.size / 4, yPos + 5 - f.size / 4, f.size / 2, f.size * 1.8);
        }
    }
}
