package ru.kpfu.drawandguess.common.protocol.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.kpfu.drawandguess.common.model.Player;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlayerPresenceMessage implements Message {

    private boolean joined;

    private Player player;

    @Override
    public MessageType getType() {
        return MessageType.GAME;
    }
}
