package ru.kpfu.drawandguess.client;

import ru.kpfu.drawandguess.common.protocol.DrawingMessage;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DrawingBoard drawingBoard;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.drawingBoard = createDrawingBoard(this);
    }

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 8888);
        Client client = new Client(socket);
        client.listenForMessages();
    }

    public void sendMessage(DrawingMessage message) {
        System.out.println("Sending new message x: " + message.getPoint().x + " y: " + message.getPoint().y);
        try {
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listenForMessages() {
        System.out.println("Listening for messages");
        new Thread(() -> {
            while (true) {
                try {
                    DrawingMessage message = (DrawingMessage) in.readObject();
                    System.out.println("Received new message with type " + message.getType());
                    switch (message.getType()) {
                        case PRESS -> drawingBoard.handleMousePressed(message.getPoint());
                        case DRAG -> drawingBoard.handleMouseDragged(message.getPoint());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private DrawingBoard createDrawingBoard(Client client) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setVisible(true);

        DrawingBoard drawingBoard = new DrawingBoard(client);
        frame.add(drawingBoard);
        return drawingBoard;
    }

}
