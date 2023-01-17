package com.example.chinczyk;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {


    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ludo_db", "root", "root");

            List<Socket> clients;
            Socket client;
            ServerSocket server;
            server = new ServerSocket(1200);
            System.out.println("New server initialized!");

            clients = Collections.synchronizedList(new ArrayList<>());

            while(true)
            {
                client = server.accept();
                clients.add(client);
                System.out.println("Ustanowiono polaczenie");
                ServerThread st = new ServerThread(client);
                st.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ServerThread extends Thread
{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ObjectInputStream in_o;
    final String joined_room = "Joined_Room";
    final String start_game = "StartGame";

    ServerThread(Socket client)
    {
        this.client = client;
    }

    public void run() {
        // TODO - poakzanie przez watek nowemu klientowi klientów ktorzy sa juz w pokojach
        // TODO - sprawdzanie czy klient juz taki istnieje pdoczas podawania nicku
        String line;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
            this.in_o = new ObjectInputStream(new BufferedInputStream(this.client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                for(List<String> item : PlayersInRooms.playersInRooms)
                {
                    System.out.println(item);
                }
                line = in.readLine();


                String splited[] = line.split(",");

                String msg_p1 = splited[0];
                String msg_p2 = splited[1];
                String msg_p3 = splited[2];

                if(msg_p1.equals(joined_room))
                {
                    int room = Integer.parseInt(msg_p2);
                    PlayersInRooms.playersInRooms.get(room).add(msg_p3);

                    RoomsPanelController roomsPanelController = new RoomsPanelController();

                    roomsPanelController.updateRooms();
                }
                else if(msg_p1.equals(start_game))
                {
                    ArrayList<ArrayList<Client>> clients = (ArrayList<ArrayList<Client>>) in_o.readObject();

                    System.out.println(clients.get(0).get(0).getUsername());
                }


            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
class ServerGameThread extends Thread
{

}

