package ru.kpfu.drawandguess.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.io.Serializable;

@AllArgsConstructor
@Getter
public class DrawingMessage implements Serializable {

    private DrawingType type;

    private Point point;

}