package ru.kpfu.drawandguess.common.protocol.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApproveUsernameMessage implements Message {

    private String username;

    @Override
    public MessageType getType() {
        return MessageType.SYSTEM;
    }
}
