package com.example.chinczyk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        for (int i = 0; i < 4; i++) {
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
            for(int i = 0; i < 4; i++) {
                sockets.put(unames[i],clientList.get(i));
            }

            // Utworzenie wątku do obsługi gry
            new ServerGameThread(sockets);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

// Thread do obsługi gry
class ServerGameThread extends Thread {
    HashMap<String, ClientHandler> sockets;
    public ServerGameThread(HashMap<String, ClientHandler> sockets) {
        this.sockets = sockets;
    }

    @Override
    public void run() {
        int turnNumber = 0;
        while(true)
        {
            turnNumber++;


        }
    }
}
