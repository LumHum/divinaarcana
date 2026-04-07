package com.divinaarcana.scene;

import com.divinaarcana.DivinaArcana;
import com.divinaarcana.model.ReadingType;
import com.divinaarcana.util.SoundOracle;
import com.divinaarcana.util.WispDialogue;
import com.divinaarcana.view.DialogueBar;
import com.divinaarcana.view.StarField;
import com.divinaarcana.view.WispView;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

// the chamber of choice — where the seeker picks their fate
public class OracleScene extends StackPane {

    private final WispView wisp;
    private final VBox mainLayout;
    private Timeline idleTimer;

    public OracleScene() {
        StarField stars = new StarField(DivinaArcana.APP_W, DivinaArcana.APP_H);

        wisp = new WispView();
        StackPane.setAlignment(wisp, Pos.BOTTOM_LEFT);
        wisp.setTranslateX(80);
        wisp.setTranslateY(-DialogueBar.BAR_H - 20);

        mainLayout = new VBox();
        setOnMouseMoved(e -> {
            double dx = (e.getX() - DivinaArcana.APP_W / 2.0) / DivinaArcana.APP_W;
            double dy = (e.getY() - DivinaArcana.APP_H / 2.0) / DivinaArcana.APP_H;
            stars.setParallaxOffset(dx, dy);
            mainLayout.setTranslateX(dx * -7);
            mainLayout.setTranslateY(dy * -5);
        });

        String seekerName = DivinaArcana.getSeekerName();

        Label heading = new Label("The Oracle Chamber");
        heading.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Label seekerLabel = new Label("✦  " + seekerName.toUpperCase() + "  ✦");
        seekerLabel.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-text-fill: #C8A84B; -fx-letter-spacing: 3;");

        Label prompt = new Label("Choose your reading, seeker:");
        prompt.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 17px; -fx-font-style: italic; -fx-text-fill: #B8A88A;");

        // reading type cards — single row of 3
        HBox row = new HBox(28);
        row.setAlignment(Pos.CENTER);

        ReadingType[] types = ReadingType.values();
        for (ReadingType rt : types) row.getChildren().add(readingTypeCard(rt));

        VBox grid = new VBox(18, row);
        grid.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(heading, seekerLabel, prompt, grid);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setSpacing(22);
        mainLayout.setPadding(new Insets(0, 0, DialogueBar.BAR_H + 10, 0));
        mainLayout.setOpacity(0);

        // idle whisper every 20s — stored as field so it can be stopped on navigation
        idleTimer = new Timeline(new KeyFrame(Duration.seconds(20), e ->
            wisp.speak(WispDialogue.idleWhisper())
        ));
        idleTimer.setCycleCount(Animation.INDEFINITE);

        DialogueBar bar = DialogueBar.get();
        StackPane.setAlignment(bar, Pos.BOTTOM_CENTER);
        bar.setPrefWidth(DivinaArcana.APP_W);

        getChildren().addAll(stars, mainLayout, wisp, bar);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(900), mainLayout);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.millis(300));
        fadeIn.setOnFinished(e -> {
            SoundOracle.playWispAppear();
            String greeting = DivinaArcana.isReturningSeeker()
                ? WispDialogue.GREETING_RETURN
                : "Welcome to the Oracle Chamber, " + seekerName + ". Choose your reading and I will translate what the cards reveal.";
            wisp.speak(greeting, () -> idleTimer.play());
        });
        fadeIn.play();
    }

    private VBox readingTypeCard(ReadingType rt) {
        Label name = new Label(rt.getDisplayName());
        name.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Label desc = new Label(rt.getDescription());
        desc.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-text-fill: #B8A88A; -fx-font-style: italic;");
        desc.setWrapText(true);
        desc.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        desc.setMaxWidth(180);

        Label cardCount = new Label("✦  " + rt.getCardCount() + (rt.getCardCount() == 1 ? " card" : " cards") + "  ✦");
        cardCount.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 11px; -fx-text-fill: #5A4A2A;");

        Button drawBtn = new Button("Draw the Cards");
        drawBtn.setStyle(
            "-fx-background-color: transparent; -fx-border-color: #D4AF37; -fx-border-width: 1;" +
            "-fx-text-fill: #D4AF37; -fx-font-family: 'Georgia'; -fx-font-size: 13px;" +
            "-fx-padding: 8 20; -fx-cursor: hand; -fx-background-radius: 4; -fx-border-radius: 4;"
        );
        drawBtn.setOnMouseEntered(e -> drawBtn.setStyle(drawBtn.getStyle().replace("transparent", "rgba(212,175,55,0.15)")));
        drawBtn.setOnMouseExited(e -> drawBtn.setStyle(drawBtn.getStyle().replace("rgba(212,175,55,0.15)", "transparent")));

        drawBtn.setOnAction(e -> {
            // stop idle timer so it doesn't keep firing after we leave
            if (idleTimer != null) idleTimer.stop();
            // navigate immediately — wisp intro plays inside ShuffleScene
            SoundOracle.playMysticChime();
            SoundOracle.playTransition();
            DivinaArcana.showScene(new ShuffleScene(rt));
        });

        VBox card = new VBox(12, name, desc, cardCount, drawBtn);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(22, 20, 22, 20));
        card.setPrefWidth(210);
        card.setPrefHeight(195);
        card.setStyle(
            "-fx-background-color: rgba(10,13,40,0.88);" +
            "-fx-border-color: #252010; -fx-border-width: 1;" +
            "-fx-background-radius: 8; -fx-border-radius: 8;"
        );
        card.setOnMouseEntered(ev -> card.setStyle(card.getStyle().replace("#252010", "#D4AF37")));
        card.setOnMouseExited(ev -> card.setStyle(card.getStyle().replace("#D4AF37", "#252010")));

        card.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(600), card);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(400 + rt.ordinal() * 120L));
        ft.play();

        return card;
    }
}
