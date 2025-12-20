package ru.kpfu.drawandguess.common.protocol.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.model.GameState;
import ru.kpfu.drawandguess.common.model.Line;
import ru.kpfu.drawandguess.common.model.Player;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;
import ru.kpfu.drawandguess.server.GameRoom;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GameSyncMessage implements Message {

    private String id;
    private String title;
    private List<Player> players;
    private List<Line> lines;
    private GameState gameState;
    private int roundTimeLeft;
    private String currentDrawer;
    private String hiddenWord;
    private int round;

    public GameSyncMessage(GameRoom gameRoom) {
        this.id = gameRoom.getId();
        this.title = gameRoom.getTitle();
        this.players = gameRoom.getPlayers().stream()
                .map(c -> new Player(c.getId(), c.getUsername(), c.getScore()))
                .toList();
        this.lines = gameRoom.getLines();
        this.gameState = gameRoom.getGameState();
        this.roundTimeLeft = gameRoom.getTimeLeft();
        this.currentDrawer = gameRoom.getCurrentDrawer() == null ? null : gameRoom.getCurrentDrawer().getUsername();
        this.hiddenWord = hideWord(gameRoom.getCurrentWord());
        this.round = gameRoom.getRound();

    }

    @Override
    public MessageType getType() {
        return MessageType.GAME;
    }

    public String hideWord(String currentWord) {
        if (currentWord == null) return null;
        return currentWord.replaceAll("[а-яА-Я]", "_");
    }
}
