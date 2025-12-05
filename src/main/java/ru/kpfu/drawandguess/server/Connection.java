package ru.kpfu.drawandguess.server;

import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


@Getter
@Setter
public class Connection implements Runnable {

    private Socket socket;

    private Server server;

    private MessageHandler messageHandler;

    private String username;

    private ObjectInputStream in;

    private ObjectOutputStream out;

    public Connection(Socket socket, Server server, MessageHandler messageHandler, String username) throws IOException {
        this.socket = socket;
        this.server = server;
        this.messageHandler = messageHandler;
        this.username = username;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                DrawingMessage message = (DrawingMessage) in.readObject();
                System.out.println("Server received new message from " + username);
                messageHandler.handleMessage(message, this.username);

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void sendMessage(Object message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
