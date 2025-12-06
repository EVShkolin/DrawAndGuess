package ru.kpfu.drawandguess.common.protocol.chat;

import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;

public class ChatMessage implements Message {

    @Override
    public MessageType getType() {
        return MessageType.CHAT;
    }
}
