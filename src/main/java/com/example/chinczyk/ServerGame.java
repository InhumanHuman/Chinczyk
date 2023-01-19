package com.example.chinczyk;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerGame {

    public ServerGame(int port) {
        ServerSocket serverSocket = null;
        HashMap<String,ClientHandler> sockets = new HashMap<>();
        ArrayList<ClientHandler> clientList = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Czekanie na 4 graczy
        for (int i = 0; i < 2; i++) {
            System.out.println("CZEKAM NA KOGOS");
            try {
                Socket client = serverSocket.accept();

                ClientHandler clientHandler = new ClientHandler(client);
                clientList.add(clientHandler);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("WSZEDLEM");
        }
        // Wystartowanie gry po dodaniu graczy
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root")) {
            PreparedStatement psSelect = connection.prepareStatement("SELECT GROUP_CONCAT(name) as 'names' FROM users WHERE room_id = ? - 1400");
            psSelect.setInt(1, port);
            ResultSet resultSet = psSelect.executeQuery();
            resultSet.next();
            String usernames = resultSet.getString("names");
            String[] unames = usernames.split(",");
            for(int i = 0; i < 2; i++) {
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
    final String tourEnd = "tourEnd";
    final String diceRoll = "diceRoll";
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
        while(true)
        {
            try {
                // Kogo nasluchujemy
                int whoToListen = turnNumber % 2;
                System.out.println("who to listen: " + whoToListen);

                // Nasluchiwanie goscia ktory wszedl jako n-ty
                String message = sockets.get(usernames.get(whoToListen)).in.readLine();

                String[] splitted = message.split(",");

                if(splitted[0].equals(tourEnd))
                {
                    if(splitted[1].equals(diceRoll))
                    {
                        turnNumber++;
                        String newMSG = tourEnd + "," + diceRoll + "," + splitted[2] + "," + turnNumber;
                        for (String key: sockets.keySet())
                        {
                            System.out.println(key);
                            System.out.println(sockets.get(key));
                            /**
                             * Wysłanie broadcast o nowej turze
                             */
                            sockets.get(key).out.println(newMSG);
                            //sockets.get(key).out.println(newMSG);
                            sockets.get(key).out.flush();

                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
