package com.example.chinczyk;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientGame
{
    protected Socket client;
    protected PrintWriter out;
    protected BufferedReader in;
    protected String username;
    protected int ID;
    protected int tourNumber;
    protected int diceRollNumber;
    protected ObservableList<String> DiceValueList = FXCollections.observableArrayList();
    protected String color;


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
            this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
            this.username = username;
            this.ID = ID;
            this.color = pickColor(ID);

            ClientGameThread CGT = new ClientGameThread(client, username, this);
            CGT.start();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(String message)
    {
        System.out.println("Wysylam wiadomosc");
        out.println(message);
        out.flush();
    }
    public String pickColor(int ID) {
        switch(ID) {
            case 0:
                return "red";
            case 1:
                return "green";
            case 2:
                return "blue";
            case 3:
                return "yellow";
        }
        return null;
    }

}

class ClientGameThread extends Thread
{
    BufferedReader in;
    PrintWriter out;
    Socket client;
    String username;
    ClientGame clientGame;

    ClientGameThread(Socket client, String username, ClientGame clientGame)
    {
        try {
            this.client = client;
            this.username = username;
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream());
            this.clientGame = clientGame;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        final String tourEnd = "tourEnd";
        final String diceRoll = "diceRoll";
        while(true)
        {
            try {
                System.out.println("czekam na info od serwera" + username);
                String gotInfo = in.readLine();
                System.out.println("gotInfo: " + gotInfo);
                String[] splited = gotInfo.split(",");
                if(splited[0].equals(tourEnd))
                {
                    if(splited[1].equals(diceRoll))
                    {
                        int diceRollNumber = Integer.parseInt(splited[2]);
                        System.out.println("WYLOSOWANA LICZBA: " + diceRollNumber);
                        int tourNumber = Integer.parseInt(splited[3]);
                        System.out.println("Aktualizuje liste");

                        clientGame.diceRollNumber = diceRollNumber;
                        clientGame.tourNumber = tourNumber;
                        clientGame.DiceValueList.add("diceRoll," + diceRollNumber);
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
