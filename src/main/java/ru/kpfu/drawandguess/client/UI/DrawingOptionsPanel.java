package ru.kpfu.drawandguess.client.UI;

import ru.kpfu.drawandguess.common.protocol.draw.DrawingMessage;
import ru.kpfu.drawandguess.common.protocol.draw.DrawingType;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class DrawingOptionsPanel extends JPanel {

    private DrawingBoard drawingBoard;
    private GameRoomPanel gameRoomPanel;
    private JLabel sizeLabel;
    private JPanel colorIndicator;

    public DrawingOptionsPanel(DrawingBoard drawingBoard, GameRoomPanel gameRoomPanel) {
        this.drawingBoard = drawingBoard;
        this.gameRoomPanel = gameRoomPanel;
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);
        this.add(Box.createHorizontalStrut(200));

        JPanel controlsPanel = getControlsPanel();
        this.add(controlsPanel);

        this.add(Box.createHorizontalStrut(300));

        setupKeyBindings();
        this.setVisible(false);
    }

    private JPanel getControlsPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(800, 100));
        mainPanel.setMaximumSize(new Dimension(800, 100));

        JPanel row1 = new JPanel();
        row1.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        row1.setBackground(Color.WHITE);
        row1.setMaximumSize(new Dimension(800, 40));
        row1.add(new JLabel("Цвет:"));

        Color[] colors = {Color.BLACK, Color.WHITE, Color.RED, Color.GREEN, Color.BLUE,
                Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.ORANGE};
        for (Color color : colors) {
            JButton colorButton = new JButton();
            colorButton.setPreferredSize(new Dimension(25, 25));
            colorButton.setBackground(color);
            colorButton.setOpaque(true);
            colorButton.setBorder(new LineBorder(Color.BLACK, 1));
            colorButton.addActionListener(e -> {
                drawingBoard.setColor(color);
                updateColorIndicator(color);
            });
            row1.add(colorButton);
        }

        this.colorIndicator = new JPanel();
        colorIndicator.setPreferredSize(new Dimension(30, 25));
        colorIndicator.setBackground(drawingBoard.getColor());
        colorIndicator.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        row1.add(Box.createHorizontalStrut(20));
        row1.add(new JLabel("Текущий:"));
        row1.add(colorIndicator);

        JPanel row2 = new JPanel();
        row2.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        row2.setBackground(Color.WHITE);
        row2.setMaximumSize(new Dimension(800, 40));
        row2.add(new JLabel("Размер:"));

        int[] sizes = {1, 2, 3, 5, 8, 10, 15, 20};
        for (int size : sizes) {
            JButton sizeButton = new JButton(String.valueOf(size));
            sizeButton.setPreferredSize(new Dimension(35, 25));
            sizeButton.setMargin(new Insets(0, 0, 0, 0));

            sizeButton.addActionListener(e -> {
                drawingBoard.setBrushSize(size);
                updateSizeLabel(size);
            });
            row2.add(sizeButton);
        }

        this.sizeLabel = new JLabel("Текущий: " + drawingBoard.getBrushSize());
        row2.add(Box.createHorizontalStrut(20));
        row2.add(sizeLabel);

        row2.add(Box.createHorizontalStrut(20));
        JButton undoButton = new JButton("Отмена");
        undoButton.setPreferredSize(new Dimension(80, 30));
        undoButton.addActionListener(e -> {
            drawingBoard.undo();
            gameRoomPanel.sendMessage(new DrawingMessage(DrawingType.UNDO));
        });
        row2.add(undoButton);

        row2.add(Box.createHorizontalStrut(10));
        JButton clearButton = new JButton("Очистить");
        clearButton.setMargin(new Insets(0, 0, 0, 0));
        clearButton.setPreferredSize(new Dimension(80, 30));
        clearButton.addActionListener(e -> {
            drawingBoard.clear();
            gameRoomPanel.sendMessage(new DrawingMessage(DrawingType.CLEAR));
        });
        row2.add(clearButton);

        mainPanel.add(row1);
        mainPanel.add(row2);

        return mainPanel;
    }

    private void setupKeyBindings() {
        InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = this.getActionMap();
        Action undoAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawingBoard.undo();
                gameRoomPanel.sendMessage(new DrawingMessage(DrawingType.UNDO));
            }
        };

        KeyStroke ctrlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(ctrlZ, "undo");
        actionMap.put("undo", undoAction);
    }

    private void updateColorIndicator(Color color) {
        if (colorIndicator != null) {
            colorIndicator.setBackground(color);
            colorIndicator.repaint();
        }
    }

    private void updateSizeLabel(int size) {
        if (sizeLabel != null) {
            sizeLabel.setText("Текущий: " + size);
        }
    }
}
