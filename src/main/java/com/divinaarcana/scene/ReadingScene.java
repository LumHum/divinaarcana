package com.divinaarcana.scene;

import com.divinaarcana.DivinaArcana;
import com.divinaarcana.model.ReadingType;
import com.divinaarcana.model.TarotCard;
import com.divinaarcana.util.SoundOracle;
import com.divinaarcana.util.WispDialogue;
import com.divinaarcana.view.CardView;
import com.divinaarcana.view.DialogueBar;
import com.divinaarcana.view.StarField;
import com.divinaarcana.view.WispView;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

// the great reveal — fate made visible, properly laid out
public class ReadingScene extends StackPane {

    private final ReadingType readingType;
    private final List<TarotCard> cards;
    private final List<CardView> cardViews = new ArrayList<>();
    private final VBox interpretationBox;
    private int revealedCount = 0;
    private final WispView wisp;
    private Button revealBtn;
    private Button newReadingBtn;

    // card dimensions per spread type — ordinal 0=SINGLE, 1=THREE_CARD, 2=DESTINY_STAR
    private static final double[][] CARD_DIMS = {
        {155, 254},  // SINGLE     — showcase piece, big and beautiful
        {125, 205},  // THREE_CARD — comfortably readable
        {108, 177},  // DESTINY_STAR — 5 cards in a star, needs a bit more room
    };

    public ReadingScene(ReadingType readingType, List<TarotCard> cards) {
        this.readingType = readingType;
        this.cards = cards;

        StarField stars = new StarField(DivinaArcana.APP_W, DivinaArcana.APP_H);
        addParallax(stars, null);

        wisp = new WispView();
        StackPane.setAlignment(wisp, Pos.TOP_RIGHT);
        wisp.setTranslateX(-24);
        wisp.setTranslateY(18);

        // --- title bar ---
        Label title = new Label(readingType.getDisplayName());
        title.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Label subtitle = new Label(readingType.getDescription());
        subtitle.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-font-style: italic; -fx-text-fill: #8A7A5A;");

        HBox titleBar = new HBox(12, title, subtitle);
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(14, 24, 10, 24));
        titleBar.setStyle("-fx-border-color: transparent transparent #1A1610 transparent; -fx-border-width: 1;");

        // --- main content: spread left, interpretation right ---
        double spreadAreaW = 580;
        double contentH = DivinaArcana.APP_H - 58 - DialogueBar.BAR_H - 10; // title + bar + padding

        // spread panel
        StackPane spreadContainer = buildSpreadPanel(spreadAreaW, contentH);
        spreadContainer.setPrefWidth(spreadAreaW);
        spreadContainer.setPrefHeight(contentH);
        spreadContainer.setStyle("-fx-border-color: #1A1610; -fx-border-width: 0 1 0 0;");

        // interpretation panel
        interpretationBox = new VBox(12);
        interpretationBox.setPadding(new Insets(10, 16, 10, 16));
        interpretationBox.setAlignment(Pos.TOP_CENTER);

        Label interpHeader = new Label("✦  The Cards Speak  ✦");
        interpHeader.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-text-fill: #5A4A2A; -fx-font-style: italic;");
        interpretationBox.getChildren().add(interpHeader);

        ScrollPane interpScroll = new ScrollPane(interpretationBox);
        interpScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        interpScroll.setFitToWidth(true);
        interpScroll.setPrefWidth(DivinaArcana.APP_W - spreadAreaW - 4);
        interpScroll.setPrefHeight(contentH);
        interpScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        HBox mainContent = new HBox(spreadContainer, interpScroll);
        mainContent.setPrefHeight(contentH);

        // --- bottom controls (above dialogue bar) ---
        revealBtn = arcaneButton("Reveal Next Card");
        newReadingBtn = arcaneButton("✦  New Reading  ✦");
        newReadingBtn.setVisible(false);
        newReadingBtn.setOpacity(0);

        Button backBtn = smallLink("← Oracle Chamber");
        backBtn.setOnAction(e -> {
            DivinaArcana.setReturningSeeker(true);
            DivinaArcana.showScene(new OracleScene());
        });

