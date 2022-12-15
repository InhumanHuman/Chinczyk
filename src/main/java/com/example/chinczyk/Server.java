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
            List<List<Integer>> playersInRooms;
            List<ClientHandler> clients;
            Socket client;
            ServerSocket server;
            server = new ServerSocket(1200);
            System.out.println("New server initialized!");

            clients = Collections.synchronizedList(new ArrayList<>());
            playersInRooms = Collections.synchronizedList(new ArrayList<>());
            for (int i = 0; i < 10; i++) {
                List<Integer> roomList = Collections.synchronizedList(new ArrayList<>());
                playersInRooms.add(roomList);
            }
            while(true)
            {
                client = server.accept();
                System.out.println("Ustanowiono polaczenie");
                ServerThread st = new ServerThread(client, clients, playersInRooms);
                st.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ServerThread extends Thread
{
    private List<List<Integer>> playersInRooms;
    private List<ClientHandler> clients;
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    ServerThread(Socket client, List<ClientHandler> clients, List<List<Integer>> playersInRooms)
    {
        this.client = client;
        this.clients = clients;
        this.playersInRooms = playersInRooms;
    }

    public void run() {
        String line;
        try {
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                line = in.readLine();
                System.out.println(line);
                out.println("Test polaczenia #1");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ClientHandler newClient = new ClientHandler(client);
                clients.add(newClient);
                // tutaj trzeba czekac na moment az cos przyjdzie do serwera od klienta
               //new SendMessage(clients);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

