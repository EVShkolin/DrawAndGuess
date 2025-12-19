package ru.kpfu.drawandguess.common.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Line implements Serializable {

    private List<Point> points;

    private int brushSize;

    private Color color;

    public Line(int brushSize, Color color) {
        this.points = new ArrayList<>();
        this.brushSize = brushSize;
        this.color = color;
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public void clear() {
        points.clear();
    }
}
