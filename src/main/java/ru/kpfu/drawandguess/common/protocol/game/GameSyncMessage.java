package ru.kpfu.drawandguess.common.protocol.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.model.ChatMessage;
import ru.kpfu.drawandguess.common.model.GameState;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;
import ru.kpfu.drawandguess.server.Connection;
import ru.kpfu.drawandguess.server.GameRoom;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GameSyncMessage implements Message {

    private String id;
    private String title;
    private List<String> players;
    private List<List<Point>> lines;
    private List<ChatMessage> messages;
    private GameState gameState;
    private int roundTimeLeft;
    private String currentDrawer;
    private String hiddenWord;
    private int round;

    public GameSyncMessage(GameRoom gameRoom) {
        this.id = gameRoom.getId();
        this.title = gameRoom.getTitle();
        this.players = gameRoom.getPlayers().stream()
                .map(Connection::getUsername)
                .toList();
        this.lines = gameRoom.getLines();
        this.messages = gameRoom.getMessages();
        this.gameState = gameRoom.getGameState();
        this.roundTimeLeft = gameRoom.getRoundTimeLeft();
        this.currentDrawer = gameRoom.getCurrentDrawer() == null ? null : gameRoom.getCurrentDrawer().getUsername();
        this.hiddenWord = hideWord(gameRoom.getCurrentWord());
        this.round = gameRoom.getRound();

    }

    @Override
    public MessageType getType() {
        return MessageType.GAME;
    }

    public String hideWord(String currentWord) {
        // todo
        return currentWord;
    }
}
