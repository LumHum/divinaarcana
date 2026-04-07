package com.divinaarcana.scene;

import com.divinaarcana.DivinaArcana;
import com.divinaarcana.util.SoundOracle;
import com.divinaarcana.util.WispDialogue;
import com.divinaarcana.view.DialogueBar;
import com.divinaarcana.view.StarField;
import com.divinaarcana.view.WispView;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;

// the door opens — first impression is everything
public class SplashScene extends StackPane {

    public SplashScene() {
        StarField stars = new StarField(DivinaArcana.APP_W, DivinaArcana.APP_H);

        // parallax on mouse move
        setOnMouseMoved(e -> {
            double dx = (e.getX() - DivinaArcana.APP_W / 2.0) / DivinaArcana.APP_W;
            double dy = (e.getY() - DivinaArcana.APP_H / 2.0) / DivinaArcana.APP_H;
            stars.setParallaxOffset(dx, dy);
        });

        Label mainTitle = new Label("DIVINA ARCANA");
        mainTitle.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");
        mainTitle.setOpacity(0);
        mainTitle.setEffect(goldenGlow());

        Label subtitle = new Label("✦  The Dark Oracle Awaits  ✦");
        subtitle.setStyle("-fx-font-family: 'Georgia'; -fx-font-style: italic; -fx-font-size: 20px; -fx-text-fill: #C8A84B;");
        subtitle.setOpacity(0);

        Label ornament = new Label("— ⟡ —");
        ornament.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 16px; -fx-text-fill: #D4AF37;");
        ornament.setOpacity(0);

        VBox content = new VBox(20, mainTitle, ornament, subtitle);
        content.setAlignment(Pos.CENTER);

        WispView wisp = new WispView();
        StackPane.setAlignment(wisp, Pos.BOTTOM_LEFT);
        wisp.setTranslateX(80);
        wisp.setTranslateY(-DialogueBar.BAR_H - 20);
        wisp.setOpacity(0);

        DialogueBar bar = DialogueBar.get();
        StackPane.setAlignment(bar, Pos.BOTTOM_CENTER);
        bar.setPrefWidth(DivinaArcana.APP_W);

        getChildren().addAll(stars, content, wisp, bar);

        beginRitual(mainTitle, ornament, subtitle, wisp);
    }

    private void beginRitual(Label title, Label ornament, Label subtitle, WispView wisp) {
        PauseTransition silence = new PauseTransition(Duration.millis(800));
        silence.setOnFinished(e -> {
            SoundOracle.playAmbientDrone();

            ParallelTransition titleIn = new ParallelTransition(
                fadeIn(title, 1400),
                scaleIn(title, 1400, 0.85)
            );
            titleIn.play();

            PauseTransition ornamentDelay = new PauseTransition(Duration.millis(950));
            ornamentDelay.setOnFinished(oe -> {
                SoundOracle.playMysticChime();
                fadeIn(ornament, 700).play();
            });
            ornamentDelay.play();

            PauseTransition subDelay = new PauseTransition(Duration.millis(1450));
            subDelay.setOnFinished(se -> fadeIn(subtitle, 800).play());
            subDelay.play();

            PauseTransition wispDelay = new PauseTransition(Duration.millis(2400));
            wispDelay.setOnFinished(we -> {
                SoundOracle.playWispAppear();
                FadeTransition wispFade = new FadeTransition(Duration.millis(1200), wisp);
                wispFade.setToValue(1);
                wispFade.setOnFinished(wfe -> {
                    wisp.speak(WispDialogue.FIRST_MEETING[0], () -> {
                        SoundOracle.playTransition();
                        PauseTransition toOnboarding = new PauseTransition(Duration.millis(400));
                        toOnboarding.setOnFinished(te -> DivinaArcana.showScene(new OnboardingScene()));
                        toOnboarding.play();
                    });
                });
                wispFade.play();
            });
            wispDelay.play();
        });
        silence.play();
    }

    private FadeTransition fadeIn(javafx.scene.Node n, int ms) {
        FadeTransition ft = new FadeTransition(Duration.millis(ms), n);
        ft.setToValue(1);
        return ft;
    }

    private ScaleTransition scaleIn(javafx.scene.Node n, int ms, double from) {
        ScaleTransition st = new ScaleTransition(Duration.millis(ms), n);
        st.setFromX(from); st.setFromY(from);
        st.setToX(1); st.setToY(1);
        st.setInterpolator(Interpolator.EASE_OUT);
        return st;
    }

    private DropShadow goldenGlow() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#D4AF37", 0.7));
        glow.setRadius(26);
        glow.setSpread(0.15);
        return glow;
    }
}
