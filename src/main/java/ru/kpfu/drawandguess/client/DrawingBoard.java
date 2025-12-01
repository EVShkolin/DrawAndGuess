package ru.kpfu.drawandguess.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

public class DrawingBoard extends JPanel {

    private List<List<Point>> lines = new ArrayList<>();
    private List<Point> currentLine;
    private int strokeSize = 3;
    private Color color = Color.BLACK;

    public DrawingBoard() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentLine = new ArrayList<>();
                Point p = new Point(e.getX(), e.getY());
                currentLine.add(p);
                lines.add(currentLine);
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = new Point(e.getX(), e.getY());
                currentLine.add(p);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(strokeSize));
        g2d.setColor(color);
        for (List<Point> points : lines) {
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }
}
