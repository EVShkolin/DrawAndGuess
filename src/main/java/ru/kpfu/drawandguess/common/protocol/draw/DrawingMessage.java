package ru.kpfu.drawandguess.common.protocol.draw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.MessageType;

import java.awt.*;

@AllArgsConstructor
@Getter
public class DrawingMessage implements Message {

    private DrawingType drawingType;

    private Point point;

    @Override
    public MessageType getType() {
        return MessageType.DRAWING;
    }

}