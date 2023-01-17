package com.example.chinczyk;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    protected Socket client;
    protected BufferedReader in;
    protected PrintWriter out;
    protected String username;

    public Client(String hostName, int ip, String username)
    {
        try {
            this.client = new Socket(hostName, ip);
            System.out.println(client.getInetAddress());
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
            this.username = username;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getUsername() {
        return username;
    }

    public void sendToServer(String message)
    {
        out.println(message);
        out.flush();
    }
    public void sendToServerObject(ArrayList<ArrayList<Client>> clients)
    {
        out.println(clients);
        out.flush();
    }
}