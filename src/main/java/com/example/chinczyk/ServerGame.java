package com.example.chinczyk;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.*;
import java.util.ArrayList;

public class ServerGame {

    public ServerGame(int port)
    {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < 4; i++)
        {
            System.out.println("CZEKAM NA KOGOS");
            try {
                serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root")) {
                PreparedStatement psSelect = connection.prepareStatement("SELECT GROUP_CONCAT(name) as 'names' FROM users WHERE room_id = ? - 1400");
                psSelect.setInt(1,port);
                ResultSet resultSet = psSelect.executeQuery();
                resultSet.next();
                String usernames = resultSet.getString("names");

                ServerGameThread sgt = new ServerGameThread(usernames);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }



            System.out.println("WSZEDLEM");
        }
    }

    private void startGame() {
    }
}
class ServerGameThread extends Thread {
    String usernames;
    public ServerGameThread(String usernames) {
        this.usernames = usernames;
        System.out.println(usernames);
    }
}
