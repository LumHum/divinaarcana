package com.divinaarcana.view;

import com.divinaarcana.model.TarotCard;
import com.divinaarcana.model.TarotCard.Element;
import javafx.animation.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.BlurType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * A single tarot card — all art procedural.
 * Enhanced with layered occult aesthetics, hover zoom and holographic sheen parallax.
 */
public class CardView extends StackPane {

    public static final double CARD_W = 130;
    public static final double CARD_H = 213; // ≈ golden ratio

    private final Canvas canvas;      // main art layer
    private final Canvas shineCanvas; // hover sheen overlay
    private final double cw, ch;
    private TarotCard card;
    private boolean faceUp    = false;
    private boolean isFlipping = false;
    private boolean isHovered  = false;

    // ── constructors ────────────────────────────────────────────────────────

    public CardView()                             { this(CARD_W, CARD_H); }
    public CardView(TarotCard card)               { this(CARD_W, CARD_H); this.card = card; }
    public CardView(TarotCard card, double w, double h) { this(w, h); this.card = card; }

    public CardView(double w, double h) {
        this.cw = w;
        this.ch = h;
        canvas      = new Canvas(w, h);
        shineCanvas = new Canvas(w, h);
        shineCanvas.setMouseTransparent(true);
        getChildren().addAll(canvas, shineCanvas);
        setPrefSize(w, h);
        setMaxSize(w, h);
        drawBack();
        setupHover();
    }

    // ── hover: zoom + glow + holographic sheen ──────────────────────────────

    private void setupHover() {
        setOnMouseEntered(e -> {
            if (isFlipping) return;
            isHovered = true;
            // Zoom in
            ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
            st.setToX(1.13); st.setToY(1.13);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();
            // Golden glow
            DropShadow glow = new DropShadow(BlurType.GAUSSIAN,
                card != null ? elementColor(card.getElement()).deriveColor(0,1,1,0.8)
                             : Color.web("#D4AF37", 0.8),
                22, 0.35, 0, 0);
            setEffect(glow);
        });

        setOnMouseMoved(e -> {
            if (!isHovered || isFlipping) return;
            drawSheen(e.getX(), e.getY());
        });

        setOnMouseExited(e -> {
            if (isFlipping) return;
            isHovered = false;
            clearSheen();
            ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
            st.setToX(1.0); st.setToY(1.0);
            st.setInterpolator(Interpolator.EASE_OUT);
            st.play();
            setEffect(null);
        });
    }

    /** Holographic light sheen that follows the cursor — like tilting a foil card. */
    private void drawSheen(double mx, double my) {
        GraphicsContext gc = shineCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, cw, ch);

        double rx = mx / cw, ry = my / ch; // 0→1 across card
        // bright hot-spot near cursor, fades to transparent at edges
        RadialGradient sheen = new RadialGradient(
            0, 0, rx, ry, 0.55, true, CycleMethod.NO_CYCLE,
            new Stop(0.0,  Color.web("#FFFFFF", 0.24)),
            new Stop(0.35, Color.web("#FFFFFF", 0.09)),
            new Stop(1.0,  Color.web("#FFFFFF", 0.00))
        );
        gc.setFill(sheen);
        fillRoundRect(gc, 1, 1, cw - 2, ch - 2, 9);

