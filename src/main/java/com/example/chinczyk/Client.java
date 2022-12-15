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

    public static void main(String[] args) {
        new Client("127.0.0.1", 1200);
    }

    public Client(String hostName, int ip) {
        String line;
        try {
            this.client = new Socket(hostName, ip);
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
            while (true) {
                out.println("Test polaczenia #2");
                out.flush();
                line = in.readLine();
                System.out.println(line);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}