package ru.kpfu.drawandguess.server;

import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.model.GameState;
import ru.kpfu.drawandguess.common.model.Line;
import ru.kpfu.drawandguess.common.model.Player;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.chat.ChatMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.game.PlayerPresenceMessage;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameRoom {

    private String id;
    private String title;
    private Connection creator;
    private List<Connection> players = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private GameState gameState;
    private int roundTimeLeft;
    private Connection currentDrawer;
    private String currentWord;
    private int round;

    public GameRoom(String id, String title) {
        this.id = id;
        this.title = title;
        this.gameState = GameState.WAITING;
        this.round = 1;
    }

    public void addPlayer(Connection player) {
        if (players.isEmpty()) {
            this.creator = player;
        }
        players.add(player);
        broadcastExcludingAuthor(
                new PlayerPresenceMessage(true, new Player(player.getId(), player.getUsername(), 0)),
                player
        );
    }

    public void removePlayer(Connection player) {
        players.remove(player);
        broadcastExcludingAuthor(
                new PlayerPresenceMessage(false, new Player(player.getId(), player.getUsername(), player.getScore())),
                player
        );
    }

    public void handleDrawingMessage(DrawingMessage message, Connection author) {
        switch (message.getDrawingType()) {
            case PRESS -> {
                Line newLine = new Line(message.getBrushSize(), message.getColor());
                newLine.addPoint(message.getPoint());
                lines.add(newLine);
            }
            case DRAG -> {
                lines.getLast().addPoint(message.getPoint());
            }
        }
        broadcastExcludingAuthor(message, author);
    }

    public void handleChatMessage(ChatMessage message) {
        System.out.println("New chat message received " + message.getText());
        chatMessages.add(message);
        broadcastAll(message);
    }

    public void handleGameMessage(Message message) {}

    public void broadcastAll(Message message) {
        for (Connection player : players) {
            System.out.println("Message sent ");
            player.sendMessage(message);
        }
    }

    public void broadcastExcludingAuthor(Message message, Connection author) {
        String authorId = author.getId();
        for (Connection player : players) {
            if (!player.getId().equals(authorId)) {
                player.sendMessage(message);
            }
        }
    }
}
