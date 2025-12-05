package ru.kpfu.drawandguess.common.protocol.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;

import java.awt.*;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class GameSyncMessage implements Message {

    private List<List<Point>> lines;

    @Override
    public MessageType getType() {
        return MessageType.GAME;
    }
}