        // subtle color-play strip perpendicular to cursor angle
        double angle = Math.atan2(ry - 0.5, rx - 0.5);
        LinearGradient rainbow = new LinearGradient(
            Math.cos(angle + Math.PI/2) * 0.5 + 0.5,
            Math.sin(angle + Math.PI/2) * 0.5 + 0.5,
            Math.cos(angle - Math.PI/2) * 0.5 + 0.5,
            Math.sin(angle - Math.PI/2) * 0.5 + 0.5,
            true, CycleMethod.NO_CYCLE,
            new Stop(0.0,  Color.web("#D4AF37", 0.00)),
            new Stop(0.4,  Color.web("#C8A0FF", 0.10)),
            new Stop(0.6,  Color.web("#A0D8FF", 0.10)),
            new Stop(1.0,  Color.web("#D4AF37", 0.00))
        );
        gc.setFill(rainbow);
        fillRoundRect(gc, 1, 1, cw - 2, ch - 2, 9);
    }

    private void clearSheen() {
        shineCanvas.getGraphicsContext2D().clearRect(0, 0, cw, ch);
    }

    // ── flip animation ──────────────────────────────────────────────────────

    /** Sounds managed by ReadingScene.doReveal — nothing played here. */
    public void flipReveal(TarotCard c, Runnable onDone) {
        this.card = c;
        isFlipping = true;
        clearSheen();
        setEffect(null);

        ScaleTransition foldOut = new ScaleTransition(Duration.millis(270), this);
        foldOut.setFromX(1); foldOut.setToX(0);
        foldOut.setInterpolator(Interpolator.EASE_IN);
        foldOut.setOnFinished(e -> {
            faceUp = true;
            drawFront();
            ScaleTransition foldIn = new ScaleTransition(Duration.millis(300), this);
            foldIn.setFromX(0); foldIn.setToX(1);
            foldIn.setInterpolator(Interpolator.EASE_OUT);
            foldIn.setOnFinished(ev -> {
                isFlipping = false;
                if (onDone != null) onDone.run();
            });
            foldIn.play();
        });
        foldOut.play();
    }

    public void showBack() {
        faceUp = false; card = null;
        drawBack(); setScaleX(1);
        clearSheen(); setEffect(null);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  CARD BACK  — deep space mandala
    // ═══════════════════════════════════════════════════════════════════════
    private void drawBack() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, cw, ch);

        // ── background: radial dark space ──────────────────────────────────
        RadialGradient bg = new RadialGradient(0, 0, 0.5, 0.42, 0.7, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#110D2A")),
            new Stop(0.6, Color.web("#09091C")),
            new Stop(1.0, Color.web("#060610"))
        );
        gc.setFill(bg);
        fillRoundRect(gc, 0, 0, cw, ch, 10);

        // ── scattered micro-sparkles ────────────────────────────────────────
        java.util.Random rng = new java.util.Random(7777L);
        for (int i = 0; i < 45; i++) {
            double sx = 8 + rng.nextDouble() * (cw - 16);
            double sy = 8 + rng.nextDouble() * (ch - 16);
            double ss = 0.5 + rng.nextDouble() * 1.8;
            double alpha = 0.15 + rng.nextDouble() * 0.35;
            gc.setFill(Color.web("#D4AF37", alpha));
            gc.fillOval(sx - ss/2, sy - ss/2, ss, ss);
        }

        // ── triple border frames ────────────────────────────────────────────
        gc.setStroke(Color.web("#D4AF37"));
        gc.setLineWidth(1.8);
        strokeRoundRect(gc, 2, 2, cw - 4, ch - 4, 9);

        gc.setStroke(Color.web("#D4AF37", 0.42));
        gc.setLineWidth(0.7);
        strokeRoundRect(gc, 6, 6, cw - 12, ch - 12, 6);

        gc.setStroke(Color.web("#D4AF37", 0.18));
        gc.setLineWidth(0.5);
        strokeRoundRect(gc, 11, 11, cw - 22, ch - 22, 4);

        // ── corner ornaments ────────────────────────────────────────────────
        drawRichCorner(gc, 10, 10, false, false);
        drawRichCorner(gc, cw - 10, 10, true, false);
        drawRichCorner(gc, 10, ch - 10, false, true);
        drawRichCorner(gc, cw - 10, ch - 10, true, true);

        // ── central mandala ─────────────────────────────────────────────────
        double mCx = cw / 2, mCy = ch / 2;
        double mR  = Math.min(cw, ch) * 0.26;

        // outer glow rings
        for (int g = 4; g > 0; g--) {
            double gr = mR + g * 6;
            gc.setFill(Color.web("#4A1D8A", 0.04 * g));
            gc.fillOval(mCx - gr, mCy - gr, gr * 2, gr * 2);
        }

        // outer and inner orbit circles
        gc.setStroke(Color.web("#D4AF37", 0.75));
        gc.setLineWidth(0.9);
        gc.strokeOval(mCx - mR, mCy - mR, mR * 2, mR * 2);
        double iR = mR * 0.52;
        gc.setStroke(Color.web("#D4AF37", 0.45));
        gc.setLineWidth(0.6);
        gc.strokeOval(mCx - iR, mCy - iR, iR * 2, iR * 2);

        // 8-spoke radial lines with diamond accents at midpoints
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4;
            double ix = mCx + iR * Math.cos(angle), iy = mCy + iR * Math.sin(angle);
            double ox = mCx + mR * Math.cos(angle), oy = mCy + mR * Math.sin(angle);
            gc.setStroke(Color.web("#D4AF37", 0.30));
            gc.setLineWidth(0.5);
            gc.strokeLine(ix, iy, ox, oy);
            // diamond pip at midpoint
            double mid = (iR + mR) / 2;
            double dmx = mCx + mid * Math.cos(angle), dmy = mCy + mid * Math.sin(angle);
            gc.setFill(Color.web("#D4AF37", 0.55));
            double ds = 2.2;
            gc.fillPolygon(new double[]{dmx, dmx+ds, dmx, dmx-ds}, new double[]{dmy-ds, dmy, dmy+ds, dmy}, 4);
        }

        // 8-pointed star
        drawStar(gc, mCx, mCy, 8, mR * 0.92, mR * 0.40, Color.web("#D4AF37", 0.70));
        // inner 4-pointed star
        drawStar(gc, mCx, mCy, 4, iR * 0.82, iR * 0.32, Color.web("#D4AF37", 0.55));

        // glowing core
        RadialGradient core = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.web("#FAE878", 0.95)),
            new Stop(0.5, Color.web("#D4AF37", 0.60)),
            new Stop(1.0, Color.web("#D4AF37", 0.00))
        );
        double coreR = 5 * (cw / CARD_W);
        gc.setFill(core);
        gc.fillOval(mCx - coreR * 2.5, mCy - coreR * 2.5, coreR * 5, coreR * 5);
        gc.setFill(Color.web("#FAE878", 0.95));
        gc.fillOval(mCx - coreR / 2, mCy - coreR / 2, coreR, coreR);

        // ── bottom title with flanking rules ────────────────────────────────
        double tScale = cw / CARD_W;
        double fontSize = Math.max(6, 8 * tScale);
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, fontSize));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.web("#D4AF37", 0.72));
        double titleY = ch - 11;
        gc.setStroke(Color.web("#D4AF37", 0.35));
        gc.setLineWidth(0.5);
        gc.strokeLine(cw * 0.12, titleY - fontSize * 0.35, cw * 0.34, titleY - fontSize * 0.35);
        gc.strokeLine(cw * 0.66, titleY - fontSize * 0.35, cw * 0.88, titleY - fontSize * 0.35);
        gc.fillText("DIVINA ARCANA", cw / 2, titleY);
    }

    // ═══════════════════════════════════════════════════════════════════════
    //  CARD FRONT — rich occult face
    // ═══════════════════════════════════════════════════════════════════════
    private void drawFront() {
        if (card == null) { drawBack(); return; }
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, cw, ch);

        Color accent     = elementColor(card.getElement());
        Color dim        = accent.deriveColor(0, 0.6, 0.55, 1.0);
        double tScale    = cw / CARD_W;

        // ── background with element-tinted atmospheric glow ─────────────────
        gc.setFill(Color.web("#07091A"));
        fillRoundRect(gc, 0, 0, cw, ch, 10);

        // atmosphere: element-colored radial glow in upper third
        RadialGradient atmo = new RadialGradient(0, 0, 0.5, 0.30, 0.55, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(dim.getRed(), dim.getGreen(), dim.getBlue(), 0.22)),
            new Stop(0.6, Color.color(dim.getRed(), dim.getGreen(), dim.getBlue(), 0.05)),
            new Stop(1.0, Color.color(dim.getRed(), dim.getGreen(), dim.getBlue(), 0.00))
        );
        gc.setFill(atmo);
        fillRoundRect(gc, 0, 0, cw, ch, 10);

        // faint scattered sparkles (same seed each card — stable)
        java.util.Random rng = new java.util.Random(card.getValue() * 31L + 13);
        for (int i = 0; i < 28; i++) {
            double sx = 8 + rng.nextDouble() * (cw - 16);
            double sy = 8 + rng.nextDouble() * (ch - 16);
            double ss = 0.4 + rng.nextDouble() * 1.4;
            gc.setFill(Color.web("#D4AF37", 0.08 + rng.nextDouble() * 0.18));
            gc.fillOval(sx - ss/2, sy - ss/2, ss, ss);
        }

        // ── triple border ───────────────────────────────────────────────────
        gc.setStroke(Color.web("#D4AF37"));
        gc.setLineWidth(1.8);
        strokeRoundRect(gc, 2, 2, cw - 4, ch - 4, 9);

        gc.setStroke(accent.deriveColor(0, 1, 1, 0.50));
        gc.setLineWidth(0.7);
        strokeRoundRect(gc, 6, 6, cw - 12, ch - 12, 6);

        gc.setStroke(Color.web("#D4AF37", 0.20));
        gc.setLineWidth(0.5);
        strokeRoundRect(gc, 11, 11, cw - 22, ch - 22, 4);

        // ── corner ornaments ────────────────────────────────────────────────
        drawRichCorner(gc, 10, 10, false, false);
        drawRichCorner(gc, cw - 10, 10, true, false);
        drawRichCorner(gc, 10, ch - 10, false, true);
        drawRichCorner(gc, cw - 10, ch - 10, true, true);

        // ── value label (top-left, in small decorative frame) ───────────────
        double valFontSize  = Math.max(8,  12 * tScale);
        double nameFontSize = Math.max(7,  10 * tScale);

        gc.setFont(Font.font("Georgia", FontWeight.BOLD, valFontSize));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFill(Color.web("#D4AF37", 0.92));
        gc.fillText(card.getValueLabel(), 14 * tScale, 17 * tScale);

        // element rune top-right
        String rune = elementRune(card.getElement());
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, valFontSize * 0.85));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setFill(accent.deriveColor(0, 1, 1, 0.70));
        gc.fillText(rune, cw - 13 * tScale, 17 * tScale);

        // ── reversed indicator (soft red glow arc) ──────────────────────────
        if (card.isReversed()) {
            gc.setFill(Color.web("#FF5050", 0.65));
            gc.setFont(Font.font("Georgia", Math.max(7, 9 * tScale)));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("⟳  reversed", cw / 2, 25 * tScale);
        }

        // ── central symbol with aura ─────────────────────────────────────────
        double symCy = ch * 0.46;
        double symR  = Math.min(cw, ch) * 0.21;

        // outer aura rings
        for (int g = 4; g > 0; g--) {
            double gr = symR + g * 7 * tScale;
            gc.setFill(Color.color(dim.getRed(), dim.getGreen(), dim.getBlue(), 0.045 * g));
            gc.fillOval(cw/2 - gr, symCy - gr, gr * 2, gr * 2);
        }
        // decorative outer orbit circle
        gc.setStroke(accent.deriveColor(0, 1, 1, 0.45));
        gc.setLineWidth(0.7);
        gc.strokeOval(cw/2 - symR, symCy - symR, symR * 2, symR * 2);
        // subtle inner orbit
        gc.setStroke(accent.deriveColor(0, 1, 1, 0.22));
        gc.setLineWidth(0.4);
        double innerR = symR * 0.64;
        gc.strokeOval(cw/2 - innerR, symCy - innerR, innerR * 2, innerR * 2);

        // main card symbol
        gc.save();
        gc.translate(cw / 2, symCy);
        gc.scale(tScale, tScale);
        drawCardSymbol(gc, card, accent);
        gc.restore();

        // ── separator: horizontal rule with center diamond ──────────────────
        double sepY = ch * 0.735;
        gc.setStroke(Color.web("#D4AF37", 0.38));
        gc.setLineWidth(0.6);
        gc.strokeLine(16 * tScale, sepY, cw / 2 - 7, sepY);
        gc.strokeLine(cw / 2 + 7, sepY, cw - 16 * tScale, sepY);
        gc.setFill(Color.web("#D4AF37", 0.55));
        gc.fillPolygon(new double[]{cw/2, cw/2+4, cw/2, cw/2-4}, new double[]{sepY-4, sepY, sepY+4, sepY}, 4);

        // ── card name (bottom) ───────────────────────────────────────────────
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, nameFontSize));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.web("#F0E0B8"));
        String name = card.getName();
        double nameY = ch - 26 * tScale;
        if (name.length() > 13) {
            int mid = name.lastIndexOf(' ', name.length() / 2 + 3);
            if (mid < 0) mid = name.length() / 2;
            gc.fillText(name.substring(0, mid), cw / 2, nameY);
            gc.fillText(name.substring(mid + 1), cw / 2, nameY + nameFontSize + 2);
        } else {
            gc.fillText(name, cw / 2, nameY + nameFontSize / 2);
        }
        // small flanking ornament under name
        gc.setFill(Color.web("#D4AF37", 0.38));
        gc.setFont(Font.font("Georgia", 7 * tScale));
        gc.fillText("✦", cw / 2, ch - 12 * tScale);
    }

    // ── card symbol: drawn at origin (0,0), pre-scaled by tScale ────────────
    private void drawCardSymbol(GraphicsContext gc, TarotCard card, Color accent) {
        gc.setStroke(accent);
        gc.setFill(accent);
        gc.setLineWidth(1.3 / (cw / CARD_W)); // compensate for external scale

        if (card.getArcana() == TarotCard.Arcana.MAJOR) {
            drawMajorSymbol(gc, card.getValue(), accent);
        } else {
            drawMinorSymbol(gc, card.getSuit(), card.getValue(), accent);
        }
    }

    // ── Major Arcana symbols — one per card ─────────────────────────────────
    private void drawMajorSymbol(GraphicsContext gc, int value, Color accent) {
        gc.setStroke(accent); gc.setFill(accent); gc.setLineWidth(1.2);
        Color faint = accent.deriveColor(0, 1, 1, 0.35);

        switch (value) {
            case 0 -> { // Fool — spiral path with sun
                gc.strokeOval(-18, -18, 36, 36);
                gc.strokeLine(0, -18, 12, -30); gc.strokeLine(12, -30, 22, -26);
                gc.setFill(faint); gc.fillOval(8, -36, 10, 10);
                for (int i = 0; i < 6; i++) { // small stars around orbit
                    double a = i * Math.PI / 3, r = 22;
                    gc.setFill(accent.deriveColor(0,1,1,0.5));
                    gc.fillOval(r*Math.cos(a)-2, r*Math.sin(a)-2, 4, 4);
                }
            }
            case 1 -> { // Magician — infinity over upward pentagram
                drawStar(gc, 0, -10, 5, 14, 6, accent);
                gc.strokeOval(-16, 8, 14, 10); gc.strokeOval(2, 8, 14, 10);
                gc.setStroke(accent.deriveColor(0,1,1,0.5)); gc.setLineWidth(0.6);
                gc.strokeOval(-22, -22, 44, 44);
            }
            case 2 -> { // High Priestess — twin pillars and moon crown
                gc.strokeLine(-16, 20, -16, -20); gc.strokeLine(16, 20, 16, -20);
                gc.strokeArc(-12, -28, 24, 20, 0, 180, ArcType.OPEN);
                gc.setFill(faint); gc.fillOval(-6, -12, 12, 12);
                gc.setStroke(accent); gc.strokeOval(-6, -12, 12, 12);
            }
            case 3 -> { // Empress — Venus with 8-pointed star crown
                gc.strokeOval(-14, -20, 28, 28);
                gc.strokeLine(0, 8, 0, 22); gc.strokeLine(-9, 15, 9, 15);
                drawStar(gc, 0, -28, 8, 9, 4, accent.deriveColor(0,1,1,0.7));
            }
            case 4 -> { // Emperor — fortress cross + 4 diamonds
                gc.strokeLine(-22, 0, 22, 0); gc.strokeLine(0, -22, 0, 22);
                for (int i = 0; i < 4; i++) {
                    double a = i * Math.PI/2 + Math.PI/4, r = 16;
                    double dx = r*Math.cos(a), dy = r*Math.sin(a);
                    gc.setFill(accent.deriveColor(0,1,1,0.55));
                    gc.fillPolygon(new double[]{dx,dx+5,dx,dx-5}, new double[]{dy-5,dy,dy+5,dy}, 4);
                }
                gc.strokeOval(-8, -8, 16, 16);
            }
            case 5 -> { // Hierophant — triple cross
                gc.strokeLine(0, -24, 0, 24);
                gc.strokeLine(-18, -10, 18, -10);
                gc.strokeLine(-12, 2, 12, 2);
                gc.strokeLine(-6, 14, 6, 14);
                gc.strokeOval(-4, -28, 8, 8);
            }
            case 6 -> { // Lovers — two entwined hearts under angel wings
                gc.strokeOval(-20, -14, 22, 22); gc.strokeOval(-2, -14, 22, 22);
                gc.setStroke(accent.deriveColor(0,1,1,0.45));
                gc.strokeArc(-28, -24, 20, 18, 0, 180, ArcType.OPEN);
                gc.strokeArc(8, -24, 20, 18, 0, 180, ArcType.OPEN);
            }
            case 7 -> { // Chariot — crowned crescent with star pair
                gc.strokeArc(-20, -20, 40, 40, 210, 300, ArcType.OPEN);
                gc.strokeLine(-20, 2, -20, 20); gc.strokeLine(20, 2, 20, 20);
                drawStar(gc, -14, -20, 5, 6, 2.5, accent.deriveColor(0,1,1,0.8));
                drawStar(gc, 14, -20, 5, 6, 2.5, accent.deriveColor(0,1,1,0.8));
                gc.setFill(faint); gc.fillOval(-10, -32, 20, 16);
            }
            case 8 -> { // Strength — infinity with lion-mane circle
                gc.strokeOval(-20, -9, 20, 18); gc.strokeOval(0, -9, 20, 18);
                gc.setStroke(accent.deriveColor(0,1,1,0.55));
                for (int i = 0; i < 12; i++) {
                    double a = i * Math.PI/6, r = 26;
                    gc.strokeLine(22*Math.cos(a), 22*Math.sin(a)-3, r*Math.cos(a), r*Math.sin(a)-3);
                }
            }
            case 9 -> { // Hermit — staff, lantern, cloak
                gc.strokeLine(-2, -26, -2, 24);
                gc.strokeLine(-2, -26, -8, -18);
                gc.strokeLine(-10, -24, -4, -18);
                gc.setFill(accent.deriveColor(0,1,1,0.8));
                gc.fillOval(-8, -32, 12, 12);
                for (int i = 0; i < 4; i++) { // lantern rays
                    double a = i * Math.PI/2;
                    gc.setStroke(accent.deriveColor(0,1,1,0.45));
                    gc.setLineWidth(0.6);
                    gc.strokeLine(-2+6*Math.cos(a), -26+6*Math.sin(a), -2+12*Math.cos(a), -26+12*Math.sin(a));
                }
            }
            case 10 -> { // Wheel — 8-spoked wheel with outer symbols
                gc.strokeOval(-22, -22, 44, 44);
                gc.strokeOval(-9, -9, 18, 18);
                for (int i = 0; i < 8; i++) {
                    double a = i * Math.PI/4;
                    gc.setStroke(accent); gc.setLineWidth(1.2);
                    gc.strokeLine(9*Math.cos(a), 9*Math.sin(a), 22*Math.cos(a), 22*Math.sin(a));
                }
                drawStar(gc, 0, 0, 8, 6, 3, accent.deriveColor(0,1,1,0.65));
            }
            case 11 -> { // Justice — double-blade scales + sword
                gc.strokeLine(0, -26, 0, 26);
                gc.strokeLine(-22, 0, 22, 0);
                gc.strokeLine(-22, 0, -22, 8); gc.strokeLine(22, 0, 22, 8);
                gc.setStroke(accent.deriveColor(0,1,1,0.6));
                gc.strokeOval(-27, 8, 10, 10); gc.strokeOval(17, 8, 10, 10);
                gc.setFill(faint); gc.fillRect(-3, -26, 6, 10);
            }
            case 12 -> { // Hanged Man — bound figure on T-cross
                gc.strokeLine(-18, -22, 18, -22);
                gc.strokeLine(0, -22, 0, 16);
                gc.strokeOval(-8, 16, 16, 16);
                gc.setStroke(accent.deriveColor(0,1,1,0.5));
                gc.strokeLine(-12, -8, 0, 0); gc.strokeLine(0, 0, 12, -8);
            }
            case 13 -> { // Death — scythe with rose
                gc.strokeArc(-22, -24, 44, 44, 30, 230, ArcType.OPEN);
                gc.strokeLine(22, -2, 0, 28);
                gc.setFill(accent.deriveColor(0,1,1,0.60));
                gc.fillOval(-6, 16, 12, 12);
                gc.setStroke(accent.deriveColor(0,1,1,0.6));
                gc.strokeLine(-5, 18, -5, 28);
            }
            case 14 -> { // Temperance — twin cups with flowing arc
                gc.strokeOval(-24, -10, 18, 16); gc.strokeOval(6, -10, 18, 16);
                gc.strokeArc(-12, -26, 24, 32, 0, 180, ArcType.OPEN);
                gc.strokeArc(-12, -6, 24, 32, 180, 180, ArcType.OPEN);
                for (int i = 0; i < 3; i++) {
                    gc.setFill(accent.deriveColor(0,1,1,0.45));
                    gc.fillOval(-2, -26+i*8, 4, 4);
                }
            }
            case 15 -> { // Devil — inverted pentagram + chains
                drawStar(gc, 0, -10, 5, 18, 7, accent); // inverted via rotation
                gc.strokeLine(-16, 22, -16, 12); gc.strokeLine(16, 22, 16, 12);
                gc.strokeLine(-12, -26, -6, -18);
                gc.strokeLine(12, -26, 6, -18);
                gc.setFill(accent.deriveColor(0,1,1,0.5));
                gc.fillOval(-5, 20, 10, 10);
            }
            case 16 -> { // Tower — lightning + falling figure
                gc.strokeRect(-10, -20, 20, 30);
                gc.strokeLine(0, -20, -5, -30);
                gc.strokeLine(-5, -30, 10, -24);
                gc.strokeLine(10, -24, 5, -34);
                gc.setFill(accent.deriveColor(0,1,1,0.55));
                gc.fillOval(-4, -20, 8, 8);
                // falling sparks
                for (int i = 0; i < 5; i++) {
                    gc.setFill(accent.deriveColor(0,1,1, 0.3+i*0.1));
                    gc.fillOval(-20+i*8, -5+i*5, 4, 4);
                }
            }
            case 17 -> { // Star — 8-pointed with 8 droplets
                drawStar(gc, 0, 0, 8, 24, 10, accent);
                for (int i = 0; i < 8; i++) {
                    double a = i * Math.PI/4, r = 28;
                    gc.setFill(accent.deriveColor(0,1,1,0.5));
                    gc.fillOval(r*Math.cos(a)-2, r*Math.sin(a)-2, 4, 4);
                }
                gc.setFill(accent.deriveColor(0,1,1,0.8));
                gc.fillOval(-5, -5, 10, 10);
            }
            case 18 -> { // Moon — layered crescents + wolf howl
                gc.strokeArc(-20, -24, 40, 40, 30, 300, ArcType.OPEN);
                gc.strokeOval(-8, -12, 16, 16);
                gc.setFill(faint); gc.fillOval(-14, 16, 28, 12);
                gc.setStroke(accent.deriveColor(0,1,1,0.55));
                gc.strokeLine(-8, 12, -14, -4); gc.strokeLine(8, 12, 14, -4);
            }
            case 19 -> { // Sun — radiant with child figure
                gc.strokeOval(-14, -14, 28, 28);
                gc.setFill(accent.deriveColor(0,1,1,0.7)); gc.fillOval(-9,-9,18,18);
                for (int i = 0; i < 16; i++) {
                    double a = i * Math.PI/8, r1 = 16, r2 = 14+((i%2==0)?8:4);
                    gc.setStroke(accent); gc.setLineWidth(i%2==0?1.2:0.7);
                    gc.strokeLine(r1*Math.cos(a), r1*Math.sin(a), r2*Math.cos(a), r2*Math.sin(a));
                }
            }
            case 20 -> { // Judgement — angel trumpet + rising souls
                gc.strokeArc(-10, -28, 20, 26, 0, 270, ArcType.OPEN);
                gc.strokeLine(-10, -2, -24, 12); gc.strokeLine(-10, 3, -24, 16);
                drawStar(gc, 14, -16, 5, 9, 3.5, accent.deriveColor(0,1,1,0.8));
                for (int i = 0; i < 3; i++) {
                    gc.setFill(faint);
                    gc.fillOval(-4+i*10, 16, 8, 8);
                    gc.strokeLine(-i*0+4+i*10, 16, -i*0+4+i*10, 8);
                }
            }
            case 21 -> { // World — double wreath with dancing figure
                gc.strokeOval(-22, -22, 44, 44);
                gc.strokeOval(-16, -16, 32, 32);
                for (int i = 0; i < 8; i++) {
                    double a = i * Math.PI/4, r = 20;
                    gc.setFill(accent.deriveColor(0,1,1,0.45));
                    gc.fillOval(r*Math.cos(a)-3, r*Math.sin(a)-3, 6, 6);
                }
                gc.setFill(accent.deriveColor(0,1,1,0.6));
                gc.fillOval(-5, -8, 10, 10); // dancer head
                gc.setStroke(accent); gc.setLineWidth(1.0);
                gc.strokeLine(0, 2, -6, 14); gc.strokeLine(0, 2, 6, 14);
                gc.strokeLine(0, 5, -8, 0);  gc.strokeLine(0, 5, 8, 2);
            }
            default -> drawStar(gc, 0, 0, 8, 22, 9, accent);
        }
    }

    // ── Minor Arcana suit symbols ────────────────────────────────────────────
    private void drawMinorSymbol(GraphicsContext gc, TarotCard.Suit suit, int value, Color accent) {
        gc.setStroke(accent); gc.setFill(accent.deriveColor(0,1,1,0.28)); gc.setLineWidth(1.2);
        Color bright = accent.deriveColor(0,1,1.2,1.0);
        Color faint  = accent.deriveColor(0,1,1,0.35);

        switch (suit) {
            case WANDS -> {
                // ornate flaming staff
                gc.strokeLine(0, 26, 0, -26);
                gc.strokeLine(-4, -18, 0, -26); gc.strokeLine(4, -18, 0, -26);
                gc.strokeLine(-4, -10, 0, -18); gc.strokeLine(4, -10, 0, -18);
                gc.setFill(bright); gc.fillOval(-5, -32, 10, 10);
                for (int i = 0; i < 4; i++) {
                    double a = i * Math.PI/2, r = 12;
                    gc.setStroke(faint); gc.setLineWidth(0.7);
                    gc.strokeLine(r*0.6*Math.cos(a), -8+r*0.6*Math.sin(a), r*Math.cos(a), -8+r*Math.sin(a));
                }
            }
            case CUPS -> {
                // chalice with moonflower above
                gc.strokeLine(-14, -18, 14, -18);
                gc.strokeArc(-14, -18, 28, 34, 180, 180, ArcType.OPEN);
                gc.strokeLine(-8, 16, 8, 16); gc.strokeLine(0, 16, 0, 22);
                gc.strokeLine(-10, 22, 10, 22);
                gc.setFill(faint); gc.fillOval(-12, -18, 24, 18);
                gc.setStroke(accent.deriveColor(0,1,1,0.5));
                gc.strokeOval(-7, -30, 14, 14);
            }
            case SWORDS -> {
                // double-edged blade with ornate crossguard
                gc.setFill(accent);
                gc.fillPolygon(new double[]{0,-4,4}, new double[]{-28,-14,  -14},3); // blade tip
                gc.strokeLine(0, -28, 0, 26);
                gc.strokeLine(-16, 10, 16, 10); // crossguard
                gc.strokeLine(-16, 14, 16, 14);
                // crossguard end ornaments
                gc.setFill(faint);
                gc.fillOval(-22,  8, 8, 8); gc.fillOval(14, 8, 8, 8);
                gc.strokeOval(-22, 8, 8, 8); gc.strokeOval(14, 8, 8, 8);
                gc.strokeOval(-5, 16, 10, 12); // pommel
            }
            case PENTACLES -> {
                // star in a double circle with runes
                drawStar(gc, 0, 0, 5, 20, 8, accent);
                gc.setStroke(accent); gc.setLineWidth(1.0);
                gc.strokeOval(-24, -24, 48, 48);
                gc.setStroke(accent.deriveColor(0,1,1,0.4)); gc.setLineWidth(0.6);
                gc.strokeOval(-18, -18, 36, 36);
                for (int i = 0; i < 5; i++) {
                    double a = -Math.PI/2 + i * 2*Math.PI/5, r = 28;
                    gc.setFill(accent.deriveColor(0,1,1,0.5));
                    gc.fillOval(r*Math.cos(a)-2, r*Math.sin(a)-2, 4, 4);
                }
            }
        }

        // pip cluster (shows value 1–10)
        if (value <= 10) {
            int pips = Math.min(value, 4);
            for (int i = 0; i < pips; i++) {
                double a = i * Math.PI/2 + Math.PI/4, r = 30;
                drawMiniSuit(gc, r * Math.cos(a), r * Math.sin(a), suit, accent);
            }
        }
    }

    private void drawMiniSuit(GraphicsContext gc, double x, double y, TarotCard.Suit suit, Color accent) {
        gc.setStroke(accent.deriveColor(0,1,1,0.55)); gc.setLineWidth(0.7);
        switch (suit) {
            case WANDS     -> gc.strokeLine(x, y-5, x, y+5);
            case CUPS      -> gc.strokeArc(x-4, y-3, 8, 9, 180, 180, ArcType.OPEN);
            case SWORDS    -> { gc.strokeLine(x-4, y, x+4, y); gc.strokeLine(x, y-4, x, y+4); }
            case PENTACLES -> drawStar(gc, x, y, 5, 5, 2, accent.deriveColor(0,1,1,0.55));
        }
    }

    // ── star polygon ────────────────────────────────────────────────────────
    private void drawStar(GraphicsContext gc, double cx, double cy, int pts, double outer, double inner, Color color) {
        double[] xp = new double[pts*2], yp = new double[pts*2];
        for (int i = 0; i < pts*2; i++) {
            double a = i * Math.PI/pts - Math.PI/2;
            double r = (i%2==0) ? outer : inner;
            xp[i] = cx + r*Math.cos(a); yp[i] = cy + r*Math.sin(a);
        }
        gc.setStroke(color); gc.strokePolygon(xp, yp, pts*2);
    }

    // ── ornate corner ornament ───────────────────────────────────────────────
    private void drawRichCorner(GraphicsContext gc, double x, double y, boolean fx, boolean fy) {
        double sx = fx ? -1 : 1, sy = fy ? -1 : 1;
        gc.setStroke(Color.web("#D4AF37", 0.62)); gc.setLineWidth(0.8);
        gc.strokeLine(x, y, x + 10*sx, y);
        gc.strokeLine(x, y, x, y + 10*sy);
        gc.strokeLine(x + 4*sx, y, x + 4*sx, y + 3*sy);
        gc.strokeLine(x, y + 4*sy, x + 3*sx, y + 4*sy);
        gc.setFill(Color.web("#D4AF37", 0.65));
        gc.fillOval(x - 2, y - 2, 4, 4);
        gc.setFill(Color.web("#D4AF37", 0.40));
        gc.fillOval(x + 5*sx - 1.5, y - 1.5, 3, 3);
        gc.fillOval(x - 1.5, y + 5*sy - 1.5, 3, 3);
    }

    // ── element → accent color ───────────────────────────────────────────────
    private Color elementColor(Element el) {
        return switch (el) {
            case FIRE   -> Color.web("#E07828");
            case WATER  -> Color.web("#4EA8D9");
            case AIR    -> Color.web("#80C4D0");
            case EARTH  -> Color.web("#5E9C40");
            case SPIRIT -> Color.web("#B560F7");
            case AETHER -> Color.web("#D4AF37");
        };
    }

    private String elementRune(Element el) {
        return switch (el) {
            case FIRE   -> "△";
            case WATER  -> "▽";
            case AIR    -> "△̶";
            case EARTH  -> "▽̶";
            case SPIRIT -> "✦";
            case AETHER -> "◈";
        };
    }

    // ── rounded rect helpers ─────────────────────────────────────────────────
    private void fillRoundRect(GraphicsContext gc, double x, double y, double w, double h, double r) {
        gc.beginPath();
        gc.moveTo(x+r,y); gc.lineTo(x+w-r,y);
        gc.arcTo(x+w,y, x+w,y+r, r); gc.lineTo(x+w,y+h-r);
        gc.arcTo(x+w,y+h, x+w-r,y+h, r); gc.lineTo(x+r,y+h);
        gc.arcTo(x,y+h, x,y+h-r, r); gc.lineTo(x,y+r);
        gc.arcTo(x,y, x+r,y, r); gc.closePath(); gc.fill();
    }

    private void strokeRoundRect(GraphicsContext gc, double x, double y, double w, double h, double r) {
        gc.beginPath();
        gc.moveTo(x+r,y); gc.lineTo(x+w-r,y);
        gc.arcTo(x+w,y, x+w,y+r, r); gc.lineTo(x+w,y+h-r);
        gc.arcTo(x+w,y+h, x+w-r,y+h, r); gc.lineTo(x+r,y+h);
        gc.arcTo(x,y+h, x,y+h-r, r); gc.lineTo(x,y+r);
        gc.arcTo(x,y, x+r,y, r); gc.closePath(); gc.stroke();
    }

    // ── public accessors ─────────────────────────────────────────────────────
    public TarotCard getCard()    { return card; }
    public boolean   isFaceUp()   { return faceUp; }
}
