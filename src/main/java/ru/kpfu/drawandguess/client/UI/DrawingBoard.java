package ru.kpfu.drawandguess.client.UI;

import lombok.Getter;
import lombok.Setter;
import ru.kpfu.drawandguess.common.model.Line;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DrawingBoard extends JPanel {

    private GameRoomPanel gameRoomPanel;

    private List<Line> lines = new ArrayList<>();
    private Line currentLine;
    private int brushSize = 3;
    private Color color = Color.BLACK;

    private MouseListener mouseListener;
    private MouseMotionListener mouseMotionListener;

    public DrawingBoard(GameRoomPanel gameRoomPanel) {
        this.gameRoomPanel = gameRoomPanel;
        this.setBackground(Color.WHITE);
        this.mouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                handleMousePressed(p, brushSize, color);
                gameRoomPanel.sendMessage(new DrawingMessage(DrawingType.PRESS, p, brushSize, color));
            }
        };

        this.mouseMotionListener = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                handleMouseDragged(p);
                gameRoomPanel.sendMessage(new DrawingMessage(DrawingType.DRAG, p));

            }
        };
    }

    public void enableDrawing() {
        addMouseListener(this.mouseListener);
        addMouseMotionListener(this.mouseMotionListener);
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    public void disableDrawing() {
        removeMouseListener(this.mouseListener);
        removeMouseMotionListener(this.mouseMotionListener);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Line line : lines) {
            g2d.setStroke(new BasicStroke(line.getBrushSize()));
            g2d.setColor(line.getColor());
            List<Point> points = line.getPoints();
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            if (points.size() == 1) {
                Point p = points.getFirst();
                g2d.drawLine(p.x, p.y, p.x, p.y);
            }
        }
    }

    public void handleMousePressed(Point p, int brushSize, Color color) {
        currentLine = new Line(brushSize, color);
        currentLine.addPoint(p);
        lines.add(currentLine);
        repaint();
    }

    public void handleMouseDragged(Point p) {
        currentLine.addPoint(p);
        repaint();
    }

    public void undo() {
        if (!lines.isEmpty()) {
            lines.removeLast();
            repaint();
        }
    }

    public void clear() {
        this.lines = new ArrayList<>();
        repaint();
    }

    public void synchronize(List<Line> lines) {
        this.lines = lines;
        repaint();
    }
}