        HBox controls = new HBox(16, revealBtn, newReadingBtn, backBtn);
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(8, 24, 8, 24));

        revealBtn.setOnAction(e -> {
            if (revealedCount < cards.size()) doReveal();
        });
        newReadingBtn.setOnAction(e -> {
            DivinaArcana.setReturningSeeker(true);
            SoundOracle.playTransition();
            DivinaArcana.showScene(new OracleScene());
        });

        // --- page layout ---
        VBox page = new VBox(titleBar, mainContent, controls);
        page.setPrefHeight(DivinaArcana.APP_H - DialogueBar.BAR_H);
        page.setAlignment(Pos.TOP_LEFT);

        // dialogue bar at bottom
        DialogueBar bar = DialogueBar.get();
        StackPane.setAlignment(bar, Pos.BOTTOM_CENTER);
        bar.setPrefWidth(DivinaArcana.APP_W);

        getChildren().addAll(stars, page, wisp, bar);

        // entrance
        page.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), page);
        fadeIn.setToValue(1);
        fadeIn.setDelay(Duration.millis(350));
        fadeIn.setOnFinished(e -> {
            SoundOracle.playWispAppear();
            wisp.speak("The cards have been drawn, " + DivinaArcana.getSeekerName() +
                       ". Press 'Reveal' or click each card in turn to unveil your fate.");
        });
        fadeIn.play();
    }

    // builds the spread pane, lays out cards, auto-scales to fit
    private StackPane buildSpreadPanel(double availW, double availH) {
        int ordinal = readingType.ordinal();
        double cw = CARD_DIMS[ordinal][0];
        double ch = CARD_DIMS[ordinal][1];
        String[] positions = readingType.getPositionNames();

        Group cardGroup = new Group();
        double[] bounds = layoutCards(cardGroup, cw, ch, positions);
        // bounds: [minX, minY, maxX, maxY]

        double naturalW = bounds[2] - bounds[0];
        double naturalH = bounds[3] - bounds[1];
        double margin = 30;
        double scaleX = (availW - margin * 2) / naturalW;
        double scaleY = (availH - margin * 2) / naturalH;
        double scale = Math.min(1.0, Math.min(scaleX, scaleY));

        // translate group so it starts at 0,0 then apply scale
        cardGroup.setTranslateX(-bounds[0] * scale);
        cardGroup.setTranslateY(-bounds[1] * scale);
        Scale scaleTransform = new Scale(scale, scale, 0, 0);
        cardGroup.getTransforms().add(scaleTransform);

        StackPane panel = new StackPane(cardGroup);
        panel.setAlignment(Pos.CENTER);
        return panel;
    }

    // places card views at computed positions, returns bounding box [minX,minY,maxX,maxY]
    private double[] layoutCards(Group group, double cw, double ch, String[] positions) {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        double[][] coords = spreadCoords(cw, ch);

        for (int i = 0; i < cards.size(); i++) {
            double x = coords[i][0], y = coords[i][1], rot = coords[i][2];
            CardView cv = new CardView(cards.get(i), cw, ch);
            cv.setLayoutX(x);
            cv.setLayoutY(y);
            cv.setRotate(rot);

            // position label — skip rotated crossing card (would overlap card below)
            if (positions != null && i < positions.length && rot == 0) {
                Label lbl = new Label(positions[i]);
                lbl.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 10px; -fx-text-fill: #6A5A3A; -fx-font-style: italic;");
                lbl.setLayoutX(x - 6);
                lbl.setLayoutY(y + ch + 4);
                lbl.setMaxWidth(cw + 12);
                lbl.setWrapText(true);
                lbl.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                group.getChildren().add(lbl);
            }

            final int idx = i;
            cv.setOnMouseClicked(e -> {
                if (!cv.isFaceUp() && idx == revealedCount) doReveal();
            });

            cv.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.millis(450), cv);
            ft.setDelay(Duration.millis(150 + i * 100L));
            ft.setToValue(1);
            ft.play();

            group.getChildren().add(cv);
            cardViews.add(cv);

            // for bounding box, account for any rotated card
            double effW = (rot == 90 || rot == -90) ? ch : cw;
            double effH = (rot == 90 || rot == -90) ? cw : ch;
            minX = Math.min(minX, x - (effW - cw) / 2);
            minY = Math.min(minY, y - (effH - ch) / 2);
            maxX = Math.max(maxX, x + cw + (effW - cw) / 2);
            maxY = Math.max(maxY, y + ch + (effH - ch) / 2);
        }
        return new double[]{minX - 6, minY - 6, maxX + 6, maxY + 28};
    }

    // returns [x, y, rotation] for each card in the spread
    private double[][] spreadCoords(double cw, double ch) {
        double gapH = 14, gapV = 14;
        return switch (readingType) {
            case SINGLE_CARD  -> singleCoords(cw, ch);
            case THREE_CARD   -> threeCoords(cw, ch, gapH);
            case DESTINY_STAR -> destinyCoords(cw, ch);
        };
    }

    private double[][] singleCoords(double cw, double ch) {
        return new double[][]{{0, 0, 0}};
    }

    private double[][] threeCoords(double cw, double ch, double gap) {
        double step = cw + gap;
        return new double[][]{
            {0, 0, 0},
            {step, 0, 0},
            {step * 2, 0, 0}
        };
    }

    // Five cards in a clean pentagon — no overlaps, proper star shape
    private double[][] destinyCoords(double cw, double ch) {
        double cx = cw / 2 + 140;
        double cy = ch / 2 + 130;
        double r = 148;
        double[][] pts = new double[5][3];
        for (int i = 0; i < 5; i++) {
            double angle = -Math.PI / 2 + i * (2 * Math.PI / 5);
            pts[i][0] = cx + r * Math.cos(angle) - cw / 2;
            pts[i][1] = cy + r * Math.sin(angle) - ch / 2;
            pts[i][2] = 0;
        }
        return pts;
    }


    private void doReveal() {
        if (revealedCount >= cards.size()) return;
        int idx = revealedCount;
        CardView cv = cardViews.get(idx);
        TarotCard card = cards.get(idx);

        // ominous sound for heavy cards
        if (card.getName().equals("Death") || card.getName().equals("The Tower") || card.getName().equals("The Devil")) {
            SoundOracle.playOminousSting();
        } else if (card.getName().equals("The Star") || card.getName().equals("The Sun")) {
            SoundOracle.playRisingSpark();
        } else {
            SoundOracle.playCardFlip();
        }

        cv.flipReveal(card, () -> {
            revealedCount++;
            addInterpretation(card, readingType.getPositionNames()[idx]);
            wisp.speak(WispDialogue.interpret(card));
            SoundOracle.playReveal();

            if (revealedCount >= cards.size()) {
                revealBtn.setText("Reading Complete  ✓");
                revealBtn.setDisable(true);
                SoundOracle.playReadingComplete();

                PauseTransition done = new PauseTransition(Duration.millis(2000));
                done.setOnFinished(e -> {
                    wisp.speak(WispDialogue.readingComplete(readingType));
                    newReadingBtn.setVisible(true);
                    FadeTransition ft = new FadeTransition(Duration.millis(600), newReadingBtn);
                    ft.setToValue(1);
                    ft.play();
                });
                done.play();
            } else {
                int remaining = cards.size() - revealedCount;
                revealBtn.setText("Reveal Next  (" + remaining + " remaining)");
            }
        });
    }

    private void addInterpretation(TarotCard card, String position) {
        VBox entry = new VBox(6);
        entry.setPadding(new Insets(12, 14, 12, 14));
        entry.setStyle(
            "-fx-background-color: rgba(10,13,40,0.82);" +
            "-fx-border-color: #251C08; -fx-border-width: 1;" +
            "-fx-background-radius: 6; -fx-border-radius: 6;"
        );

        Label posLabel = new Label("[ " + position + " ]");
        posLabel.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 11px; -fx-text-fill: #6A5A3A; -fx-font-style: italic;");

        Label cardName = new Label(card.getName() + "  " + card.getOrientationLabel());
        cardName.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #D4AF37;");

        Label keywords = new Label("✦  " + card.getKeywords() + "  ✦");
        keywords.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 10px; -fx-text-fill: #7A6A4A; -fx-font-style: italic;");

        Label meaning = new Label(card.getDisplayMeaning());
        meaning.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 13px; -fx-text-fill: #C8B88A;");
        meaning.setWrapText(true);

        entry.getChildren().addAll(posLabel, cardName, keywords, meaning);
        entry.setOpacity(0);
        interpretationBox.getChildren().add(entry);

        FadeTransition ft = new FadeTransition(Duration.millis(500), entry);
        ft.setToValue(1);
        ft.play();
    }

    private void addParallax(StarField stars, Pane contentLayer) {
        setOnMouseMoved(e -> {
            double dx = (e.getX() - DivinaArcana.APP_W / 2.0) / DivinaArcana.APP_W;
            double dy = (e.getY() - DivinaArcana.APP_H / 2.0) / DivinaArcana.APP_H;
            stars.setParallaxOffset(dx, dy);
            if (contentLayer != null) {
                contentLayer.setTranslateX(dx * -6);
                contentLayer.setTranslateY(dy * -4);
            }
        });
    }

    private Button arcaneButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: transparent; -fx-border-color: #D4AF37; -fx-border-width: 1.2;" +
            "-fx-text-fill: #D4AF37; -fx-font-family: 'Georgia'; -fx-font-size: 13px;" +
            "-fx-padding: 8 22; -fx-cursor: hand; -fx-background-radius: 4; -fx-border-radius: 4;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle().replace("transparent", "rgba(212,175,55,0.12)")));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("rgba(212,175,55,0.12)", "transparent")));
        return btn;
    }

    private Button smallLink(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #555; -fx-font-family: 'Georgia'; -fx-font-size: 11px; -fx-cursor: hand; -fx-border-color: transparent;");
        return btn;
    }
}
