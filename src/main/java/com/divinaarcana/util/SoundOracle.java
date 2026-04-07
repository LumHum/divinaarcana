package com.divinaarcana.util;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

// the unseen musician — weaves mystical tones from pure mathematics
public class SoundOracle {

    private static final ExecutorService choir = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "sound-oracle");
        t.setDaemon(true);
        return t;
    });
    private static final Random rng = new Random();
    private static boolean silenced = false;

    // --- public API ---

    public static void playCardFlip() {
        choir.submit(() -> playTone(200, 0.22, ToneShape.SWEEP_DOWN, 0.55));
    }

    // low ethereal chord — reveal a card
    public static void playReveal() {
        choir.submit(() -> {
            playTone(396, 0.35, ToneShape.FADE_IN, 0.4);
            sleep(120);
            playTone(528, 0.28, ToneShape.PING, 0.3);
            sleep(90);
            playTone(660, 0.20, ToneShape.PING, 0.2);
        });
    }

    // three chimes ascending
    public static void playMysticChime() {
        choir.submit(() -> {
            int[] freqs = {432, 528, 639, 741};
            int start = rng.nextInt(2);
            for (int i = start; i < start + 3; i++) {
                playTone(freqs[i], 0.18, ToneShape.PING, 0.25);
                sleep(140);
            }
        });
    }

    // low rumble drone for ambience
    public static void playAmbientDrone() {
        choir.submit(() -> {
            playTone(55, 2.8, ToneShape.DRONE, 0.18);
            sleep(100);
            playTone(82, 2.0, ToneShape.DRONE, 0.10);
        });
    }

    // papery shuffle texture
    public static void playShuffle() {
        choir.submit(() -> {
            for (int i = 0; i < 5; i++) {
                playTone(160 + rng.nextInt(60), 0.07, ToneShape.RUSTLE, 0.35);
                sleep(65);
            }
        });
    }

    // wisp appears / materializes
    public static void playWispAppear() {
        choir.submit(() -> {
            playTone(264, 0.5, ToneShape.FADE_IN, 0.3);
            sleep(150);
            playTone(396, 0.4, ToneShape.FADE_OUT, 0.2);
        });
    }

    // dark low sting — for heavy cards (Death, Tower, Devil)
    public static void playOminousSting() {
        choir.submit(() -> {
            playTone(65, 0.8, ToneShape.DRONE, 0.35);
            sleep(200);
            playTone(49, 0.6, ToneShape.FADE_OUT, 0.28);
        });
    }

    // hopeful rising tone — for The Star, The Sun
    public static void playRisingSpark() {
        choir.submit(() -> {
            for (int i = 0; i < 4; i++) {
                playTone(330 + i * 110, 0.14, ToneShape.PING, 0.22 - i * 0.03);
                sleep(120);
            }
        });
    }

    // scene transition whoosh
    public static void playTransition() {
        choir.submit(() -> playTone(180, 0.5, ToneShape.SWEEP_DOWN, 0.22));
    }

    // reading complete — full chord bloom
    public static void playReadingComplete() {
        choir.submit(() -> {
            int[] chord = {264, 330, 396, 528};
            for (int f : chord) {
                playTone(f, 0.8, ToneShape.FADE_OUT, 0.18);
                sleep(60);
            }
        });
    }

    public static void silence() { silenced = true; }
    public static void unsilence() { silenced = false; }

    // ------ tone engine ------

    private enum ToneShape { FADE_IN, FADE_OUT, PING, SWEEP_DOWN, DRONE, RUSTLE }

    private static void playTone(double hz, double seconds, ToneShape shape, double volume) {
        if (silenced) return;
        try {
            int sr = 44100;
            int frames = (int) (sr * seconds);
            byte[] buf = new byte[frames * 2];
            for (int i = 0; i < frames; i++) {
                double t = (double) i / sr;
                double freq = (shape == ToneShape.SWEEP_DOWN) ? hz * (1.0 - t * 0.45) : hz;
                double wave = Math.sin(2 * Math.PI * freq * t);
                // harmonics — makes it sound organic, not digital beep
                wave += 0.35 * Math.sin(2 * Math.PI * freq * 2 * t);
                wave += 0.15 * Math.sin(2 * Math.PI * freq * 3 * t);
                wave += 0.05 * Math.sin(2 * Math.PI * freq * 5 * t);
                wave = (wave / 1.55) * envelope(i, frames, shape) * volume;
                if (shape == ToneShape.RUSTLE) wave += (rng.nextDouble() - 0.5) * 0.12;
                short s = (short) Math.max(-32767, Math.min(32767, (int)(wave * 32767)));
                buf[i * 2] = (byte) (s & 0xFF);
                buf[i * 2 + 1] = (byte) ((s >> 8) & 0xFF);
            }
            AudioFormat fmt = new AudioFormat(sr, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, fmt);
            if (!AudioSystem.isLineSupported(info)) return;
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(fmt);
            line.start();
            line.write(buf, 0, buf.length);
            line.drain();
            line.close();
        } catch (Exception ignored) {}
    }

    private static double envelope(int frame, int total, ToneShape shape) {
        double t = (double) frame / total;
        return switch (shape) {
            case FADE_IN -> Math.min(t * 6, 1.0) * Math.max(1 - t * 0.3, 0.4);
            case FADE_OUT -> Math.exp(-t * 2.5);
            case PING -> Math.exp(-t * 7);
            case SWEEP_DOWN -> (1 - t) * Math.min(t * 12, 1.0);
            case DRONE -> 0.5 + 0.5 * Math.sin(Math.PI * t);
            case RUSTLE -> Math.random() * Math.exp(-t * 9);
        };
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
