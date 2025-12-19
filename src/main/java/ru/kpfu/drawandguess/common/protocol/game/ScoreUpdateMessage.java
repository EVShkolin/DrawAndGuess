package ru.kpfu.drawandguess.common.protocol.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScoreUpdateMessage implements Message {

    private String id;

    private String score;

    @Override
    public MessageType getType() {
        return MessageType.GAME;
    }
}
