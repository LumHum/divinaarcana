package com.divinaarcana.scene;

import com.divinaarcana.DivinaArcana;
import com.divinaarcana.util.SoundOracle;
import com.divinaarcana.util.WispDialogue;
import com.divinaarcana.view.DialogueBar;
import com.divinaarcana.view.StarField;
import com.divinaarcana.view.WispView;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

// wisp teaches you the ways — seeker is welcomed into the fold
public class OnboardingScene extends StackPane {

    private final WispView wisp;

    public OnboardingScene() {
        StarField stars = new StarField(DivinaArcana.APP_W, DivinaArcana.APP_H);

        setOnMouseMoved(e -> {
            double dx = (e.getX() - DivinaArcana.APP_W / 2.0) / DivinaArcana.APP_W;
            double dy = (e.getY() - DivinaArcana.APP_H / 2.0) / DivinaArcana.APP_H;
            stars.setParallaxOffset(dx, dy);
        });

        wisp = new WispView();
        wisp.setOpacity(0);
        StackPane.setAlignment(wisp, Pos.BOTTOM_LEFT);
        wisp.setTranslateX(80);
        wisp.setTranslateY(-DialogueBar.BAR_H - 20);

        Label title = new Label("The Ritual Begins");
        title.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Label infoText = new Label(
            "Divina Arcana reads from the full 78-card tradition.\n" +
            "Major Arcana: 22 cards of cosmic fate and transformation.\n" +
            "Minor Arcana: 56 cards of daily truth and consequence.\n\n" +
            "The cards do not lie. Wisp does not judge.\n" +
            "You will not always like what you hear."
        );
        infoText.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-text-fill: #C8B88A; -fx-text-alignment: center;");
        infoText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        infoText.setWrapText(true);
        infoText.setMaxWidth(480);

        Label namePrompt = new Label(WispDialogue.ENTER_NAME);
        namePrompt.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-text-fill: #D4AF37; -fx-font-style: italic;");
        namePrompt.setWrapText(true);
        namePrompt.setMaxWidth(440);
        namePrompt.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        namePrompt.setOpacity(0);

        TextField nameField = new TextField();
        nameField.setPromptText("your name, seeker...");
        nameField.setMaxWidth(280);
        nameField.setOpacity(0);

        Button beginButton = arcaneButton("✦  Enter the Veil  ✦");
        beginButton.setOpacity(0);

        VBox centerContent = new VBox(22, title, infoText, namePrompt, nameField, beginButton);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setMaxWidth(540);
        centerContent.setOpacity(0);
        centerContent.setPadding(new Insets(0, 0, DialogueBar.BAR_H + 20, 0));

        DialogueBar bar = DialogueBar.get();
        StackPane.setAlignment(bar, Pos.BOTTOM_CENTER);
        bar.setPrefWidth(DivinaArcana.APP_W);

        getChildren().addAll(stars, centerContent, wisp, bar);

        beginButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "wanderer";
            String finalName = name;
            DivinaArcana.setSeekerName(finalName);
            SoundOracle.playMysticChime();
            beginButton.setDisable(true);
            wisp.speak("Ah... " + finalName + ". I will remember that name for longer than you will. Come — let us see what the cards have kept for you.", () -> {
                SoundOracle.playTransition();
                DivinaArcana.showScene(new OracleScene());
            });
        });

        kickOff(centerContent, namePrompt, nameField, beginButton);
    }

    private void kickOff(VBox content, Label namePrompt, TextField nameField, Button beginButton) {
        PauseTransition intro = new PauseTransition(Duration.millis(350));
        intro.setOnFinished(e -> {
            FadeTransition wispIn = new FadeTransition(Duration.millis(800), wisp);
            wispIn.setToValue(1);
            wispIn.play();

            SoundOracle.playWispAppear();

            FadeTransition contentIn = new FadeTransition(Duration.millis(900), content);
            contentIn.setDelay(Duration.millis(500));
            contentIn.setToValue(1);
            contentIn.play();

            PauseTransition step1 = new PauseTransition(Duration.millis(2000));
            step1.setOnFinished(ev -> {
                // chain all remaining FIRST_MEETING lines before revealing name input
                wisp.speak(WispDialogue.FIRST_MEETING[1], () ->
                    wisp.speak(WispDialogue.FIRST_MEETING[2], () ->
                        wisp.speak(WispDialogue.FIRST_MEETING[3], () ->
                            wisp.speak(WispDialogue.FIRST_MEETING[4], () -> {
                                FadeTransition nameIn = new FadeTransition(Duration.millis(500), namePrompt);
                                nameIn.setToValue(1);
                                nameIn.play();

                                PauseTransition nd = new PauseTransition(Duration.millis(300));
                                nd.setOnFinished(ne -> {
                                    FadeTransition fi = new FadeTransition(Duration.millis(500), nameField);
                                    fi.setToValue(1);
                                    fi.play();
                                    PauseTransition bd = new PauseTransition(Duration.millis(350));
                                    bd.setOnFinished(be -> {
                                        FadeTransition bi = new FadeTransition(Duration.millis(500), beginButton);
                                        bi.setToValue(1);
                                        bi.play();
                                    });
                                    bd.play();
                                });
                                nd.play();
                            })
                        )
                    )
                );
            });
            step1.play();
        });
        intro.play();
    }

    private Button arcaneButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: transparent; -fx-border-color: #D4AF37; -fx-border-width: 1.5;" +
            "-fx-text-fill: #D4AF37; -fx-font-family: 'Georgia'; -fx-font-size: 16px;" +
            "-fx-padding: 12 28; -fx-cursor: hand; -fx-background-radius: 4; -fx-border-radius: 4;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle().replace("-fx-background-color: transparent", "-fx-background-color: rgba(212,175,55,0.12)")));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-background-color: rgba(212,175,55,0.12)", "-fx-background-color: transparent")));
        return btn;
    }
}
