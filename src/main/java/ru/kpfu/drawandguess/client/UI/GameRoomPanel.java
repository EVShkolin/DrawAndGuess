package ru.kpfu.drawandguess.client.UI;

import lombok.Getter;
import ru.kpfu.drawandguess.client.controller.GameController;
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

        JPanel smallBottomPanel = createSmallBottomPanel();

        container.add(smallBottomPanel);

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

        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(200, 100));
        left.setBackground(Color.WHITE);

        centerPanel.add(top, BorderLayout.NORTH);
        centerPanel.add(left, BorderLayout.WEST);
        centerPanel.add(this.chat, BorderLayout.EAST);
        centerPanel.add(this.drawingBoard, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createSmallBottomPanel() {
        JPanel smallPanel = new JPanel();
        smallPanel.setBackground(Color.WHITE);
        smallPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setOpaque(false);

        wrapper.add(Box.createHorizontalStrut(200));

        smallPanel.setPreferredSize(new Dimension(800, 100));
        smallPanel.setMaximumSize(new Dimension(800, 100));
        wrapper.add(smallPanel);

        wrapper.add(Box.createHorizontalStrut(300));

        return wrapper;
    }

    public void sendMessage(Message message) {
        gameController.sendMessage(message);
    }

    public void handleDrawingMessage(DrawingMessage message) {
        switch (message.getDrawingType()) {
            case PRESS -> drawingBoard.handleMousePressed(message.getPoint());
            case DRAG -> drawingBoard.handleMouseDragged(message.getPoint());
        }
    }

    public void handleChatMessage(ChatMessage message) {
        chat.appendMessage(message);
    }

    public void synchronize(GameSyncMessage message) {
        drawingBoard.synchronize(message.getLines());
    }
}

