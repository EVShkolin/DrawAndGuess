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
            String gameRoomId = connectFirstPlayer(serverSocket).getId(); // remove later
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");
                Connection connection = new Connection(socket, roomManager, socket.getPort() + "");
                connections.add(connection);
                roomManager.addPlayerToRoom(connection, gameRoomId);
                new Thread(connection).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GameRoom connectFirstPlayer(ServerSocket serverSocket) throws IOException { // temporary method
        Socket socket = serverSocket.accept();
        Connection connection = new Connection(socket, roomManager, socket.getPort() + "");
        connections.add(connection);
        new Thread(connection).start();
        return roomManager.createRoom("Test Room", connection);
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.init();
    }

}
