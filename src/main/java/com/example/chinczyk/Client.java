package com.example.chinczyk;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {
    protected Socket client;
    protected BufferedReader in;
    protected PrintWriter out;
    protected ObjectOutputStream out_o;
    protected String username;

    public Client(String hostName, int ip, String username)
    {
        try {
            this.client = new Socket(hostName, ip);
            System.out.println(client.getLocalSocketAddress());
            System.out.println(client.getLocalPort());
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
            this.out_o = new ObjectOutputStream(client.getOutputStream());
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
    public void sendToServerObject(ArrayList<Client> clients)
    {
        try {
            out_o.writeObject(clients);
            out_o.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void closeConnection()
    {
        try {
            out_o.close();
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}