package com.lissajouslaser;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App.
 */
public class App extends Application {
    private final int windowWidth = 640;
    private final int windowHeight = 480;
    private final int textAreaHeight = 720;
    private final int buttonWidth = 240;
    private final int padding = 5;

    @Override
    public void start(final Stage stage) {

        TextArea text = new TextArea("");
        text.setPrefHeight(textAreaHeight);

        Button wrapButton = new Button("Wrap Text");
        wrapButton.setPrefWidth(buttonWidth);

        VBox topLayout = new VBox();
        BorderPane centeredButton = new BorderPane();
        centeredButton.setPadding(new Insets(padding, 0, padding, 0));
        centeredButton.setCenter(wrapButton);
        topLayout.getChildren().add(text);
        topLayout.getChildren().add(centeredButton);


        var scene = new Scene(topLayout, windowWidth, windowHeight);

        stage.setScene(scene);
        stage.setTitle("text Wrap");
        stage.show();
    }

    /**
     * Entry point into program.
     */
    public static void main(String[] args) {
        launch();
    }
}
