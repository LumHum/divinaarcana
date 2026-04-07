package com.divinaarcana;

import com.divinaarcana.scene.SplashScene;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DivinaArcana extends Application {

    public static final double APP_W = 1020;
    public static final double APP_H = 720;

    private static Stage mainStage;
    private static String seekerName = "wanderer";
    private static boolean returningSeeker = false;

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        stage.setTitle("Divina Arcana");
        stage.setWidth(APP_W);
        stage.setHeight(APP_H);
        stage.setResizable(false);

        // dark icon if javafx.scene.image.Image is available
        stage.getIcons().clear();

        showScene(new SplashScene());
        stage.show();
    }

    // transitions with a quick fade-out/fade-in between scenes
    public static void showScene(Pane pane) {
        Scene scene = new Scene(pane, APP_W, APP_H);
        scene.setFill(javafx.scene.paint.Color.web("#080B1A"));

        // load our CSS
        try {
            String css = DivinaArcana.class.getResource("/css/arcana.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception ignored) {
            // works fine without the css too
        }

        mainStage.setScene(scene);
    }

    public static void setSeekerName(String name) {
        seekerName = name;
    }

    public static String getSeekerName() {
        return seekerName;
    }

    public static boolean isReturningSeeker() {
        return returningSeeker;
    }

    public static void setReturningSeeker(boolean val) {
        returningSeeker = val;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
