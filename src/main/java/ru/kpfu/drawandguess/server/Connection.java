package ru.kpfu.drawandguess.server;

import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.model.ChatMessage;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;


@Getter
@Setter
public class Connection implements Runnable {

    private String id;
    private String username;
    private Socket socket;
    private RoomManager roomManager;
    private GameRoom gameRoom;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Connection(Socket socket, RoomManager roomManager, String username) throws IOException {
        this.id = UUID.randomUUID().toString();
        this.socket = socket;
        this.roomManager = roomManager;
        this.username = username;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = (Message) in.readObject();
                handleMessage(message, this);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleMessage(Message message, Connection author) {
        switch (message.getType()) {
            case DRAWING -> gameRoom.handleDrawingMessage((DrawingMessage) message, author);
            case CHAT -> gameRoom.handleChatMessage((ChatMessage) message, author);
            case GAME -> gameRoom.handleGameMessage(message, author);
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
