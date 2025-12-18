package ru.kpfu.drawandguess.client.controller;

import ru.kpfu.drawandguess.client.UI.MainFrame;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppController {
    private GameController gameController;
    private NetworkController networkController;
    private MainFrame mainFrame;

    public AppController() {
        SwingUtilities.invokeLater(() -> {
            this.mainFrame = new MainFrame();
            this.mainFrame.setVisible(true);

            initControllers();
            connectToServer();
        });

    }

    private void initControllers() {
        this.networkController = new NetworkController();
        this.gameController = new GameController(networkController);

        this.mainFrame.setGameController(gameController);

        this.gameController.setGameRoomPanel(mainFrame.getGameRoomPanel());

        this.networkController.setGameController(gameController);
    }

    private void connectToServer() {
        try {
            networkController.connect(InetAddress.getLocalHost(), 8888);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
