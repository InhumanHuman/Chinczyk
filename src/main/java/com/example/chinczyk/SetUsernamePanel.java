package com.example.chinczyk;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SetUsernamePanel implements Initializable {
    @FXML
    TextField clientUsername;

    @FXML
    Button clientSendUsername;

    String username;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientSendUsername.setOnAction(event -> {
            username = clientUsername.getText();
            //stworz klienta i przekaz go do roomspanel
            Client client = new Client("127.0.0.1", 1200);
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(SetUsernamePanel.class.getResource("roomsPanel.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RoomsPanelController roomsPanelController = loader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setUserData(username);
            stage.setTitle("Pokoje");
            stage.setScene(new Scene(root, 900,600));
            stage.show();


        });

    }
}
