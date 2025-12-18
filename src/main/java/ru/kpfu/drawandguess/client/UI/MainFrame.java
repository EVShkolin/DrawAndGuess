package ru.kpfu.drawandguess.client.UI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.kpfu.drawandguess.client.controller.GameController;

import javax.swing.*;
import java.awt.*;

@AllArgsConstructor
@Getter
public class MainFrame extends JFrame {

    private Panel loginPanel;
    private Panel gameListPanel;
    private GameRoomPanel gameRoomPanel;
    private String username;

    public MainFrame() {
        this.setTitle("Draw & Guess");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setMinimumSize(new Dimension(1000, 800));
        this.setLocationRelativeTo(null);
    }

    public void setGameController(GameController gameController) {
        this.gameRoomPanel = new GameRoomPanel(gameController);
        this.add(gameRoomPanel);
    }

    public void setUsername(String username) {
        this.username = username;
        gameRoomPanel.getChat().setUsername(username);
    }

}
