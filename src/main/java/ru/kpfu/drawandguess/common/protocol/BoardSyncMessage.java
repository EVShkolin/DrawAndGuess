package ru.kpfu.drawandguess.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Getter
public class BoardSyncMessage implements Serializable {

    private List<List<Point>> lines;

}
