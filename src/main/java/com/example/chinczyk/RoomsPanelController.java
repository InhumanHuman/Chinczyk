package com.example.chinczyk;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class RoomsPanelController implements Initializable {

    private String clientName;


    @FXML
    Button joinButton0, joinButton1, joinButton2, joinButton3, joinButton4;
    @FXML
    Button joinButton5, joinButton6, joinButton7, joinButton8, joinButton9;
    @FXML
    ListView playersListView0, playersListView1, playersListView2, playersListView3, playersListView4;
    @FXML
    ListView playersListView5, playersListView6, playersListView7, playersListView8, playersListView9;

    public void updateRooms() {
        playersListView0.setItems(PlayersInRooms.playersInRooms.get(0));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    public void joinRoom(ActionEvent event) {

        //klient nacisnal na join room
        //przeslij do ktorego pokoju doszedl czyli jakies Client -> sendMessage()

        Button button = (Button) event.getSource();
        String ID = button.getId();

        String room_number = ID.replaceAll("([A-Za-z])", "");



        ObservableList<String> listOfPlayers = FXCollections.observableArrayList();

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        Client client = (Client) stage.getUserData();

        String message = "Joined_Room," + room_number + "," + client.getUsername();
        client.sendToServer(message);


    }
}
