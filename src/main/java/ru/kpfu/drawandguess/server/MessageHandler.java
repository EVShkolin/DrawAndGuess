package ru.kpfu.drawandguess.server;

import lombok.AllArgsConstructor;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.game.GameSyncMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class MessageHandler {

    private List<Connection> connections;
    private List<List<Point>> lines;
    private List<Point> currentLine;

    public MessageHandler(List<Connection> connections) {
        this.connections = connections;
        this.lines = new ArrayList<>();
    }

    public void handleMessage(DrawingMessage message, String authorName) {
        switch (message.getDrawingType()) {
            case PRESS -> handlePressMessage(message);
            case DRAG -> handleDragMessage(message);
        }
        broadcastMessage(message, authorName);
    }

    private void broadcastMessage(DrawingMessage message, String authorName) {
        for (Connection connection : connections) {
            if (!connection.getUsername().equals(authorName)) {
                System.out.println("Message sent to " + connection.getUsername());
                connection.sendMessage(message);
            }
        }
    }

    private void handlePressMessage(DrawingMessage message) {
        currentLine = new ArrayList<>();
        lines.add(currentLine);
        currentLine.add(message.getPoint());
    }

    private void handleDragMessage(DrawingMessage message) {
        currentLine.add(message.getPoint());
    }

    public void sendSyncMessage(Connection connection) {
        connection.sendMessage(new GameSyncMessage(lines));
    }
}
