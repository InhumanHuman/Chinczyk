package com.example.chinczyk;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomsPanelController implements Initializable {

    private String clientName;


    @FXML
    Text usernameText;
    @FXML
    Button joinButton1, joinButton2, joinButton3, joinButton4, joinButton5;
    @FXML
    Button joinButton6, joinButton7, joinButton8, joinButton9, joinButton10;
    @FXML
    ListView playersListView10;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public void joinRoom(ActionEvent event) {

        //klient nacisnal na join room
        //przeslij do ktorego pokoju doszedl czyli jakies Client -> sendMessage()

        Button button = (Button) event.getSource();
        String ID = button.getId();
        ObservableList<String> listOfPlayers = FXCollections.observableArrayList();

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        String name = stage.getUserData().toString();
        usernameText.setText(name);

        listOfPlayers.add(name);

        playersListView10.setItems(listOfPlayers);
    }
}
