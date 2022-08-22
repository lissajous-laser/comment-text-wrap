package com.lissajouslaser;

import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * JavaFX App.
 */
public class App extends Application {
    private CommentWrap commentWrap;
    private final int windowWidth = 720;
    private final int windowHeight = 360;
    private final int textAreaHeight = 2160;
    private final int buttonWidth = 240;
    private final int padding = 6;
    private final int textSize = 13;

    @Override
    public void start(Stage stage) {
        commentWrap = new CommentWrap();

        TextArea text = new TextArea("");
        text.setPrefHeight(textAreaHeight);

        try {
            text.setFont(
                Font.loadFont(new
                        FileInputStream("src/main/resources/NotoSansMono-Regular.ttf"), textSize)
            );
        } catch (IOException e) {
            text.setFont(Font.getDefault());
        }

        Button wrapButton = new Button("Wrap Text");
        wrapButton.setPrefWidth(buttonWidth);

        VBox topLayout = new VBox();
        BorderPane centeredButton = new BorderPane();
        centeredButton.setPadding(new Insets(padding, 0, padding, 0));
        centeredButton.setCenter(wrapButton);
        topLayout.getChildren().add(text);
        topLayout.getChildren().add(centeredButton);

        wrapButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                text.setText(commentWrap.wrap(text.getText()));
            }
        });

        var scene = new Scene(topLayout, windowWidth, windowHeight);

        stage.setScene(scene);
        stage.setTitle("Comment Text Wrap");
        stage.show();
    }

    /**
     * Entry point into program.
     */
    public static void main(String[] args) {
        launch();
    }
}
