package ru.kpfu.drawandguess.client.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kpfu.drawandguess.client.UI.GameRoomPanel;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.chat.ChatMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.game.GameSyncMessage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameController {

    private NetworkController networkController;
    private GameRoomPanel gameRoomPanel;

    public GameController(NetworkController networkController) {
        this.networkController = networkController;

    }

    public void handleMessage(Message message) {
        switch (message.getType()) {
            case DRAWING -> gameRoomPanel.handleDrawingMessage((DrawingMessage) message);
            case CHAT -> gameRoomPanel.handleChatMessage((ChatMessage) message);
            default -> handleGameMessage(message);
        }
    }


    private void handleGameMessage(Message message) {
        if (message instanceof GameSyncMessage syncMessage) {
            gameRoomPanel.synchronize(syncMessage);
        }
    }

    public void sendMessage(Message message) {
        networkController.sendMessage(message);
    }

}
