package org.example.cinema_app;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    static ServerSocket serverSocket;
    private static void startConnection() throws IOException{
        serverSocket = new ServerSocket(4450);
        System.out.println("server started: waiting for client to connect");
    }
    private static void connectToClient() throws IOException{
        while (true) {
            Socket newClient = serverSocket.accept();
            System.out.println("client connected");
            ServiceHandler service = new ServiceHandler(newClient);
            service.start();
        }
    }
    private static void closeConnection() throws IOException{
        serverSocket.close();
        System.out.println("client disconnected");
    }
    public static void main(String[] args) {
        try{
            startConnection();
            connectToClient();
            closeConnection();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }
}
