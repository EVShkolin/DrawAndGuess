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

    private int brushSize;

    private Color color;

    public DrawingMessage(DrawingType drawingType) {
        this.drawingType = drawingType;
    }

    public DrawingMessage(DrawingType drawingType, Point point) {
        this.drawingType = drawingType;
        this.point = point;
    }

    @Override
    public MessageType getType() {
        return MessageType.DRAWING;
    }

}