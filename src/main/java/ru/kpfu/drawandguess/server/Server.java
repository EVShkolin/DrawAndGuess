package ru.kpfu.drawandguess.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static List<Connection> connections;

    private static MessageHandler messageHandler;


    public Server() {
        this.connections = new ArrayList<>();
        this.messageHandler = new MessageHandler(connections);
    }

    public void init() {
        try (ServerSocket serverSocket = new ServerSocket(8888)) {
            while (true) {
                Socket socket = serverSocket.accept();
                Connection connection = new Connection(socket, this, messageHandler, socket.getPort() + "");

                new Thread(connection).start();
                connections.add(connection);
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
