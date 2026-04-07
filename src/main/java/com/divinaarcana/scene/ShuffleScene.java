package com.divinaarcana.scene;

import java.util.List;

import com.divinaarcana.DivinaArcana;
import com.divinaarcana.model.ReadingType;
import com.divinaarcana.model.TarotCard;
import com.divinaarcana.util.MagicDivination;
import com.divinaarcana.util.SoundOracle;
import com.divinaarcana.util.WispDialogue;
import com.divinaarcana.view.CardView;
import com.divinaarcana.view.DialogueBar;
import com.divinaarcana.view.StarField;
import com.divinaarcana.view.WispView;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

// the sacred shuffle — the seeker's intent enters the deck
public class ShuffleScene extends StackPane {

    private static final int FAN_CARDS = 11;

    private final ReadingType readingType;
    private final WispView wisp;
    private int shuffleCount = 0;
    private boolean isShuffling = false;

    // stored original fan positions so the riffle can return to them
    private final double[] origTX  = new double[FAN_CARDS];
    private final double[] origTY  = new double[FAN_CARDS];
    private final double[] origRot = new double[FAN_CARDS];

    public ShuffleScene(ReadingType readingType) {
        this.readingType = readingType;
        StarField stars = new StarField(DivinaArcana.APP_W, DivinaArcana.APP_H);

        wisp = new WispView();
        StackPane.setAlignment(wisp, Pos.BOTTOM_LEFT);
        wisp.setTranslateX(80);
        wisp.setTranslateY(-DialogueBar.BAR_H - 20);

        VBox mainLayout = new VBox();
        setOnMouseMoved(e -> {
            double dx = (e.getX() - DivinaArcana.APP_W / 2.0) / DivinaArcana.APP_W;
            double dy = (e.getY() - DivinaArcana.APP_H / 2.0) / DivinaArcana.APP_H;
            stars.setParallaxOffset(dx, dy);
            mainLayout.setTranslateX(dx * -6);
            mainLayout.setTranslateY(dy * -4);
        });

        Label title = new Label(readingType.getDisplayName());
        title.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Label instruction = new Label("Concentrate on your question.\nShuffle until your intention is fully clear.");
        instruction.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 17px; -fx-font-style: italic; -fx-text-fill: #B8A88A; -fx-text-alignment: center;");
        instruction.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        instruction.setWrapText(true);

        Label shuffleCounter = new Label("shuffles: 0");
        shuffleCounter.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 12px; -fx-text-fill: #555;");

        // ── deck display: fanned-out cards ───────────────────────────────────
        StackPane deckDisplay = buildFanDeck();

        Button shuffleBtn = arcaneButton("✦  Shuffle the Deck  ✦");
        Button drawBtn    = arcaneButton("✦  Draw the Cards  ✦");
        drawBtn.setVisible(false);
        drawBtn.setOpacity(0);

        Button backBtn = new Button("← Return to Chamber");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555; -fx-font-family: 'Georgia'; -fx-font-size: 12px; -fx-cursor: hand; -fx-border-color: transparent;");
        backBtn.setOnAction(e -> DivinaArcana.showScene(new OracleScene()));

        shuffleBtn.setOnAction(e -> {
            if (isShuffling) return;
            shuffleCount++;
            shuffleCounter.setText("shuffles: " + shuffleCount);
            SoundOracle.playShuffle();
            animateRiffle(deckDisplay, () -> {
                if (shuffleCount == 1) {
                    drawBtn.setVisible(true);
                    FadeTransition fi = new FadeTransition(Duration.millis(600), drawBtn);
                    fi.setToValue(1);
                    fi.play();
                    instruction.setText("The cards feel it. Draw when the moment calls to you.");
                    wisp.speak(WispDialogue.beforeDraw());
                }
            });
        });

        drawBtn.setOnAction(e -> {
            drawBtn.setDisable(true);
            shuffleBtn.setDisable(true);
            SoundOracle.playMysticChime();
            List<TarotCard> drawn = MagicDivination.castTheLot(readingType.getCardCount());
            SoundOracle.playTransition();
            DivinaArcana.showScene(new ReadingScene(readingType, drawn));
        });

        mainLayout.getChildren().addAll(title, instruction, deckDisplay, shuffleCounter, shuffleBtn, drawBtn, backBtn);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(18);
        mainLayout.setPadding(new Insets(0, 0, DialogueBar.BAR_H + 10, 0));
        mainLayout.setOpacity(0);

        DialogueBar bar = DialogueBar.get();
        StackPane.setAlignment(bar, Pos.BOTTOM_CENTER);
        bar.setPrefWidth(DivinaArcana.APP_W);

        getChildren().addAll(stars, mainLayout, wisp, bar);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), mainLayout);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.millis(300));
        fadeIn.play();

        PauseTransition wispDelay = new PauseTransition(Duration.millis(900));
        wispDelay.setOnFinished(we -> {
            SoundOracle.playWispAppear();
            wisp.speak(WispDialogue.readingIntro(readingType));
        });
        wispDelay.play();
    }

    // ── build the fan deck ──────────────────────────────────────────────────
    private StackPane buildFanDeck() {
        StackPane deck = new StackPane();
        // wide enough to contain the fanned arc, card height + a little room
        deck.setPrefSize(CardView.CARD_W * 2.8, CardView.CARD_H + 40);

        for (int i = 0; i < FAN_CARDS; i++) {
            double t   = (i / (double)(FAN_CARDS - 1)) - 0.5; // -0.5 to +0.5
            double tx  = t * CardView.CARD_W * 1.6;
            double ty  = -Math.abs(t) * 18 + 8;             // slight arc: edges lift
            double rot = t * 38;                              // -19° to +19°

            origTX[i]  = tx;
            origTY[i]  = ty;
            origRot[i] = rot;

            CardView cv = new CardView();
            cv.setTranslateX(tx);
            cv.setTranslateY(ty);
            cv.setRotate(rot);
            deck.getChildren().add(cv);
        }
        return deck;
    }

    // ── riffle shuffle animation (4 phases) ─────────────────────────────────
    private void animateRiffle(StackPane deck, Runnable onDone) {
        isShuffling = true;
        var cards = deck.getChildren();
        int n     = cards.size();
        int half  = n / 2;

        // ── Phase 1: lift and spread wide (200ms) ────────────────────────────
        ParallelTransition phase1 = new ParallelTransition();
        for (int i = 0; i < n; i++) {
            Node c  = cards.get(i);
            double spreadTX  = origTX[i] * 1.55;
            double spreadTY  = origTY[i] - 10;
            double spreadRot = origRot[i] * 1.4;

            TranslateTransition tt = new TranslateTransition(Duration.millis(200), c);
            tt.setToX(spreadTX); tt.setToY(spreadTY);
            tt.setInterpolator(Interpolator.EASE_OUT);

            RotateTransition rt = new RotateTransition(Duration.millis(200), c);
            rt.setToAngle(spreadRot);
            rt.setInterpolator(Interpolator.EASE_OUT);

            phase1.getChildren().addAll(tt, rt);
        }

        // ── Phase 2: split into two halves (160ms) ──────────────────────────
        ParallelTransition phase2 = new ParallelTransition();
        for (int i = 0; i < n; i++) {
            Node c = cards.get(i);
            TranslateTransition tt = new TranslateTransition(Duration.millis(160), c);
            RotateTransition    rt = new RotateTransition(Duration.millis(160), c);

            double leftRatio  = (double) i / (half - 1);  // 0→1 across left half
            double rightRatio = (double)(i - half) / (n - half - 1);

            if (i < half) {
                tt.setToX(-CardView.CARD_W * 0.85 + leftRatio * CardView.CARD_W * 0.35);
                tt.setToY(-6);
                rt.setToAngle(-28 + leftRatio * 12);
            } else if (i == half) {
                tt.setToX(0); tt.setToY(-16); rt.setToAngle(0);
            } else {
                tt.setToX(CardView.CARD_W * 0.85 - rightRatio * CardView.CARD_W * 0.35);
                tt.setToY(-6);
                rt.setToAngle(28 - rightRatio * 12);
            }
            tt.setInterpolator(Interpolator.EASE_OUT);
            rt.setInterpolator(Interpolator.EASE_OUT);
            phase2.getChildren().addAll(tt, rt);
        }

        // ── Phase 3: riffle (staggered 28ms per card, 80ms per move) ────────
        // Cards alternate from left and right half dropping to center
        ParallelTransition phase3 = new ParallelTransition();
        for (int slot = 0; slot < n; slot++) {
            // alternate: 0→left[0], 1→right[0], 2→left[1], ...
            int cardIdx;
            if (slot % 2 == 0) {
                cardIdx = slot / 2;
                if (cardIdx >= half) cardIdx = half - 1;
            } else {
                cardIdx = half + slot / 2;
                if (cardIdx >= n) cardIdx = n - 1;
            }
            Node c = cards.get(cardIdx);

            TranslateTransition tt = new TranslateTransition(Duration.millis(85), c);
            tt.setToX(0); tt.setToY(-10);
            tt.setDelay(Duration.millis(slot * 30L));
            tt.setInterpolator(Interpolator.EASE_IN);

            RotateTransition rt = new RotateTransition(Duration.millis(85), c);
            rt.setToAngle(0);
            rt.setDelay(Duration.millis(slot * 30L));

            phase3.getChildren().addAll(tt, rt);
        }

        // ── Phase 4: re-fan to original positions (320ms, staggered) ────────
        ParallelTransition phase4 = new ParallelTransition();
        for (int i = 0; i < n; i++) {
            Node c = cards.get(i);

            TranslateTransition tt = new TranslateTransition(Duration.millis(320), c);
            tt.setToX(origTX[i]); tt.setToY(origTY[i]);
            tt.setDelay(Duration.millis(i * 18L));
            tt.setInterpolator(Interpolator.EASE_OUT);

            RotateTransition rt = new RotateTransition(Duration.millis(320), c);
            rt.setToAngle(origRot[i]);
            rt.setDelay(Duration.millis(i * 18L));
            rt.setInterpolator(Interpolator.EASE_OUT);

            phase4.getChildren().addAll(tt, rt);
        }

        SequentialTransition full = new SequentialTransition(phase1, phase2, phase3, phase4);
        full.setOnFinished(e -> {
            isShuffling = false;
            if (onDone != null) onDone.run();
        });
        full.play();
    }

    private Button arcaneButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: transparent; -fx-border-color: #D4AF37; -fx-border-width: 1.5;" +
            "-fx-text-fill: #D4AF37; -fx-font-family: 'Georgia'; -fx-font-size: 16px;" +
            "-fx-padding: 12 30; -fx-cursor: hand; -fx-background-radius: 4; -fx-border-radius: 4;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle().replace("-fx-background-color: transparent", "-fx-background-color: rgba(212,175,55,0.12)")));
        btn.setOnMouseExited(e ->  btn.setStyle(btn.getStyle().replace("-fx-background-color: rgba(212,175,55,0.12)", "-fx-background-color: transparent")));
        return btn;
    }
}
