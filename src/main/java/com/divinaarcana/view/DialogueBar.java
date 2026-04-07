package com.divinaarcana.view;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

// the oracle speaks here — wide RPG-style dialogue bar at the bottom
public class DialogueBar extends StackPane {

    public static final double BAR_H = 136;

    private static DialogueBar instance;

    private final WispView miniWisp;
    private final Label textLabel;
    private final Button nextBtn;
    private boolean isSpeaking = false;
    private Timeline currentTypewriter;
    private FadeTransition currentDismiss;
    private Timeline cooldownTimer;
    private String currentMessage = "";
    private Runnable pendingOnDone;
    private boolean typewriterDone = false;

    private DialogueBar() {
        // sizing — width is set externally per scene via bar.setPrefWidth(APP_W)
        setPrefHeight(BAR_H);
        setMaxHeight(BAR_H);
        setMinHeight(BAR_H);

        // dark background + gold top border via CSS (avoids needing a bound Rectangle child)
        setStyle(
            "-fx-background-color: rgba(6,9,22,0.97);" +
            "-fx-border-color: #D4AF37 transparent transparent transparent;" +
            "-fx-border-width: 1.5 0 0 0;"
        );

        // ── left section: mini wisp (fixed 118px) ────────────────────────────
        miniWisp = new WispView(108, 108);
        StackPane wispPane = new StackPane(miniWisp);
        wispPane.setPrefWidth(118);
        wispPane.setMinWidth(118);
        wispPane.setMaxWidth(118);
        wispPane.setAlignment(Pos.CENTER);

        // gold vertical divider (fixed 14px)
        Line divider = new Line(0, 10, 0, BAR_H - 10);
        divider.setStroke(Color.web("#D4AF37", 0.45));
        divider.setStrokeWidth(0.8);
        StackPane divPane = new StackPane(divider);
        divPane.setPrefWidth(14);
        divPane.setMinWidth(14);
        divPane.setMaxWidth(14);

        // ── right section: header row + text (grows to fill all remaining space) ──

        // header row: "WISP" label + spacer + Next button
        Label wispName = new Label("WISP  ✦  THE DARK ORACLE");
        wispName.setStyle(
            "-fx-font-family: 'Georgia'; -fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-text-fill: #C8A84B;"
        );

        nextBtn = new Button("Next  ▶");
        nextBtn.setStyle(
            "-fx-background-color: transparent; -fx-border-color: #D4AF37; -fx-border-width: 1;" +
            "-fx-text-fill: #D4AF37; -fx-font-family: 'Georgia'; -fx-font-size: 11px;" +
            "-fx-padding: 3 12; -fx-cursor: hand; -fx-background-radius: 3; -fx-border-radius: 3;"
        );
        nextBtn.setDisable(true);
        nextBtn.setOpacity(0.4);
        nextBtn.setOnAction(e -> handleNext());

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox headerRow = new HBox(8, wispName, headerSpacer, nextBtn);
        headerRow.setAlignment(Pos.CENTER_LEFT);
        headerRow.setMaxWidth(Double.MAX_VALUE);

        // main text label
        textLabel = new Label();
        textLabel.setWrapText(true);
        textLabel.setMaxWidth(Double.MAX_VALUE);
        textLabel.setMaxHeight(Double.MAX_VALUE);
        textLabel.setAlignment(Pos.TOP_LEFT);
        textLabel.setStyle(
            "-fx-font-family: 'Georgia'; -fx-font-size: 18px; -fx-text-fill: #F5ECD7; -fx-line-spacing: 3;"
        );

        // text area VBox — HBox.setHgrow(ALWAYS) makes it consume all remaining space
        VBox textArea = new VBox(8, headerRow, textLabel);
        textArea.setAlignment(Pos.TOP_LEFT);
        textArea.setPadding(new Insets(12, 22, 10, 12));
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(textLabel, Priority.ALWAYS);
        HBox.setHgrow(textArea, Priority.ALWAYS);  // ← key: fills everything after the fixed left panel

        // ── main HBox: [wisp fixed] [divider fixed] [text grows] ────────────
        HBox mainRow = new HBox(0, wispPane, divPane, textArea);
        mainRow.setMaxWidth(Double.MAX_VALUE);
        mainRow.setMaxHeight(Double.MAX_VALUE);
        StackPane.setAlignment(mainRow, Pos.CENTER_LEFT);

        getChildren().add(mainRow);
        setOpacity(0);
        setVisible(false);
    }

