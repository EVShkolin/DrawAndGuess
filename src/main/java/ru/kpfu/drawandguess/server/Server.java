package ru.kpfu.drawandguess.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private List<Connection> connections;

    private RoomManager roomManager;


    public Server() {
        this.connections = new ArrayList<>();
        this.roomManager = new RoomManager();
    }

    public void init() {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            System.out.println("Server IP: " + InetAddress.getLocalHost().getHostAddress());
            while (true) {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, roomManager);
                connections.add(connection);
                new Thread(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }

}
