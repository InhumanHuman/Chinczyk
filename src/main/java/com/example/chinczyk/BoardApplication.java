package com.example.chinczyk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.IOException;

public class BoardApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BoardApplication.class.getResource("setUsernamePanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 300, 430);
            stage.setScene(scene);
            //stage.getIcons().add(new Image("file:src/main/resources/com/example/chinczyk/pawn-black-chess-piece.png"));
            stage.getIcons().add(new Image("file:src/main/resources/com/example/chinczyk/icon.png"));
            stage.setTitle("Gra - Chińczyk");
            stage.show();

            stage.setOnCloseRequest(windowEvent -> {
                windowEvent.consume();
                Close_Aplication(stage);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Close_Aplication(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Za chwilę wyjdziesz z aplikacji!");
        alert.setContentText("Czy chcesz kontynuwać?");

        if(alert.showAndWait().get() == ButtonType.OK) {
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}