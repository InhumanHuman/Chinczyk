package com.example.chinczyk;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class RoomsPanelController implements Initializable {

    private String clientName;
    private Client client;

    @FXML
    Button joinButton0, joinButton1, joinButton2, joinButton3, joinButton4;
    @FXML
    Button joinButton5, joinButton6, joinButton7, joinButton8, joinButton9;
    @FXML
    Button refreshButton;

    @FXML
    Button readyButton0, readyButton1, readyButton2, readyButton3, readyButton4;
    @FXML
    Button readyButton5, readyButton6, readyButton7, readyButton8, readyButton9;
    @FXML
    ListView playersListView0, playersListView1, playersListView2, playersListView3, playersListView4;
    @FXML
    ListView playersListView5, playersListView6, playersListView7, playersListView8, playersListView9;
    @FXML
    Label errorMessage;
    ArrayList<ArrayList<Client>> listOfClients = new ArrayList<>();

    public void updateRooms() {
        playersListView0.setItems(PlayersInRooms.playersInRooms.get(0));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        for (int i = 0; i < 10; i++) {
            ArrayList<Client> newListClients = new ArrayList<>();
            listOfClients.add(newListClients);
        }
    }

    public ListView returnCorrectList(int room_nr) {
        switch(room_nr) {
            case 0:
                return playersListView0;
            case 1:
                return playersListView1;
            case 2:
                return playersListView2;
            case 3:
                return playersListView3;
            case 4:
                return playersListView4;
            case 5:
                return playersListView5;
            case 6:
                return playersListView6;
            case 7:
                return playersListView7;
            case 8:
                return playersListView8;
            case 9:
                return playersListView9;
            default:
                return null;
        }
    }

    public Button returnCorrectButton(int room_nr) {
        switch(room_nr) {
            case 0:
                return readyButton0;
            case 1:
                return readyButton1;
            case 2:
                return readyButton2;
            case 3:
                return readyButton3;
            case 4:
                return readyButton4;
            case 5:
                return readyButton5;
            case 6:
                return readyButton6;
            case 7:
                return readyButton7;
            case 8:
                return readyButton8;
            case 9:
                return readyButton9;
            default:
                return null;
        }
    }

    public void refreshRooms(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root")) {
            PreparedStatement psSelect = connection.prepareStatement("SELECT GROUP_CONCAT(name) as 'names', room_id FROM users GROUP BY room_id");
            ResultSet resultSet = psSelect.executeQuery();

            // TODO - zoptymalizowac kiedys (czytaj nigdy)
            while (resultSet.next()) {
                ObservableList<String> usersInRoom = FXCollections.observableArrayList();
                int room_id = resultSet.getInt("room_id");
                String names = resultSet.getString("names");
                String [] namesList = names.split(",");
                ListView room = returnCorrectList(room_id);
                for(String elem: namesList) {
                    usersInRoom.add(elem);
                }
                room.setItems(usersInRoom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void leaveRoom(String username, int roomNumber) {
            if(roomNumber != 0) {
                ObservableList<String> list0 = playersListView0.getItems();
                if (list0.contains(username)) list0.removeAll(username);
            }
            if(roomNumber != 1) {
                ObservableList<String> list1 = playersListView1.getItems();
                if (list1.contains(username)) list1.removeAll(username);
            }
            if(roomNumber != 2) {
                ObservableList<String> list2 = playersListView2.getItems();
                if (list2.contains(username)) list2.removeAll(username);
            }
            if(roomNumber != 3) {
                ObservableList<String> list3 = playersListView3.getItems();
                if (list3.contains(username)) list3.removeAll(username);
            }
            if(roomNumber != 4) {
                ObservableList<String> list4 = playersListView4.getItems();
                if (list4.contains(username)) list4.removeAll(username);
            }
            if(roomNumber != 5) {
                ObservableList<String> list5 = playersListView5.getItems();
                if (list5.contains(username)) list5.removeAll(username);
            }
            if(roomNumber != 6) {
                ObservableList<String> list6 = playersListView6.getItems();
                if (list6.contains(username)) list6.removeAll(username);
            }
            if(roomNumber != 7) {
                ObservableList<String> list7 = playersListView7.getItems();
                if (list7.contains(username)) list7.removeAll(username);
            }
            if(roomNumber != 8) {
                ObservableList<String> list8 = playersListView8.getItems();
                if (list8.contains(username)) list8.removeAll(username);
            }
            if(roomNumber != 9) {
                ObservableList<String> list9 = playersListView9.getItems();
                if (list9.contains(username)) list9.removeAll(username);
            }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root")) {
            PreparedStatement getUserOccurence = connection.prepareStatement("SELECT room_id FROM users WHERE name = ? AND room_id != ?");
            getUserOccurence.setString(1,username);
            getUserOccurence.setInt(2, roomNumber);

            ResultSet resultSet = getUserOccurence.executeQuery();
            while(resultSet.next()) {
                //System.out.println(resultSet.getInt("room_id"));
                int roomNr = resultSet.getInt("room_id");
                PreparedStatement psUpdateFreeSpots = connection.prepareStatement("UPDATE rooms SET free_spots = free_spots + 1 WHERE room_id = ?");
                psUpdateFreeSpots.setInt(1,roomNr);
                psUpdateFreeSpots.executeUpdate();
            }

            PreparedStatement removeUserOccurence = connection.prepareStatement("DELETE FROM users WHERE name = ? AND room_id != ?");
            removeUserOccurence.setString(1,username);
            removeUserOccurence.setInt(2,roomNumber);
            removeUserOccurence.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
            this.refreshRooms(new ActionEvent());
    }

    public void joinRoom(ActionEvent event) {

        // TODO - Sprawdzanie ilosci osob w pokoju czy nie przekracza 4
        // TODO - opuszczenie pokoju usuwa z bazy gracza
        //klient nacisnal na join room
        //przeslij do ktorego pokoju doszedl czyli jakies Client -> sendMessage()

        Button button = (Button) event.getSource();
        String ID = button.getId();

        int room_number = Integer.parseInt(ID.replaceAll("([A-Za-z])", ""));

        //ObservableList<String> listOfPlayers = FXCollections.observableArrayList();

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        client = (Client) stage.getUserData();

        clientName = client.getUsername();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root")) {
            PreparedStatement psCheck = connection.prepareStatement("SELECT free_spots FROM rooms WHERE room_id = ?");
            psCheck.setInt(1, room_number);
            ResultSet resultSet = psCheck.executeQuery();
            resultSet.next();
            // TODO - do optymalizacji
            int spots = resultSet.getInt("free_spots");
            ObservableList<String> usersInRoomList = returnCorrectList(room_number).getItems();
            if (!usersInRoomList.contains(clientName)) {
                if (spots > 1) {
                    listOfClients.get(room_number).add(client);
                    errorMessage.setText("");
                    leaveRoom(clientName, room_number);
                    Button roomReadyButton = returnCorrectButton(room_number);
                    roomReadyButton.setVisible(true);
                    PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users(name, room_id) VALUES(?,?)");
                    psInsert.setString(1, client.getUsername());
                    psInsert.setInt(2, room_number);
                    psInsert.executeUpdate();

                    PreparedStatement psUpdate = connection.prepareStatement("UPDATE rooms SET free_spots = free_spots - 1 WHERE room_id = ?");
                    psUpdate.setInt(1, room_number);
                    psUpdate.executeUpdate();
                    refreshRooms(event);

                    String message_startgame = "StartGame," + room_number + ", null";
                    client.sendToServer(message_startgame);


                    //client.sendToServerObject(listOfClients.get(room_number));
                } else if (spots == 1) {
                    listOfClients.get(room_number).add(client);
                    Button roomReadyButton = returnCorrectButton(room_number);
                    roomReadyButton.setVisible(true);
                    PreparedStatement psInsert = connection.prepareStatement("INSERT INTO users(name, room_id) VALUES(?,?)");
                    psInsert.setString(1, client.getUsername());
                    psInsert.setInt(2, room_number);
                    psInsert.executeUpdate();

                    PreparedStatement psUpdate = connection.prepareStatement("UPDATE rooms SET free_spots = free_spots - 1 WHERE room_id = ?");
                    psUpdate.setInt(1, room_number);
                    psUpdate.executeUpdate();
                    refreshRooms(event);
                } else {
                    errorMessage.setText("Pokój pełny!");
                }
            }
            } catch(SQLException e){
                throw new RuntimeException(e);
            }
    }

    public void readyForGame(ActionEvent event) {
        Button button = (Button) event.getSource();
        button.setDisable(true);
        String ID = button.getId();


        int room_number = Integer.parseInt(ID.replaceAll("([A-Za-z])", ""));
        int port = 1400 + room_number;


        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root")) {
            PreparedStatement checkGameServers = connection.prepareStatement("SELECT `empty` FROM game_servers WHERE port = ?");
            checkGameServers.setInt(1, 1400 + room_number);
            ResultSet resultSet = checkGameServers.executeQuery();
            resultSet.next();
            int serverStatus = resultSet.getInt("empty");


            if (serverStatus == 1) {
                Thread t = new Thread(() -> {
                    ServerGame serverGame = new ServerGame(port);
                });
                t.start();
                System.out.println("new server for game");
                PreparedStatement updateServerStatus = connection.prepareStatement("UPDATE game_servers SET `empty` = 0 WHERE port = ?");
                updateServerStatus.setInt(1, 1400 + room_number);
                updateServerStatus.executeUpdate();
                client.closeConnection();
                PreparedStatement getClientCount = connection.prepareStatement("SELECT free_spots FROM rooms WHERE room_id = ?");
                getClientCount.setInt(1,room_number);
                ResultSet usersCount = getClientCount.executeQuery();
                usersCount.next();
                int playerID = 4 - usersCount.getInt("free_spots");
                ClientGame clientGame = new ClientGame("127.0.0.1", port, clientName, playerID);

                Parent root = null;
                FXMLLoader loader = new FXMLLoader(RoomsPanelController.class.getResource("GamePanel.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BoardController GamePanelController = loader.getController();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setUserData(clientGame);
                stage.setTitle("Gra");
                stage.setScene(new Scene(root, 600,600));
                stage.show();
            } else {
                System.out.println("joined game");
                client.closeConnection();
                PreparedStatement getClientCount = connection.prepareStatement("SELECT free_spots FROM rooms WHERE room_id = ?");
                getClientCount.setInt(1,room_number);
                ResultSet usersCount = getClientCount.executeQuery();
                usersCount.next();
                int playerID = 4 - usersCount.getInt("free_spots");
                ClientGame clientGame = new ClientGame("127.0.0.1", port, clientName, playerID);

                Parent root = null;
                FXMLLoader loader = new FXMLLoader(RoomsPanelController.class.getResource("GamePanel.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BoardController GamePanelController = loader.getController();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setUserData(clientGame);
                stage.setTitle("Gra");
                stage.setScene(new Scene(root, 600,600));
                stage.show();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
