package com.example.chinczyk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class NetworkClient {

    String name;

    public NetworkClient(String name) throws IOException {
        this.name = name;
        doConnection();
    }

    public void doConnection() throws IOException {

        Socket s1 = null;
        String line;
        BufferedReader is = null;
        PrintWriter os = null;
        Boolean end_flag = false;

        try {

            s1 = new Socket("127.0.0.1", 4445);
            is = new BufferedReader(new InputStreamReader(s1.getInputStream())); // czytanie co przyszło od serwera
            os = new PrintWriter(s1.getOutputStream()); // wysyłanie wiadomości do serwera
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Client Address : " + "127.0.0.1");
        System.out.println("Client name: " + name);
        System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

        //line = br.readLine(); // czytanie co zostało napisane w konsoli #1
        System.out.println(name);

    }
}