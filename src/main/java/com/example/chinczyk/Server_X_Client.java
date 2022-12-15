package com.example.chinczyk;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server_X_Client {
    public static void main(String args[]){


        Socket s;
        ServerSocket ss2=null;
        System.out.println("Server Listening......");
        try{
            ss2 = new ServerSocket(4445); // can also use static final PORT_NUM , when defined

        }
        catch(IOException e){
            e.printStackTrace();
            System.out.println("Server error");

        }

        while(true){
            try{
                s= ss2.accept();
                System.out.println("connection Established");
                ServerThread st=new ServerThread(s);
                st.start();

            }

            catch(Exception e){
                e.printStackTrace();
                System.out.println("Connection Error");

            }
        }

    }

}

class ServerThread extends Thread{

    String line = null;
    BufferedReader is = null;
    PrintWriter os = null;
    Socket s;

    public ServerThread(Socket s){
        this.s=s;
    }

    public void run() {
        while(true)
        {
        }
    }
}