package ru.kpfu.drawandguess.client.controller;

import ru.kpfu.drawandguess.client.UI.MainFrame;
import ru.kpfu.drawandguess.common.protocol.Message;
import ru.kpfu.drawandguess.common.protocol.system.ApproveUsernameMessage;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppController {
    private GameController gameController;
    private NetworkController networkController;
    private MainFrame mainFrame;
    private String username;

    public AppController() {
        SwingUtilities.invokeLater(() -> {
            this.mainFrame = new MainFrame();
            this.mainFrame.setVisible(true);

            initControllers();
            connectToServer();
        });

    }

    private void initControllers() {
        this.networkController = new NetworkController(this);
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

    public void handleSystemMessage(Message message) {
        if (message instanceof ApproveUsernameMessage usernameMessage) {
            mainFrame.setUsername(usernameMessage.getUsername());
        }
    }

}
