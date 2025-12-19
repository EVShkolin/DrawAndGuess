package ru.kpfu.drawandguess.client.UI;

import lombok.Getter;
import ru.kpfu.drawandguess.client.controller.GameController;
import ru.kpfu.drawandguess.common.model.Player;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.chat.ChatMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.game.GameSyncMessage;

import javax.swing.*;
import java.awt.*;

@Getter
public class GameRoomPanel extends JPanel {

    private GameController gameController;

    private DrawingBoard drawingBoard;
    private ChatPanel chat;
    private PlayerListPanel playerList;

    public GameRoomPanel(GameController gameController) {
        this.gameController = gameController;
        this.drawingBoard = new DrawingBoard(this);
        this.chat = new ChatPanel(this);
        this.playerList = new PlayerListPanel();

        this.setBackground(Color.CYAN);
        this.setLayout(new GridBagLayout());

        JPanel container = createTwoPanelsContainer();
        this.add(container, new GridBagConstraints());
    }

    private JPanel createTwoPanelsContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);

        JPanel bigCenterPanel = createBigCenterPanel();
        container.add(bigCenterPanel);

        container.add(Box.createVerticalStrut(5));

        DrawingOptionsPanel drawingOptionsPanel = new DrawingOptionsPanel(
                drawingBoard,
                this
        );
        container.add(drawingOptionsPanel);

        return container;
    }

    private JPanel createBigCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setPreferredSize(new Dimension(1300, 700));
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BorderLayout(5, 5));

        JPanel top = new JPanel();
        top.setPreferredSize(new Dimension(100, 70));
        top.setBackground(Color.WHITE);

        JLabel gameStatusLabel = new JLabel("Ожидание начала игры...", SwingConstants.CENTER);
        gameStatusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        top.add(gameStatusLabel);

        centerPanel.add(top, BorderLayout.NORTH);
        centerPanel.add(this.playerList, BorderLayout.WEST);
        centerPanel.add(this.chat, BorderLayout.EAST);
        centerPanel.add(this.drawingBoard, BorderLayout.CENTER);

        return centerPanel;
    }

    public void sendMessage(Message message) {
        gameController.sendMessage(message);
    }

    public void handleDrawingMessage(DrawingMessage message) {
        switch (message.getDrawingType()) {
            case PRESS -> drawingBoard.handleMousePressed(message.getPoint(), message.getBrushSize(), message.getColor());
            case DRAG -> drawingBoard.handleMouseDragged(message.getPoint());
            case UNDO -> drawingBoard.undo();
            case CLEAR -> drawingBoard.clear();
        }
    }

    public void handleChatMessage(ChatMessage message) {
        chat.appendMessage(message);
    }

    public void addPlayer(Player player) {
        playerList.addPlayer(player);
        chat.appendMessage(new ChatMessage("System", player.getUsername() + " присоединился к игре"));
    }

    public void removePlayer(Player player) {
        playerList.removePlayer(player.getId());
        chat.appendMessage(new ChatMessage("System", player.getUsername() + " покинул игру"));
    }

    public void synchronize(GameSyncMessage message) {
        playerList.setPlayers(message.getPlayers());
        drawingBoard.synchronize(message.getLines());
    }
}

