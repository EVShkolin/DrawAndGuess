package ru.kpfu.drawandguess.client.UI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.client.controller.GameController;
import ru.kpfu.drawandguess.common.model.ChatMessage;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.game.GameSyncMessage;

import javax.swing.*;
import java.awt.*;


@AllArgsConstructor
@Getter
@Setter
public class GameRoomPanel extends JPanel {

    private GameController gameController;

    private DrawingBoard drawingBoard;
    private ChatPanel chat;
    private PlayerListPanel playerList;

    public GameRoomPanel(GameController gameController) {
        setLayout(new BorderLayout());

        this.gameController = gameController;
        this.drawingBoard = new DrawingBoard(this);
        this.chat = new ChatPanel();
        this.playerList = new PlayerListPanel();

        this.add(drawingBoard);
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

    }

    public void synchronize(GameSyncMessage message) {
        drawingBoard.synchronize(message.getLines());
    }

}
