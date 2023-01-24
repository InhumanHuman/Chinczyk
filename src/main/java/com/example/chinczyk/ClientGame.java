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
    protected int turnNumber;
    protected int diceRollNumber;
    protected ObservableList<String> DiceValueList = FXCollections.observableArrayList();
    protected ObservableList<String> PawnsValueList = FXCollections.observableArrayList();
    protected ObservableList<String> PawnsBeaten = FXCollections.observableArrayList();
    protected ObservableList<String> TurnEnded = FXCollections.observableArrayList();
    protected Field oldPosition;
    protected Field newPosition;
    protected String color;
    protected int fromWho;
    protected String beatenPawn;
    protected String beatenPawnColor;
    protected int turnInfo;


    public int getID()
    {
        return ID;
    }

    public ClientGame(String hostName, int ip, String username, int ID)
    {
        try {
            this.client = new Socket(hostName, ip);
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
        final String turnEnd = "turnEnd";
        final String diceRoll = "diceRoll";
        final String pawnMoved = "pawnMoved";
        final String pawnBeaten = "pawnBeaten";
        final int PLAYER_COUNT = 2;
        while(true)
        {
            try {
                String gotInfo = in.readLine();
                String[] splited = gotInfo.split(",");

                if(splited[0].equals(diceRoll))
                {
                    int diceRollNumber = Integer.parseInt(splited[1]);

                    clientGame.diceRollNumber = diceRollNumber;
                    clientGame.DiceValueList.add("UPDATE");
                }
                if(splited[0].equals(turnEnd))
                {
                    int turnNumber = Integer.parseInt(splited[1]);
                    clientGame.turnNumber = turnNumber;
                    clientGame.turnInfo = turnNumber % PLAYER_COUNT;

                    clientGame.TurnEnded.add("UPDATE");
                }
                if(splited[0].equals(pawnMoved))
                {
                    int X_1 = Integer.parseInt(splited[1]);
                    int Y_1 = Integer.parseInt(splited[2]);
                    int X_2 = Integer.parseInt(splited[3]);
                    int Y_2 = Integer.parseInt(splited[4]);

                    clientGame.oldPosition = new Field(X_1, Y_1);
                    clientGame.newPosition = new Field(X_2, Y_2);
                    clientGame.fromWho = Integer.parseInt(splited[5]);

                    clientGame.PawnsValueList.add("UPDATE");
                }
                if(splited[0].equals(pawnBeaten))
                {
                    clientGame.beatenPawn = splited[1];
                    clientGame.beatenPawnColor = splited[2];

                    clientGame.PawnsBeaten.add("UPDATE");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
