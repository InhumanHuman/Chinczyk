package com.example.chinczyk;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientGame
{
    protected Socket client;

    protected String username;
    protected int ID;

    public int getID()
    {
        return ID;
    }

    public ClientGame(String hostName, int ip, String username, int ID)
    {
        try {
            System.out.println(hostName);
            System.out.println(ip);
            this.client = new Socket(hostName, ip);
            System.out.println("User joins game session");

            this.username = username;
            this.ID = ID;

            ClientGameThread clientGameThread = new ClientGameThread(client, username);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientGameThread extends Thread
{
    BufferedReader in;
    PrintWriter out;
    Socket client;
    String username;

    ClientGameThread(Socket client, String username)
    {
        try {
            this.client = client;
            this.username = username;
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        
        while(true)
        {
            try {
                in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
