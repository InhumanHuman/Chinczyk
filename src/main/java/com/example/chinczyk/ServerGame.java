package com.example.chinczyk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerGame {
    final int PLAYERS_COUNT = 2;

    public ServerGame(int port) {
        ServerSocket serverSocket;
        HashMap<String,ClientHandler> sockets = new HashMap<>();
        ArrayList<ClientHandler> clientList = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Czekanie na 4 graczy
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            try {
                Socket client = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(client);
                clientList.add(clientHandler);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // Wystartowanie gry po dodaniu graczy
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root")) {
            PreparedStatement psSelect = connection.prepareStatement("SELECT GROUP_CONCAT(name) as 'names' FROM users WHERE room_id = ? - 1400");
            psSelect.setInt(1, port);
            ResultSet resultSet = psSelect.executeQuery();
            resultSet.next();
            String usernames = resultSet.getString("names");

            String[] unames = usernames.split(",");
            for(int i = 0; i < PLAYERS_COUNT; i++) {
                sockets.put(unames[i],clientList.get(i));
            }

            // Utworzenie wątku do obsługi gry
            ServerGameThread SGT = new ServerGameThread(sockets);
            SGT.start();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

// Thread do obsługi gry
class ServerGameThread extends Thread {
    HashMap<String, ClientHandler> sockets;
    final String turnEnd = "turnEnd";
    final String diceRoll = "diceRoll";
    final String pawnMoved = "pawnMoved";
    final String pawnBeaten = "pawnBeaten";
    final String gameEnded = "gameEnded";
    boolean gameEndedFlag = false;
    final int PLAYERS_COUNT = 2;
    public ServerGameThread(HashMap<String, ClientHandler> sockets) {
        this.sockets = sockets;
    }

    @Override
    public void run() {
        ArrayList<String> usernames = new ArrayList<>();
        for (String key : sockets.keySet())
        {
            usernames.add(key);
        }
        int turnNumber = 0;
        while(!gameEndedFlag)
        {
            try {
                // Kogo nasluchujemy
                int whoToListen = turnNumber % PLAYERS_COUNT;

                // Nasluchiwanie goscia ktory wszedl jako n-ty
                String message = sockets.get(usernames.get(whoToListen)).in.readLine();

                String[] splitted = message.split(",");

                sendBroadcast("turnInfo," + whoToListen);

                //PRZYKŁAD : diceRoll,4
                if(splitted[0].equals(diceRoll))
                {
                    String newMSG = diceRoll + "," + splitted[1];

                    //Wysłanie broadcast do wszystkich o nowej wartości kości
                    sendBroadcast(newMSG);

                }
                //PRZYKŁAD: pawnMoved,1,2,5,1,3
                // 1,2 - STARA POZYCJA
                // 5,1 - NOWA POZYCJA
                // 3 - ID Gracza
                if(splitted[0].equals(pawnMoved))
                {
                    sendBroadcast(message);
                }
                //PRZYKŁAD: tourEnd
                if (splitted[0].equals(turnEnd))
                {
                    turnNumber++;

                    String newMSG = turnEnd + "," + turnNumber;
                    sendBroadcast(newMSG);

                }
                if(splitted[0].equals(pawnBeaten))
                {
                    sendBroadcast(message);
                }
                if(splitted[0].equals(gameEnded))
                {
                    gameEndedFlag = true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void sendBroadcast(String newMSG)
    {
        for (String key: sockets.keySet())
        {
            sockets.get(key).out.println(newMSG);
            sockets.get(key).out.flush();
        }
    }
}