    public static DialogueBar get() {
        if (instance == null) instance = new DialogueBar();
        return instance;
    }

    // called by WispView.speak()
    public void show(String message, Runnable onDone) {
        // stop anything in progress
        if (currentTypewriter != null) { currentTypewriter.stop(); currentTypewriter = null; }
        if (currentDismiss  != null) { currentDismiss.stop();  currentDismiss  = null; }
        if (cooldownTimer   != null) { cooldownTimer.stop();   cooldownTimer   = null; }

        isSpeaking     = true;
        typewriterDone = false;
        currentMessage = message;
        pendingOnDone  = onDone;

        textLabel.setText("");
        nextBtn.setDisable(true);
        nextBtn.setOpacity(0.4);
        nextBtn.setText("Next  ▶");

        // force fully visible
        setVisible(true);
        setOpacity(1.0);

        // typewriter — 16ms per character
        currentTypewriter = new Timeline();
        for (int i = 0; i < message.length(); i++) {
            final String partial = message.substring(0, i + 1);
            currentTypewriter.getKeyFrames().add(
                new KeyFrame(Duration.millis(16L * i), e -> textLabel.setText(partial))
            );
        }
        long totalMs = 16L * message.length();
        currentTypewriter.getKeyFrames().add(new KeyFrame(Duration.millis(totalMs + 40), e -> {
            typewriterDone = true;
            textLabel.setText(currentMessage);
        }));
        currentTypewriter.play();

        // 2-second cooldown before Next becomes clickable
        startCooldown(2);
    }

    public void show(String message) {
        show(message, null);
    }

    // countdown then enable Next
    private void startCooldown(int seconds) {
        nextBtn.setDisable(true);
        nextBtn.setOpacity(0.4);
        if (seconds <= 0) {
            nextBtn.setText("Next  ▶");
            nextBtn.setDisable(false);
            nextBtn.setOpacity(1.0);
            return;
        }
        nextBtn.setText("Next (" + seconds + "s)");
        cooldownTimer = new Timeline();
        for (int i = 1; i <= seconds; i++) {
            final int remaining = seconds - i;
            cooldownTimer.getKeyFrames().add(new KeyFrame(Duration.millis(1000L * i), e -> {
                if (remaining > 0) {
                    nextBtn.setText("Next (" + remaining + "s)");
                } else {
                    nextBtn.setText("Next  ▶");
                    nextBtn.setDisable(false);
                    nextBtn.setOpacity(1.0);
                }
            }));
        }
        cooldownTimer.play();
    }

    // Next button: skip typewriter OR dismiss
    private void handleNext() {
        if (!isSpeaking) return;
        if (!typewriterDone) {
            // skip — show full text, restart cooldown
            if (currentTypewriter != null) { currentTypewriter.stop(); currentTypewriter = null; }
            if (cooldownTimer     != null) { cooldownTimer.stop(); }
            typewriterDone = true;
            textLabel.setText(currentMessage);
            nextBtn.setDisable(true);
            nextBtn.setOpacity(0.4);
            startCooldown(2);
        } else {
            // dismiss and fire callback
            dismiss(pendingOnDone);
            pendingOnDone = null;
        }
    }

    public void dismiss(Runnable onDone) {
        if (!isSpeaking) return;
        isSpeaking     = false;
        typewriterDone = false;
        if (currentTypewriter != null) { currentTypewriter.stop(); currentTypewriter = null; }
        if (cooldownTimer     != null) { cooldownTimer.stop();     cooldownTimer     = null; }

        currentDismiss = new FadeTransition(Duration.millis(400), this);
        currentDismiss.setToValue(0);
        currentDismiss.setOnFinished(e -> {
            setVisible(false);
            textLabel.setText("");
            nextBtn.setText("Next  ▶");
            nextBtn.setDisable(true);
            nextBtn.setOpacity(0.4);
            currentDismiss = null;
            if (onDone != null) onDone.run();
        });
        currentDismiss.play();
    }

    public boolean isSpeaking() { return isSpeaking; }
    public WispView getMiniWisp() { return miniWisp; }
}
