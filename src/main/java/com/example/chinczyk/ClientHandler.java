package com.example.chinczyk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler {
    protected Socket client;
    protected PrintWriter out;// to co przyszło z socketu trafia do klienta
    protected BufferedReader in; // to co ma wysłać trafia na socket

    public ClientHandler(Socket client) {
        this.client = client;
        try {
            this.out = new PrintWriter(client.getOutputStream()); // wysylanie
            this.in = new BufferedReader(new InputStreamReader(client.getInputStream())); // odbieranie
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
