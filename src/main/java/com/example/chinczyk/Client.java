package com.example.chinczyk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    protected Socket client;
    protected BufferedReader in;
    protected PrintWriter out;
    protected String username;

    public Client(String hostName, int ip, String username)
    {

        try {
            this.client = new Socket(hostName, ip);
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
    public void readFromServer()
    {

    }
}