package ru.kpfu.drawandguess.client.UI;

import javax.swing.*;
import java.awt.*;

public class RoundInfoPanel extends JPanel {

    private JLabel timerLabel;
    private JLabel roundLabel;
    private JLabel wordLengthLabel;

    public RoundInfoPanel() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(100, 70));
        this.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(Color.WHITE);

        timerLabel = new JLabel("Время: --");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));

        roundLabel = new JLabel("Текущий раунд: ", SwingConstants.LEFT);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 16));

        leftPanel.add(timerLabel);
        leftPanel.add(Box.createHorizontalStrut(20));
        leftPanel.add(roundLabel);

        this.add(leftPanel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(Color.WHITE);

        wordLengthLabel = new JLabel("Длина слова: ");
        rightPanel.add(wordLengthLabel);

        this.add(rightPanel, BorderLayout.EAST);
    }

    public void updateTimer(int timeLeft) {
        SwingUtilities.invokeLater(() -> {
            timerLabel.setText("Время: " + timeLeft + " сек");

            if (timeLeft <= 10) {
                timerLabel.setForeground(Color.RED);
                timerLabel.setFont(new Font("Arial", Font.BOLD, 22));
            } else if (timeLeft <= 30) {
                timerLabel.setForeground(Color.ORANGE);
                timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            } else {
                timerLabel.setForeground(Color.BLUE);
                timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
            }
        });
    }

    public void updateRound(int currentRound, int totalRounds) {
        SwingUtilities.invokeLater(() -> {
            roundLabel.setText("Раунд: " + currentRound + " из " + totalRounds);
        });
    }

    public void setWordLength(int length) {
        SwingUtilities.invokeLater(() -> {
            wordLengthLabel.setText("Длина слова: " + length + " букв");
        });
    }

}
