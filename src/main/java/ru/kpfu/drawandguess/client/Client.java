package ru.kpfu.drawandguess.client;

import javax.swing.*;

public class Client {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setVisible(true);

        DrawingBoard drawingBoard = new DrawingBoard();
        frame.add(drawingBoard);
    }

}
