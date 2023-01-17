package com.example.chinczyk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        try {
            // Stworzenie pustej tablicy 2 wymiarowej pokoi

            PlayersInRooms playersInRooms = new PlayersInRooms();
            playersInRooms.addLists();

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
                ServerThread st = new ServerThread(client, clients);
                st.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ServerThread extends Thread
{
    private List<Socket> clients;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    final String joined_room = "Joined_Room";

    ServerThread(Socket client, List<Socket> clients)
    {
        this.client = client;
        this.clients = clients;
    }

    public void run() {
        // TODO - poakzanie przez watek nowemu klientowi klientów ktorzy sa juz w pokojach
        // TODO - sprawdzanie czy klient juz taki istnieje pdoczas podawania nicku
        String line;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
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
                    System.out.println("WCHODZE");
                    int room = Integer.parseInt(msg_p2);
                    PlayersInRooms.playersInRooms.get(room).add(msg_p3);

                    RoomsPanelController roomsPanelController = new RoomsPanelController();

                    roomsPanelController.updateRooms();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

