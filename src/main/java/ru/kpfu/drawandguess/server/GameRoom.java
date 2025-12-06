package ru.kpfu.drawandguess.server;

import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.model.ChatMessage;
import ru.kpfu.drawandguess.common.model.GameState;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameRoom {

    private String id;
    private String title;
    private List<Connection> players = new ArrayList<>();
    private List<List<Point>> lines = new ArrayList<>();
    private List<ChatMessage> messages = new ArrayList<>();
    private GameState gameState;
    private int roundTimeLeft;
    private Connection currentDrawer;
    private String currentWord;
    private int round;

    public GameRoom(String id, String title, Connection creator) {
        this.id = id;
        this.title = title;
        players.add(creator);
        this.gameState = GameState.WAITING;
        this.round = 1;
    }

    public void addPlayer(Connection player) {
        players.add(player);
    }

    public void handleDrawingMessage(DrawingMessage message, Connection author) {
        switch (message.getDrawingType()) {
            case PRESS -> {
                List<Point> newLine = new ArrayList<>();
                newLine.add(message.getPoint());
                lines.add(newLine);
            }
            case DRAG -> {
                lines.getLast().add(message.getPoint());
            }
        }
        broadcastDrawing(message, author);
    }

    public void broadcastDrawing(Message message, Connection author) {
        String authorId = author.getId();
        for (Connection player : players) {
            if (!player.getId().equals(authorId)) {
                player.sendMessage(message);
            }
        }
    }

    public void handleChatMessage(ChatMessage message, Connection author) {

    }

    public void broadcastChat(Message message) {

    }

    public void handleGameMessage(Message message, Connection author) {

    }

}
