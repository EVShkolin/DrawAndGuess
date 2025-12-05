package ru.kpfu.drawandguess.client.controller;

import lombok.Setter;
import ru.kpfu.drawandguess.common.protocol.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

@Setter
public class NetworkController {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private GameController gameController;

    public void connect(InetAddress host, int port) {
        try {
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            new Thread(this::receiveMessages).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void receiveMessages() {
        while (!socket.isClosed()) {
            try {
                Message message = (Message) in.readObject();
                gameController.handleMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sendMessage(Message message) {
        try {
            System.out.println("Message sent " + message.getType());
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
