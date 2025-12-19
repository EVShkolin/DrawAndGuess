package ru.kpfu.drawandguess.server;

import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.chat.ChatMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.system.ApproveUsernameMessage;

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
    private int score;
    private Socket socket;
    private RoomManager roomManager;
    private GameRoom gameRoom;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Connection(Socket socket, RoomManager roomManager) throws IOException {
        this.id = UUID.randomUUID().toString();
        this.socket = socket;
        this.roomManager = roomManager;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            ChatMessage usernameRequest = new ChatMessage("System", "Введите имя");
            out.writeObject(usernameRequest);
            while (username == null) {
                Message newMessage = (Message) in.readObject();
                if (!(newMessage instanceof ChatMessage message)) {
                    continue;
                }
                if (message.getText().equals("System")) {
                    ChatMessage forbiddenUsername = new ChatMessage("System", "Это имя нельзя использовать");
                    out.writeObject(forbiddenUsername);
                } else {
                    this.username = message.getText();
                    out.writeObject(new ApproveUsernameMessage(this.username));
                    System.out.println("New user: " + username);
                    roomManager.addPlayerToRoom(this);
                }
            }

            while (true) {
                Message message = (Message) in.readObject();
                handleMessage(message, this);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(username + " has left");
            gameRoom.removePlayer(this);
        }

    }

    private void handleMessage(Message message, Connection author) {
        switch (message.getType()) {
            case DRAWING -> gameRoom.handleDrawingMessage((DrawingMessage) message, author);
            case CHAT -> gameRoom.handleChatMessage((ChatMessage) message);
            case GAME -> gameRoom.handleGameMessage(message);
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
