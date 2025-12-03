package ru.kpfu.drawandguess.client;

import ru.kpfu.drawandguess.common.protocol.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.DrawingType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DrawingBoard extends JPanel {

    private Client client;

    private List<List<Point>> lines = new ArrayList<>();
    private List<Point> currentLine;
    private int brushSize = 3;
    private Color color = Color.BLACK;

    public DrawingBoard(Client client) {
        this.client = client;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                handleMousePressed(p);
                client.sendMessage(new DrawingMessage(DrawingType.PRESS, p));
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                handleMouseDragged(p);
                client.sendMessage(new DrawingMessage(DrawingType.DRAG, p));

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
