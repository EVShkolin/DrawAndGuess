package ru.kpfu.drawandguess.client.UI;

import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class DrawingBoard extends JPanel {

    private GameRoomPanel gameRoomPanel;

    private List<List<Point>> lines = new ArrayList<>();
    private List<Point> currentLine;
    private int brushSize = 3;
    private Color color = Color.BLACK;

    public DrawingBoard(GameRoomPanel gameRoomPanel) {
        this.gameRoomPanel = gameRoomPanel;
        this.setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                handleMousePressed(p);
                gameRoomPanel.sendMessage(new DrawingMessage(DrawingType.PRESS, p));
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                handleMouseDragged(p);
                gameRoomPanel.sendMessage(new DrawingMessage(DrawingType.DRAG, p));

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(brushSize));
        g2d.setColor(color);
        for (List<Point> points : lines) {
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

    public void handleMousePressed(Point p) {
        currentLine = new ArrayList<>();
        currentLine.add(p);
        lines.add(currentLine);
        repaint();
    }

    public void handleMouseDragged(Point p) {
        currentLine.add(p);
        repaint();
    }

    public void synchronize(List<List<Point>> lines) {
        this.lines = lines;
        repaint();
    }
}
